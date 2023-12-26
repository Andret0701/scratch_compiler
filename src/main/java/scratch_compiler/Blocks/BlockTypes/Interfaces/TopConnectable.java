package scratch_compiler.Blocks.BlockTypes.Interfaces;

public interface TopConnectable extends Connectable {
    public void connectToTop(BottomConnectable connectable);
}
