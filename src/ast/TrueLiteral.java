package ast;

import symbols.value.Value;

public class TrueLiteral extends Constant{

	public TrueLiteral(){
		
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}