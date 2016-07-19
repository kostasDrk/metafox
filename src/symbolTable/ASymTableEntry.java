package symbolTable;

public abstract class ASymTableEntry {

    private boolean _isActive;
    private final String _name;
    private final int _scope;
    private final SymTableEntryType _type;
    private ASymTableEntry _nextScopeListNode;

    /**
     * Constructor
     *
     * @param name
     * @param scope
     * @param type
     */
    public ASymTableEntry(SymTableEntryType type, String name, int scope) {
        this._isActive = true;
        this._name = name;
        this._scope = scope;
        this._type = type;
        this._nextScopeListNode = null;
    }

    public boolean isActive() {
        return _isActive;
    }

    public String getName() {
        return _name;
    }

    public int getScope() {
        return _scope;
    }

    public SymTableEntryType getType() {
        return _type;
    }

    public ASymTableEntry getNextScopeListNode() {
        return _nextScopeListNode;
    }

    public void setIsActive(boolean isActive) {
        this._isActive = isActive;
    }

    public void setNextScopeListNode(ASymTableEntry nextScopeListNode) {
        nextScopeListNode._nextScopeListNode = this._nextScopeListNode;
        this._nextScopeListNode = nextScopeListNode;
    }

}
