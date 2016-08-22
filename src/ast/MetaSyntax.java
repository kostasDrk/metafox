package ast;

import java.util.ArrayList;
import symbols.value.Value;

public class MetaSyntax extends TermExpression {

	private Expression _expression;
	private ArrayList<Statement> _statementList;
	
	public MetaSyntax(Expression expression) {
		this._expression = expression;
		this._statementList = new ArrayList<Statement>();
	}

	public MetaSyntax(ArrayList<Statement> statementList) {
		this._statementList = statementList;
		this._expression = null;
	}
	
	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		this._expression = expression;
	}

	public ArrayList<Statement> getStatementList() {
		return _statementList;
	}

	public void setStatemenetList(ArrayList<Statement> statementList) {
		this._statementList = statementList;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
