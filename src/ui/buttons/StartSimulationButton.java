package ui.buttons;

import java.awt.event.ActionListener;

import ui.UIParameters;

public class StartSimulationButton extends ProgramButton {

	private static final long serialVersionUID = 1L;

	public StartSimulationButton(String label, ActionListener actionListener) {
		super(label, actionListener, UIParameters.Dimensions.BUTTON_WIDTH_START_SIMULATION);
	}

}
