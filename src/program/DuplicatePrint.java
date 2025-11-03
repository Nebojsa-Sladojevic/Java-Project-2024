package program;

import java.util.Collection;
import java.util.function.Function;

public interface DuplicatePrint {

	public void printDuplicateError();

	public default void addToCollection(Function<DuplicatePrint, Boolean> addFunction) {
		if (addFunction.apply(this) == false)
			printDuplicateError();
	}

	public default void addToCollection(Collection<DuplicatePrint> collection) {
		if (collection.add((DuplicatePrint)this) == false)
			printDuplicateError();
	}

}
