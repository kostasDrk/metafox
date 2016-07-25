package ast;

import java.util.ArrayList;

public class FunctionDef extends Statement {

	private String _funcName;
	private ArrayList<String> _arguments;
	private Block _body;
        private static int _anonumousFunctionCounter = 0 ;
        
	public FunctionDef(String funcName, ArrayList<String> arguments, Block body){
                if(funcName.equals("#ANONYMOUS#_")){
                    funcName+= _anonumousFunctionCounter;
                    _anonumousFunctionCounter++;
                }
                
                this._funcName = funcName;
		this._arguments = arguments;
		this._body = body;
	}

	public String getFuncName(){
		return this._funcName;
	}

	public void setFuncName(String funcName){
		this._funcName = funcName;
	}

	public ArrayList<String> getArguments(){
		return this._arguments;
	}

	public void setArguments(ArrayList<String> arguments){
		this._arguments = arguments;
	}

	public Block getBody(){
		return this._body;
	}

	public void setBody(Block body){
		this._body = body;
	}

	@Override
	public void accept(ASTVisitor visitor) throws ASTVisitorException {
		visitor.visit(this);
	}
}