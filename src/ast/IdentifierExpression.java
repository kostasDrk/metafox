package ast;

import symbols.utils.SymbolEnv;
import symbols.value.Value;

public class IdentifierExpression extends Lvalue {

    private String _identifier;
    private SymbolEnv _symbolEnv;

    public IdentifierExpression(String identifier) {
        _identifier = identifier;
        _symbolEnv = null;
    }

    public String getIdentifier() {
        return _identifier;
    }

    public void setIdentifier(String identifier) {
        IdentifierExpression oldIdentifier = new IdentifierExpression(_identifier);
        _identifier = identifier;

        _symbolEnv.setIdentifier(oldIdentifier, this);
    }

    public SymbolEnv getSymbolEnv() {
        return _symbolEnv;
    }

    public void setSymbolEnv(SymbolEnv symbolEnv) {
        _symbolEnv = symbolEnv;
    }

    public void removeSymbolEnv() {
        _symbolEnv = null;
    }

    @Override
    public Value accept(ASTVisitor visitor) throws ASTVisitorException {
        return visitor.visit(this);
    }

}
