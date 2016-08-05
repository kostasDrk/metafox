package libraryFunctions;

public enum LibraryFunctions {

    PRINT("print"),
    SQRT("sqrt"),
    COS("cos"),
    SIN("sin");

    private final String _name;

    private LibraryFunctions(String name) {
        this._name = name;
    }

    @Override
    public String toString() {
        return this._name;
    }

    public static boolean isLibraryFunction(String name) {
        return name.equals(LibraryFunctions.PRINT.toString())
                || name.equals(LibraryFunctions.SQRT.toString())
                || name.equals(LibraryFunctions.COS.toString())
                || name.equals(LibraryFunctions.SIN.toString());

    }

}
