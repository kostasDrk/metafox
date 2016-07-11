package ast;

public class TermExpressionStmt extends TermExpression {

	private Expression _expression;

	public TermExpressionStmt(Expression expression){
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
