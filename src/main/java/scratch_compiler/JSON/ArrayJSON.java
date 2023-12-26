package scratch_compiler.JSON;

import java.util.ArrayList;

public class ArrayJSON extends ArrayList<Object> implements ToJSON {

    @Override
    public String toJSON() {
        String json = "[";
        for (int i = 0; i < this.size(); i++) {
            json += this.getToJSON(i);
            if (i < this.size() - 1)
                json += ",";
        }

        json+="]";
        return json;
    }

    public String getToJSON(int index)
    {
        Object object=get(index);
        if(object instanceof ToJSON)
            return ((ToJSON) object).toJSON();

        if(object instanceof String)
            return "\""+object.toString()+"\"";
        
        return object.toString();
    }


}
