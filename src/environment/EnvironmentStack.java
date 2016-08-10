package environment;

import java.util.ArrayDeque;

import symbols.value.Value;
import symbols.value.DynamicVal;
import static utils.Constants.ENTER_FUNCTION_ENV_INIT_SCOPE;

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
        topEnv().push(new EnvironmentScope(ENTER_FUNCTION_ENV_INIT_SCOPE));
    }

    public Value exitFunction() {
        System.out.println("##exitFunction");
        FunctionEnv functionEnv = (FunctionEnv) _environmentStack.pop();
        return functionEnv.getReturnVal();
    }

    public Environment getFunctionEnv() {
        return topEnv();
    }

    public void setReturnValue(Value value) {
        System.out.println("##settingRetValue");
        FunctionEnv functionEnv = (FunctionEnv) topEnv();
        functionEnv.setReturnVal(value);
    }

    public void enterBlock(int scope) {
        System.out.println("##enterBlock: " + scope);
        topEnv().push(new EnvironmentScope(scope));
    }

    public void exitBlock() {
        System.out.println("##exitBlock");
        topEnv().pop();
    }

    public DynamicVal lookupGlobalScope(String name) {
        return _globalEnv.lookupBottom(name);
    }

    /**
     * Use it to search for functions and functions arguments in the current
     * scope.
     *
     * @param name
     * @return
     */
    public DynamicVal lookupCurrentScope(String name) {
        return topEnv().lookupTop(name);
    }

    public DynamicVal lookupAll(String name) {
        DynamicVal varInfo = topEnv().lookupAll(name);

        //If we don't find the variable in the top environment then check in 
        //in the global.
        if (varInfo == null) {
            varInfo = lookupGlobalScope(name);
        }

        return varInfo;
    }

    public void insertSymbol(String name, DynamicVal value) {
        System.out.print("##insertSymbol: " + name + ", " + value.getType());
        topEnv().insert(name, value);
        // System.out.println(toString());
    }

    public void insertSymbol(String name) {
        System.out.print("##insertSymbol: " + name);
        topEnv().insert(name);
        // System.out.println(toString());
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
