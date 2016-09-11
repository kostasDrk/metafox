package ast;

public enum Operator {

    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    PLUS_PLUS("++"),
    MINUS_MINUS("--"),
    MOD("%"),
    LOGIC_AND("and"),
    LOGIC_OR("or"),
    LOGIC_NOT("not"),
    CMP_EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_OR_EQUAL(">="),
    GREATER(">"),
    LESS_OR_EQUAL("<="),
    LESS("<");

    private final String _type;

    private Operator(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

    @Override
    public String toString() {
        return _type;
    }

    public boolean isLogical() {
        return this.equals(Operator.CMP_EQUAL)
                || this.equals(Operator.NOT_EQUAL)
                || this.equals(Operator.GREATER)
                || this.equals(Operator.GREATER_OR_EQUAL)
                || this.equals(Operator.LESS)
                || this.equals(Operator.LESS_OR_EQUAL)
                || this.equals(Operator.LOGIC_AND)
                || this.equals(Operator.LOGIC_OR);
    }

    public static Operator toOperator(String operator) {
        switch (operator) {
            case "+":
                return Operator.PLUS;

            case "-":
                return Operator.MINUS;

            case "*":
                return Operator.MUL;

            case "/":
                return Operator.DIV;

            case "++":
                return Operator.PLUS_PLUS;

            case "--":
                return Operator.MINUS_MINUS;

            case "%":
                return Operator.MOD;

            case "and":
                return Operator.LOGIC_AND;

            case "or":
                return Operator.LOGIC_OR;

            case "not":
                return Operator.LOGIC_NOT;

            case "==":
                return Operator.CMP_EQUAL;

            case "!=":
                return Operator.NOT_EQUAL;

            case ">=":
                return Operator.GREATER_OR_EQUAL;

            case ">":
                return Operator.GREATER;

            case "<=":
                return Operator.LESS_OR_EQUAL;

            case "<":
                return Operator.LESS;

            default:
                return null;
        }

    }
}
