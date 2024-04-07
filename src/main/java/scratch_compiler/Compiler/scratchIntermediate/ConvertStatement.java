package scratch_compiler.Compiler.scratchIntermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertStatement {
    public static ArrayList<Statement> convert(Statement statement) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (statement instanceof Push) {
            Push push = (Push) statement;
            statements.add(new SimpleArrayAssignment("stack:stack",
                    new SimpleVariableValue("stack:pointer", VariableType.INT), push.getExpression()));
            statements.add(new SimpleVariableAssignment("stack:pointer", new BinaryOperator(OperatorType.ADDITION,
                    new SimpleVariableValue("stack:pointer", VariableType.INT), new IntValue(1),
                    new Type(VariableType.INT))));

        } else if (statement instanceof Pop) {
            Pop pop = (Pop) statement;
            statements.add(new SimpleVariableAssignment("stack:pointer", new BinaryOperator(OperatorType.SUBTRACTION,
                    new SimpleVariableValue("stack:pointer", VariableType.INT), new IntValue(1),
                    new Type(VariableType.INT))));
            statements.add(
                    new SimpleVariableAssignment(pop.getName(), new SimpleArrayValue("stack:stack", VariableType.INT,
                            new SimpleVariableValue("stack:pointer", VariableType.INT))));
        } else if (statement instanceof ArrayPop) {
            ArrayPop arrayPop = (ArrayPop) statement;
            statements.add(new SimpleVariableAssignment("stack:pointer", new BinaryOperator(OperatorType.SUBTRACTION,
                    new SimpleVariableValue("stack:pointer", VariableType.INT), new IntValue(1),
                    new Type(VariableType.INT))));
            statements.add(new SimpleArrayAssignment(arrayPop.getName(),
                    new SimpleVariableValue("stack:pointer", VariableType.INT), arrayPop.getIndex()));
        } else if (statement instanceof Scope)
            statements.add(ConvertScope.convert((Scope) statement));

        else {
            for (int i = 0; i < statement.getScopeCount(); i++) {
                statement.setScope(i, ConvertScope.convert(statement.getScope(i)));
            }
            statements.add(statement);
        }

        return statements;
    }
}
