package scratch_compiler.Compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CodeLine {
    private ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
    private ArrayList<CodeLine> children = new ArrayList<CodeLine>();

    public CodeLine(String code) {
        tokens = Compiler.parseCode(code);
    }

    public void addChild(CodeLine child) {
        children.add(child);
    }

    public void addChildren(Collection<CodeLine> children) {
        this.children.addAll(children);
    }

    @Override
    public String toString() {
        String out = "Code: " + tokens.toString();
        if (children.size() == 0)
            return out;
        for (CodeLine child : children) {
            String childString = child.toString();
            childString = childString.replaceAll("\n", "\n   ");
            out += "\n   " + childString;
        }
        return out;
    }

}
