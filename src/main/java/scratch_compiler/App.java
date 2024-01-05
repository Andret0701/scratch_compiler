package scratch_compiler;


import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.ScratchAssembler.ScratchAssembler;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;

public class App {
    public static void main(String[] args) throws Exception {
        //MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        //System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();

        Figure figure =new Figure("Cat");
        
        String code = CompilerUtils.readFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\code.txt");
        figure.addBlock(ScratchAssembler.assemble(code));
        project.addFigure(figure, 10, 10);


        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("Mitt Scratch-prosjekt2 (8).sb3", "project.json");
        System.out.println(ParserJSON.parse(file).toJSON());

        ZipUtils.writeZipFile("Mitt Scratch-prosjekt2 (8).sb3", "project.json", projectJSON.toJSON());

        


    }
}

