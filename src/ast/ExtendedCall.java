package ast;

import symbols.value.Value;

public class ExtendedCall extends Call {

    private Call _call;
    private NormCall _normCall;
    private LvalueCall _lvalueCall;

    public ExtendedCall(Call call, NormCall normCall) {
        _call = call;
        _normCall = normCall;
        _lvalueCall = null;
    }

    public Call getCall() {
        return this._call;
    }

    public void setCall(Call call) {
        this._call = call;
    }

    public NormCall getNormCall() {
        return _normCall;
    }

    public void setNormCall(NormCall _normCall) {
        this._normCall = _normCall;
    }

    public LvalueCall getLvalueCall() {
        return _lvalueCall;
    }

    public void setLvalueCall(String lvalue, NormCall normCall) {
        _lvalueCall = new LvalueCall(new IdentifierExpression(lvalue, true), normCall);
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
