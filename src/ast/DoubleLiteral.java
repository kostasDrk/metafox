package ast;

public class DoubleLiteral extends Constant{

	private Double _literal;

	public DoubleLiteral(Double literal){
		this._literal = literal;
	}

	public Double getLiteral(){
		return this._literal;
	}

	public void setLiteral(Double literal){
		this._literal = literal;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}