package ast;

import symbols.value.Value;

public class FalseLiteral extends Constant{

	public FalseLiteral(){
		
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}