package ast;

public class ExpressionStatement extends Statement {
    
    private Expression _expression;

    public ExpressionStatement(Expression expression) {
        this._expression = expression;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        this._expression = expression;
    }
    
    @Override
    public void accept(ASTVisitor visitor) throws ASTVisitorException {
            visitor.visit(this);
    }
}