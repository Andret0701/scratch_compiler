package scratch_compiler.Blocks;

import java.util.ArrayList;

public class Function extends Block {
    private String name;
    private ArrayList<String> arguments;
    private boolean isWarp;
    public Function(String name, boolean isWarp, ArrayList<String> arguments) {
        super("procedures_definition");
        this.name = name;
        this.isWarp = isWarp;
        this.arguments = new ArrayList<>(arguments);
    }

    
        //                         "next": "e7D`uPCy3h-v|0IKg(;R",
        //                         "parent": null,
        //                         "shadow": false,
        //                         "inputs": {"custom_block": [1,"k7mCR`,xw$)%^W_4GHb1"]},
        //                         "topLevel": true,
        //                         "x": 44,
        //                         "y": 44,
        //                         "opcode": "procedures_definition",
        //                         "fields": {}
        //                 }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFunctionArguments() {
        return arguments;
    }

    public boolean isWarp() {
        return isWarp;
    }

    @Override
    public Function clone() {
        Function function = new Function(name, isWarp, arguments);
        function.inputs = new ArrayList<>(inputs);//clone inputs later
        function.fields = new ArrayList<>(fields);
        function.children = new ArrayList<>(children);
        for (int i = 0; i < children.size(); i++) {
            Block child = children.get(i);
            if (child != null) {
                Block childClone = child.clone();
                function.connectChild(childClone, i);
            }
        }
        return new Function(name, isWarp, arguments);
    }

// },
// "t7Fb9%1MP|e_Fz/7P|yy": {
//         "next": "e7D`uPCy3h-v|0IKg(;R",
//         "parent": null,
//         "shadow": false,
//         "inputs": {"custom_block": [1,"k7mCR`,xw$)%^W_4GHb1"]},
//         "topLevel": true,
//         "x": 44,
//         "y": 44,
//         "opcode": "procedures_definition",
//         "fields": {}
// },

// "k7mCR`,xw$)%^W_4GHb1": {
//                                 "next": null,
//                                 "parent": "t7Fb9%1MP|e_Fz/7P|yy",
//                                 "mutation": {
//                                         "argumentnames": "["i1","i2"]",
//                                         "argumentdefaults": "["",""]",
//                                         "children": [],
//                                         "proccode": "function%s%s",
//                                         "argumentids": "["-g::9jN`.L$5hs?I!V@P","O*sp$B;:ZLK^nyx~EmJV"]",
//                                         "tagName": "mutation",
//                                         "warp": "true"
//                                 },
//                                 "shadow": true,
//                                 "inputs": {
//                                         "-g::9jN`.L$5hs?I!V@P": [1,"bLW#9$D;jo[K~T2zFAxa"],
//                                         "O*sp$B;:ZLK^nyx~EmJV": [1,"R,2.XA|r)L^!YK?QlcqD"]
//                                 },
//                                 "topLevel": false,
//                                 "opcode": "procedures_prototype",
//                                 "fields": {}
//                         },
}