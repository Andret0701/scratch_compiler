package scratch_compiler.Compiler.optimiser;

public class Optimized {
    private Object object;
    private boolean optimized;

    public Optimized(Object object, boolean optimized) {
        this.object = object;
        this.optimized = optimized;
    }

    public Object getObject() {
        return object;
    }

    public boolean isOptimized() {
        return optimized;
    }
}
