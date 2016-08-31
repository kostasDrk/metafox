package libraryFunctions;

public enum LibraryFunction_t {

    PRINT("print", -1),
    PRINT_LN("println", -1),
    SQRT("sqrt", 1),
    COS("cos", 1),
    SIN("sin", 1),
    LEN("len", 1),
    KEYS("keys", 1),
    VALUES("values", 1),
    STR("str", 1),
    ISNULL("isNull", 1),
    ISUNDEFINED("isUndefined", 1),
    ISINTEGER("isInteger", 1),
    ISREAL("isReal", 1),
    ISSTRING("isString", 1),
    ISBOOLEAN("isBoolean", 1),
    ISTABLE("isTable", 1),
    ISFUNC("isFunc", 1),
    ISLIBFUNC("isLibFunc", 1),
    ISOBJECT("isObject", 1),
    ISAST("isAST", 1),
    //FACTORY("factory", -2),
    //COPY("copy", 1),
    DIAGNOSE("diagnose", 3),
    ADDFIRST("addFirst", 2),
    ADDONEXITPOINTS("addOnExitPoints", 2);

    private final String _name;
    private final int _totalArgs;

    private LibraryFunction_t(String name, int totalArgs) {
        _name = name;
        _totalArgs = totalArgs;
    }

    @Override
    public String toString() {
        return _name;
    }

    public int totalArgs() {
        return _totalArgs;
    }

    public static int totalArgs(String func) {
        int totalArgs = 0;

        switch (func) {
            case "print":
                totalArgs = -1;
                break;
            case "println":
                totalArgs = -1;
                break;
            case "sqrt":
                totalArgs = 1;
                break;
            case "cos":
                totalArgs = 1;
                break;
            case "sin":
                totalArgs = 1;
                break;
            case "len":
                totalArgs = 1;
                break;
            case "keys":
                totalArgs = 1;
                break;
            case "values":
                totalArgs = 1;
                break;
            case "str":
                totalArgs = 1;
                break;
            case "isNull":
                totalArgs = 1;
                break;
            case "isUndefined":
                totalArgs = 1;
                break;
            case "isInteger":
                totalArgs = 1;
                break;
            case "isReal":
                totalArgs = 1;
                break;
            case "isString":
                totalArgs = 1;
                break;
            case "isBoolean":
                totalArgs = 1;
                break;
            case "isTable":
                totalArgs = 1;
                break;
            case "isFunc":
                totalArgs = 1;
                break;
            case "isLibFunc":
                totalArgs = 1;
                break;
            case "isObject":
                totalArgs = 1;
                break;
            case "isAST":
                totalArgs = 1;
                break;
//            case "factory":
//                totalArgs = -2;
//                break;
//            case "copy":
//                totalArgs = 1;
//                break;
            case "diagnose":
                totalArgs = 3;
                break;
            case "addFirst":
                totalArgs = 2;
                break;
            case "addOnExitPoints":
                totalArgs = 2;
                break;
            default:
                //fatal error
                break;
        }

        return totalArgs;
    }

    public static boolean isLibraryFunction(String name) {
        return name.equals(LibraryFunction_t.PRINT.toString())
                || name.equals(LibraryFunction_t.SQRT.toString())
                || name.equals(LibraryFunction_t.COS.toString())
                || name.equals(LibraryFunction_t.SIN.toString())
                || name.equals(LibraryFunction_t.LEN.toString())
                || name.equals(LibraryFunction_t.KEYS.toString())
                || name.equals(LibraryFunction_t.VALUES.toString())
                || name.equals(LibraryFunction_t.DIAGNOSE.toString())
                || name.equals(LibraryFunction_t.STR.toString())
                || name.equals(LibraryFunction_t.ISNULL.toString())
                || name.equals(LibraryFunction_t.ISUNDEFINED.toString())
                || name.equals(LibraryFunction_t.ISINTEGER.toString())
                || name.equals(LibraryFunction_t.ISREAL.toString())
                || name.equals(LibraryFunction_t.ISSTRING.toString())
                || name.equals(LibraryFunction_t.ISBOOLEAN.toString())
                || name.equals(LibraryFunction_t.ISTABLE.toString())
                || name.equals(LibraryFunction_t.ISFUNC.toString())
                || name.equals(LibraryFunction_t.ISLIBFUNC.toString())
                || name.equals(LibraryFunction_t.ISOBJECT.toString())
                || name.equals(LibraryFunction_t.ISAST.toString())
                || name.equals(LibraryFunction_t.ADDFIRST.toString())
                || name.equals(LibraryFunction_t.ADDONEXITPOINTS.toString());
    }

}
