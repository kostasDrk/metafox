package libraryFunctions;

public enum LibraryFunction_t {

    PRINT("print"),
    PRINT_LN("println"),
    SQRT("sqrt"),
    COS("cos"),
    SIN("sin"),
    LEN("len"),
    KEYS("keys"),
    VALUES("values");

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
                || name.equals(LibraryFunction_t.VALUES.toString());

    }

}
