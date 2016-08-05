package ast;

public class ContinueStatement extends Statement{

	public ContinueStatement(){

	}

	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}