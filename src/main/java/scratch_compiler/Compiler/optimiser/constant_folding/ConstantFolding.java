package scratch_compiler.Compiler.optimiser.constant_folding;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
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
        if (expression instanceof BinaryOperator) {
            return optimizeBinary((BinaryOperator) expression);
        }

        return new Optimized(expression, false);
    }

    private static Optimized optimizeBinary(BinaryOperator binaryOperator) {
        Optimized left = optimizeExpression(binaryOperator.getLeft());
        Optimized right = optimizeExpression(binaryOperator.getRight());

        boolean changed = left.isOptimized() || right.isOptimized();

        binaryOperator = new BinaryOperator(binaryOperator.getOperatorType(),
                (Expression) left.getObject(), (Expression) right.getObject(), binaryOperator.getType());

        switch (binaryOperator.getOperatorType()) {
            case ADDITION:
                return optimizeAddition(binaryOperator);
            case SUBTRACTION:
                return optimizeSubtraction(binaryOperator);
            case MULTIPLICATION:
                return optimizeMultiplication(binaryOperator);
            case DIVISION:
                return optimizeDivision(binaryOperator);
            case MODULUS:
                return optimizeModulus(binaryOperator);
            case LESS_THAN:
                return optimizeLessThan(binaryOperator);
            case GREATER_THAN:
                return optimizeGreaterThan(binaryOperator);
            case LESS_EQUALS:
                return optimizeLessEquals(binaryOperator);
            case GREATER_EQUALS:
                return optimizeGreaterEquals(binaryOperator);
            case EQUALS:
                return optimizeEquals(binaryOperator);
            case NOT_EQUALS:
                return optimizeNotEquals(binaryOperator);
            case AND:
                return optimizeAnd(binaryOperator);
            case OR:
                return optimizeOr(binaryOperator);
            default:
                break;
        }
        return new Optimized(binaryOperator, changed);
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

}
