package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;

public class ArgumentParser {
    public static ArrayList<Variable> parseArguments(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<Variable> arguments = new ArrayList<>();

        if (!nextIsArgument(tokens, declarationTable))
            return arguments;

        arguments.add(parseArgument(tokens, declarationTable));
        while (true) {
            if (tokens.isNext(TokenType.COMMA))
                tokens.pop();
            else
                break;
            arguments.add(parseArgument(tokens, declarationTable));
        }
        return arguments;
    }

    private static Variable parseArgument(TokenReader tokens, DeclarationTable declarationTable) {
        Type type = TypeParser.parse(tokens, declarationTable);
        String name = tokens.pop().getValue();
        return new Variable(name, type);
    }

    private static boolean nextIsArgument(TokenReader tokens, DeclarationTable declarationTable) {
        return TypeParser.nextIsType(tokens, declarationTable);
    }
}
