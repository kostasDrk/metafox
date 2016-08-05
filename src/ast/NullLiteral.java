package ast;

public class NullLiteral extends Constant{

	public NullLiteral(){
		
	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}