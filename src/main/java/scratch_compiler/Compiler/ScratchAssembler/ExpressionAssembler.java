package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class ExpressionAssembler {
    static ValueField assemble(Expression expression) {
        if (expression instanceof IntValue)
            return new NumberField(((IntValue) expression).getValue());
        if (expression instanceof FloatValue)
            return new NumberField(((FloatValue) expression).getValue());
        if (expression instanceof BooleanValue)
            return new NumberField(((BooleanValue) expression).getValue() ? 1 : 0);
        if (expression instanceof StringValue)
            return new StringField(((StringValue) expression).getString());
        if (expression instanceof SimpleVariableValue)
            return assembleVariableValue((SimpleVariableValue) expression);
        // if (expression instanceof VariableValue) {
        // String name = ((VariableValue) expression).getName();
        // // if (ScratchCoreAssembler.isVariable(name))
        // // return ScratchCoreAssembler.assembleExpression(name);

        // int stackIndex = stack.getVariableIndex(getVariable((VariableValue)
        // expression));
        // if (isFunction)
        // return StackAssembler.getElementOfStack(
        // new AdditionField(FunctionAssembler.getStackOffset(), new
        // NumberField(stackIndex)));
        // return StackAssembler.getElementOfStack(new NumberField(stackIndex));
        // }
        if (expression instanceof BinaryOperator)
            return assembleBinaryExpression((BinaryOperator) expression);
        if (expression instanceof UnaryOperator)
            return errorField(expression);

        return errorField(expression);
    }

    private static ValueField assembleBinaryExpression(BinaryOperator expression) {
        ValueField left = assemble(expression.getLeft());
        ValueField right = assemble(expression.getRight());

        OperatorType operator = expression.getOperatorType();

        // add the rest of the operators
        switch (operator) {
            case ADDITION:
                return new AdditionField(left, right);
            default:
                return errorField(expression);
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

    private static ValueField assembleVariableValue(SimpleVariableValue variableValue) {
        return new VariableField(variableValue.getName(), false);
    }

    private static ValueField errorField(Expression expression) {
        return new StringField("Compile Error: " + expression);
    }
}
