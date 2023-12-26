package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.SyntaxToken;
import scratch_compiler.ValueFields.NumberField;

public class App {
    public static void main(String[] args) throws Exception {
        MoveBlock moveBlock = new MoveBlock(new NumberField(10));
        System.out.println(moveBlock.toJSON());



        ScratchProject project = new ScratchProject();
        Figure figure =new Figure("Cat");

        System.out.println(project.addFigure(figure, 10, 10));
        System.out.println(project.addFigure(figure, 20, 20));
        System.out.println(project.addFigure(figure, 30, 30));
        


        String file = ZipUtils.readZipFile("twocats.sb3", "project.json");
        System.out.println(file);

        ZipUtils.writeZipFile("twocats.sb3", "project.json", project.toJSON());
        //add move block to cat

    }
}
