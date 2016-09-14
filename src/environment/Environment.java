package environment;

import symbols.utils.Symbol;
import symbols.value.DynamicVal;

import java.util.ArrayDeque;

public class Environment {

    private final ArrayDeque<EnvironmentScope> _environment;

    public Environment(int scope) {
        _environment = new ArrayDeque<>();
        push(new EnvironmentScope(scope));
    }

    DynamicVal lookupTop(Symbol symbol) {
        return _environment.peek().lookup(symbol);
    }

    DynamicVal lookupAll(Symbol symbol) {
        for (EnvironmentScope envScope : _environment) {
            DynamicVal value = envScope.lookup(symbol);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    DynamicVal lookupBottom(Symbol symbol) {
        return _environment.peekLast().lookup(symbol);
    }

    public void insert(Symbol symbol) {
        _environment.peek().insert(symbol);
    }

    public void insert(Symbol symbol, DynamicVal value) {
        _environment.peek().insert(symbol, value);
    }

    final void push(EnvironmentScope envScope) {
        _environment.push(envScope);
    }

    EnvironmentScope pop() {
        EnvironmentScope envScope = _environment.pop();
        envScope.cleanEnvReferences();

        return envScope;
    }

    public int topScope() {
        return _environment.peek().getScope();
    }

    public int totalActuals() {
        return _environment.peekFirst().size();
    }

    public DynamicVal getActualArgument(String arg) {
        return _environment.peekFirst().lookup(arg);
    }

    @Override
    public String toString() {
        return _environment.toString();
    }

}
