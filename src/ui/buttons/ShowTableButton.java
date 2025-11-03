package ui.buttons;

import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import ui.windows.ToTable;
import ui.windows.TableWindow;

public class ShowTableButton extends ProgramButton {

	private static final long serialVersionUID = 1L;

	private TableWindow tableWindow = null;

	private synchronized void showTableWindow(String title, String[] tableHeader,
			Collection<? extends ToTable> collection, Optional<Function<Object, String[]>> customRowFunction) {
		if (tableWindow != null)
			tableWindow.dispose();
		tableWindow = new TableWindow(title, tableHeader, collection, customRowFunction);
	}

	public ShowTableButton(Color color, String title, String[] tableHeader,
			Supplier<Collection<? extends ToTable>> supplier, Optional<Function<Object, String[]>> customRowFunction) {
		super(title, null, color);
		this.addActionListener(e -> showTableWindow(title, tableHeader, supplier.get(), customRowFunction));
	}

	public ShowTableButton(Color color, String title, String[] tableHeader,
			Supplier<Collection<? extends ToTable>> supplier) {
		this(color, title, tableHeader, supplier, Optional.empty());
	}

}
