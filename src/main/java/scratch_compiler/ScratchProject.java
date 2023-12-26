package scratch_compiler;

import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.NestedJSON;

public class ScratchProject extends NestedJSON {

    public ScratchProject() {
        super();
        setArray("targets", new ArrayJSON());
        getArray("targets").add(new Background());
        setArray("monitors", new ArrayJSON());
        setArray("extensions", new ArrayJSON());   
        setNested("meta", getMetadata());
    }

    public boolean containsFigure(String name)
    {        
        if (containsName(name)&&getBoolean("isStage")==false)
            return true;
        return false;
    }

    private boolean containsName(String name)
    {
        for (Object element : getArray("targets"))
        {
            NestedJSON nested = (NestedJSON) element;
            if(nested.getString("name").equals(name))
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
        getArray("targets").add(clone);
        return name;
    }

    public String addFigure(Figure figure,int x,int y) {
        String name = getValidName(figure.getName());
        Figure clone = figure.clone();
        clone.setName(name);
        clone.setPosition(x, y);
        getArray("targets").add(clone);
        return name;
    }

    public void removeFigure(String name) {
        if (!containsFigure(name))
            return;

        for (int i = 0; i < getArray("targets").size(); i++)
        {
            NestedJSON nested = (NestedJSON) getArray("targets").get(i);
            if(nested.getString("name").equals(name))
            {
                getArray("targets").remove(i);
                return;
            }
        }
    }

    public Figure getFigure(String name) {
        if (!containsFigure(name))
            return null;

        for (Object element : getArray("targets"))
        {
            NestedJSON nested = (NestedJSON) element;
            if(nested.getString("name").equals(name))
                return (Figure) nested;
        }
        return null;
    }

    private NestedJSON getMetadata() {
        NestedJSON metadata = new NestedJSON();
        metadata.setString("semver", "3.0.0");
        metadata.setString("vm", "0.2.0-prerelease.20220222132735");
        metadata.setString("agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Scratch/3.29.1 Chrome/94.0.4606.81 Electron/15.3.1 Safari/537.36");
        return metadata;
    }

}
