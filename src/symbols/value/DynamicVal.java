package symbols.value;

public class DynamicVal<T> extends Value {

    private final String _errorInfo;

    public DynamicVal(Value_t type, T data, String errorInfo) {
        super(type, data);
        _errorInfo = errorInfo;
    }

    public DynamicVal(Value staticVal, String errorInfo) {
        super(staticVal.getType(), staticVal.getData());
        _errorInfo = errorInfo;

    }

    public DynamicVal(DynamicVal dynamicVal) {
        super(dynamicVal.getType(), dynamicVal.getData());
        _errorInfo = dynamicVal._errorInfo;

    }

    public DynamicVal(String errorInfo) {
        super();
        _errorInfo = errorInfo;

    }

    public void setType(Value_t type) {
        super._type = type;
    }

    public void setData(T _data) {
        super._data = _data;
    }

    public String getErrorInfo() {
        return _errorInfo;
    }

}
