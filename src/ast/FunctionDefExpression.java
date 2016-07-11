package ast;

public class FunctionDefExpression extends Primary{

	private FunctionDef _funcDef;

	public FunctionDefExpression(FunctionDef funcDef){
		this._funcDef = funcDef;
	}

	public FunctionDef getFunctionDef(){
		return this._funcDef;
	}

	public void setFunctionDef(FunctionDef funcDef){
		this._funcDef = funcDef;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}