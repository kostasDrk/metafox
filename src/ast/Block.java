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

	public void addStatementOnExit(Statement stmt){
		int pos = 0;
		ArrayList<Statement> stmtlist = getStatementList();
		int size = stmtlist.size();
		Statement curstmt;
		for(int i = 0; i < size; i++){
			curstmt = stmtlist.get(i);
			if(curstmt instanceof ReturnStatement){
				addStatement(stmt, i);
			}else if(curstmt instanceof IfStatement){
				Statement ifstmt = ((IfStatement)curstmt).getStatement();
				if(ifstmt instanceof ReturnStatement){
					ArrayList<Statement> tmp_list = new ArrayList<Statement>();
					tmp_list.add(stmt);
					tmp_list.add(ifstmt);
					Block new_ifstmt = new Block(tmp_list);
					((IfStatement)curstmt).setStatement(new_ifstmt);
				} else if(ifstmt instanceof Block) ((Block) ifstmt).addStatementOnExit(stmt);

				Statement elsestmt = ((IfStatement)curstmt).getElseStatement();
				if(elsestmt instanceof ReturnStatement){
					ArrayList<Statement> tmp_list = new ArrayList<Statement>();
					tmp_list.add(stmt);
					tmp_list.add(elsestmt);
					Block new_elsestmt = new Block(tmp_list);
					((IfStatement)curstmt).setElseStatement(new_elsestmt);
				} else if(elsestmt instanceof Block) ((Block) elsestmt).addStatementOnExit(stmt);

			}else if(curstmt instanceof WhileStatement){
				Statement loopstmt = ((WhileStatement)curstmt).getStatement();
				if(loopstmt instanceof ReturnStatement){
					ArrayList<Statement> tmp_list = new ArrayList<Statement>();
					tmp_list.add(stmt);
					tmp_list.add(loopstmt);
					Block new_loopstmt = new Block(tmp_list);
					((WhileStatement)curstmt).setStatement(new_loopstmt);
				}else if(loopstmt instanceof Block) ((Block) loopstmt).addStatementOnExit(stmt);

			}else if(curstmt instanceof ForStatement){
				Statement loopstmt = ((ForStatement)curstmt).getStatement();
				if(loopstmt instanceof ReturnStatement){
					ArrayList<Statement> tmp_list = new ArrayList<Statement>();
					tmp_list.add(stmt);
					tmp_list.add(loopstmt);
					Block new_loopstmt = new Block(tmp_list);
					((ForStatement)curstmt).setStatement(new_loopstmt);
				}else if(loopstmt instanceof Block) ((Block) loopstmt).addStatementOnExit(stmt);

			}
			stmtlist = getStatementList();
		}
	}


	public void addStatementsOnExit(ArrayList<Statement> new_stmtlist){
		int pos = 0;
		ArrayList<Statement> stmtlist = getStatementList();
		int size = stmtlist.size();
		Statement curstmt;
		for(int i = 0; i < size; i++){
			curstmt = stmtlist.get(i);
			if(curstmt instanceof ReturnStatement){
				addStatements(new_stmtlist, i);
			}else if(curstmt instanceof IfStatement){
				Statement ifstmt = ((IfStatement)curstmt).getStatement();
				if(ifstmt instanceof ReturnStatement){
					new_stmtlist.add(ifstmt);
					Block new_ifstmt = new Block(new_stmtlist);
					new_stmtlist.remove(new_stmtlist.size() - 1);
					((IfStatement)curstmt).setStatement(new_ifstmt);
				} else if(ifstmt instanceof Block) ((Block) ifstmt).addStatementsOnExit(new_stmtlist);
				
				Statement elsestmt = ((IfStatement)curstmt).getElseStatement();
				if(elsestmt instanceof ReturnStatement){
					new_stmtlist.add(elsestmt);
					Block new_elsestmt = new Block(new_stmtlist);
					new_stmtlist.remove(new_stmtlist.size() - 1);
					((IfStatement)curstmt).setElseStatement(new_elsestmt);
				} else if(elsestmt instanceof Block) ((Block) elsestmt).addStatementsOnExit(new_stmtlist);

			}else if(curstmt instanceof WhileStatement){
				Statement loopstmt = ((WhileStatement)curstmt).getStatement();
				if(loopstmt instanceof ReturnStatement){
					new_stmtlist.add(loopstmt);
					Block new_loopstmt = new Block(new_stmtlist);
					new_stmtlist.remove(new_stmtlist.size() - 1);
					((WhileStatement)curstmt).setStatement(new_loopstmt);
				}else if(loopstmt instanceof Block) ((Block) loopstmt).addStatementsOnExit(new_stmtlist);

			}else if(curstmt instanceof ForStatement){
				Statement loopstmt = ((ForStatement)curstmt).getStatement();
				if(loopstmt instanceof ReturnStatement){
					new_stmtlist.add(loopstmt);
					Block new_loopstmt = new Block(new_stmtlist);
					new_stmtlist.remove(new_stmtlist.size() - 1);
					((ForStatement)curstmt).setStatement(new_loopstmt);
				}else if(loopstmt instanceof Block) ((Block) loopstmt).addStatementsOnExit(new_stmtlist);

			}
			stmtlist = getStatementList();
		}
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}