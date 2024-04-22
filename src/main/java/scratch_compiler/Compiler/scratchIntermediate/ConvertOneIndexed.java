package scratch_compiler.Compiler.scratchIntermediate;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.CopyCode;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertOneIndexed {
    public static IntermediateCode convert(IntermediateCode code) {
        if (!code.isZeroIndexed())
            throw new IllegalArgumentException("Code is already one indexed");

        code.setGlobalScope(convert(CopyCode.copy(code.getGlobalScope())));
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            function.setScope(convert(CopyCode.copy(function.getScope())));
        }

        code.setZeroIndexed(false);
        return code;
    }

    private static Scope convert(Scope scope) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            converted.addStatement(convert(statement));
        }
        return converted;
    }

    private static Statement convert(Statement statement) {
        convert((ExpressionContainer) statement);
        if (statement instanceof Scope)
            return convert((Scope) statement);

        if (statement instanceof SimpleArrayAssignment) {
            SimpleArrayAssignment arrayAssignment = (SimpleArrayAssignment) statement;
            arrayAssignment.setIndex(addOne(arrayAssignment.getIndex()));
        }

        for (int i = 0; i < statement.getScopeCount(); i++) {
            Scope scope = statement.getScope(i);
            statement.setScope(i, convert(scope));
        }

        return statement;
    }

    private static void convert(ExpressionContainer container) {
        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            expression = convert(expression);
            convert((ExpressionContainer) expression);
            container.setExpression(i, expression);
        }
    }

    private static Expression convert(Expression expression) {
        if (expression instanceof SimpleArrayValue) {
            SimpleArrayValue arrayValue = (SimpleArrayValue) expression;
            arrayValue.setIndex(addOne(arrayValue.getIndex()));
        }

        return expression;
    }

    private static Expression addOne(Expression expression) {
        return new BinaryOperator(OperatorType.ADDITION, expression, new IntValue(1), new Type(VariableType.INT));
    }
}
