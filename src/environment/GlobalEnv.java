package environment;

import libraryFunctions.LibraryFunction_t;

import symbols.value.Value_t;
import symbols.value.DynamicVal;

import static utils.Constants.GLOBAL_SCOPE;

public class GlobalEnv extends Environment {

    public GlobalEnv() {
        super(GLOBAL_SCOPE);

        //Insert the library functions in global environment.
        for (LibraryFunction_t libraryFunction : LibraryFunction_t.values()) {
            DynamicVal<String> varInfo = new DynamicVal(Value_t.LIBRARY_FUNCTION, libraryFunction.toString(), libraryFunction.toString());
            super.insert(libraryFunction.toString(), varInfo);
        }
    }

}
