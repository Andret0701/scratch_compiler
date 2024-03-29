package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.StructValue;

public class StructParser {
    public static StructValue parse(TypeDefinition type, TokenReader tokens, DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.OPEN_BRACE);
        ArrayList<Expression> values = new ArrayList<>();

        ArrayList<TypeField> fields = type.getFields();
        for (int i = 0; i < fields.size(); i++) {
            TypeDefinition field = fields.get(i).getType();
            Expression value = ExpressionParser.parse(new Type(field), tokens, declarationTable);
            if (value == null)
                CompilerUtils.throwError("Invalid struct value", tokens.peek().getLine());
            values.add(value);
            if (i < fields.size() - 1)
                tokens.expectNext(TokenType.COMMA);
        }
        tokens.expectNext(TokenType.CLOSE_BRACE);
        return new StructValue(type, values);
    }
}
