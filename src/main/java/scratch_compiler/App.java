package scratch_compiler;

import java.beans.Expression;
import java.lang.reflect.Array;
import java.util.ArrayList;

import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.WaitBlock;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Blocks.PenClearBlock;
import scratch_compiler.Blocks.PenUpBlock;
import scratch_compiler.Blocks.SetPositionBlock;
import scratch_compiler.Blocks.FunctionCallBlock;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.GreenFlagBlock;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.ScratchAssembler.ScratchAssembler;
import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.parser.ExpressionParser;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.Types.Vector2;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<String> flags = getFlags(args);
        boolean optimise = flags.contains("-o");
        String fileName = getFileName(args);

        ScratchProject project = new ScratchProject();

        Figure figure = new Figure("Pen");
        // figure.setDirection(90);

        String code = CompilerUtils
                .readFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\snake.scc");

        ScratchProgram program = ScratchAssembler.assemble(code);

        GreenFlagBlock greenFlagBlock = new GreenFlagBlock();
        greenFlagBlock.push(new PenClearBlock());
        greenFlagBlock.push(new PenUpBlock());
        greenFlagBlock.push(new SetPositionBlock(0, 0));
        greenFlagBlock.push(program.getStack());

        ArrayList<ValueField> fields = new ArrayList<ValueField>();
        fields.add(new NumberField(1));

        figure.addBlock(greenFlagBlock);
        for (HatBlock hatBlock : program.getHatBlocks()) {
            figure.addBlock(hatBlock);
        }

        figure.addCostume(
                new Costume("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\Assets\\empty.svg",
                        new Vector2(53, 46)));
        project.addFigure(figure);
        Background background = new Background();
        background.addCostume(
                new Costume("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\Assets\\background.svg",
                        new Vector2(0, 0)));
        project.setBackgroud(background);

        try {
            String content = ZipUtils.readZipFile(
                    "C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3",
                    "project.json");
            // System.out.println(content);
            ToJSON json = ParserJSON.parse(content);
            // System.out.println("Project:" + json.toJSON());
            // ZipUtils.writeZipFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3",
            // "project.json", json.toJSON());

        } catch (Exception e) {
            System.out.println(e);
        }
        ScratchProjectCreater.createProject("myScratchProject2",
                "C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler", project);

    }

    private static ArrayList<String> getFlags(String[] args) {
        ArrayList<String> flags = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-')
                flags.add(args[i]);

        }
        return flags;
    }

    private static String getFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) != '-')
                return args[i];
        }
        return null;
    }
}