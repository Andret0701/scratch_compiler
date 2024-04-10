package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;

public class OptimisationEvaluator {

    private int lines;
    private int operations;
    private int variables;
    private int functions;

    public OptimisationEvaluator(IntermediateCode code) {
        this.lines = LineCounter.countLines(code);
        this.operations = OperationCounter.countOperations(code);
        this.variables = VariableCounter.countVariables(code);
        this.functions = code.getFunctions().size();
    }

    public void evaluate(IntermediateCode optimized) {
        evaluate(optimized, 0);
    }

    public void evaluate(IntermediateCode optimized, int padding) {
        int optimizedLines = LineCounter.countLines(optimized);
        int optimizedOperations = OperationCounter.countOperations(optimized);
        int optimizedVariables = VariableCounter.countVariables(optimized);
        int optimizedFunctions = optimized.getFunctions().size();

        if (lines != optimizedLines)
            System.out.println(" ".repeat(padding) +
                    lines + " lines -> " + optimizedLines + " lines: " + (lines - optimizedLines) + " lines " +
                    (lines - optimizedLines > 0 ? "removed" : "added"));
        if (operations != optimizedOperations)
            System.out.println(
                    " ".repeat(padding) + operations + " operations -> " + optimizedOperations + " operations: "
                            + (operations - optimizedOperations) + " operations "
                            + (operations - optimizedOperations > 0
                                    ? "removed"
                                    : "added"));
        if (variables != optimizedVariables)
            System.out.println(" ".repeat(padding) + variables + " variables -> " + optimizedVariables + " variables: "
                    + (variables - optimizedVariables) + " variables "
                    + (variables - optimizedVariables > 0 ? "removed" : "added"));
        if (functions != optimizedFunctions)
            System.out.println(" ".repeat(padding) + functions + " functions -> " + optimizedFunctions + " functions: "
                    + (functions - optimizedFunctions) + " functions " + (functions - optimizedFunctions > 0
                            ? "removed"
                            : "added"));

        lines = optimizedLines;
        operations = optimizedOperations;
        variables = optimizedVariables;
        functions = optimizedFunctions;
    }
}
