package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.SyntaxToken;

public class App {
    public static void main(String[] args) throws Exception {
        // Block block = new Block("motion_ifonedgebounce", null, null);
        // System.out.println(block.toJSON());

        // load code.txt
        String code = Utils.readFile("src\\code.txt");
        ArrayList<SyntaxToken> tokens = Compiler.parseCode(code);

        for (SyntaxToken token : tokens) {
            System.out.println(token);
        }

        // ScratchProject project = new ScratchProject();
        // String file = ZipUtils.readZipFile("Mitt Scratch-prosjekt2 (8).sb3",
        // "project.json");
        // System.out.println(file);

        // ZipUtils.writeZipFile("Mitt Scratch-prosjekt2 (8).sb3", "project.json",
        // project.toJSON());

    }
}
