package ast;

public class FalseLiteral extends Constant{

	public FalseLiteral(){
		
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}