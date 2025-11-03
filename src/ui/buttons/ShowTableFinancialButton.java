package ui.buttons;

import java.util.Collection;
import java.util.function.Supplier;

import ui.UIParameters;
import ui.windows.ToTable;

public class ShowTableFinancialButton extends ShowTableButton {

	private static final long serialVersionUID = 1L;

	public ShowTableFinancialButton(String title, String[] tableHeader,
			Supplier<Collection<? extends ToTable>> supplier) {
		super(UIParameters.Colors.BUTTON_BACKGROUND_FINANCIAL, title, tableHeader, supplier);
		setWidth(UIParameters.Dimensions.BUTTON_WIDTH_FINANCIAL);
	}

}
