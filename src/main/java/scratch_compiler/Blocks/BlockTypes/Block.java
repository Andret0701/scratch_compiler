package scratch_compiler.Blocks.BlockTypes;

import java.util.ArrayList;

import scratch_compiler.Input;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.Variables.Variable;

public class Block {
    protected String opcode;
    protected Block next=null;
    protected Block parent=null;
    protected ArrayList<Input> inputs = new ArrayList<>();
    public Block(String opcode) {
        this.opcode = opcode;
    }

    public String getOpcode() {
        return opcode;
    }

    public Block getNext() {
        return next;
    }

    public Block getParent() {
        return parent;
    }

    
    public void setNext(Block block) {
        if (block == this || next == block)
        return;
        next = block;
    }
    
    public void setParent(Block block) {
        if (block == this || parent == block)
            return;
        parent = block;
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

    // public void connectTo(Block parent) {
    //     if (parent == this || this.parent == parent)
    //         return;

    //     if (next == parent && parent.parent == this)
    //         parent.connectTo(null);

    //     Block oldParent = this.parent;
    //     this.parent = parent;

    //     if (oldParent != null)
    //         oldParent.setNext(null);

    //     if (parent != null) {
    //         if (parent.next != null)
    //             parent.next.setParent(null);
    //         parent.setNext(this);
    //     }
    // }

    // public void connectInside(ContainerBlock block) {
    //     if (block == this || this.parent == block)
    //         return;

    //     if (next == block && block.parent == this)
    //         block.connectInside(null);

    //     Block oldParent = this.parent;
    //     this.parent = block;

    //     if (oldParent != null)
    //         oldParent.setNext(null);

    //     if (block != null) {
    //         if (block.child != null)
    //             block.child.setParent(null);
    //         block.setChild(this);
    //     }
    // }

    protected ArrayList<Block> getInputsBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();
        for (Input input : inputs) {
            ArrayList<Block> inputBlocks = input.getValueField().getBlocks();
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
     
    public ArrayList<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(this);
        if (next != null)
            blocks.addAll(next.getBlocks());

        blocks.addAll(getInputsBlocks());
    

        return blocks;
    }

    public ArrayList<Variable> getVariables() {
        return new ArrayList<Variable>(getInputsVariables());
    }

    @Override
    public Block clone() {
        Block block = new Block(opcode);
        block.inputs = new ArrayList<>(inputs);

        if (next!=null&& block.next==next)
            block.next = next.clone();
        return block;
    }
}
