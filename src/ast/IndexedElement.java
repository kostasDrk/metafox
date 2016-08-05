package ast;

import symbols.value.Value;

public class IndexedElement extends ASTNode {

	private Expression _expression1;
	private Expression _expression2;
	
	public IndexedElement(Expression expression1, Expression expression2) {
		this._expression1 = expression1;
		this._expression2 = expression2;
	}
	
	public Expression getExpression1() {
		return _expression1;
	}

	public void setExpression1(Expression expression1) {
		this._expression1 = expression1;
	}

	public Expression getExpression2() {
		return _expression2;
	}

	public void setExpression2(Expression expression2) {
		this._expression2 = expression2;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
