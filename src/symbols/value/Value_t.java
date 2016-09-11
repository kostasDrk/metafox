package symbols.value;

public enum Value_t {

    INTEGER("Integer"),
    REAL("Real"),
    STRING("String"),
    BOOLEAN("Boolean"),
    TABLE("Table"),
    USER_FUNCTION("UserFunction"),
    LIBRARY_FUNCTION("LibraryFunction"),
    NULL("Null"),
    OBJECT("Object"),
    AST("Ast"),
    ERROR("Error"),
    UNDEFINED("Undefined");

    private final String _type;

    private Value_t(String type) {
        _type = "Fox." + type;
    }

    @Override
    public String toString() {
        return _type;
    }

}
