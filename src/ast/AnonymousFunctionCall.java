package ast;

import symbols.value.Value;

public class AnonymousFunctionCall extends Call {

    private FunctionDef _FunctionDef;
    private NormCall _normCall;

    public AnonymousFunctionCall(FunctionDef functionDef, NormCall normCall) {
        _FunctionDef = functionDef;
        _normCall = normCall;
    }

    public FunctionDef getFunctionDef() {
        return _FunctionDef;
    }

    public void setFunctionDef(FunctionDef _FunctionDef) {
        this._FunctionDef = _FunctionDef;
    }

    public NormCall getNormCall() {
        return _normCall;
    }

    public void setNormCall(NormCall normCall) {
        this._normCall = normCall;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
