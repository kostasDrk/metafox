package ast;

import symbols.value.Value;

public class ParenthesisExpression extends TermExpression {

	private Expression _expression;

	public ParenthesisExpression(Expression expression){
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
