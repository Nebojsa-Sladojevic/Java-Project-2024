package ui.buttons;

import java.util.function.Supplier;

import ui.UIParameters;
import ui.windows.ProgramDialog;

public class FinancialStatsButton extends ProgramButton {

	private static final long serialVersionUID = 1L;

	public FinancialStatsButton(String label, Supplier<String[]> strings) {
		super(label, null, UIParameters.Colors.BUTTON_BACKGROUND_FINANCIAL);
		ProgramDialog financialStatus = new ProgramDialog();
		addActionListener(e -> financialStatus.launchProgramDialog(label, strings.get()));
		setWidth(UIParameters.Dimensions.BUTTON_WIDTH_FINANCIAL);
	}

}
