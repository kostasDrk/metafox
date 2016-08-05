package ast;

public class BreakStatement extends Statement{

	public BreakStatement(){

	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}			
}