package world;

import ui.windows.ToTable;

public class Bicycle extends Vehicle implements ToTable {

	private int range;

	public Bicycle(String ID, String manufacturer, String model, int range, Money priceBought) {
		super(ID, manufacturer, model, priceBought, 0.04f);
		this.range = range;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}

	@Override
	public String[] toTableRow() {
		return new String[] { ID, manufacturer, model, priceBought.toString(), String.valueOf(range) };
	}

	public static final String[] TABLE_HEADER = { "ID", "Manufacturer", "Model", "Price", "Range" };

}
