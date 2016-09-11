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
        Operator op;

        switch (operator) {
            case "+":
                op = Operator.PLUS;
                break;

            case "-":
                op = Operator.MINUS;
                break;

            case "*":
                op = Operator.MUL;
                break;

            case "/":
                op = Operator.DIV;
                break;

            case "++":
                op = Operator.PLUS_PLUS;
                break;

            case "--":
                op = Operator.MINUS_MINUS;
                break;

            case "%":
                op = Operator.MOD;
                break;

            case "and":
                op = Operator.LOGIC_AND;
                break;

            case "or":
                op = Operator.LOGIC_OR;
                break;

            case "not":
                op = Operator.LOGIC_NOT;
                break;

            case "==":
                op = Operator.CMP_EQUAL;
                break;

            case "!=":
                op = Operator.NOT_EQUAL;
                break;

            case ">=":
                op = Operator.GREATER_OR_EQUAL;
                break;

            case ">":
                op = Operator.GREATER;
                break;

            case "<=":
                op = Operator.LESS_OR_EQUAL;
                break;

            case "<":
                op = Operator.LESS;
                break;

            default:
                op = null;
                break;
        }

        return op;
    }
}
