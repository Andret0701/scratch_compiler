package scratch_compiler.Variables;

public class NumberVariable extends Variable {
    private double value;

    public NumberVariable(String name, double value) {
        super(name);
        this.value = value;
    }

    @Override
    protected String valueToJSON() {
        return "" + value;
    }

}
