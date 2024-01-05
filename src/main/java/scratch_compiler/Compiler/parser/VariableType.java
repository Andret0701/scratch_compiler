package scratch_compiler.Compiler.parser;

public enum VariableType {
    STRING,
    FLOAT,
    INT,
    BOOLEAN,
    STRUCT;

    //make a can be converted to function   
    public boolean canBeConvertedTo(VariableType other) {
        int score = score();
        int otherScore = other.score();

        if (score == -1 || otherScore == -1)
            return false;

        return score >= otherScore;
    }

    public VariableType next() {
        switch (this) {
            case BOOLEAN:
                return INT;
            case INT:
                return FLOAT;
            case FLOAT:
                return STRING;
            case STRING:
                return STRING;
            default:
                return null;
            }
    }

    private int score() {
        switch (this) {
            case STRING:
                return 0;
            case FLOAT:
                return 1;
            case INT:
                return 2;
            case BOOLEAN:
                return 3;
            default:
                return -1;
        }
    }
}
