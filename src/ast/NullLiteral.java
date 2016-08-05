package ast;

import symbols.value.Value;

public class NullLiteral extends Constant{

	public NullLiteral(){
		
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}