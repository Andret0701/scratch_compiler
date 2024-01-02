package scratch_compiler.JSON;

public class NumberJSON implements ToJSON {
    private double value;

    public NumberJSON(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NumberJSON)
            return ((NumberJSON) other).getValue() == this.value;
        return false;
    }
    
    @Override
    public String toJSON() {
        return Double.toString(value);
    }
}
