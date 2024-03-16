package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableReference {
    private String name;
    private Expression index;
    private VariableReference next;

    public VariableReference(String name) {
        this.name = name;
        this.index = null;
        this.next = null;
    }

    public VariableReference(String name, Expression index) {
        this.name = name;
        this.index = index;
        this.next = null;
    }

    public VariableReference(String name, Expression index, VariableReference next) {
        this.name = name;
        this.index = index;
        this.next = next;
    }

    public void setNext(VariableReference next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public Expression getIndex() {
        return index;
    }

    public VariableReference getNext() {
        return next;
    }

    @Override
    public String toString() {
        String out = name;
        if (index != null)
            out += "[" + index + "]";

        if (next != null)
            out += "." + next;

        return out;
    }
}
