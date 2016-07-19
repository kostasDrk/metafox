package symbolTable.entries;

public enum SymTableEntryType {

    GLOBAL("GLOBAL"),
    LOCAL("LOCAL"),
    FORMAL("FORMAL"),
    USER_FUNCTION("USER_FUNCTION"),
    LIBRARY_FUNCTION("LIBRARY_FUNCTION"),
    NO_TYPE("NO_TYPE");

    private final String _type;

    private SymTableEntryType(String type) {
        this._type = type;
    }

    @Override
    public String toString() {
        return this._type;
    }

}
