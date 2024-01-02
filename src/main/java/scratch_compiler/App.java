package scratch_compiler;

import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.ValueFields.VariableField;
public class App {
    public static void main(String[] args) throws Exception {
        //MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        //System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();
        Figure figure =new Figure("Cat");
        Block block = new StartBlock();
        for (int i = 0; i < 0; i++) {
            figure.addBlock(block);
        }

        figure.addBlock(new MoveBlock(new VariableField("test", true)));
        figure.addBlock(new MoveBlock(new VariableField("test", false)));
        figure.addBlock(new MoveBlock(new VariableField("hello", true)));

        System.out.println(project.addFigure(figure, 10, 10));
        System.out.println(project.addFigure(figure, 20, 20));
        System.out.println(project.addFigure(figure, 30, 30));
        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(ParserJSON.parse(file).toJSON());

        ZipUtils.writeZipFile("twocats.sb3", "project.json", projectJSON.toJSON());
    }
}
