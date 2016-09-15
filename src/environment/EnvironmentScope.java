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

import ast.IdentifierExpression;

import symbols.utils.*;
import symbols.value.DynamicVal;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import symbols.value.Value_t;

/**
 * CLASS EnvironmentScope
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class EnvironmentScope {

    private final HashMap<String, DynamicVal> _env;
    private final HashMap<String, HashSet<IdentifierExpression>> _references;
    private final int _scope;

    EnvironmentScope(int scope) {
        _env = new HashMap();
        _references = new HashMap();
        _scope = scope;
    }

    int getScope() {
        return _scope;
    }

    DynamicVal lookup(Symbol symbol) {
        DynamicVal value = _env.get(symbol.getName());
        if (value != null) {
            keepReference(symbol);
        }

        return value;
    }

    DynamicVal lookup(String symbol) {
        return _env.get(symbol);
    }

    void insert(Symbol symbol) {
        //System.out.println(", scope " + _scope);
        String name = symbol.getName();
        DynamicVal value = new DynamicVal(name);
        _env.put(name, value);

        keepReference(symbol);
    }

    void insert(Symbol symbol, DynamicVal dynamicVal) {
        //System.out.println(", scope " + _scope);
        String name = symbol.getName();
        _env.put(name, dynamicVal);

        keepReference(symbol);
    }

    int size() {
        return _env.size();
    }

    private void keepReference(Symbol symbol) {
        /*Keep reference from specific symbol to identifier.*/
        String symbolName = symbol.getName();
        HashSet<IdentifierExpression> references = _references.get(symbolName);

        if (references == null) {
            references = new HashSet();
            _references.put(symbolName, references);
        }

        IdentifierExpression symbolExpr = symbol.getIdExpression();
        references.add(symbolExpr);

        /*Keep reference from identifier to symbol.*/
        symbolExpr.setSymbolEnv(new SymbolEnv(this));
    }

    public void renameSymbol(Symbol oldSymbol, Symbol newSymbol) {
        DynamicVal value = _env.remove(oldSymbol.getName());
        cleanSymbolReferences(oldSymbol);

        insert(newSymbol, value);
    }

    public void newSymbol(String oldName, Symbol symbol) {
        DynamicVal value = _env.get(oldName);
        DynamicVal newValue = new DynamicVal(value);

        _references.get(oldName).remove(symbol.getIdExpression());

        insert(symbol, newValue);
    }

    private void cleanSymbolReferences(Symbol symbol) {
        HashSet<IdentifierExpression> references;
        references = _references.remove(symbol.getName());

        references.stream().forEach((reference) -> {
            reference.removeSymbolEnv();
        });
    }

    void cleanEnvReferences() {
        _references.entrySet().stream().map((entry) -> entry.getValue()).forEach((references) -> {
            references.stream().forEach((reference) -> {
                reference.removeSymbolEnv();
            });
        });

        _references.clear();
    }

    @Override
    public String toString() {
        String msg = "\n-------------------------- Scope: " + _scope + " --------------------------\n";

        for (Map.Entry<String, DynamicVal> entry : _env.entrySet()) {
            if (entry.getValue().getType() != Value_t.LIBRARY_FUNCTION) {
                msg += String.format("%-20s %s", entry.getKey(), entry.getValue()) + "\n";
            }
        }
        return msg;
    }
}
