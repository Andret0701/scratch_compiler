package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.StructValue;

public class StructParser {
    public static StructValue parse(Type type, TokenReader tokens, DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.OPEN_BRACE);
        ArrayList<Expression> values = new ArrayList<>();
        for (int i = 0; i < type.getFields().size(); i++) {
            values.add(ExpressionParser.parse(type.getFields().get(i).getType(), tokens, declarationTable));
            if (i < type.getFields().size() - 1)
                tokens.expectNext(TokenType.COMMA);
        }
        tokens.expectNext(TokenType.CLOSE_BRACE);
        return new StructValue(type, values);
    }
}
