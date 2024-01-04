package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.parser.nodes.BinaryOperationNode;
import scratch_compiler.Compiler.parser.nodes.BooleanNode;
import scratch_compiler.Compiler.parser.nodes.Expression;
import scratch_compiler.Compiler.parser.nodes.FloatNode;
import scratch_compiler.Compiler.parser.nodes.IntNode;
import scratch_compiler.Compiler.parser.nodes.VariableNode;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.Declaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.ValueFields.LogicFields.BooleanField;

public class ScratchAssembler {
    public static Block assemble(String code) {
        Scope scope = CompilerUtils.compile(code);
        StartBlock startBlock = new StartBlock();
        Block stack = compileScope(scope);
        startBlock.addToStack(stack);
        return startBlock;
    }

    private static Block compileScope(Scope scope) {
        ArrayList<Statement> statements = scope.getStatements();
        if (statements.size() == 0)
            return defaultBlock();

        Block stack = assembleStatement(statements.get(0));

        for (int i = 1; i < statements.size(); i++) {
            Block block = assembleStatement(statements.get(i));
            stack.addToStack(block);
        }

        return stack;
    }

    private static Block assembleStatement(Statement statement) {
        if (statement instanceof Declaration) 
            return assembleDeclaration((Declaration) statement);
        if (statement instanceof Assignment)
            return assembleAssignment((Assignment) statement);
        if (statement instanceof Scope)
            return compileScope((Scope) statement);
        

        return defaultBlock();
    }

    private static Block assembleDeclaration(Declaration declaration) {
        return new SetVariableBlock(declaration.getName(), false, assembleExpression(declaration.getExpression()));
    }

    private static Block assembleAssignment(Assignment assignment) {
        return new SetVariableBlock(assignment.getName(), false, assembleExpression(assignment.getExpression()));
    }

    private static ValueField assembleExpression(Expression expression) {
        if (expression instanceof IntNode)
            return new NumberField(((IntNode) expression).getValue());
        if (expression instanceof FloatNode)
            return new NumberField(((FloatNode) expression).getValue());
        if (expression instanceof BooleanNode)
            return new BooleanField(((BooleanNode) expression).getValue());
        if (expression instanceof VariableNode)
            return new VariableField(((VariableNode) expression).getName(), false);
        if (expression instanceof BinaryOperationNode)
            return assembleBinaryExpression((BinaryOperationNode) expression);
        
        return defaultField();
    }

    private static ValueField assembleBinaryExpression(BinaryOperationNode expression) {
        switch (expression.getToken().getType())
        {
            case PLUS:
                return new AdditionField(assembleExpression(expression.getLeft()), assembleExpression(expression.getRight()));
            case MINUS:
                return new SubtractionField(assembleExpression(expression.getLeft()), assembleExpression(expression.getRight()));
            case MUL:
                return new MultiplicationField(assembleExpression(expression.getLeft()), assembleExpression(expression.getRight()));
            case DIV:
                return new DivisionField(assembleExpression(expression.getLeft()), assembleExpression(expression.getRight()));
            

            default:
                return defaultField();
        }
    }

    private static Block defaultBlock() {
        return new MoveBlock(0);
    }

    private static ValueField defaultField() {
        return new NumberField(0);
    }
    

}
