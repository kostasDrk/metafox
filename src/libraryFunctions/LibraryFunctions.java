package libraryFunctions;

import environment.Environment;
import environment.FunctionEnv;

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

import static utils.Constants.LIBRARY_FUNC_ARG;

public class LibraryFunctions {

    public static void print(Environment env) {
        int totalActuals = env.totalActuals();

        for (int i = 0; i < totalActuals; i++) {
            String data;

            if (env.getActualArgument(LIBRARY_FUNC_ARG + i).isUndefined()) {
                data = "UNDEFINED";
            } else if (env.getActualArgument(LIBRARY_FUNC_ARG + i).isNull()) {
                data = "NULL";
            } else {
                data = env.getActualArgument(LIBRARY_FUNC_ARG + i).getData().toString();
            }

            System.out.print(data);
        }
    }

    public static void println(Environment env) {
        print(env);
        System.out.println("");
    }

    public static void sqrt(Environment env) {

        Double data = getArgument(env);
        if (data == null) {
            return;
        }

        StaticVal retVal;
        data = Math.sqrt(data);

        //Return sprt
        if ((data == Math.floor(data)) && !Double.isInfinite(data)) {
            retVal = new StaticVal(Value_t.INTEGER, data);
        } else {
            retVal = new StaticVal(Value_t.REAL, data);
        }

        ((FunctionEnv) env).setReturnVal(retVal);
    }

    public static void cos(Environment env) {

        Double data = getArgument(env);
        if (data == null) {
            return;
        }

        data = Math.cos(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);

    }

    public static void sin(Environment env) {

        Double data = getArgument(env);
        if (data == null) {
            return;
        }

        data = Math.sin(data);

        StaticVal retVal = new StaticVal(Value_t.REAL, data);
        ((FunctionEnv) env).setReturnVal(retVal);
    }

    private static Double getArgument(Environment env) {
        double data;
        Value value = env.getActualArgument(LIBRARY_FUNC_ARG + 0);
        if (value.isReal()) {
            data = (double) value.getData();

        } else if (value.isInteger()) {
            data = (double) (int) value.getData();

        } else if (value.isString() && value.isConvertedToNumeric()) {
            data = Double.parseDouble((String) value.getData());
        } else if (value.isUndefined()) {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument is UNDEFINED.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        } else {
            StaticVal retVal = new StaticVal(Value_t.ERROR, "Argument can not cast to Double.");
            ((FunctionEnv) env).setReturnVal(retVal);
            return null;
        }
        return data;
    }
}
