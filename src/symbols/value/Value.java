package symbols.value;

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

    public boolean isNumeric(){
        if(this.getType().equals(Value_t.INTEGER) || this.getType().equals(Value_t.REAL))
            return true;
        return false;
    }

    public boolean isInteger(){
        if(this.getType().equals(Value_t.INTEGER))
                return true;
            return false;   
    }

    public boolean isReal(){
        if(this.getType().equals(Value_t.REAL))
                return true;
            return false;   
    }

    public boolean isBoolean(){
        if(this.getType().equals(Value_t.BOOLEAN))
            return true;
        return false;
    }

    public boolean isString(){
        if(this.getType().equals(Value_t.STRING))
            return true;
        return false;
    }

    public boolean isUndefined(){
        if(this.getType().equals(Value_t.UNDEFINED))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return String.format("Value_t: %-25s Data: %s", _type, _data)+".\t";
    }

}
