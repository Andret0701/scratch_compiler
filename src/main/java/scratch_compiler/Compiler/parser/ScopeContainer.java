package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.parser.statements.Scope;

public interface ScopeContainer {
    public default Scope getScope(int index) {
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    public default int getScopeCount() {
        return 0;
    }

    public default void setScope(int index, Scope scope) {
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }
}
