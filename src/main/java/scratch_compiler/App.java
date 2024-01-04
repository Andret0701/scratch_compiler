package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.Blocks.AddListBlock;
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
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.ScratchAssembler.ScratchAssembler;
import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.parser.StatementParser;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.ParserJSON;
import scratch_compiler.ScratchJSON.ScratchProjectToJSON;
import scratch_compiler.ScratchObjects.Figure;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.ListElementField;
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
        
        String code = CompilerUtils.readFile("C:\\Users\\andre\\OneDrive\\Dokumenter\\java\\scratch_compiler\\code.txt");
        //figure.addBlock(ScratchAssembler.assemble(code));
        
        figure.addBlock(new AddListBlock("list", true, new ListElementField("test",true, new NumberField(1))));
        System.out.println(project.addFigure(figure, 10, 10));


        
        ObjectJSON projectJSON=ScratchProjectToJSON.projectToJSON(project);
        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(ParserJSON.parse(file).toJSON());

        ZipUtils.writeZipFile("twocats.sb3", "project.json", projectJSON.toJSON());

        


    }
}

