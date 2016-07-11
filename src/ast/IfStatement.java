package ast;

public class IfStatement extends Statement{

	private Expression _expression;
	private Statement _statement;
	private Statement _elsestmt;

	public IfStatement(Expression expression, Statement statement, Statement elsestmt){
		this._expression = expression;
		this._statement = statement;
		this._elsestmt = elsestmt;
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

	public Statement getElseStatement(){
		return this._elsestmt;
	}

	public void setElseStatement(Statement elsestmt){
		this._elsestmt = elsestmt;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}