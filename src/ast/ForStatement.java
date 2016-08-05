package ast;

import java.util.ArrayList;

public class ForStatement extends Statement{

	private ArrayList<Expression> _expressionList1;
	private ArrayList<Expression> _expressionList2;
	private Expression _expression;
	private Statement _statement;

	public ForStatement(ArrayList<Expression> expressionList1, Expression expression, ArrayList<Expression> expressionList2, Statement statement){
		if(expressionList1 == null)
			this._expressionList1 = new ArrayList<Expression>();
		else
			this._expressionList1 = expressionList1;
		
		this._expression = expression;

		if(expressionList2 == null)
			this._expressionList2 = new ArrayList<Expression>();
		else
			this._expressionList2 = expressionList2;
		this._statement = statement;
	}

	public ArrayList<Expression> getExpressionList1(){
		return this._expressionList1;
	}

	public void setExpressionList1(ArrayList<Expression> expressionList1){
		this._expressionList1 = expressionList1;
	}

	public ArrayList<Expression> getExpressionList2(){
		return this._expressionList2;
	}

	public void setExpressionList2(ArrayList<Expression> expressionList2){
		this._expressionList2 = expressionList2;
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
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}