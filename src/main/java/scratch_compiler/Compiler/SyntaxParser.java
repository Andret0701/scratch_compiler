package scratch_compiler.Compiler;

import java.util.ArrayList;

public class SyntaxParser {

    public static ArrayList<SyntaxToken> parseCode(String _code) {
        Code code = new Code(_code);
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();

        while (!code.isAtEnd()) {

        }

        return tokens;
    }
}
