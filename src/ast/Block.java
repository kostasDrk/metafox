package ast;

import java.util.ArrayList;
import java.util.ListIterator;
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

	public void addStatement(Statement stmt, int pos){
		this._statementList.add(pos, stmt);
	}

	public void addStatements(ArrayList<Statement> statementList, int pos){
		for(Statement curstmt : statementList){
			addStatement(curstmt, pos);
			pos++;
		}
	}

	public void prependStatement(Statement stmt){
		addStatement(stmt, 0);
	}

	public void prependStatements(ArrayList<Statement> statementList){
		addStatements(statementList, 0);
	}

	public void appendStatement(Statement stmt){
		int pos = this.getStatementList().size() - 1;
		if(_statementList.get(pos) instanceof ReturnStatement)
			return;
		else
			addStatement(stmt, pos+1);
	}

	public void appendStatements(ArrayList<Statement> statementList){
		int pos = this.getStatementList().size() - 1;
		if(_statementList.get(pos) instanceof ReturnStatement)
			return;
		else
		 	addStatements(statementList, pos+1);
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}