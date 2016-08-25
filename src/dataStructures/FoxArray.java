/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;
import java.util.Collection;

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

import static utils.Constants.UNDEFINED;

public class FoxArray extends AFoxDataStructure{

    private final HashMap<Value, Value> _numberIndexedData;
    private final HashMap<Value, Value> _otherTypeIndexedData;

    private int _numberIndexedDataMaxIndex;

    public FoxArray() {
        _numberIndexedData = new HashMap<>();
        _otherTypeIndexedData = new HashMap<>();

        _numberIndexedDataMaxIndex = 0;
    }

    public FoxArray(HashMap<Value, Value> numberIndexedData) {
        _numberIndexedData = numberIndexedData;
        _otherTypeIndexedData = new HashMap<>();

        _numberIndexedDataMaxIndex = numberIndexedData.size();
    }

    @Override
    public void put(Value key, Value value) {

        if (key.isInteger() || key.isConvertedToInteger()) {
            _numberIndexedData.put(key, value);

            int newIndex = key.isInteger() ? (int) key.getData() : Integer.parseInt((String) key.getData());

            if (_numberIndexedDataMaxIndex == newIndex) {
                _numberIndexedDataMaxIndex++;
            } else {
                _numberIndexedDataMaxIndex = newIndex + 1;
            }

        } else {
            _otherTypeIndexedData.put(key, value);
        }
    }

    public Value get(int value) {
        return get(new StaticVal(Value_t.INTEGER, value));
    }

    @Override
    public Value get(Value key) {
        Value value = _numberIndexedData.get(key);
        if (value != null) {
            return value;
        }

        value = _otherTypeIndexedData.get(key);
        if (value != null) {
            return value;
        }

        return new StaticVal(Value_t.UNDEFINED, null);
    }

    public void add(Value value) {
        Value key = new StaticVal(Value_t.INTEGER, _numberIndexedDataMaxIndex);
        _numberIndexedDataMaxIndex++;

        _numberIndexedData.put(key, value);
    }

    @Override
    public void remove(Value key) {
        if (_numberIndexedData.containsKey(key)) {

            if ((int) key.getData() == (_numberIndexedDataMaxIndex - 1)) {
                _numberIndexedData.remove(key);
                _numberIndexedDataMaxIndex--;

            } else {
                _numberIndexedData.put(key, UNDEFINED);
            }

        } else {
            _otherTypeIndexedData.remove(key);
        }
    }

    @Override
    public int size() {
        return _numberIndexedDataMaxIndex;
    }

    @Override
    public HashMap<Value, Value> values() {
        Collection<Value> valuesCollection = _numberIndexedData.values();
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
    
    /*@Override
    public String toString() {
        String msg = String.format("%-48s %s", "Keys:", "Values:") + "\n";
        Iterator it = _numberIndexedData.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            msg += "KEY: "+pair.getKey()+"\tVALUE: "+pair.getValue();
        }
        return msg;
    }*/

}
