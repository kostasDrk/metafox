package symbols.value;

public class StaticVal<T> extends Value {

    public StaticVal() {
        super();
    }

    public StaticVal(Value_t type, T data) {
        super(type, data);
    }

    public StaticVal(Value value){
        super(value.getType(), value.getData());
    }
}
