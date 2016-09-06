package ast;

import symbols.value.Value;

public class IdentifierExpression extends Lvalue {

	private String _identifier;

	public IdentifierExpression(String identifier) {
		this._identifier = identifier;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(String identifier) {
		this._identifier = identifier;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
