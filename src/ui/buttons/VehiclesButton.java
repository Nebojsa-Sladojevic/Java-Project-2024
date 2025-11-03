package ui.buttons;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import ui.UIParameters;
import ui.windows.ToTable;

public class VehiclesButton extends ShowTableButton {

	private static final long serialVersionUID = 1L;

	public VehiclesButton(String title, String[] tableHeader, Supplier<Collection<? extends ToTable>> supplier,
			Function<Object, String[]> customRowFunction) {
		super(UIParameters.Colors.BUTTON_BACKGROUND_VEHICLES, title, tableHeader, supplier,
				Optional.ofNullable(customRowFunction));
	}

	public VehiclesButton(String title, String[] tableHeader, Supplier<Collection<? extends ToTable>> supplier) {
		this(title, tableHeader, supplier, null);
	}

}
