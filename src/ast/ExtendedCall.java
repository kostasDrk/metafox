package ast;
import java.util.ArrayList;

public class ExtendedCall extends Call {

	private Call _call;
	private ArrayList<Expression> _expressionList;

	public ExtendedCall(Call call, ArrayList<Expression> expressionList){
		this._call = call;
		this._expressionList = expressionList;
	}

	public Call getCall(){
		return this._call;
	}

	public void setCall(Call call){
		this._call = call;
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
