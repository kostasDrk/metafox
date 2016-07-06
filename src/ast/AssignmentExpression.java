package ast;

public class AssignmentExpression extends Expression {

	private String _identifier;
	private Expression _expression;
	
	public AssignmentExpression(String identifier, Expression expression) {
		this._identifier = identifier;
		this._expression = expression;
	}
	
	public String getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(String identifier) {
		this._identifier = identifier;
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
