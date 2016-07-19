package symbolTable;

public class VariableEntry extends ASymTableEntry {

    /**
     * Constructor 
     * 
     * @param type
     * @param name
     * @param scope 
     */
    public VariableEntry(SymTableEntryType type, String name, int scope) {
        super(type, name, scope);

    }

}
