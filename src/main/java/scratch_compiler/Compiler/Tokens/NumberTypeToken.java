package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;
import scratch_compiler.Compiler.Types.NumberType;

public class NumberTypeToken extends TypeToken {

    public NumberTypeToken(int position) {
        super(position, new NumberType());
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        // Find name
        NameToken nameToken = new NameToken(code.getPosition());
        if (!nameToken.isNextToken(code))
            throw new RuntimeException("Expected name after type declaration");
        nameToken.stripToken(code);
        tokens.addAll(nameToken.parseSubTokens(code));

        // Check if next is function or variable declaration
        FunctionDeclarationToken functionDeclarationToken = new FunctionDeclarationToken(code.getPosition());
        if (functionDeclarationToken.isNextToken(code)) {
            functionDeclarationToken.stripToken(code);
            tokens.addAll(functionDeclarationToken.parseSubTokens(code));
            return tokens;
        }

        VariableDeclarationToken variableDeclarationToken = new VariableDeclarationToken(code.getPosition());
        if (variableDeclarationToken.isNextToken(code)) {
            variableDeclarationToken.stripToken(code);
            tokens.addAll(variableDeclarationToken.parseSubTokens(code));
            return tokens;
        }

        throw new RuntimeException("Expected function or variable declaration after type declaration");
    }
}
