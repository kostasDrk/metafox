package environment;

import java.util.ArrayDeque;

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
        System.out.println("##enterFunction");
        _environmentStack.push(new FunctionEnv());
    }

    public Value exitFunction() {
        System.out.println("##exitFunction");
        FunctionEnv functionEnv = (FunctionEnv) _environmentStack.pop();
        Value returnVal = functionEnv.getReturnVal();
        return returnVal;
    }

    public void enterBlock(int scope) {
        System.out.println("##enterBlock: " + scope);
        topEnv().push(new EnvironmentScope(scope));
    }

    public void exitBlock() {
        System.out.println("##exitBlock");
        topEnv().pop();
    }

    public Value lookupGlobalScope(String name) {
        return _globalEnv.lookupBottom(name);
    }

    /**
     * Use it to search for functions and functions arguments in the current
     * scope.
     *
     * @param name
     * @return
     */
    public Value lookupCurrentScope(String name) {
        return topEnv().lookupTop(name);
    }

    public Value lookupAll(String name) {
        Value varInfo = topEnv().lookupAll(name);

        //If we don't find the variable in the top environment then check in 
        //in the global.
        if (varInfo == null) {
            varInfo = lookupGlobalScope(name);
        }

        return varInfo;
    }

    public void insertSymbol(String name, Value value) {
        System.out.print("##insertSymbol: " + name + ", " + value.getType());
        topEnv().insert(name, value);
        System.out.println(toString());
    }

    public void insertSymbol(String name) {
        System.out.print("##insertSymbol: " + name);
        topEnv().insert(name, new Value());
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String msg = "############################## EnvStack ##############################\n";
        int count = _environmentStack.size()-1;
        for (Environment env : _environmentStack) {
            msg += "\n_______________________ Environment (" + count + ") ______________________";
            msg += env.toString();
            count--;
        }

        return msg;

    }

}
