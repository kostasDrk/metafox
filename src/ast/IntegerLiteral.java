package ast;

public class IntegerLiteral extends Constant{

	private Integer _literal;

	public IntegerLiteral(Integer literal){
		this._literal = literal;
	}

	public Integer getLiteral(){
		return this._literal;
	}

	public void setLiteral(Integer literal){
		this._literal = literal;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}