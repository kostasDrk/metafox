package ast;

import symbols.value.Value;

public class BreakStatement extends Statement{

	public BreakStatement(){

	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}			
}