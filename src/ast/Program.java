package ast;

import java.util.ArrayList;
import java.util.List;

public class Program extends ASTNode {

	private List<Statement> _statements;
	
	public Program() { 
		_statements = new ArrayList<Statement>();
	}
	
	public Program(List<Statement> statements) {
		this._statements = statements;
	}
	
	public List<Statement> getStatements() {
		return _statements;
	}

	public void setStatements(List<Statement> statements) {
		this._statements = statements;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
	
}
