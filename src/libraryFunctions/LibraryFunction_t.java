package libraryFunctions;

public enum LibraryFunction_t {

    PRINT("print"),
    PRINT_LN("println"),
    SQRT("sqrt"),
    COS("cos"),
    SIN("sin"),
    LEN("len"),
    KEYS("keys"),
    VALUES("values"),
    STR("str"),
    ISNULL("isNull"),
    ISUNDEFINED("isUndefined"),
    ISINTEGER("isInteger"),
    ISREAL("isReal"),
    ISSTRING("isString"),
    ISBOOLEAN("isBoolean"),
    ISTABLE("isTable"),
    ISFUNC("isFunc"),
    ISLIBFUNC("isLibFunc"),
    ISOBJECT("isObject"),
    ISAST("isAST"),
    DIAGNOSE("diagnose"),
    ADDFIRST("addFirst"),
    ADDLAST("addLast"),
    ADDONEXITPOINTS("addOnExitPoints");

    private final String _name;

    private LibraryFunction_t(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
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
                || name.equals(LibraryFunction_t.ADDLAST.toString())
                || name.equals(LibraryFunction_t.ADDONEXITPOINTS.toString());

    }

}
