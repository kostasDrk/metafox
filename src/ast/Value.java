package ast;

public class Value {
	private Object _val;

	public Value(Object val){
		this._val = val;
	}

	public Object getValue(){
		return this._val;
	}

	public void setValue(Object val){
		this._val = val;
	}

	public Class getType(){
		return this._val.getClass();
	}
}