package symbolTable.entries;

import java.util.ArrayList;

public abstract class AFunctionEntry extends ASymTableEntry {

    private final ArrayList<FormalVariableEntry> _arguments;

    /**
     * ASymTableEntry Constructor
     *
     * @param name
     * @param arguments
     * @param scope
     */
    protected AFunctionEntry(String name,
            ArrayList<FormalVariableEntry> arguments, int scope) {

        super(name, scope);
        this._arguments = arguments;
    }

    public ArrayList<FormalVariableEntry> getArguments() {
        return _arguments;
    }

}
