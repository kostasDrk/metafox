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
import java.util.Set;
import java.util.Collection;

import symbols.value.*;

import static utils.Constants.NULL;

/**
 * CLASS FoxObject
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class FoxObject extends AFoxDataStructure {

    private final HashMap<Value, Value> _data;

    public FoxObject() {
        _data = new HashMap<>();
    }

    public FoxObject(HashMap<Value, Value> data) {
        _data = new HashMap<>(data);
    }

    public FoxObject(FoxObject foxObject) {
        this();

        foxObject.getData().entrySet().stream().forEach((pair) -> {
            Value key = new StaticVal(pair.getKey());
            Value value = new DynamicVal((DynamicVal) pair.getValue());

            _data.put(key, value);
        });

    }

    @Override
    public void put(Value key, Value value) {
        _data.put(key, value);
    }

    @Override
    public Value get(Value key) {
        Value retVal = _data.get(key);

        if (retVal == null) {
            retVal = NULL;
        }

        return retVal;
    }

    @Override
    public void remove(Value key) {
        _data.remove(key);
    }

    @Override
    public int size() {
        return _data.size();
    }

    private HashMap<Value, Value> getData() {
        return _data;
    }

    public HashMap<Value, Value> keys() {
        Set<Value> setKeys = _data.keySet();
        HashMap<Value, Value> keys = new HashMap<>();
        int count = 0;

        for (Value value : setKeys) {
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

        msg.append("{ ");
        _data.entrySet().stream().forEach((pair) -> {
            msg.append(pair.getKey().getData()).append(":");
            msg.append(pair.getValue().getData()).append(", ");
        });

        msg.setCharAt(msg.length() - 1, '}');
        if (msg.length() > 2) {
            msg.setCharAt(msg.length() - 2, ' ');
        }

        return msg.toString();

    }

}
