package ast;

public class Member extends Lvalue{

	private Lvalue _lvalue;
	private String _identifier;
	private Call _call;
	private Expression _expression;

	public Member(Lvalue lvalue, String identifier, Call call, Expression expression){
		this._lvalue = lvalue;
		this._identifier = identifier;
		this._call = call;
		this._expression = expression;
	}

	public Lvalue getLvalue(){
		return this._lvalue;
	}

	public void setLvalue(Lvalue lvalue){
		this._lvalue = lvalue;
	}

	public String getIdentifier(){
		return this._identifier;
	}

	public void setIdentifier(String identifier){
		this._identifier = identifier;
	}

	public Call getCall(){
		return this._call;
	}

	public void setCall(Call call){
		this._call = call;
	}

	public Expression getExpression(){
		return this._expression;
	}

	public void setExpression(Expression expression){
		this._expression = expression;
	}


	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}
