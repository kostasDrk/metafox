package symbols.value;

public enum Value_t {

    INTEGER("INTEGER"),
    REAL("REAL"),
    STRING("STRING"),
    BOOLEAN("BOOLEAN"),
    TABLE("TABLE"),
    USER_FUNCTION("USER_FUNCTION"),
    LIBRARY_FUNCTION("LIBRARY_FUNCTION"),
    NULL("NULL"),
    OBJECT("OBJECT"),
    AST("AST"),
    ERROR("ERROR"),     
    UNDEFINED("UNDEFINED");

    private final String _type;

    private Value_t(String type) {
        _type = type;
    }

    @Override
    public String toString() {
        return _type;
    }

}
