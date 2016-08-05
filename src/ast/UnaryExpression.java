package ast;

public class UnaryExpression extends TermExpression {

	private Operator _operator;
	private Expression _expression;
	private Lvalue _lvalue;
	
	public UnaryExpression(Operator operator, Expression expression, Lvalue lvalue) { 
		this._operator = operator;
		this._expression = expression;
		this._lvalue = lvalue;
	}
	
	public Operator getOperator() {
		return _operator;
	}

	public void setOperator(Operator operator) {
		this._operator = operator;
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		this._expression = expression;
	}

	public Lvalue getLvalue() {
		return _lvalue;
	}

	public void setLvalue(Lvalue lval) {
		this._lvalue = lval;
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
