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
import symbols.value.Value;
import symbols.value.DynamicVal;

import java.util.ArrayDeque;

/**
 * CLASS EnvironmentStack
 *
 * @author Drakonakis Kostas  < kostasDrk  at csd.uoc.gr >
 * @author Kokolaki Anna      < kokolaki   at csd.uoc.gr >
 * @author Nikitakis Giorgos  < nikitak    at csd.uoc.gr >
 *
 * @version 1.0.0
 */
public class EnvironmentStack {

    private final ArrayDeque<Environment> _environmentStack;
    private final GlobalEnv _globalEnv;

    public EnvironmentStack() {
        _environmentStack = new ArrayDeque<>();
        _globalEnv = new GlobalEnv();
        _environmentStack.push(_globalEnv);
    }

    private Environment topEnv() {
        return _environmentStack.peek();
    }

    public void enterFunction() {
        // System.out.println("##enterFunction");
        _environmentStack.push(new FunctionEnv());
        //topEnv().push(new EnvironmentScope(FUNCTION_ENV_INIT_SCOPE));
    }

    public Value exitFunction() {
        // System.out.println("##exitFunction");
        FunctionEnv functionEnv = (FunctionEnv) _environmentStack.pop();
        return functionEnv.getReturnVal();
    }

    public Environment getFunctionEnv() {
        //Check that it is not the Global env but it is a function env >0 
        //if it is global env then fatal error!
        return topEnv();
    }

    public void setReturnValue(Value value) {
        // System.out.println("##settingRetValue");
        FunctionEnv functionEnv = (FunctionEnv) topEnv();
        functionEnv.setReturnVal(value);
    }

    public void enterBlock(int scope) {
        // System.out.println("##enterBlock: " + scope);
        topEnv().push(new EnvironmentScope(scope));
    }

    public void exitBlock() {
        // System.out.println("##exitBlock");
        topEnv().pop();
    }

    public DynamicVal lookupGlobalScope(Symbol symbol) {
        return _globalEnv.lookupBottom(symbol);
    }

    /**
     * Use it to search for functions and functions arguments in the current
     * scope.
     *
     * @param symbol
     * @return
     */
    public DynamicVal lookupCurrentScope(Symbol symbol) {
        return topEnv().lookupTop(symbol);
    }

    /**
     * Use it to search for symbols with local in front of them in the current
     * environment.
     *
     * @param symbol
     * @return
     */
    public DynamicVal lookupCurrentEnv(Symbol symbol) {
        return topEnv().lookupAll(symbol);
    }

    public DynamicVal lookupAll(Symbol symbol) {
        DynamicVal varInfo = lookupCurrentEnv(symbol);

        //If we don't find the variable in the top environment then check in 
        //in the global.
        if (varInfo == null) {
            varInfo = lookupGlobalScope(symbol);
        }

        return varInfo;
    }

    public void insertSymbol(Symbol symbol, DynamicVal value) {
        topEnv().insert(symbol, value);
        //System.out.println(toString());
    }

    public void insertSymbol(Symbol symbol) {
        topEnv().insert(symbol);
        //System.out.println(toString());
    }

    public int topEnvScope() {
        return _environmentStack.peek().topScope();
    }

    public void setValue(DynamicVal dest, Value src) {
        dest.setType(src.getType());
        dest.setData(src.getData());
    }

    @Override
    public String toString() {
        String msg = "############################## EnvStack ##############################\n";
        int count = _environmentStack.size() - 1;
        for (Environment env : _environmentStack) {
            msg += "\n_______________________ Environment (" + count + ") ______________________";
            msg += env.toString();
            count--;
        }

        return msg;

    }

}
