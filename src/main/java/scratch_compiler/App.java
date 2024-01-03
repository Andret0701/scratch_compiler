package scratch_compiler;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.BounceOnEdgeBlock;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.LoopBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.ChangeVariableBlock;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.SubtractionField;
public class App {
    public static void main(String[] args) throws Exception {
        //MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        //System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();

        Figure figure =new Figure("Cat");
        
        StartBlock startBlock = new StartBlock();

        LoopForeverBlock loopForeverBlock = new LoopForeverBlock();
        startBlock.connectUnder(loopForeverBlock);

        MoveBlock moveBlock = new MoveBlock(new AdditionField(new SubtractionField(new MultiplicationField(new DivisionField(new ModulusField(new NumberField(10), new NumberField(2)), new NumberField(2)), new NumberField(2)), new NumberField(2)), new NumberField(2)));
        AdditionField additionField = new AdditionField(new NumberField(10), new NumberField(10));
        for (int i = 0; i < 20; i++) {
            AdditionField temp= new AdditionField(null, new NumberField(i));
            temp.setLeft(additionField);
            additionField = temp;
        }
        moveBlock.setSteps(additionField);
        
        loopForeverBlock.connectInside(moveBlock);
        moveBlock.addToStack(new BounceOnEdgeBlock());


        figure.addBlock(startBlock);
        System.out.println(project.addFigure(figure, 10, 10));



        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(ParserJSON.parse(file).toJSON());

        ZipUtils.writeZipFile("twocats.sb3", "project.json", projectJSON.toJSON());
    }
}
