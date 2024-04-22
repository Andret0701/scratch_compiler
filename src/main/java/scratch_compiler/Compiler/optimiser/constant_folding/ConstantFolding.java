package scratch_compiler.Compiler.optimiser.constant_folding;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConstantFolding implements Optimization {
    @Override
    public Optimized optimize(IntermediateCode code) {

        boolean changed = false;
        Optimized optimizedGlobalScope = optimizeScope(code.getGlobalScope());
        changed = changed || optimizedGlobalScope.isOptimized();
        code.setGlobalScope((Scope) optimizedGlobalScope.getObject());

        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            Optimized optimizedFunctionScope = optimizeScope(function.getScope());
            changed = changed || optimizedFunctionScope.isOptimized();
            function.setScope((Scope) optimizedFunctionScope.getObject());
        }

        return new Optimized(code, changed);
    }

    private static Optimized optimizeScope(Scope scope) {
        boolean changed = false;
        for (int i = 0; i < scope.getStatements().size(); i++) {
            Statement statement = scope.getStatements().get(i);
            for (int j = 0; j < statement.getExpressionCount(); j++) {
                Optimized optimized = optimizeExpression(statement.getExpression(j));
                changed = changed || optimized.isOptimized();
                Expression expression = (Expression) optimized.getObject();
                statement.setExpression(j, expression);
            }

            for (int j = 0; j < statement.getScopeCount(); j++) {
                Optimized optimized = optimizeScope(statement.getScope(j));
                changed = changed || optimized.isOptimized();
                Scope innerScope = (Scope) optimized.getObject();
                statement.setScope(j, innerScope);
            }
        }

        return new Optimized(scope, changed);
    }

    private static Optimized optimizeExpression(Expression expression) {
        boolean changed = false;
        if (expression instanceof BinaryOperator) {
            Optimized optimized = optimizeBinary((BinaryOperator) expression);
            changed = optimized.isOptimized();
            expression = (Expression) optimized.getObject();
        }

        if (expression instanceof UnaryOperator) {
            Optimized optimized = optimizeUnary((UnaryOperator) expression);
            changed = optimized.isOptimized();
            expression = (Expression) optimized.getObject();
        }

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            Optimized optimized = optimizeExpression(expression.getExpression(i));
            changed = changed || optimized.isOptimized();
            expression.setExpression(i, (Expression) optimized.getObject());
        }

        return new Optimized(expression, changed);
    }

    private static Optimized optimizeBinary(BinaryOperator binaryOperator) {
        Optimized left = optimizeExpression(binaryOperator.getLeft());
        Optimized right = optimizeExpression(binaryOperator.getRight());

        boolean changed = left.isOptimized() || right.isOptimized();

        binaryOperator = new BinaryOperator(binaryOperator.getOperatorType(),
                (Expression) left.getObject(), (Expression) right.getObject(), binaryOperator.getType());

        Expression optimizedExpression = binaryOperator;
        Optimized optimized = null;
        switch (binaryOperator.getOperatorType()) {
            case ADDITION:
                optimized = optimizeAddition(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case SUBTRACTION:
                optimized = optimizeSubtraction(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case MULTIPLICATION:
                optimized = optimizeMultiplication(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case DIVISION:
                optimized = optimizeDivision(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case MODULUS:
                optimized = optimizeModulus(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case LESS_THAN:
                optimized = optimizeLessThan(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case GREATER_THAN:
                optimized = optimizeGreaterThan(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case LESS_EQUALS:
                optimized = optimizeLessEquals(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case GREATER_EQUALS:
                optimized = optimizeGreaterEquals(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case EQUALS:
                optimized = optimizeEquals(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case NOT_EQUALS:
                optimized = optimizeNotEquals(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case AND:
                optimized = optimizeAnd(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case OR:
                optimized = optimizeOr(binaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            default:
                break;
        }
        return new Optimized(optimizedExpression, changed);
    }

    private static Optimized optimizeAddition(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // string + string
        if (left instanceof StringValue && right instanceof StringValue) {
            return new Optimized(new StringValue(((StringValue) left).getString() + ((StringValue) right).getString()),
                    true);
        }

        // string + int
        if (left instanceof StringValue && right instanceof IntValue) {
            return new Optimized(new StringValue(((StringValue) left).getString() + right.toString()), true);
        }

        // int + string
        if (left instanceof IntValue && right instanceof StringValue) {
            return new Optimized(new StringValue(left.toString() + ((StringValue) right).getString()), true);
        }

        // string + float
        if (left instanceof StringValue && right instanceof FloatValue) {
            return new Optimized(new StringValue(((StringValue) left).getString() + right.toString()), true);
        }

        // float + string
        if (left instanceof FloatValue && right instanceof StringValue) {
            return new Optimized(new StringValue(left.toString() + ((StringValue) right).getString()), true);
        }

        // int + int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new IntValue(((IntValue) left).getValue() + ((IntValue) right).getValue()), true);
        }

        // float + float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() + ((FloatValue) right).getValue()),
                    true);
        }

        // int + float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((IntValue) left).getValue() + ((FloatValue) right).getValue()), true);
        }

        // float + int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() + ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeSubtraction(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int - int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new IntValue(((IntValue) left).getValue() - ((IntValue) right).getValue()), true);
        }

        // float - float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() - ((FloatValue) right).getValue()),
                    true);
        }

        // int - float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((IntValue) left).getValue() - ((FloatValue) right).getValue()), true);
        }

        // float - int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() - ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeMultiplication(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int * int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new IntValue(((IntValue) left).getValue() * ((IntValue) right).getValue()), true);
        }

        // float * float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() * ((FloatValue) right).getValue()),
                    true);
        }

        // int * float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((IntValue) left).getValue() * ((FloatValue) right).getValue()), true);
        }

        // float * int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() * ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeDivision(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int / int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(
                    new FloatValue(((double) ((IntValue) left).getValue()) / ((IntValue) right).getValue()), true);
        }

        // float / float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() / ((FloatValue) right).getValue()),
                    true);
        }

        // int / float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((IntValue) left).getValue() / ((FloatValue) right).getValue()), true);
        }

        // float / int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() / ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeModulus(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int % int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new IntValue(((IntValue) left).getValue() % ((IntValue) right).getValue()), true);
        }

        // float % float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() % ((FloatValue) right).getValue()),
                    true);
        }

        // int % float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(new FloatValue(((IntValue) left).getValue() % ((FloatValue) right).getValue()), true);
        }

        // float % int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(new FloatValue(((FloatValue) left).getValue() % ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeLessThan(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int < int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new BooleanValue(((IntValue) left).getValue() < ((IntValue) right).getValue()),
                    true);
        }

        // float < float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() < ((FloatValue) right).getValue()), true);
        }

        // int < float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() < ((FloatValue) right).getValue()), true);
        }

        // float < int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() < ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeGreaterThan(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int > int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new BooleanValue(((IntValue) left).getValue() > ((IntValue) right).getValue()),
                    true);
        }

        // float > float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() > ((FloatValue) right).getValue()), true);
        }

        // int > float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() > ((FloatValue) right).getValue()), true);
        }

        // float > int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() > ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeLessEquals(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int <= int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(new BooleanValue(((IntValue) left).getValue() <= ((IntValue) right).getValue()),
                    true);
        }

        // float <= float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() <= ((FloatValue) right).getValue()), true);
        }

        // int <= float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() <= ((FloatValue) right).getValue()), true);
        }

        // float <= int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() <= ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeGreaterEquals(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int >= int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() >= ((IntValue) right).getValue()),
                    true);
        }

        // float >= float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() >= ((FloatValue) right).getValue()), true);
        }

        // int >= float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() >= ((FloatValue) right).getValue()), true);
        }

        // float >= int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() >= ((IntValue) right).getValue()), true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeEquals(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int == int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() == ((IntValue) right).getValue()),
                    true);
        }

        // float == float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() == ((FloatValue) right).getValue()), true);
        }

        // int == float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() == ((FloatValue) right).getValue()), true);
        }

        // float == int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() == ((IntValue) right).getValue()), true);
        }

        // string == string
        if (left instanceof StringValue && right instanceof StringValue) {
            return new Optimized(
                    new BooleanValue(((StringValue) left).getString().equals(((StringValue) right).getString())),
                    true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeNotEquals(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // int != int
        if (left instanceof IntValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() != ((IntValue) right).getValue()),
                    true);
        }

        // float != float
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() != ((FloatValue) right).getValue()), true);
        }

        // int != float
        if (left instanceof IntValue && right instanceof FloatValue) {
            return new Optimized(
                    new BooleanValue(((IntValue) left).getValue() != ((FloatValue) right).getValue()), true);
        }

        // float != int
        if (left instanceof FloatValue && right instanceof IntValue) {
            return new Optimized(
                    new BooleanValue(((FloatValue) left).getValue() != ((IntValue) right).getValue()), true);
        }

        // string != string
        if (left instanceof StringValue && right instanceof StringValue) {
            return new Optimized(
                    new BooleanValue(!((StringValue) left).getString().equals(((StringValue) right).getString())),
                    true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeAnd(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // boolean && boolean
        if (left instanceof BooleanValue && right instanceof BooleanValue) {
            return new Optimized(
                    new BooleanValue(((BooleanValue) left).getValue() && ((BooleanValue) right).getValue()),
                    true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeOr(BinaryOperator binaryOperator) {
        Expression left = binaryOperator.getLeft();
        Expression right = binaryOperator.getRight();

        // boolean || boolean
        if (left instanceof BooleanValue && right instanceof BooleanValue) {
            return new Optimized(
                    new BooleanValue(((BooleanValue) left).getValue() || ((BooleanValue) right).getValue()),
                    true);
        }

        return new Optimized(binaryOperator, false);
    }

    private static Optimized optimizeUnary(UnaryOperator unaryOperator) {
        Optimized operand = optimizeExpression(unaryOperator.getOperand());

        boolean changed = operand.isOptimized();

        unaryOperator = new UnaryOperator(unaryOperator.getOperatorType(), (Expression) operand.getObject(),
                unaryOperator.getType());

        Expression optimizedExpression = unaryOperator;
        Optimized optimized = null;
        switch (unaryOperator.getOperatorType()) {
            case UNARY_NEGATION:
                optimized = optimizeNegation(unaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
                break;
            case NOT:
                optimized = optimizeNot(unaryOperator);
                changed = changed || optimized.isOptimized();
                optimizedExpression = (Expression) optimized.getObject();
            default:
                break;
        }
        return new Optimized(optimizedExpression, changed);
    }

    private static Optimized optimizeNegation(UnaryOperator unaryOperator) {
        Expression operand = unaryOperator.getOperand();

        // -int
        if (operand instanceof IntValue) {
            return new Optimized(new IntValue(-((IntValue) operand).getValue()), true);
        }

        // -float
        if (operand instanceof FloatValue) {
            return new Optimized(new FloatValue(-((FloatValue) operand).getValue()), true);
        }

        return new Optimized(unaryOperator, false);
    }

    private static Optimized optimizeNot(UnaryOperator unaryOperator) {
        Expression operand = unaryOperator.getOperand();

        // !boolean
        if (operand instanceof BooleanValue) {
            return new Optimized(new BooleanValue(!((BooleanValue) operand).getValue()), true);
        }

        return new Optimized(unaryOperator, false);
    }

}
