package ast;

public class ReturnStatement extends Statement{

	private Expression _expression;

	public ReturnStatement(){
		this._expression = null;
	}

	public ReturnStatement(Expression expression){
		this._expression = expression;
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