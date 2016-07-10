package ast;

public class FalseLiteral extends Constant{

	public FalseLiteral(){
		
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}