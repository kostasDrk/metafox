package ast;

import symbols.value.Value;

public class Member extends Lvalue {

    private Lvalue _lvalue;
    private final IdentifierExpression _identifier;
    private Call _call;
    private Expression _expression;

    private boolean _isLValue;

    public Member(Lvalue lvalue, IdentifierExpression identifier, Call call, Expression expression) {
        _lvalue = lvalue;
        _identifier = identifier;
        _call = call;
        _expression = expression;
        _isLValue = false;
    }

    public Lvalue getLvalue() {
        return _lvalue;
    }

    public void setLvalue(Lvalue lvalue) {
        _lvalue = lvalue;
    }

    public String getIdentifier() {
        return _identifier != null ? _identifier.getIdentifier() : null;
    }

    public IdentifierExpression getIdentifierExpr() {
        return _identifier;
    }

    public void setIdentifier(String identifier) {
        _identifier.setIdentifier(identifier);
    }

    public Call getCall() {
        return _call;
    }

    public void setCall(Call call) {
        _call = call;
    }

    public Expression getExpression() {
        return _expression;
    }

    public void setExpression(Expression expression) {
        _expression = expression;
    }

    public boolean isLValue() {
        return _isLValue;
    }

    public void setIsLValue() {
        _isLValue = true;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
