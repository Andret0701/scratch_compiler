package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.Variable;
import scratch_compiler.Blocks.AddListBlock;
import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.ChangeListBlock;
import scratch_compiler.Blocks.ClearListBlock;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.RemoveListBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.DivisionExpression;
import scratch_compiler.Compiler.parser.expressions.EqualsExpression;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FloatValue;
import scratch_compiler.Compiler.parser.expressions.GreaterThanExpression;
import scratch_compiler.Compiler.parser.expressions.IntValue;
import scratch_compiler.Compiler.parser.expressions.LessThanExpression;
import scratch_compiler.Compiler.parser.expressions.ModulusExpression;
import scratch_compiler.Compiler.parser.expressions.MultiplicationExpression;
import scratch_compiler.Compiler.parser.expressions.StringValue;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;
import scratch_compiler.Compiler.parser.expressions.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.JoinField;
import scratch_compiler.ValueFields.ListElementField;
import scratch_compiler.ValueFields.ListLengthField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.GreaterThanField;
import scratch_compiler.ValueFields.LogicFields.LessThanField;
public class ScratchAssembler {
    public static Block assemble(String code) {
        Scope scope = CompiledCode.compile(code, ScratchVariablesAssembler.getIdentiferTypes()).getScope();

        StartBlock startBlock = new StartBlock();
        startBlock.addToStack(new ClearListBlock("Stack", false));
        Block stack = compileScope(scope, new StackReference());
        startBlock.addToStack(stack);
        return startBlock;
    }

    private static Block compileScope(Scope scope,StackReference stack) {
        stack.addScope(); 
        ArrayList<Statement> statements = scope.getStatements();
        if (statements.size() == 0)
            return defaultBlock();

        Block outBlock = assembleStatement(statements.get(0),stack);

        for (int i = 1; i < statements.size(); i++) {
            Block block = assembleStatement(statements.get(i),stack);
            outBlock.addToStack(block);
        }

        int numRemoved=stack.removeScope();
        for (int i = 0; i < numRemoved; i++)
            outBlock.addToStack(getListRemoveLastBlock("Stack", false));
        return outBlock;
    }

    static Block assembleStatement(Statement statement,StackReference stack) {
        if (statement instanceof VariableDeclaration) 
            return assembleDeclaration((VariableDeclaration) statement,stack);
        if (statement instanceof Assignment)
            return assembleAssignment((Assignment) statement,stack);
        if (statement instanceof Scope)
            return compileScope((Scope) statement,stack);
        if (statement instanceof IfStatement)
            return IfAssembler.assemble((IfStatement) statement,stack);
        if (statement instanceof WhileStatement)
            return WhileAssembler.assemble((WhileStatement) statement,stack);
        if (statement instanceof ForStatement)
            return ForAssembler.assemble((ForStatement) statement,stack);
        

        return defaultBlock();
    }

    static Block assembleDeclaration(VariableDeclaration declaration,StackReference stack) {
        AddListBlock addListBlock = new AddListBlock("Stack", false, assembleExpression(declaration.getExpression(),stack));
        stack.addVariable(getVariable(declaration));
        return addListBlock;
    }

    private static Variable getVariable(VariableDeclaration declaration) {
        return new Variable(declaration.getName(), false);
    }

    private static Variable getVariable(Assignment assignment) {
        return new Variable(assignment.getName(), false);
    }

    private static Variable getVariable(VariableValue variable) {
        return new Variable(variable.getName(), false);
    }

    public static Block assembleAssignment(Assignment assignment,StackReference stack) {
        String name = assignment.getName();
        if (ScratchVariablesAssembler.isVariable(name))
            return ScratchVariablesAssembler.assembleAssignment(name, assembleExpression(assignment.getExpression(),stack));

        return new ChangeListBlock("Stack", false, new NumberField(stack.getVariableIndex(getVariable(assignment))), assembleExpression(assignment.getExpression(),stack));
    }

    static ValueField assembleExpression(Expression expression,StackReference stack) {
        if (expression instanceof IntValue)
            return new NumberField(((IntValue) expression).getValue());
        if (expression instanceof FloatValue)
            return new NumberField(((FloatValue) expression).getValue());
        if (expression instanceof BooleanValue)
            return new NumberField(((BooleanValue) expression).getValue() ? 1 : 0);
        if  (expression instanceof StringValue)
            return new StringField(((StringValue) expression).getString());
        if (expression instanceof VariableValue)
        {
            String name = ((VariableValue) expression).getName();
            if (ScratchVariablesAssembler.isVariable(name))
                return ScratchVariablesAssembler.assembleExpression(name);
            return new ListElementField("Stack", false, new NumberField(stack.getVariableIndex(getVariable((VariableValue) expression))));
        }
        if (expression instanceof BinaryOperationExpression)
            return assembleBinaryExpression((BinaryOperationExpression) expression,stack);
        
        return defaultField();
    }

    private static ValueField assembleBinaryExpression(BinaryOperationExpression expression,StackReference stack) {
        ValueField left = assembleExpression(expression.getLeft(),stack);
        ValueField right = assembleExpression(expression.getRight(),stack);

        ValueField out=defaultField();
        if (expression instanceof AdditionExpression)
        {
            if (expression.getType() ==VariableType.STRING)
                out= new JoinField(left, right);
            else
                out= new AdditionField(left, right);
        }
        else if (expression instanceof SubtractionExpression)
                out= new SubtractionField(left, right);
        else if  (expression instanceof MultiplicationExpression)
                out= new MultiplicationField(left, right);
        else if (expression instanceof DivisionExpression)
                out= new DivisionField(left, right);
        else if (expression instanceof ModulusExpression)
                out= new ModulusField(left, right);
        else if (expression instanceof EqualsExpression)
                out= new EqualsField(left, right);
        else if (expression instanceof GreaterThanExpression)
                out= new GreaterThanField(left, right);
        else if (expression instanceof LessThanExpression)
                out= new LessThanField(left, right);


        
        if (expression.getType()==VariableType.BOOLEAN)
            out= new AdditionField(out, new NumberField(0));
    

        return out;
    }

    static Block defaultBlock() {
        return new SayBlock(defaultField());
    }

    static ValueField defaultField() {
        return new StringField("Compile Error"); 
    }

    public static Block getListRemoveLastBlock(String name, boolean isGlobal) {
        return new RemoveListBlock(name, isGlobal, new ListLengthField(name, isGlobal));
    }
    

}
