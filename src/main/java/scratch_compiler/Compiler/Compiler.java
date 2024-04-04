package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.ConvertToIntermediate;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.parser.BinaryOperatorDefinition;
import scratch_compiler.Compiler.parser.GlobalParser;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.types.BinaryOperatorDefinitionType;
import scratch_compiler.Compiler.parser.expressions.types.UnaryOperatorDefinitionType;

public class Compiler {

    public static IntermediateCode compile(String code, ArrayList<SystemCall> systemCalls, boolean optimise) {
        ArrayList<Token> tokens = Lexer.lex(code);
        System.out.println(tokens);

        TokenReader reader = new TokenReader(tokens);
        DeclarationTable declarationTable = createDeclarationTable(systemCalls);
        CompiledCode compiledCode = GlobalParser.parse(reader, declarationTable);
        System.out.println("Parsed code:");
        System.out.println(compiledCode);
        System.out.println();
        IntermediateCode intermediateCode = ConvertToIntermediate.convert(compiledCode);
        System.out.println("Intermediate code:");
        System.out.println(intermediateCode);
        System.out.println();
        // if (optimise)
        // compiledCode = Optimizer.optimize(compiledCode);

        // throw new UnsupportedOperationException("Not implemented yet");

        return intermediateCode;
    }

    private static DeclarationTable createDeclarationTable(ArrayList<SystemCall> systemCalls) {
        DeclarationTable declarationTable = new DeclarationTable();

        // Declare primitive types
        declarationTable.declareType(new TypeDefinition(VariableType.BOOLEAN));
        declarationTable.declareType(new TypeDefinition(VariableType.INT));
        declarationTable.declareType(new TypeDefinition(VariableType.FLOAT));
        declarationTable.declareType(new TypeDefinition(VariableType.STRING));
        declarationTable.declareType(new TypeDefinition(VariableType.VOID));

        // Declare unary operators
        for (UnaryOperatorDefinitionType unaryOperator : UnaryOperatorDefinitionType.values()) {
            declarationTable.declareOperator(new UnaryOperatorDefinition(unaryOperator.getOperatorType(),
                    new TypeDefinition(unaryOperator.getOperandType()),
                    new TypeDefinition(unaryOperator.getReturnType())));
        }

        // Declare binary operators
        for (BinaryOperatorDefinitionType binaryOperator : BinaryOperatorDefinitionType.values()) {
            declarationTable.declareOperator(new BinaryOperatorDefinition(binaryOperator.getOperatorType(),
                    new TypeDefinition(binaryOperator.getLeftType()), new TypeDefinition(binaryOperator.getRightType()),
                    new TypeDefinition(binaryOperator.getReturnType())));
        }

        // Declare type conversions
        declarationTable.declareConversion(new Type(VariableType.BOOLEAN),
                new Type(VariableType.INT));
        declarationTable.declareConversion(new Type(VariableType.INT),
                new Type(VariableType.FLOAT));
        declarationTable.declareConversion(new Type(VariableType.FLOAT),
                new Type(VariableType.STRING));

        // Declare system calls
        for (SystemCall systemCall : systemCalls)
            declarationTable.declareSystemCall(systemCall);
        return declarationTable;
    }
}
