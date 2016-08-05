package environment;

public class FunctionEnv extends Environment {

    private Value _returnVal;

    public FunctionEnv() {
        _returnVal = null;

    }

    public Value getReturnVal() {
        return _returnVal;
    }

    public void setReturnVal(Value returnVal) {
        _returnVal = returnVal;
    }

}
