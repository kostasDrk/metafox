package ast;

public class StringLiteral extends Constant{

	private String _literal;

	public StringLiteral(String literal){
		this._literal = literal;
	}

	public String getLiteral(){
		return this._literal;
	}

	public void setLiteral(String literal){
		this._literal = literal;
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}