package symbolTable.entries;

public abstract class ASymTableEntry {

    private static final int GLOBAL_SCOPE = 0;

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
        this._value = null;
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

    public boolean hasGlobalScope() {
        return this._scope == GLOBAL_SCOPE;
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

    public Object getValue(){
        return this._value;
    }

    public void setValue(Object value){
        this._value = value;
    }

    @Override
    public String toString() {
        return "Entry    name:  " + this._name
                + "\n\t scope: " + this._scope
                + "\n\t class: " + this.getClass()
                + "\n\t active:" + this._isActive
                + "\n";

    }
}
