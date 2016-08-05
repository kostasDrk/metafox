package environment;

public class Value<T> {

    private final Value_t _type;
    private T _data;

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

    public void setData(T _data) {
        this._data = _data;
    }

    @Override
    public String toString() {
        return String.format("Value_t: %-25s Data: %s", _type, _data)+".\t";
    }

}
