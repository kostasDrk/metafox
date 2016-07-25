package symbolTable.entries;

public abstract class ASymTableEntry {

    private boolean _isActive;
    private final String _name;
    private final int _scope;
    private ASymTableEntry _nextScopeListNode;

    /**
     * ASymTableEntry Constructor
     *
     * @param name
     * @param scope
     */
    protected ASymTableEntry(String name, int scope) {
        this._isActive = true;
        this._name = name;
        this._scope = scope;
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

    public ASymTableEntry getNextScopeListNode() {
        return _nextScopeListNode;
    }

    public void setIsActive(boolean isActive) {
        this._isActive = isActive;
    }

    public void setNextScopeListNode(ASymTableEntry nextScopeListNode) {
        this._nextScopeListNode = nextScopeListNode;
    }

}
