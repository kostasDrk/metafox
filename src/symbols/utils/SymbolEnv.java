package symbols.utils;

import ast.IdentifierExpression;
import environment.EnvironmentScope;

public class SymbolEnv {

    private final EnvironmentScope _env;

    public SymbolEnv(EnvironmentScope env) {
        _env = env;
    }

    public void setIdentifier(IdentifierExpression oldIdentifier, IdentifierExpression newIdentifier) {
        _env.renameSymbol(new Symbol(oldIdentifier), new Symbol(newIdentifier));
    }

    public void setIdentifierNew(String oldName, IdentifierExpression newIdentifier) {
        _env.newSymbol(oldName, new Symbol(newIdentifier));
    }

}
