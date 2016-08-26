/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

import symbols.value.Value;
import symbols.value.Value_t;
import symbols.value.StaticVal;

public class FoxObject extends FoxDataStructure{

    HashMap<Value, Value> _data;

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

    public void setData(HashMap<Value, Value> data){
        this._data = data;
    }

    public HashMap<Value, Value> keys() {
        Set<Value> setKeys =  _data.keySet();
        HashMap<Value, Value> keys = new HashMap<Value, Value>();
        int count = 0;
        
        for(Value value : setKeys){
            StaticVal key = new StaticVal(Value_t.INTEGER, count);
            StaticVal val = new StaticVal(value);
            keys.put(key, val);
            count++;
        }

        return keys;
    }

    public HashMap<Value, Value> values() {
        Collection<Value> valuesCollection = _data.values();
        HashMap<Value, Value> values = new HashMap<Value, Value>();
        int count = 0;

        for(Value value: valuesCollection){
            StaticVal key = new StaticVal(Value_t.INTEGER, count);
            StaticVal val = new StaticVal(value);
            values.put(key, val);
            count++;
        }

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
