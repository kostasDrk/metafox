package ast;

public class TrueLiteral extends Constant{

	public TrueLiteral(){
		
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}