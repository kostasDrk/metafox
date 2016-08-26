package environment;

import symbols.value.StaticVal;
import symbols.value.Value;

import static utils.Constants.FUNCTION_ENV_INIT_SCOPE;

public class FunctionEnv extends Environment {

    private Value _returnVal;

    public FunctionEnv() {
        super(FUNCTION_ENV_INIT_SCOPE);
        _returnVal = new StaticVal();

    }

    public Value getReturnVal() {
        return _returnVal;
    }

    public void setReturnVal(Value returnVal) {
        _returnVal = new StaticVal(returnVal);
    }

}
