package scratch_compiler;

import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.Variables.Variable;
import scratch_compiler.Variables.VariableType;

public class App {
    public static void main(String[] args) throws Exception {
        MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();
        Figure figure =new Figure("Cat");
        Block block = new Block("event_whenflagclicked");
        figure.addBlock(block);
        figure.addVariable(new Variable("test", true, VariableType.BOOLEAN,true));
        figure.addVariable(new Variable("test2", false, VariableType.NUMBER));

        System.out.println(project.addFigure(figure, 10, 10));
        System.out.println(project.addFigure(figure, 20, 20));
        System.out.println(project.addFigure(figure, 30, 30));
        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(file);

        ZipUtils.writeZipFile("twocats.sb3", "project.json", projectJSON.toJSON());
        //add move block to cat

    }
}
