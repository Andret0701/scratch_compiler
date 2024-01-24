package scratch_compiler;


import java.util.ArrayList;

import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.GreenFlagBlock;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.ScratchAssembler.ScratchAssembler;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.Types.Vector2;

public class App {
    public static void main(String[] args) throws Exception {
        ScratchProject project = new ScratchProject();

        Figure figure =new Figure("Cat");
        
        String code = CompilerUtils.readFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\code.txt");
        
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("x");
        //figure.addBlock(new Function("leo sin funksjon",true, arguments));
        GreenFlagBlock startBlock = new GreenFlagBlock();
        startBlock.push(ScratchAssembler.assemble(code));

        figure.addBlock(startBlock);
        figure.addCostume(new Costume("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\Assets\\cat2.svg", new Vector2(53, 46)));
        project.addFigure(figure);
        Background background =new Background();
        background.addCostume(new Costume("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\Assets\\cat1.svg", new Vector2(0, 0)));
        project.setBackgroud(background);

        try {
            String content = ZipUtils.readZipFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3", "project.json");
            System.out.println(content);
            ToJSON json =ParserJSON.parse (content);
            System.out.println("Project:" + json.toJSON());
          //  ZipUtils.writeZipFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\myScratchProject2.sb3", "project.json", json.toJSON());

        } catch (Exception e) {
            System.out.println(e);
        }
        ScratchProjectCreater.createProject("myScratchProject2", "C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler", project);

    }
}

