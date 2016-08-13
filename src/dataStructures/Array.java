/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.util.HashMap;

import symbols.value.StaticVal;
import symbols.value.Value;
import symbols.value.Value_t;

import static utils.Constants.UNDEFINED;

public class Array {

    HashMap<Value, Value> _numberIndexedData;
    HashMap<Value, Value> _otherTypeIndexedData;

    int _numberIndexedDataMaxIndex;

    public Array() {
        _numberIndexedData = new HashMap<>();
        _otherTypeIndexedData = new HashMap<>();

        _numberIndexedDataMaxIndex = 0;
    }

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

    public int size() {
        return _numberIndexedDataMaxIndex;
    }

    @Override
    public String toString() {
        String msg = String.format("%-48s %s", "Keys:", "Values:") + "\n";
        msg = _numberIndexedData.entrySet().stream().map((entry)
                -> String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n")
                .reduce(msg, String::concat);
        return msg;

    }

}
