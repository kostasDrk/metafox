/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import java.util.Set;
import java.util.Collection;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;

public class FoxObject extends AFoxDataStructure{

    private final HashMap<Value, Value> _data;

    public FoxObject() {
        _data = new HashMap<>();
    }

    public FoxObject(HashMap<Value, Value> data) {
        _data = new HashMap<Value, Value>(data);
    }

    @Override
    public void put(Value key, Value value) {
        _data.put(key, value);
    }

    @Override
    public Value get(Value key) {
        return _data.get(key);
    }

    @Override
    public void remove(Value key) {
        _data.remove(key);
    }

    @Override
    public int size() {
        return _data.size();
    }

    public HashMap<Value, Value> getData(){
        return _data;
    }

    public HashMap<Value, Value> keys() {
        Set<Value> setKeys =  _data.keySet();
        HashMap<Value, Value> keys = new HashMap<>();
        int count = 0;
        
        for(Value value : setKeys){
            StaticVal key = new StaticVal(Value_t.INTEGER, count);
            StaticVal val = new StaticVal(value);
            keys.put(key, val);
            count++;
        }

        return keys;
    }

    @Override
    public HashMap<Value, Value> values() {
        Collection<Value> valuesCollection = _data.values();
        HashMap<Value, Value> values = new HashMap<>();
        int count = 0;

        for(Value value: valuesCollection){
            StaticVal key = new StaticVal(Value_t.INTEGER, count);
            StaticVal val = new StaticVal(value);
            values.put(key, val);
            count++;
        }

        return values;
    }

   @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        
        msg.append("{ ");
        _data.entrySet().stream().forEach((pair) -> {
            msg.append(pair.getKey().getData()).append(":");
            msg.append(pair.getValue().getData()).append(", ");
        });
        msg.setCharAt(msg.length()-1, '}');
        msg.setCharAt(msg.length()-2, ' ');
        
        return msg.toString();

    }
    
}
