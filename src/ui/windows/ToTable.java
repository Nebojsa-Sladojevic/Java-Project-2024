package ui.windows;

import java.util.Collection;
import java.util.function.Function;

public interface ToTable {

	static String[][] toRows(Collection<? extends ToTable> collection) {
		return collection.stream().map(ToTable::toTableRow).toArray(String[][]::new);
	}

	static String[][] toRows(Collection<? extends ToTable> collection, Function<Object, String[]> customFunction) {
		return collection.stream().map(customFunction::apply).toArray(String[][]::new);
	}

	abstract String[] toTableRow();

}