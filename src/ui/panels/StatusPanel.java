package ui.panels;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.UIParameters;

public class StatusPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public final Color busyColor;
	public final Color queuedColor;
	public final Color waitingColor;

	public StatusPanel(String ID, Color busyColor, Color queuedColor, Color waitingColor) {
		this.busyColor = busyColor;
		this.queuedColor = queuedColor;
		this.waitingColor = waitingColor;
		setBackground(waitingColor);
		setPreferredSize(UIParameters.Dimensions.VEHICLE_STATUS_PANEL_DIMENSION);
		if (ID != null) {
			JLabel label = new JLabel(ID);
			label.setFont(UIParameters.Fonts.LABEL_FONT);
			label.setForeground(Color.BLACK);
			add(label);
		}
	}

	public StatusPanel(String ID, Color waitingColor) {
		this(ID, Color.RED, Color.YELLOW, waitingColor);
	}

	public StatusPanel(String ID) {
		this(ID, Color.GREEN);
	}

	public StatusPanel() {
		this(null);
	}

	public synchronized void setWaiting() {
		setBackground(waitingColor);
	}

	public synchronized void setQueued() {
		setBackground(queuedColor);
	}

	public synchronized void setBusy() {
		setBackground(busyColor);
	}

}
