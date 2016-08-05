package ast;

public class LvalueCall extends Call {

	private Lvalue _lvalue;
	private CallSuffix _callSuffix;

	public LvalueCall(Lvalue lvalue, CallSuffix callSuffix){
		this._lvalue = lvalue;
		this._callSuffix = callSuffix;
	}

	public Lvalue getLvalue(){
		return this._lvalue;
	}

	public void setLvalue(Lvalue lvalue){
		this._lvalue = lvalue;
	}

	public CallSuffix getCallSuffix(){
		return this._callSuffix;
	}

	public void setCallSuffix(CallSuffix callSuffix){
		this._callSuffix = callSuffix;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}
