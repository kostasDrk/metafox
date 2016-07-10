package ast;

import java.util.ArrayList;

public class AnonymousFunctionCall extends Call{

	private FunctionDef _funcDef;
	private ArrayList<Expression> _expressionList;

	public AnonymousFunctionCall(FunctionDef funcDef, ArrayList<Expression> expressionList){
		this._funcDef = funcDef;
		this._expressionList = expressionList;
	}

	public FunctionDef getFunctionDef(){
		return this._funcDef;
	}

	public void setFunctionDef(FunctionDef funcDef){
		this._funcDef = funcDef;
	}

	public ArrayList<Expression> getExpressionList(){
		return this._expressionList;
	}

	public void setExpressionList(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}