package ast;

import symbols.value.Value;

public class MethodCall extends CallSuffix {

    private String _identifier;
    private NormCall _normCall;

    public MethodCall(String identifier, NormCall normCall) {
        _identifier = identifier;
        _normCall = normCall;
    }

    public String getIdentifier() {
        return this._identifier;
    }

    public void setIdentifier(String identifier) {
        this._identifier = identifier;
    }

    public NormCall getNormCall() {
        return _normCall;
    }

    public void setNormCall(NormCall _normCall) {
        this._normCall = _normCall;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
