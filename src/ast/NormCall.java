package ast;
import java.util.ArrayList;

public class NormCall extends CallSuffix {

	private ArrayList<Expression> _expressionList;

	public NormCall(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
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
