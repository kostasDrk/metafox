package ast;

import java.util.ArrayList;
import java.util.List;

public class ObjectDefinition extends Primary {

	private ArrayList<IndexedElement> _indexedElementList;
	
	public ObjectDefinition() { 
		_indexedElementList = new ArrayList<IndexedElement>();
	}
	
	public ObjectDefinition(ArrayList<IndexedElement> indexedElementList) {
		this._indexedElementList = indexedElementList;
	}
	
	public ArrayList<IndexedElement> getIndexedElementList() {
		return _indexedElementList;
	}

	public void setIndexedElementList(ArrayList<IndexedElement> indexedElementList) {
		this._indexedElementList = indexedElementList;
	}

	@Override
	public Value accept(ASTVisitor visitor) throws ASTVisitorException {
		return visitor.visit(this);
	}
	
}
