package utils;

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

public class Constants {

    public static final int GLOBAL_ENV_SCOPE = 0;
    public static final int FUNCTION_ENV_INIT_SCOPE = 0;

    public static final String LIBRARY_FUNC_ARG = "#libraryFunctionArg_";

    public static final String BREAK = "BREAK";
    public static final String CONTINUE = "CONTINUE";
    public static final String RETURN = "RETURN";

    public static final Value NULL = new StaticVal(Value_t.NULL, null);
    public static final Value UNDEFINED = new StaticVal(Value_t.UNDEFINED, null);
}
