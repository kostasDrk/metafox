/**
 * Metafox - A DYNAMIC, INTERPRETED, META-PROGRAMMING LANGUAGE, RUN AND
 * SUPPORTED BY ITS OWN INDEPENDENT INTERPRETER.
 *
 * UNIVERSITY OF CRETE (UOC)
 *
 * COMPUTER SCIENCE DEPARTMENT (UOC)
 *
 * https://www.csd.uoc.gr/
 *
 * CS-540 ADVANCED TOPICS IN PROGRAMMING LANGUAGES DEVELOPMENT
 *
 * LICENCE: This file is part of Metafox. Metafox is free: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * Metafox is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Metafox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016
 *
 */
package dataStructures;

import java.util.HashMap;
import java.util.Collection;

import symbols.value.*;

import static utils.Constants.NULL;
import static utils.Constants.UNDEFINED;

/**
 * CLASS FoxArray
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
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
        if (key.isInteger() && (int) key.getData() >= 0) {
            _numberIndexedData.put(key, value);

            int newIndex = (int) key.getData();

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

    public HashMap<Value, Value> getNumberIndexedData() {
        return _numberIndexedData;
    }

    public HashMap<Value, Value> getOtherTypeIndexedData() {
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
