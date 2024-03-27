package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.StatementParser;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class CompilerUtils {

    public static String readFile(String path) {
        try {
            java.io.File file = new java.io.File(path);
            java.util.Scanner input = new java.util.Scanner(file);
            String text = "";
            while (input.hasNext()) {
                text += input.nextLine() + "\n";
            }
            input.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void throwExpected(String expected, int line, String found) throws RuntimeException {
        throw new RuntimeException("Expected " + expected + " at line " + line + ". Found '" + found + "'");
    }

    public static void throwExpected(String expected, int line, Token found) throws RuntimeException {
        if (found == null)
            throwExpected(expected, line, "nothing");
        throwExpected(expected, line, found.getValue());
    }

    public static void throwMustBeOfType(TokenType expected, TokenType found) throws RuntimeException {
        throw new IllegalArgumentException("Expected " + expected + ". Found " + found);
    }

    public static void throwOperationNotDefined(TokenType operator, TokenType left, TokenType right)
            throws RuntimeException {
        throw new IllegalArgumentException(
                "Operation " + operator + " not defined for types " + left + " and " + right);
    }

    public static void assertIsType(Token token, TokenType type) throws RuntimeException {
        if (token.getType() != type)
            throwMustBeOfType(type, token.getType());
    }

    public static void throwInvalidType(String name, int line, TypeDefinition found, TypeDefinition expected) {
        throw new RuntimeException(
                "Invalid type " + found + " for " + name + " at line " + line + ". Expected " + expected);
    }

    public static void throwError(String string, int line) {
        throw new RuntimeException(string + " at line " + line);
    }

}
