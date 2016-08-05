package ast;

public class ContinueStatement extends Statement{

	public ContinueStatement(){

	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}