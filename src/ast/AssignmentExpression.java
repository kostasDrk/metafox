package ast;

public class AssignmentExpression extends Expression {

	private Lvalue _lvalue;
	private Expression _expression;
	
	public AssignmentExpression(Lvalue lvalue, Expression expression) {
		this._lvalue = lvalue;
		this._expression = expression;
	}
	
	public Lvalue getLvalue() {
		return _lvalue;
	}

	public void setLvalue(Lvalue lvalue) {
		this._lvalue = lvalue;
	}
	
	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		this._expression = expression;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
	
}