package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;
import java.util.Stack;

import scratch_compiler.Compiler.Function;
import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchProgram;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Blocks.AddListBlock;
import scratch_compiler.Blocks.ChangeListBlock;
import scratch_compiler.Blocks.ClearListBlock;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.RemoveListBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.FunctionArgumentField;
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
    public static ScratchProgram assemble(String code) {
        CompiledCode compiledCode = Compiler.compile(code, ScratchCoreAssembler.getSystemCalls(), true);

        ArrayList<FunctionDeclaration> functionDeclarations = compiledCode.getFunctions();

        BlockStack blockStack = new BlockStack();
        blockStack.push(StackAssembler.initializeStack());

        ArrayList<HatBlock> functionDefinitionBlocks = new ArrayList<>();
        for (FunctionDeclaration functionDeclaration : functionDeclarations) {
            FunctionDefinitionBlock functionDefinitionBlock = FunctionAssembler.assembleFunction(functionDeclaration);
            functionDefinitionBlocks.add(functionDefinitionBlock);
        }

        return new ScratchProgram(blockStack, functionDefinitionBlocks);
    }

    public static BlockStack compileScope(Scope scope, VariableStackReference stack, boolean isFunction) {
        stack.addScope();
        ArrayList<Statement> statements = scope.getStatements();
        if (statements.size() == 0)
            return new BlockStack();

        BlockStack blockStack = assembleStatement(statements.get(0), stack, isFunction);

        for (int i = 1; i < statements.size(); i++)
            blockStack.push(assembleStatement(statements.get(i), stack, isFunction));

        int numRemoved = stack.removeScope();
        if (numRemoved > 0)
            blockStack.push(StackAssembler.offsetPointer(-numRemoved));

        return blockStack;
    }

    static BlockStack assembleStatement(Statement statement, VariableStackReference stack, boolean isFunction) {
        if (statement instanceof Scope)
            return compileScope((Scope) statement, stack, isFunction);

        if (statement instanceof ForStatement)
            return ForAssembler.assemble((ForStatement) statement, stack, isFunction);
        if (statement instanceof FunctionCall)
            return FunctionCallAssembler.assemble((FunctionCall) statement, stack, isFunction);
        if (statement instanceof Assignment)
            return assembleAssignment((Assignment) statement, stack, isFunction);
        if (statement instanceof ReturnStatement)
            return FunctionAssembler.assembleReturnStatement((ReturnStatement) statement, stack);
        if (statement instanceof VariableDeclaration)
            return assembleDeclaration((VariableDeclaration) statement, stack, isFunction);

        StackBlock block = defaultBlock();
        if (statement instanceof IfStatement)
            block = IfAssembler.assemble((IfStatement) statement, stack, isFunction);
        if (statement instanceof WhileStatement)
            block = WhileAssembler.assemble((WhileStatement) statement, stack, isFunction);

        return new BlockStack(block);
    }

    static BlockStack assembleDeclaration(VariableDeclaration declaration, VariableStackReference stack,
            boolean isFunction) {
        stack.addVariable(getVariable(declaration));
        BlockStack blockStack = new BlockStack();
        // StackAssembler.addExpressionToStack(bl
        for (Expression argument : declaration.getValue().getArguments())
            blockStack.push(StackAssembler.addValueToStack(assembleExpression(argument, stack, isFunction)));
        return blockStack;
    }

    private static ScratchVariable getVariable(VariableDeclaration declaration) {
        return new ScratchVariable(declaration.getName(), false);
    }

    private static ScratchVariable getVariable(Assignment assignment) {
        return new ScratchVariable(assignment.getName(), false);
    }

    private static ScratchVariable getVariable(VariableValue variable) {
        return new ScratchVariable(variable.getName(), false);
    }

    public static BlockStack assembleAssignment(Assignment assignment, VariableStackReference stack,
            boolean isFunction) {
        String name = assignment.getName();
        if (ScratchCoreAssembler.isVariable(name))
            return ScratchCoreAssembler.assembleAssignment(name,
                    assembleExpression(assignment.getExpression(), stack, isFunction));

        return StackAssembler.assignValueToStack(assembleExpression(assignment.getExpression(), stack, isFunction),
                new NumberField(stack.getVariableIndex(getVariable(assignment))));
    }

    static ArrayList<ValueField> toArray(ValueField... fields) {
        ArrayList<ValueField> list = new ArrayList<>();
        for (ValueField field : fields)
            list.add(field);
        return list;
    }

    static ArrayList<ValueField> assembleExpression(Expression expression, VariableStackReference stack,
            boolean isFunction) {
        if (expression instanceof IntValue)
            return toArray(new NumberField(((IntValue) expression).getValue()));
        if (expression instanceof FloatValue)
            return toArray(new NumberField(((FloatValue) expression).getValue()));
        if (expression instanceof BooleanValue)
            return toArray(new NumberField(((BooleanValue) expression).getValue() ? 1 : 0));
        if (expression instanceof StringValue)
            return toArray(new StringField(((StringValue) expression).getString()));
        if (expression instanceof VariableValue) {
            String name = ((VariableValue) expression).
            if (ScratchCoreAssembler.isVariable(name))
                return ScratchCoreAssembler.assembleExpression(name);

            int stackIndex = stack.getVariableIndex(getVariable((VariableValue) expression));
            if (isFunction)
                return StackAssembler.getElementOfStack(
                        new AdditionField(FunctionAssembler.getStackOffset(), new NumberField(stackIndex)));
            return StackAssembler.getElementOfStack(new NumberField(stackIndex));
        }
        if (expression instanceof BinaryOperator)
            return assembleBinaryExpression((BinaryOperator) expression, stack, isFunction);
        if (expression instanceof UnaryOperator)
            return defaultField();

        return defaultField();
    }

    private static ValueField assembleBinaryExpression(BinaryOperator expression,
            VariableStackReference stack, boolean isFunction) {
        ValueField left = assembleExpression(expression.getLeft(), stack, isFunction);
        ValueField right = assembleExpression(expression.getRight(), stack, isFunction);

        OperatorType operator = expression.getOperatorType();

        // add the rest of the operators
        switch (operator) {
            case ADDITION:
                return new AdditionField(left, right);
            default:
                return defaultField();
        }

        // switch (expression.getOperatorType()) {
        // case ADD:
        // out = new AdditionField(left, right);
        // break;
        // case SUB:

        // if (expression instanceof AdditionExpression) {
        // if (expression.getType() == VariableType.STRING)
        // out = new JoinField(left, right);
        // else
        // out = new AdditionField(left, right);
        // } else if (expression instanceof SubtractionExpression)
        // out = new SubtractionField(left, right);
        // else if (expression instanceof MultiplicationExpression)
        // out = new MultiplicationField(left, right);
        // else if (expression instanceof DivisionExpression)
        // out = new DivisionField(left, right);
        // else if (expression instanceof ModulusExpression)
        // out = new ModulusField(left, right);
        // else if (expression instanceof EqualsExpression)
        // out = new EqualsField(left, right);
        // else if (expression instanceof GreaterThanExpression)
        // out = new GreaterThanField(left, right);
        // else if (expression instanceof LessThanExpression)
        // out = new LessThanField(left, right);

        // if (expression.getType() == VariableType.BOOLEAN)
        // out = new AdditionField(out, new NumberField(0));
    }

    static StackBlock defaultBlock() {
        return new SayBlock(defaultField());
    }

    static ValueField defaultField() {
        return new StringField("Compile Error");
    }

}
