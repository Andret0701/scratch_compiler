package scratch_compiler;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.BounceOnEdgeBlock;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.LoopBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.SetXBlock;
import scratch_compiler.Blocks.ChangeVariableBlock;
import scratch_compiler.Blocks.IfBlock;
import scratch_compiler.Blocks.IfElseBlock;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.ScratchValues.DirectionField;
import scratch_compiler.ValueFields.ScratchValues.TimerField;
import scratch_compiler.ValueFields.ScratchValues.XPositionField;
import scratch_compiler.ValueFields.ScratchValues.YPositionField;
import scratch_compiler.Blocks.StartBlock;
import scratch_compiler.Blocks.UnaryOperatorBlock;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.RoundField;
import scratch_compiler.ValueFields.SubtractionField;
public class App {
    public static void main(String[] args) throws Exception {
        //MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        //System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();

        Figure figure =new Figure("Cat");
        
        StartBlock startBlock = new StartBlock();

        figure.addBlock(new MoveBlock(new AdditionField(new TimerField(), new XPositionField())));
        figure.addBlock(new MoveBlock(new DivisionField(new DirectionField(), new YPositionField())));
        figure.addBlock(new MoveBlock(new RoundField(new TimerField())));
        figure.addBlock(new SetXBlock(new ModulusField(new TimerField(), new NumberField(10))));
        
        System.out.println(project.addFigure(figure, 10, 10));



        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(ParserJSON.parse(file).toJSON());

        ZipUtils.writeZipFile("twocats.sb3", "project.json", projectJSON.toJSON());
    }
}

