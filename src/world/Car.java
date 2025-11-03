package world;

import java.time.LocalDate;

import program.ProgramParser;
import ui.windows.ToTable;

public class Car extends Vehicle implements ToTable {

	private LocalDate dateBought;
	private String description;

	public Car(String ID, String manufacturer, String model, LocalDate dateBought, Money priceBought,
			String description) {
		super(ID, manufacturer, model, priceBought, 0.07f);
		this.dateBought = dateBought;
		this.description = description;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}

	@Override
	public String[] toTableRow() {
		return new String[] { ID, manufacturer, model, ProgramParser.toString(dateBought), priceBought.toString(),
				description };
	}

	public static final String[] TABLE_HEADER = { "ID", "Manufacturer", "Model", "Date bought", "Price",
			"Description" };

}
