package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
public class VariableDeclaration extends Statement{
    private String name;
    private Expression expression;
    private VariableType type;    

    public VariableDeclaration(String name, VariableType type, Expression expression) {
        this.name = name;
        this.expression = expression;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name + " = " + expression.toString();
    }
}