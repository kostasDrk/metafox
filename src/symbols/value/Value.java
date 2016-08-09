package symbols.value;

public abstract class Value<T> {

    protected Value_t _type;
    protected T _data;

    public Value() {
        _type = Value_t.UNDEFINED;
        _data = null;
    }

    public Value(Value_t type, T data) {
        _type = type;
        _data = data;
    }

    public Value_t getType() {
        return _type;
    }

    public T getData() {
        return _data;
    }

    public boolean isUserFunction() {
        return _type.equals(Value_t.USER_FUNCTION);
    }

    public boolean isLibraryFunction() {
        return _type.equals(Value_t.LIBRARY_FUNCTION);
    }

    public boolean isNumeric() {
        return _type.equals(Value_t.INTEGER) || _type.equals(Value_t.REAL);
    }

    public boolean isInteger() {
        return _type.equals(Value_t.INTEGER);
    }

    public boolean isReal() {
        return _type.equals(Value_t.REAL);
    }

    public boolean isBoolean() {
        return _type.equals(Value_t.BOOLEAN);
    }

    public boolean isString() {
        return _type.equals(Value_t.STRING);
    }

    public boolean isUndefined() {
        return _type.equals(Value_t.UNDEFINED);
    }

    public boolean isConvertedToNumeric() {
        if (!isString()) {
            return false;
        }

        try {
            double d = Double.parseDouble((String) _data);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Value_t: %-25s Data: %s", _type, _data) + ".\t";
    }

}
