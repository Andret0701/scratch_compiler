package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;

public class ScratchProject {
    private Background background;
    private ArrayList<Figure> figures;
    public ScratchProject() {
        background = new Background();
        figures = new ArrayList<>();
    }

    public boolean containsFigure(String name)
    {        
        for (Figure figure : figures)
        {
            if(figure.getName().equals(name))
                return true;
        }

        return false;
    }

    private boolean containsName(String name)
    {
        if (name.equals(background.getName()))
            return true;

        for (Figure figure : figures)
        {
            if(figure.getName().equals(name))
                return true;
        }

        return false;
    }

    private String getValidName(String name)
    {
        if (!containsName(name))
            return name;
        
        int i = 2;
        while(containsName(name + i))
            i++;
        return name + i;
    }

    public String addFigure(Figure figure) {
        String name = getValidName(figure.getName());
        Figure clone = figure.clone();
        clone.setName(name);
        figures.add(clone);
        return name;
    }

    public String addFigure(Figure figure,int x,int y) {
        String name = getValidName(figure.getName());
        Figure clone = figure.clone();
        clone.setName(name);
        clone.setPosition(x, y);
        figures.add(clone);
        return name;
    }

    public void removeFigure(String name) {
        if (!containsFigure(name))
            return;

        for (int i = 0; i < figures.size(); i++)
        {
            if(figures.get(i).getName().equals(name))
            {
                figures.remove(i);
                return;
            }
        }
    }

    public Figure getFigure(String name) {
        if (!containsFigure(name))
            return null;

        for (Figure figure : figures)
        {
            if(figure.getName().equals(name))
                return figure;
        }
        return null;
    }

    public ArrayList<Variable> getGlobalVariables() {
        ArrayList<Variable> globalVariables = new ArrayList<>();
        globalVariables.addAll(background.getGlobalVariables());
        for (Figure figure : figures)
        {
            for (Variable variable : figure.getGlobalVariables())
            {
                if (!globalVariables.contains(variable))
                    globalVariables.add(variable);
            }
        }
        return globalVariables;
    }
    



    public ArrayList<Figure> getFigures() {
        return new ArrayList<>(figures);
    }

    public Background getBackground() {
        return background;
    }


    public ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<>();
        assets.addAll(background.getAssets());
        for (Figure figure : figures)
            assets.addAll(figure.getAssets());
        return assets;
    }

    public void setBackgroud(Background background) {
        this.background = background;
    }

}
