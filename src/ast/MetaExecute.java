package ast;

import symbols.value.Value;

public class MetaExecute extends TermExpression {

	private Expression _expression;
	
	public MetaExecute(Expression expression) {
		this._expression = expression;
	}
	
	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		this._expression = expression;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
