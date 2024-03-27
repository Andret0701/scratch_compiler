package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableReference {
    private String name;
    private VariableReference next;

    public VariableReference(String name) {
        this.name = name;
        this.next = null;
    }

    public VariableReference(String name, VariableReference next) {
        this.name = name;
        this.next = next;
    }

    public void setNext(VariableReference next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public VariableReference getNext() {
        return next;
    }

    @Override
    public String toString() {
        String out = name;
        if (next != null)
            out += "." + next;

        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariableReference) {
            VariableReference other = (VariableReference) obj;
            return name.equals(other.name)
                    && (next == null ? other.next == null : next.equals(other.next));
        }
        return false;
    }
}
