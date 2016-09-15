package ast;

import symbols.value.Value;

public class UnaryExpression extends TermExpression {

	private Operator _operator;
	private Expression _expression;
	private Lvalue _lvalue;
	private final boolean _isLvaluePositionedLeft; 
        
	public UnaryExpression(Operator operator, Expression expression, Lvalue lvalue, boolean isLvaluePositionedLeft) { 
		this._operator = operator;
		this._expression = expression;
		this._lvalue = lvalue;
                this._isLvaluePositionedLeft = isLvaluePositionedLeft;
	}
	
	public Operator getOperator() {
		return _operator;
	}

	public void setOperator(Operator operator) {
		this._operator = operator;
	}

	public Expression getExpression() {
		return _expression;
	}

	public void setExpression(Expression expression) {
		this._expression = expression;
	}

	public Lvalue getLvalue() {
		return _lvalue;
	}

	public void setLvalue(Lvalue lval) {
		this._lvalue = lval;
	}

        public boolean isIsLvaluePositionedLeft() {
            return _isLvaluePositionedLeft;
        }
        
	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
