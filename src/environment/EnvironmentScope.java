package environment;

import symbols.value.Value;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class EnvironmentScope {

    private final HashMap<String, Value> _env;
    private final int _scope;

    EnvironmentScope(int scope) {
        _env = new HashMap();
        _scope = scope;
    }

    int getScope() {
        return _scope;
    }

    Value lookup(String name) {
        return _env.get(name);
    }

    void insert(String name, Value value) {
        System.out.println(", scope " + _scope);
        _env.put(name, value);
    }

    @Override
    public String toString() {
        String msg = "\n-------------------------- Scope: " + _scope + " --------------------------\n";

        for (Map.Entry<String, Value> entry : _env.entrySet()) {
            msg += String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n";
        }
        return msg;
    }
}
