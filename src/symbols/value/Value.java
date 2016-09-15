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
package symbols.value;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS Value
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 * @param <T>
 */
public abstract class Value<T> implements Serializable {

    protected Value_t _type;
    protected T _data;

    public Value() {
        _type = Value_t.UNDEFINED;
        _data = (T) Value_t.UNDEFINED.toString();
    }

    public Value(Value_t type, T data) {
        _type = type;
        _data = data;
    }

    public Value_t getType() {
        return _type;
    }

    public T getData() {
        return _data;
    }

    public boolean isUserFunction() {
        return _type.equals(Value_t.USER_FUNCTION);
    }

    public boolean isLibraryFunction() {
        return _type.equals(Value_t.LIBRARY_FUNCTION);
    }

    public boolean isNumeric() {
        return _type.equals(Value_t.INTEGER) || _type.equals(Value_t.REAL);
    }

    public boolean isInteger() {
        return _type.equals(Value_t.INTEGER);
    }

    public boolean isReal() {
        return _type.equals(Value_t.REAL);
    }

    public boolean isBoolean() {
        return _type.equals(Value_t.BOOLEAN);
    }

    public boolean isString() {
        return _type.equals(Value_t.STRING);
    }

    public boolean isTable() {
        return _type.equals(Value_t.TABLE);
    }

    public boolean isObject() {
        return _type.equals(Value_t.OBJECT);
    }

    public boolean isNull() {
        return _type.equals(Value_t.NULL);
    }

    public boolean isUndefined() {
        return _type.equals(Value_t.UNDEFINED);
    }

    public boolean isAST(){
        return _type.equals(Value_t.AST);
    }

    public boolean isConvertedToInteger() {
        if (!isString()) {
            return false;
        }

        try {
            Integer.parseInt((String) _data);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }

        // only got here if we didn't return false
        return true;

    }

    public boolean isConvertedToNumeric() {
        if (!isString()) {
            return false;
        }

        try {
            double d = Double.parseDouble((String) _data);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Value_t: %-25s Data: %s", _type, _data) + ".\t";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass() && !(obj instanceof Value)) {
            return false;
        }

        final Value<?> other = (Value<?>) obj;

        return Objects.equals(_type, other._type) && Objects.equals(_data, other._data);
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 13 * Objects.hashCode(_type) + 37 * Objects.hashCode(_data) + hash;

        return hash;
    }
}
