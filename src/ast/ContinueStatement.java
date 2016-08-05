package ast;

import symbols.value.Value;

public class ContinueStatement extends Statement{

	public ContinueStatement(){

	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}