package symbolTable.entries;

public abstract class AVariableEntry extends ASymTableEntry {

    /**
     * VariableEntry Constructor
     *
     * @param name
     * @param scope
     */
    protected AVariableEntry(String name, int scope) {
        super(name, scope);

    }

}
