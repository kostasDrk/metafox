package ast;

public class Call extends Primary {






	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}
