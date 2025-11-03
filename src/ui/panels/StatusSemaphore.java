package ui.panels;

import java.awt.Dimension;

import ui.UIParameters;

public class StatusSemaphore extends StatusPanel {

	private static final long serialVersionUID = 1L;

	public StatusSemaphore() {
		setPreferredSize(
				new Dimension(2 * UIParameters.Dimensions.BUTTON_HEIGHT, UIParameters.Dimensions.BUTTON_HEIGHT));
	}

}
