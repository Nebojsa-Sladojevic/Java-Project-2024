package ui.panels;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;

import ui.UIParameters;
import world.Vehicle;

public class MapField extends JLabel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	private final Color DEFAULT_COLOR;
	public final boolean IS_INNER;

	private Color getColorLevel() {
		switch (vehicles.size()) {
		case 0:
			return DEFAULT_COLOR;
		case 1:
			return UIParameters.Colors.FIELD_BACKGROUND_1_VEHICLE;
		case 2:
			return UIParameters.Colors.FIELD_BACKGROUND_2_VEHICLES;
		default:
			return UIParameters.Colors.FIELD_BACKGROUND_3_VEHICLES;
		}
	}

	private void rewriteLabel() {
		StringBuilder labelText = new StringBuilder();
		for (int i = 0; i < vehicles.size(); i++) {
			if (i > 0)
				labelText.append(' ');
			labelText.append(vehicles.get(i));
		}
		setBackground(getColorLevel());
		setText(labelText.toString());
	}

	public MapField(boolean isInner) {
		this.IS_INNER = isInner;
		setOpaque(true);
		setFont(UIParameters.Fonts.LABEL_FONT);
		setForeground(UIParameters.Colors.TEXT_FIELD);
		setHorizontalAlignment(CENTER);
		setBackground(DEFAULT_COLOR = isInner ? UIParameters.Colors.FIELD_BACKGROUND_INNER_ZONE
				: UIParameters.Colors.FIELD_BACKGROUND_OUTER_ZONE);
		setPreferredSize(UIParameters.Dimensions.MAP_FIELD_DIMENSION);
	}

	public void occupy(Vehicle vehicle) {
		synchronized (this) {
			vehicles.add(vehicle);
			rewriteLabel();
		}
	}

	public void leave(Vehicle vehicle) {
		synchronized (this) {
			vehicles.remove(vehicle);
			rewriteLabel();
		}
	}

}
