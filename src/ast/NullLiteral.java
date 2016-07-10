package ast;

public class NullLiteral extends Constant{

	public NullLiteral(){
		
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}