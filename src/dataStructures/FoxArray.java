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
import symbols.value.DynamicVal;

import static utils.Constants.NULL;
import static utils.Constants.UNDEFINED;

public class FoxArray extends AFoxDataStructure {

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

    public FoxArray(FoxArray foxArray) {
        this();

        for (int i = 0; i < foxArray.size(); i++) {
            StaticVal key = new StaticVal(Value_t.INTEGER, i);
            Value value = foxArray.get(key);

            if (value != NULL) {
                this.put(key, value);
            }
        }

        foxArray.getOtherTypeIndexedData().entrySet().stream().forEach((pair) -> {
            Value key = new StaticVal(pair.getKey());
            Value value = new DynamicVal((DynamicVal) pair.getValue());

            this.put(key, value);
        });

    }

    @Override
    public final void put(Value key, Value value) {

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

        return NULL;
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

    private HashMap<Value, Value> getNumberIndexedData() {
        return _numberIndexedData;
    }

    private HashMap<Value, Value> getOtherTypeIndexedData() {
        return _otherTypeIndexedData;
    }

    @Override
    public HashMap<Value, Value> values() {
        Collection<Value> valuesCollection = _numberIndexedData.values();
        HashMap<Value, Value> values = new HashMap<>();
        int count = 0;

        for (Value value : valuesCollection) {
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

        msg.append("[ ");
        for (int i = 0; i < _numberIndexedDataMaxIndex; i++) {
            StaticVal key = new StaticVal(Value_t.INTEGER, i);
            Value data = _numberIndexedData.get(key);
            if (data != null) {
                msg.append(_numberIndexedData.get(key).getData()).append(", ");
            } else {
                msg.append(", ");
            }
        }

        msg.setCharAt(msg.length() - 1, ']');
        if (msg.length() > 2) {
            msg.setCharAt(msg.length() - 2, ' ');
        }

        return msg.toString();
    }

}
