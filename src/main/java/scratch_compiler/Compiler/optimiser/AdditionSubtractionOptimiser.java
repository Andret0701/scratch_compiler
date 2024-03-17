package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;

public class AdditionSubtractionOptimiser {

    public static Expression optimise(Expression expression) {
        if (expression instanceof AdditionExpression)
            expression = optimiseExpression((AdditionExpression) expression);
        else if (expression instanceof SubtractionExpression)
            expression = optimiseExpression((SubtractionExpression) expression);
        else if (expression instanceof BinaryOperationExpression) {
            ((BinaryOperationExpression) expression)
                    .setLeft(optimize(((BinaryOperationExpression) expression).getLeft()));
            ((BinaryOperationExpression) expression)
                    .setRight(optimize(((BinaryOperationExpression) expression).getRight()));
        }
        return expression;
    }

    private static Expression optimiseExpression(BinaryOperationExpression expression) {
        ArrayList<Term> terms = getTerms(expression, true);
        terms = addTerms(terms, expression.getType());

        if (terms.size() == 1)
            return terms.get(0).getExpression();
        else if (terms.size() == 0)
            return new IntValue(0);

        Term first = terms.get(0);
        for (int i = 0; i < terms.size(); i++) {
            first = terms.get(i);
            if (first.isPositive()) {
                terms.remove(i);
                break;
            }
        }

        Expression optimizedExpression = first.getExpression();
        if (!first.isPositive()) {
            optimizedExpression = new SubtractionExpression();
            ((SubtractionExpression) optimizedExpression).setLeft(new IntValue(0));
            ((SubtractionExpression) optimizedExpression).setRight(first.getExpression());
        }

        for (Term term : terms) {
            if (term.isPositive()) {
                AdditionExpression newExpression = new AdditionExpression();
                newExpression.setLeft(optimizedExpression);
                newExpression.setRight(term.getExpression());
                optimizedExpression = newExpression;
            } else {
                SubtractionExpression newExpression = new SubtractionExpression();
                newExpression.setLeft(optimizedExpression);
                newExpression.setRight(term.getExpression());
                optimizedExpression = newExpression;
            }
        }
        return optimizedExpression;
    }

    private static ArrayList<Term> getTerms(BinaryOperationExpression expression, boolean positive) {
        System.out.println(
                expression.getClass().getSimpleName() + " Left: " + expression.getLeft().getClass().getSimpleName()
                        + " Right: " + expression.getRight().getClass().getSimpleName());
        ArrayList<Term> terms = new ArrayList<>();
        if ((expression.getLeft() instanceof AdditionExpression
                || expression.getLeft() instanceof SubtractionExpression)
                && expression.getType() == expression.getLeft().getType()) {
            terms.addAll(getTerms((BinaryOperationExpression) expression.getLeft(), positive));
        } else
            terms.add(new Term(expression.getLeft(), positive));

        if (expression instanceof SubtractionExpression)
            positive = !positive;

        if ((expression.getRight() instanceof AdditionExpression
                || expression.getRight() instanceof SubtractionExpression)
                && expression.getType() == expression.getRight().getType()) {
            terms.addAll(getTerms((BinaryOperationExpression) expression.getRight(), positive));
        } else
            terms.add(new Term(expression.getRight(), positive));

        return terms;
    }

    private static ArrayList<Term> addTerms(ArrayList<Term> terms, VariableType type) {
        ArrayList<Term> optimizedTerms = new ArrayList<>();
        IntValue sumInt = new IntValue(0);
        FloatValue sumFloat = new FloatValue(0);
        for (Term term : terms) {
            Expression expression = term.getExpression();
            boolean positive = term.isPositive();
            if (expression instanceof BooleanValue) {
                if (type == VariableType.INT)
                    sumInt = new IntValue(
                            add(sumInt.getValue(), (((BooleanValue) expression).getValue() ? 1 : 0), positive));
                else if (type == VariableType.FLOAT)
                    sumFloat = new FloatValue(
                            add(sumFloat.getValue(), (((BooleanValue) expression).getValue() ? 1 : 0), positive));
                else
                    optimizedTerms.add(term);
            } else if (term.getExpression() instanceof IntValue) {
                if (type == VariableType.INT)
                    sumInt = new IntValue(add(sumInt.getValue(), ((IntValue) expression).getValue(), positive));
                else if (type == VariableType.FLOAT)
                    sumFloat = new FloatValue(add(sumFloat.getValue(), ((IntValue) expression).getValue(), positive));
                else
                    optimizedTerms.add(term);
            } else if (term.getExpression() instanceof FloatValue)
                sumFloat = new FloatValue(add(sumFloat.getValue(), ((FloatValue) expression).getValue(), positive));
            else
                optimizedTerms.add(term);

        }

        if (sumInt.getValue() != 0)
            optimizedTerms.add(new Term(sumInt, true));
        if (sumFloat.getValue() != 0)
            optimizedTerms.add(new Term(sumFloat, true));
        return optimizedTerms;
    }

    private static int add(int operand1, int operand2, boolean positive) {
        if (positive)
            return operand1 + operand2;
        else
            return operand1 - operand2;
    }

    private static double add(double operand1, double operand2, boolean positive) {
        if (positive)
            return operand1 + operand2;
        else
            return operand1 - operand2;
    }

}
