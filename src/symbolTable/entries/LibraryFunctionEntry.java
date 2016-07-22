package symbolTable.entries;

import java.util.ArrayList;

public class LibraryFunctionEntry extends AFunctionEntry {

    /**
     * LibraryFunctionEntry Constructor
     *
     * @param name
     */
    public LibraryFunctionEntry(String name) {
        super(name, null, 0);
    }

    public void setArguments(ArrayList<FormalVariableEntry> arguments) {
        this._arguments = arguments;
    }
}
