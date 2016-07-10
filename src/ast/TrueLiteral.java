package ast;

public class TrueLiteral extends Constant{

	public TrueLiteral(){
		
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}