package environment;

import symbolTable.libraryFunctions.LibraryFunctions;
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
