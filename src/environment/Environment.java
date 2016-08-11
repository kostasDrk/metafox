package environment;

import java.util.ArrayDeque;

import symbols.value.DynamicVal;

public class Environment {

    private final ArrayDeque<EnvironmentScope> _environment; 

    public Environment() {
        _environment = new ArrayDeque();
    }

    public Environment(int scope) {
        _environment = new ArrayDeque<>();
        push(new EnvironmentScope(scope));
    }

    DynamicVal lookupTop(String name) {
        return _environment.peek().lookup(name);
    }

    DynamicVal lookupAll(String name) {
        for (EnvironmentScope envScope : _environment) {
            DynamicVal value = envScope.lookup(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    DynamicVal lookupBottom(String name) {
        return _environment.peekLast().lookup(name);
    }

    void insert(String name) {
        _environment.peek().insert(name);
    }

    void insert(String name, DynamicVal value) {
        _environment.peek().insert(name, value);
    }

    final void push(EnvironmentScope envScope) {
        _environment.push(envScope);
    }

    EnvironmentScope pop() {
        return _environment.pop();
    }

    public int topScope(){
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
