package environment;

import java.util.HashMap;
import java.util.Map;

import symbols.value.Value;
import symbols.value.DynamicVal;

class EnvironmentScope {

    private final HashMap<String, DynamicVal> _env;
    private final int _scope;

    EnvironmentScope(int scope) {
        _env = new HashMap();
        _scope = scope;
    }

    int getScope() {
        return _scope;
    }

    DynamicVal lookup(String name) {
        return _env.get(name);
    }

    void insert(String name) {
        // System.out.println(", scope " + _scope);
        DynamicVal value = new DynamicVal(name);
        _env.put(name, value);
    }

    void insert(String name, DynamicVal dynamicVal) {
        // System.out.println(", scope " + _scope);
        _env.put(name, dynamicVal);
    }

    DynamicVal popArgument(String name){
        DynamicVal ret = lookup(name);
        _env.remove(name);
        return ret;
    }

    int size() {
        return _env.size();
    }

    @Override
    public String toString() {
        String msg = "\n-------------------------- Scope: " + _scope + " --------------------------\n";

        for (Map.Entry<String, DynamicVal> entry : _env.entrySet()) {
            msg += String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n";
        }
        return msg;
    }
}
