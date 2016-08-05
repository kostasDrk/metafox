package ast;

public class WhileStatement extends Statement{

	private Expression _expression;
	private Statement _statement;

	public WhileStatement(Expression expression, Statement statement){
		this._expression = expression;
		this._statement = statement;
	}

	public Expression getExpression(){
		return this._expression;
	}

	public void setExpression(Expression expression){
		this._expression = expression;
	}

	public Statement getStatement(){
		return this._statement;
	}

	public void setStatement(Statement statement){
		this._statement = statement;
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}