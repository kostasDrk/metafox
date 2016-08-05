package ast;
import java.util.ArrayList;

public class MethodCall extends CallSuffix {

	private String _identifier;
	private ArrayList<Expression> _expressionList;

	public MethodCall(String identifier, ArrayList<Expression> expressionList){
		this._identifier = identifier;
		this._expressionList = expressionList;
	}
	
	public MethodCall(String identifier){
		this._identifier = identifier;
		this._expressionList = new ArrayList<Expression>();
	}

	public String getIdentifier(){
		return this._identifier;
	}

	public void setIdentifier(String identifier){
		this._identifier = identifier;
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
