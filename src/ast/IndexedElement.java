package ast;

import symbols.value.Value;

public class IndexedElement extends ASTNode {

	private Expression _expression1;
	private Expression _expression2;
        private FunctionDef _functionDef;
	private final boolean _isValueExpression;
        
	public IndexedElement(Expression expression1, Expression expression2) {
		this._expression1 = expression1;
		this._expression2 = expression2;
                
                _isValueExpression = Boolean.TRUE;
	}

        public IndexedElement(Expression expression1, FunctionDef functionDef) {
		_expression1 = expression1;
		_functionDef = functionDef;
                
                _isValueExpression = Boolean.FALSE;
	}
        
	public Expression getExpression1() {
		return _expression1;
	}

	public void setExpression1(Expression expression1) {
		this._expression1 = expression1;
	}

	public Expression getExpression2() {
		return _expression2;
	}

	public void setExpression2(Expression expression2) {
		this._expression2 = expression2;
	}

        public FunctionDef getFunctionDef() {
            return _functionDef;
        }

        public void setFunctionDef(FunctionDef functionDef) {
            _functionDef = functionDef;
        }

        public boolean isValueExpression() {
            return _isValueExpression;
        }
        
        
	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
