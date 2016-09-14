package ast;

import java.util.ArrayList;
import symbols.value.Value;

public class FunctionDef extends Statement {

	private final IdentifierExpression _funcNameIdentifier;
	private ArrayList<IdentifierExpression> _arguments;
	private Block _body;
        private static int _anonumousFunctionCounter = 0 ;
        
	public FunctionDef(String funcName, ArrayList<IdentifierExpression> arguments, Block body){
                if(funcName.equals("#ANONYMOUS#_")){
                    funcName+= _anonumousFunctionCounter;
                    _anonumousFunctionCounter++;
                }
                
                _funcNameIdentifier = new IdentifierExpression(funcName);
                _arguments = arguments;
		_body = body;
	}
                
	public String getFuncName(){
		return _funcNameIdentifier.getIdentifier();
	}

        public IdentifierExpression getFuncNameIdentifierExpr(){
		return _funcNameIdentifier;
	}

        public void setFuncName(String funcName){
		_funcNameIdentifier.setIdentifier(funcName);
	}
        
	public ArrayList<IdentifierExpression> getArguments(){
		return _arguments;
	}

	public void setArguments(ArrayList<IdentifierExpression> arguments){
		_arguments = arguments;
	}

	public Block getBody(){
		return _body;
	}

	public void setBody(Block body){
		_body = body;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
}