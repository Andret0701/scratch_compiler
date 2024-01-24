package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;

public class SayBlock extends StackBlock {

    public SayBlock(ValueField message) {
        super("looks_say");
        setMessage(message);
    }

    public SayBlock(String message) {
        this(new StringField(message));
    }

    public void setMessage(ValueField message) {
        setInput("MESSAGE", message);
    }


}
