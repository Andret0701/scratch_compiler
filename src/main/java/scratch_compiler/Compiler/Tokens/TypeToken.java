package scratch_compiler.Compiler.Tokens;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;
import scratch_compiler.Compiler.Type;

public abstract class TypeToken extends SyntaxToken {
    protected Type type;

    public TypeToken(int position, Type type) {
        super(position, type.getType(), null);
        this.type = type;
    }

    public String getOpcode() {
        return "Type " + type.getType();
    }

    public boolean isNextToken(Code code) {
        return code.startsWith(type.getType());
    }

    public void stripToken(Code code) {
        code.stripToken(type.getType());
    }

}

// Number val = 5;
// Number val = 5.5; error allready defined
// val = 5.5;
// String val = "Hello";