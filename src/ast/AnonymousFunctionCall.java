package ast;

import symbols.value.Value;

public class AnonymousFunctionCall extends Call {

    private FunctionDef _FunctionDef;
    private LvalueCall _lvalueCall;

    public AnonymousFunctionCall(FunctionDef functionDef, NormCall normCall) {
        _FunctionDef = functionDef;
        _lvalueCall = new LvalueCall(new IdentifierExpression(functionDef.getFuncName(), true), normCall);
    }

    public FunctionDef getFunctionDef() {
        return _FunctionDef;
    }

    public void setFunctionDef(FunctionDef _FunctionDef) {
        this._FunctionDef = _FunctionDef;
    }

    public LvalueCall getLvalueCall() {
        return _lvalueCall;
    }

    public void setLvalueCall(LvalueCall _lvalueCall) {
        this._lvalueCall = _lvalueCall;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }
}
