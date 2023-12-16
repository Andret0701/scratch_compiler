package scratch_compiler.Compiler.Tokens;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

import java.util.ArrayList;

public class FunctionDeclarationToken extends SyntaxToken {
    public FunctionDeclarationToken(int position) {
        super(position, "(", null);
    }

    public String getOpcode() {
        return "FunctionDeclaration";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith("(");
    }

    public void stripToken(Code code) {
        code.stripToken("(");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        FunctionEndToken functionEndToken = new FunctionEndToken(code.getPosition());
        if (functionEndToken.isNextToken(code)) {
            functionEndToken.stripToken(code);
            tokens.addAll(functionEndToken.parseSubTokens(code));
            return tokens;
        }

        throw new RuntimeException("Expected ')' at line " + code.getLineNumber());
    }
}

// Number name = number
