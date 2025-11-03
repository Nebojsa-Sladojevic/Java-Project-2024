package ui.buttons;

import java.util.Collection;
import java.util.function.Supplier;

import ui.UIParameters;
import ui.windows.ToTable;

public class UsersRentButton extends ShowTableButton {

	private static final long serialVersionUID = 1L;

	public UsersRentButton(String title, String[] tableHeader, Supplier<Collection<? extends ToTable>> supplier) {
		super(UIParameters.Colors.BUTTON_BACKGROUND_USERS_RENTS, title, tableHeader, supplier);
	}

}
