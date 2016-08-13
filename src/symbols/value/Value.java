package symbols.value;

import java.io.Serializable;
import java.util.Objects;

public abstract class Value<T> implements Serializable {

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

    public boolean isTable() {
        return _type.equals(Value_t.TABLE);
    }

    public boolean isObject() {
        return _type.equals(Value_t.OBJECT);
    }

    public boolean isNull() {
        return _type.equals(Value_t.NULL);
    }

    public boolean isUndefined() {
        return _type.equals(Value_t.UNDEFINED);
    }

    public boolean isConvertedToInteger() {
        if (!isString()) {
            return false;
        }

        try {
            Integer.parseInt((String) _data);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }

        // only got here if we didn't return false
        return true;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass() && !(obj instanceof Value)) {
            return false;
        }

        final Value<?> other = (Value<?>) obj;

        return Objects.equals(_type, other._type) && Objects.equals(_data, other._data);
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 13 * Objects.hashCode(_type) + 37 * Objects.hashCode(_data) + hash;

        return hash;
    }
}
