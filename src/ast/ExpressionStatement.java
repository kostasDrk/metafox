package ast;

public class ExpressionStatement extends Statement {
    
    private Expression _expr;

    public ExpressionStatement(Expression expr) {
        this._expr = expr;
    }

    public Expression getExpr() {
        return _expr;
    }

    public void setExpr(Expression expr) {
        this._expr = expr;
    }
    
    @Override
    public void accept(ASTVisitor visitor) throws ASTVisitorException {
            visitor.visit(this);
    }
}