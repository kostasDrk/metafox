package environment;

import symbols.value.Value;
import java.util.ArrayDeque;

public class Environment {

    private final ArrayDeque<EnvironmentScope> _environment;

    public Environment() {
        _environment = new ArrayDeque();
    }

    public Environment(int scope) {
        _environment = new ArrayDeque<>();
        push(new EnvironmentScope(scope));
    }

    Value lookupTop(String name) {
        return _environment.peek().lookup(name);
    }

    Value lookupAll(String name) {
        for (EnvironmentScope envScope : _environment) {
            Value value = envScope.lookup(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    Value lookupBottom(String name) {
        return _environment.peekLast().lookup(name);
    }

    void insert(String name, Value value) {
        _environment.peek().insert(name, value);
    }

    final void push(EnvironmentScope envScope) {
        _environment.push(envScope);
    }

    EnvironmentScope pop() {
        return _environment.pop();
    }

    @Override
    public String toString() {
        return _environment.toString();
    }

}
