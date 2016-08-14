/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import symbols.value.Value;

public class FoxObject {

    HashMap<Value, Value> _data;

    public FoxObject() {
        _data = new HashMap<>();
    }

    public FoxObject(HashMap<Value, Value> data) {
        _data = data;
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

    public ArrayList keysArrayList() {
        Set<Value> setKeys =  _data.keySet();
        Value[] arrayKey = setKeys.toArray(new Value[setKeys.size()]);
        ArrayList<Value> keys = new ArrayList<Value>(Arrays.asList(arrayKey));

        return keys;
    }

    public ArrayList valuesArrayList() {
        ArrayList<Value> values = new ArrayList<Value>(_data.values());

        return values;
    }

   /*@Override
    public String toString() {
        String msg = String.format("%-48s %s", "Keys:", "Values:") + "\n";
        msg = _data.entrySet().stream().map((entry)
                -> String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n")
                .reduce(msg, String::concat);
        return msg;

    }*/
}
