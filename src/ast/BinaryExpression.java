package ast;

import symbols.value.Value;

public class BinaryExpression extends Expression {

	private Operator _operator;
	private Expression _expression1;
	private Expression _expression2;
	
	public BinaryExpression(Operator operator, Expression expression1, Expression expression2) {
		this._operator = operator;
		this._expression1 = expression1;
		this._expression2 = expression2;
	}
	
	public Operator getOperator() {
		return _operator;
	}

	public void setOperator(Operator operator) {
		this._operator = operator;
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
