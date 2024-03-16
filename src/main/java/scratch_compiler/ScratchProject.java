package scratch_compiler;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;

public class ScratchProject {
    private Background background;
    private HashMap<String,Figure> figures;
    public ScratchProject() {
        background = new Background();
        figures = new HashMap<>();
    }

    public void addFigure(Figure figure) {
        if (background.getName().equals(figure.getName()))
            throw new IllegalArgumentException("Figure name cannot be the same as the background name");

        if (figures.containsKey(figure.getName()))
            throw new IllegalArgumentException("Figure name already exists");            

        figures.put(figure.getName(), figure);
    }

    public ArrayList<ScratchVariable> getGlobalVariables() {
        ArrayList<ScratchVariable> globalVariables = new ArrayList<>();
        globalVariables.addAll(background.getGlobalVariables());
        for (Figure figure : figures.values())
        {
            for (ScratchVariable variable : figure.getGlobalVariables())
            {
                if (!globalVariables.contains(variable))
                    globalVariables.add(variable);
            }
        }
        return globalVariables;
    }

    public ArrayList<Figure> getFigures() {
        return new ArrayList<>(figures.values());
    }

    public Background getBackground() {
        return background;
    }

    public ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<>();
        assets.addAll(background.getAssets());
        for (Figure figure : getFigures())
            assets.addAll(figure.getAssets());
        return assets;
    }

    public void setBackgroud(Background background) {
        this.background = background;
    }

}
