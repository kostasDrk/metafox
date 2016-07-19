package symbolTable;

import java.util.ArrayList;

public class FunctionEntry extends ASymTableEntry {

    private final ArrayList<VariableEntry> _arguments;

    /**
     * Constructor
     * 
     * @param type
     * @param name
     * @param arguments
     * @param scope 
     */
    public FunctionEntry(SymTableEntryType type, String name,
            ArrayList<VariableEntry> arguments, int scope) {
        
        super(type, name, scope);
        this._arguments = arguments;
    }

    public ArrayList<VariableEntry> getArguments() {
        return _arguments;
    }

}
