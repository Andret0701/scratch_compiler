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
            printStatus("lines", lines, optimizedLines, padding);
        if (operations != optimizedOperations)
            printStatus("operations", operations, optimizedOperations, padding);
        if (variables != optimizedVariables)
            printStatus("variables", variables, optimizedVariables, padding);
        if (functions != optimizedFunctions)
            printStatus(null, optimizedVariables, optimizedFunctions, padding);

        lines = optimizedLines;
        operations = optimizedOperations;
        variables = optimizedVariables;
        functions = optimizedFunctions;
    }

    private static void printStatus(String feature, int old, int optimized, int padding) {
        System.out.print(" ".repeat(padding));
        if (old == optimized) {
            System.out.println(old + " " + feature + " unchanged");
            return;
        }

        if (old > optimized)
            System.out.println(" ".repeat(padding) + old + " " + feature + " -> " + optimized + " " + feature + ": "
                    + (old - optimized) + " " + feature + " removed");
        else
            System.out.println(" ".repeat(padding) + old + " " + feature + " -> " + optimized + " " + feature + ": "
                    + (optimized - old) + " " + feature + " added");
    }
}
