package symbolTable.entries;

import java.util.ArrayList;


public class UserFunctionEntry extends AFunctionEntry{
    
    
    /**
     * UserFunctionEntry Constructor
     * 
     * @param name
     * @param arguments
     * @param scope 
     */
    public UserFunctionEntry(String name, 
            ArrayList<FormalVariableEntry> arguments, int scope){
        super(name, arguments, scope);
    }
    
}
