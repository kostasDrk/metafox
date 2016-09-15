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

/**
 * CLASS DynamicVal
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 * @param <T>
 */
public class DynamicVal<T> extends Value {

    private final String _errorInfo;

    public DynamicVal(Value_t type, T data, String errorInfo) {
        super(type, data);
        _errorInfo = errorInfo;
    }

    public DynamicVal(Value staticVal, String errorInfo) {
        super(staticVal.getType(), staticVal.getData());
        _errorInfo = errorInfo;

    }

    public DynamicVal(DynamicVal dynamicVal) {
        super(dynamicVal.getType(), dynamicVal.getData());
        _errorInfo = dynamicVal._errorInfo;

    }

    public DynamicVal(String errorInfo) {
        super();
        _errorInfo = errorInfo;

    }

    public void setType(Value_t type) {
        super._type = type;
    }

    public void setData(T _data) {
        super._data = _data;
    }

    public String getErrorInfo() {
        return _errorInfo;
    }

}
