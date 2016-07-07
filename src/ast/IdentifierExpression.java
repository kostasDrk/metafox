package ast;

public class IdentifierExpression extends Lvalue {

	private String _identifier;
	private boolean _local;

	public IdentifierExpression(String identifier, boolean local) {
		this._identifier = identifier;
		this._local = local;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(String identifier) {
		this._identifier = identifier;
	}

	public boolean isLocal(){
		return this._local;
	}

	public void setLocal(boolean local){
		this._local = local;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
	
}
