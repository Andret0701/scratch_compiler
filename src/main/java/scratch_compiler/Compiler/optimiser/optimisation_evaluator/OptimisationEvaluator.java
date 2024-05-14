package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;

public class OptimisationEvaluator {

    private int initialLines;
    private int initialOperations;
    private int initialVariables;
    private int initialFunctions;

    private int lines;
    private int operations;
    private int variables;
    private int functions;

    public OptimisationEvaluator(IntermediateCode code) {
        this.lines = LineCounter.countLines(code);
        this.operations = OperationCounter.countOperations(code);
        this.variables = VariableCounter.countVariables(code);
        this.functions = code.getFunctions().size();

        this.initialLines = lines;
        this.initialOperations = operations;
        this.initialVariables = variables;
        this.initialFunctions = functions;
    }

    public void evaluate(IntermediateCode optimized) {
        evaluate(optimized, 0);
    }

    public void evaluate(IntermediateCode optimized, int padding) {
        int optimizedLines = LineCounter.countLines(optimized);
        int optimizedOperations = OperationCounter.countOperations(optimized);
        int optimizedVariables = VariableCounter.countVariables(optimized);
        int optimizedFunctions = optimized.getFunctions().size();

        printStatus("lines", lines, optimizedLines, padding);
        printStatus("operations", operations, optimizedOperations, padding);
        printStatus("variables", variables, optimizedVariables, padding);
        printStatus("functions", functions, optimizedFunctions, padding);

        lines = optimizedLines;
        operations = optimizedOperations;
        variables = optimizedVariables;
        functions = optimizedFunctions;
    }

    public void totalEvaluate(IntermediateCode optimized, int padding) {
        lines = LineCounter.countLines(optimized);
        operations = OperationCounter.countOperations(optimized);
        variables = VariableCounter.countVariables(optimized);
        functions = optimized.getFunctions().size();

        printStatus("lines", initialLines, lines, padding);
        printStatus("operations", initialOperations, operations, padding);
        printStatus("variables", initialVariables, variables, padding);
        printStatus("functions", initialFunctions, functions, padding);
    }

    private static void printStatus(String feature, int old, int optimized, int padding) {
        System.out.print(" ".repeat(padding));
        if (old == optimized) {
            System.out.println(old + " " + feature + " unchanged");
            return;
        }

        if (old > optimized)
            System.out.println(old + " " + feature + " -> " + optimized + " " + feature + ": "
                    + (old - optimized) + " " + feature + " removed");
        else
            System.out.println(old + " " + feature + " -> " + optimized + " " + feature + ": "
                    + (optimized - old) + " " + feature + " added");
    }

}
