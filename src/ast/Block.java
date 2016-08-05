package ast;

import java.util.ArrayList;
import symbols.value.Value;

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
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}