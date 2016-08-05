package ast;
import java.util.ArrayList;
import symbols.value.Value;

public class NormCall extends CallSuffix {

	private ArrayList<Expression> _expressionList;

	public NormCall(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
	}

	public NormCall(){
		this._expressionList = new ArrayList<Expression>();
	}

	public ArrayList<Expression> getExpressionList(){
		return this._expressionList;
	}

	public void setExpressionList(ArrayList<Expression> expressionList){
		this._expressionList = expressionList;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}
