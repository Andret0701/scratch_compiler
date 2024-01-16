package scratch_compiler.Blocks;

import java.util.ArrayList;

import scratch_compiler.Field;
import scratch_compiler.Function;
import scratch_compiler.Input;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.Variable;

public class Block {
    protected String opcode;
    protected ArrayList<Block> children = new ArrayList<>();
    protected Block parent=null;
    protected ArrayList<Input> inputs = new ArrayList<>();
    protected ArrayList<Field> fields = new ArrayList<>();
    private Function function;
    public Block(String opcode) {
        this.opcode = opcode;
        this.function = null;
        children = new ArrayList<>();
        children.add(null);
    }

    public Block(String opcode, Function function) {
        this.opcode = opcode;
        this.function = function;
        children = new ArrayList<>();
        children.add(null);
    }



    public String getOpcode() {
        return opcode;
    }

    public Block getNext() {
        return children.get(0);
    }

    public ArrayList<Block> getInsideChildren() {
        ArrayList<Block> insideChildren = new ArrayList<>();
        for (int i = 1; i < children.size(); i++) {
            insideChildren.add(children.get(i));
        }
        return insideChildren;
    }

    public Block getParent() {
        return parent;
    }


    private boolean containsInput(String name) {
        for (Input input : inputs) {
            if (input.getName().equals(name))
                return true;
        }
        return false;
    }

    protected void setInput(String name, ValueField field) {
        if (!containsInput(name)){
            inputs.add(new Input(name, field));
            return;
        }
  
        for (int i = 0; i < inputs.size(); i++) {
            String inputName = inputs.get(i).getName();
            if (inputName.equals(name)) {
                inputs.set(i, new Input(inputName, field));
                return;
            }
        }
    }

    public ArrayList<Input> getInputs() {
        return new ArrayList<>(inputs);
    }

    private boolean containsField(String name) {
        for (Field field : fields) {
            if (field.getName().equals(name))
                return true;
        }
        return false;
    }

    protected void setField(Field field) {
        String name = field.getName();
        if (!containsField(name)){
            fields.add(field);
            return;
        }
  
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getName();
            if (fieldName.equals(name)) {
                fields.set(i, field);
                return;
            }
        }
        
    }

    public ArrayList<Field> getFields() {
        return new ArrayList<>(fields);
    }

    public void addToStack(Block child) {
        if (getNext() != null)
            getNext().addToStack(child);
        else
            connectUnder(child);
    }

    public void connectUnder(Block child) {
        connectChild(child, 0);
    }

    private void addChildren(int index) {
        while (children.size() <= index)
            children.add(null);
    }


    protected void connectChild(Block child, int childIndex) {
        addChildren(childIndex);
        if (child == this || children.get(childIndex) == child)
            return;
    
        if (child.parent != null && child.parent.children.contains(child))
            child.parent.disconnectChild(child);
    
        Block oldChild = this.children.get(childIndex);
        this.children.set(childIndex, child);
    
        if (oldChild != null)
            oldChild.parent = null;
    
        if (child != null) {
            if (child.parent != null)
                child.parent.disconnectChild(child);
            child.parent = this;
        }
    }

    protected void disconnectChild(Block child) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == child) {
                child.parent = null;
                children.set(i, null);
                return;
            }
        }
    }


    protected ArrayList<Block> getInputsBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();
        for (Input input : inputs) {
            ArrayList<Block> inputBlocks = input.getValueField().getBlocks(this);
            blocks.addAll(inputBlocks);
        }
        return blocks;
    }

    protected ArrayList<Variable> getInputsVariables() {
        ArrayList<Variable> variables = new ArrayList<>();
        for (Input input : inputs) {
            ArrayList<Variable> inputVariables = input.getValueField().getVariables();
            variables.addAll(inputVariables);
        }
        return variables;
    }

    protected ArrayList<Block> getFieldsBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();
        for (Field field : fields) {
            ArrayList<Block> fieldBlocks = field.getBlocks(this);
            blocks.addAll(fieldBlocks);
        }
        return blocks;
    }
    
    protected ArrayList<Variable> getFieldsVariables() {
        ArrayList<Variable> variables = new ArrayList<>();
        for (Field field : fields) {
            ArrayList<Variable> fieldVariables = field.getVariables();
            variables.addAll(fieldVariables);
        }
        return variables;
    }
     
    public ArrayList<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(this);
        for (Block child : children) {
            if (child != null)
                blocks.addAll(child.getBlocks());
        }

        blocks.addAll(getInputsBlocks());
        blocks.addAll(getFieldsBlocks());
    

        return blocks;
    }

    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.addAll(getInputsVariables());
        variables.addAll(getFieldsVariables());
        return variables;
    }

    @Override
    public Block clone() {
        Block block = new Block(opcode);
        block.inputs = new ArrayList<>(inputs);//clone inputs later
        block.fields = new ArrayList<>(fields);
        block.children = new ArrayList<>(children);
        for (int i = 0; i < children.size(); i++) {
            Block child = children.get(i);
            if (child != null) {
                Block childClone = child.clone();
                block.connectChild(childClone, i);
            }
        }

        return block;
    }

    public Function getFunction() {
        return function;
    }


    @Override
    public String toString() {
        String string = opcode;
        return string;
    }
}
