/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import symbols.value.Value;

public class Object {

    HashMap<Value, Value> _data;

    public Object() {
        _data = new HashMap<>();
    }

    public void put(Value key, Value value) {
        _data.put(key, value);
    }

    public Value get(Value key) {
        return _data.get(key);
    }

    public void remove(Value key) {
        _data.remove(key);
    }

    public int size() {
        return _data.size();
    }

    public Array keysArray() {
        Array keys = new Array();
        _data.keySet().stream().forEach((key) -> {
            keys.add(key);
        });

        return keys;
    }

    public Array valuesArray() {
        Array values = new Array();
        _data.values().stream().forEach((value) -> {
            values.add(value);
        });

        return values;
    }

    @Override
    public String toString() {
        String msg = String.format("%-48s %s", "Keys:", "Values:") + "\n";
        msg = _data.entrySet().stream().map((entry)
                -> String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n")
                .reduce(msg, String::concat);
        return msg;

    }
}
