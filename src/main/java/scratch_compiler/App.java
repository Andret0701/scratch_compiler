package scratch_compiler;

import java.util.ArrayList;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Blocks.PenClearBlock;
import scratch_compiler.Blocks.PenUpBlock;
import scratch_compiler.Blocks.SetPositionBlock;
import scratch_compiler.Blocks.GreenFlagBlock;
import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.ScratchAssembler.ScratchAssembler;
import scratch_compiler.Compiler.ScratchAssembler.ScratchCoreAssembler;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.Types.Vector2;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<String> flags = getFlags(args);
        boolean optimize = flags.contains("-o");
        boolean debug = flags.contains("-d");
        String path = getFilePath(args);
        if (path == null) {
            System.out.println("How to use: scratch_compiler [flags] <file.scc>");
            return;
        }

        ScratchProject project = new ScratchProject();

        Figure figure = new Figure("Pen");
        String code = CompilerUtils.readFile(path);

        IntermediateCode intermediateCode;
        try {
            intermediateCode = Compiler.compile(code, ScratchCoreAssembler.getSystemCalls(), optimize, debug);
        } catch (Exception e) {
            System.out.println("Compilation error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        ScratchProgram program;
        try {
            program = ScratchAssembler.assemble(intermediateCode, optimize, debug);
        } catch (Exception e) {
            System.out.println("Scratch assembler error: " + e.getMessage());
            return;
        }

        GreenFlagBlock greenFlagBlock = new GreenFlagBlock();
        greenFlagBlock.push(program.getStack());

        figure.addBlock(greenFlagBlock);
        for (HatBlock hatBlock : program.getHatBlocks()) {
            figure.addBlock(hatBlock);
        }

        figure.addCostume(
                new Costume(".\\Assets\\empty.svg",
                        new Vector2(53, 46)));
        project.addFigure(figure);
        Background background = new Background();
        background.addCostume(
                new Costume(".\\Assets\\background.svg",
                        new Vector2(0, 0)));
        project.setBackgroud(background);

        // try {
        // String content = ZipUtils.readZipFile(
        // "C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3",
        // "project.json");
        // // System.out.println(content);
        // ToJSON json = ParserJSON.parse(content);
        // // System.out.println("Project:" + json.toJSON());
        // //
        // ZipUtils.writeZipFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3",
        // // "project.json", json.toJSON());

        // } catch (Exception e) {
        // System.out.println(e);
        // }

        String scratchName = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
        String scratchPath = path.substring(0, path.lastIndexOf("\\"));

        try {
            ScratchProjectCreater.createProject(scratchName, scratchPath, project);
        } catch (Exception e) {
            System.out.println("Error creating project: " + e.getMessage());
            return;
        }

        System.out.println("Successfully compiled " + scratchName + ".scc to " + scratchName + ".sb3");

    }

    private static ArrayList<String> getFlags(String[] args) {
        ArrayList<String> flags = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-')
                flags.add(args[i]);

        }
        return flags;
    }

    private static String getFilePath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) != '-') {
                String path = args[i];
                if (path.startsWith("\"") && path.endsWith("\"") && path.length() > 1)
                    path = path.substring(1, path.length() - 1);

                if (path.endsWith(".scc")) {
                    if (path.contains("\\"))
                        return path;
                    else
                        return System.getProperty("user.dir") + "\\" + path;
                }
            }
        }
        return null;
    }
}