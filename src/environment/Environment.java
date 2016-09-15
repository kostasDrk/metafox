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
package environment;

import symbols.utils.Symbol;
import symbols.value.DynamicVal;

import java.util.ArrayDeque;

/**
 * CLASS Environment
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class Environment {

    private final ArrayDeque<EnvironmentScope> _environment;

    public Environment(int scope) {
        _environment = new ArrayDeque<>();
        push(new EnvironmentScope(scope));
    }

    DynamicVal lookupTop(Symbol symbol) {
        return _environment.peek().lookup(symbol);
    }

    DynamicVal lookupAll(Symbol symbol) {
        for (EnvironmentScope envScope : _environment) {
            DynamicVal value = envScope.lookup(symbol);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    DynamicVal lookupBottom(Symbol symbol) {
        return _environment.peekLast().lookup(symbol);
    }

    public void insert(Symbol symbol) {
        _environment.peek().insert(symbol);
    }

    public void insert(Symbol symbol, DynamicVal value) {
        _environment.peek().insert(symbol, value);
    }

    final void push(EnvironmentScope envScope) {
        _environment.push(envScope);
    }

    EnvironmentScope pop() {
        EnvironmentScope envScope = _environment.pop();
        envScope.cleanEnvReferences();

        return envScope;
    }

    public int topScope() {
        return _environment.peek().getScope();
    }

    public int totalActuals() {
        return _environment.peekFirst().size();
    }

    public DynamicVal getActualArgument(String arg) {
        return _environment.peekFirst().lookup(arg);
    }

    @Override
    public String toString() {
        return _environment.toString();
    }

}
