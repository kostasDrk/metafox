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

	public String getType(){
		Class nativeType = getNativeType();
		if(nativeType.equals(Integer.class))
			return "Integer";
		else if(nativeType.equals(Double.class))
			return "Double";
		else if(nativeType.equals(String.class))
			return "String";
		else if(nativeType.equals(Boolean.class))
			return "Boolean";
		return "Unknown";
	}

	public Class getNativeType(){
		return this._val.getClass();
	}

	public boolean isNumeric(){
		if(this.getNativeType().equals(Integer.class) || this.getNativeType().equals(Double.class))
			return true;
		return false;
	}

	public boolean isBoolean(){
		if(this.getNativeType().equals(Boolean.class))
			return true;
		return false;
	}

	public boolean isString(){
		if(this.getNativeType().equals(String.class))
			return true;
		return false;
	}
}