package ast;

import symbols.value.Value;

public class MetaSyntax extends TermExpression {

	private Expression _expression;
	
	public MetaSyntax(Expression expression) {
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
