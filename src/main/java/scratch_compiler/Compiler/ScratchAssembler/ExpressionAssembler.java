package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.intermediate.ConvertExpression;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SystemCallExpression;
import scratch_compiler.Compiler.parser.expressions.TypeConversionExpression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.JoinField;
import scratch_compiler.ValueFields.ListElementField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.ValueFields.LogicFields.AndField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.GreaterThanField;
import scratch_compiler.ValueFields.LogicFields.LessThanField;
import scratch_compiler.ValueFields.LogicFields.LogicField;
import scratch_compiler.ValueFields.LogicFields.NotField;
import scratch_compiler.ValueFields.LogicFields.OrField;

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
            return new VariableField(((SimpleVariableValue) expression).getName(), false);
        if (expression instanceof SimpleArrayValue)
            return new ListElementField(((SimpleArrayValue) expression).getName(), false,
                    ExpressionAssembler.assemble(((SimpleArrayValue) expression).getIndex()));
        if (expression instanceof TypeConversionExpression)
            return assemble(((TypeConversionExpression) expression).getExpression());

        if (expression instanceof SystemCallExpression)
            return SystemCallAssembler.assemble((SystemCallExpression) expression);

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
            return assembleUnaryExpression((UnaryOperator) expression);

        System.out.println("Error: " + expression + " " + expression.getClass());
        return errorField(expression);
    }

    private static ValueField assembleBinaryExpression(BinaryOperator expression) {
        ValueField left = assemble(expression.getLeft());
        ValueField right = assemble(expression.getRight());

        OperatorType operator = expression.getOperatorType();

        // add the rest of the operators
        switch (operator) {
            case ADDITION:
                if (expression.getLeft().getType().getType().getType() == VariableType.STRING
                        || expression.getRight().getType().getType().getType() == VariableType.STRING)
                    return new JoinField(left, right);
                return new AdditionField(left, right);
            case SUBTRACTION:
                return new SubtractionField(left, right);
            case MULTIPLICATION:
                return new MultiplicationField(left, right);
            case DIVISION:
                return new DivisionField(left, right);
            case LESS_THAN:
                return new LessThanField(left, right);
            case GREATER_THAN:
                return new GreaterThanField(left, right);
            case EQUALS:
                return new EqualsField(left, right);
            case NOT_EQUALS:
                return new NotField(new EqualsField(left, right));
            case LESS_EQUALS:
                return new OrField(new LessThanField(left, right), new EqualsField(left, right));
            case GREATER_EQUALS:
                return new OrField(new GreaterThanField(left, right), new EqualsField(left, right));
            case AND:
                return new AndField(left, right);
            case OR:
                return new OrField(left, right);
            case MODULUS:
                return new ModulusField(left, right);
            default:
                return errorField(expression);
        }

    }

    private static ValueField assembleUnaryExpression(UnaryOperator expression) {
        ValueField value = assemble(expression.getOperand());

        switch (expression.getOperatorType()) {
            case UNARY_NEGATION:
                return new SubtractionField(new NumberField(0), value);
            case NOT:
                return new NotField(toLogicField(value));
            default:
                return errorField(expression);
        }
    }

    public static ValueField errorField(Expression expression) {
        return new StringField("Compile Error: " + expression);
    }

    public static LogicField toLogicField(ValueField value) {
        if (value instanceof LogicField)
            return (LogicField) value;
        return new EqualsField(value, new NumberField(1));
    }
}
