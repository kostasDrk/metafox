package environment;

import symbols.value.Value_t;
import symbols.value.Value;
import libraryFunctions.LibraryFunctions;
import static utils.Constants.GLOBAL_SCOPE;

public class GlobalEnv extends Environment {

    public GlobalEnv() {
        super(GLOBAL_SCOPE);

        //Insert the library functions in global environment.
        for (LibraryFunctions libraryFunction : LibraryFunctions.values()) {
            Value<String> varInfo = new Value(Value_t.LIBRARY_FUNCTION, libraryFunction.toString());
            super.insert(libraryFunction.toString(), varInfo);
        }
    }

}
