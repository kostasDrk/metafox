package ast;

import java.util.ArrayList;
import java.util.List;
import symbols.value.Value;

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
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
