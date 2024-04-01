package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

public class IntermediateTable {
    private ArrayList<String> usedTemps = new ArrayList<>();

    public IntermediateTable() {
    }

    public String getUniqueTemp(String name) {
        String temp = "temp:" + name;
        int i = 2;
        while (containsTemp(temp)) {
            temp = "temp:" + name + i;
            i++;
        }
        usedTemps.add(temp);
        return temp;
    }

    private boolean containsTemp(String temp) {
        return usedTemps.contains(temp);
    }
}
