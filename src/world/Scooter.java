package world;

import ui.windows.ToTable;

public class Scooter extends Vehicle implements ToTable {

	private int maxSpeed;

	public Scooter(String ID, String manufacturer, String model, int maxSpeed, Money priceBought) {
		super(ID, manufacturer, model, priceBought, 0.02f);
		this.maxSpeed = maxSpeed;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}

	@Override
	public String[] toTableRow() {
		return new String[] { ID, manufacturer, model, priceBought.toString(), String.valueOf(maxSpeed) };
	}

	public static final String[] TABLE_HEADER = { "ID", "Manufacturer", "Model", "Price", "Max speed" };

}
