package ast;

import symbols.value.Value;

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
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}