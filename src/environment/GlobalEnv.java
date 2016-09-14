package environment;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import libraryFunctions.LibraryFunction_t;
import libraryFunctions.LibraryFunctions;

import symbols.utils.Symbol;
import symbols.value.Value_t;
import symbols.value.DynamicVal;

import static utils.Constants.GLOBAL_ENV_SCOPE;

public class GlobalEnv extends Environment {

    public GlobalEnv() {
        super(GLOBAL_ENV_SCOPE);

        //Insert the library functions in global environment.
        for (LibraryFunction_t libraryFunction : LibraryFunction_t.values()) {

            try {
                String name = libraryFunction.toString();
                // System.out.println(name);
                Method method = LibraryFunctions.class.getMethod(name, Environment.class);
                DynamicVal<String> varInfo = new DynamicVal(Value_t.LIBRARY_FUNCTION, method, libraryFunction.toString());
                Symbol symbol = new Symbol(name);
                super.insert(symbol, varInfo);

            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(GlobalEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
