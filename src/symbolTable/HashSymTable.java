package symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class HashSymTable<E> implements SymTable<E> {

	private Hashtable<String, E> table = new Hashtable<String, E>();
	private SymTable<E> nextSymTable;

	public HashSymTable() {
		this(null);
	}

	public HashSymTable(SymTable<E> nextSymTable) {
		this.nextSymTable = nextSymTable;
	}

	@Override
	public E lookup(String s) {
		E r = table.get(s);
		if (r != null)
			return r;
		if (nextSymTable != null)
			return nextSymTable.lookup(s);
		return null;
	}

	@Override
	public E lookupOnlyInTop(String s) {
		return table.get(s);
	}

	@Override
	public void put(String s, E symbol) {
		table.put(s, symbol);
	}

	@Override
	public Collection<E> getSymbols() {
		List<E> symbols = new ArrayList<E>();
		symbols.addAll(table.values());
		if (nextSymTable != null) {
			symbols.addAll(nextSymTable.getSymbols());
		}
		return symbols;
	}

	@Override
	public Collection<E> getSymbolsOnlyInTop() {
		List<E> symbols = new ArrayList<E>();
		symbols.addAll(table.values());
		return symbols;
	}

	@Override
	public void clearOnlyInTop() {
		table.clear();
	}

}
