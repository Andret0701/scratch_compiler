package scratch_compiler.Compiler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.Compiler.Commands.Command;
import scratch_compiler.Compiler.Tokens.NewStatementToken;

public class Compiler {
    public static ArrayList<Block> compile(String code) {
        // code = removeComments(code);
        // code = removeWhitespace(code);

        ArrayList<Block> blocks = new ArrayList<Block>();
        Block currentBlock = null;

        // currentBlock = new StartBlock();
        // blocks.add(currentBlock);
        // while (code.length() != 0) {
        // if (code.startsWith("loop")) {
        // code.substring(4);
        // code = stripWhitespace(code);
        // if (!code.startsWith("("))
        // throw new RuntimeException("Expected '(' after loop");
        // code = code.substring(1);
        // code = stripWhitespace(code);

        // if (code.startsWith(")")) {
        // Block loopBlock = new LoopForeverBlock();
        // loopBlock.connectTo(currentBlock);
        // blocks.add(loopBlock);
        // currentBlock = loopBlock;
        // code = code.substring(1);
        // code = stripWhitespace(code);

        // }
        // else if()

        // }
        // }

        return blocks;
    }

    public static ArrayList<SyntaxToken> parseCode(String _code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<>();
        Code code = new Code(_code);

        NewStatementToken newStatementToken = new NewStatementToken(code.getPosition());
        tokens.addAll(newStatementToken.parseSubTokens(code));

        return tokens;
    }

    public static ArrayList<Command> compileTokens(ArrayList<SyntaxToken> tokens) {
        ArrayList<Command> commands = new ArrayList<>();
        for (SyntaxToken token : tokens) {
            commands.addAll(token.compile());
        }
        return commands;
    }

}
