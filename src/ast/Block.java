package ast;

import java.util.ArrayList;

public class Block extends Statement {

	private ArrayList<Statement> _statementList;

	public Block(){
		this._statementList = new ArrayList<Statement>();
	}

	public Block(ArrayList<Statement> statementList){
		this._statementList = statementList;
	}

	public ArrayList<Statement> getStatementList(){
		return this._statementList;
	}

	public void setStatementList(ArrayList<Statement> statementList){
		this._statementList = statementList;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}