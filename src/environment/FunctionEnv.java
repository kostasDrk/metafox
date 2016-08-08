package environment;

import symbols.value.StaticVal;
import symbols.value.Value;

public class FunctionEnv extends Environment {

    private Value _returnVal;

    public FunctionEnv() {
        _returnVal = null;

    }

    public Value getReturnVal() {
        return _returnVal;
    }

    public void setReturnVal(Value returnVal) {
        _returnVal = new StaticVal(returnVal);
    }

}
