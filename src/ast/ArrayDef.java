package ast;

import java.util.ArrayList;

public class ArrayDef extends Primary{

	private ArrayList<Expression> _expressionList;

	public ArrayDef(){
		this._expressionList = new ArrayList<Expression>();
	}

	public ArrayDef(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
	}

	public ArrayList<Expression> getExpressionList(){
		return this._expressionList;
	}

	public void setExpressionList(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
	}

	
	@Override
	public Object accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}