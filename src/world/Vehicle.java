package world;

import java.time.LocalDateTime;

import program.DuplicatePrint;
import program.ProgramParser;
import program.ProgramProperties;
import program.RentalService;
import ui.panels.MapField;
import ui.panels.StatusPanel;
import ui.windows.ToTable;

/**
 * @author Nebojša Sladojević
 * @version 1.0 
 */
/**
 * The {@code Vehicle} class represents an abstract vehicle in the system. It
 * provides methods for managing vehicle breakdowns, income, and movement. This
 * class implements {@code ToTable}, {@code Comparable<Vehicle>}, and
 * {@code DuplicatePrint}.
 */

public abstract class Vehicle implements ToTable, Comparable<Vehicle>, DuplicatePrint {

	/**
	 * The {@code BreakDown} class represents a breakdown of a vehicle. It includes
	 * description, with date and time of the breakdown.
	 */
	public class BreakDown implements ToTable, Comparable<BreakDown> {

		private final String vehicleID;
		private final String description;
		private final LocalDateTime dateTime;

		public BreakDown(LocalDateTime dateTime, String description) {
			this.dateTime = dateTime;
			this.vehicleID = Vehicle.this.ID;
			this.description = description;
		}

		@Override
		public String[] toTableRow() {
			return new String[] { ProgramParser.toString(dateTime), vehicleID, Vehicle.this.serviceCost.toString(),
					description };
		}

		public static final String[] TABLE_HEADER = new String[] { "Date/Time", "Vehicle ID", "Repair cost",
				"Description" };

		@Override
		public int compareTo(BreakDown break2) {
			return dateTime.compareTo(break2.dateTime);
		}

		public Money getServiceCost() {
			return Vehicle.this.serviceCost;
		}

	}

	/**
	 * The {@code Income} class represents the income details of a vehicle, such as
	 * gross income, and maintenance costs.
	 */
	public class Income implements ToTable, Comparable<Income> {

		public final String ID;
		private Money grossIncome = new Money();
		private Money maintenance = new Money();

		public Income(String ID) {
			this.ID = ID;
		}

		public synchronized void addCosts(Money grossIncome, Money maintenance) {
			this.grossIncome.add(grossIncome);
			this.maintenance.add(maintenance);
		}

		private Money getNetIncome() {
			return Money.sub(grossIncome, maintenance);
		}

		@Override
		public synchronized String[] toTableRow() {
			return new String[] { ID, grossIncome.toString(), maintenance.toString(), getNetIncome().toString() };
		}

		@Override
		public int compareTo(Income vehInc) {
			int cmp = vehInc.getNetIncome().compareTo(this.getNetIncome());
			if (cmp == 0)
				cmp = ID.compareTo(vehInc.ID);
			return cmp;
		}

		@Override
		public int hashCode() {
			return ID.hashCode();
		}

	}

	public static final String[] INCOME_TABLE_HEADER = { "Vehicle", "Gross income", "Maintenance", "Net income" };

	public final String ID;
	protected final String manufacturer, model;
	protected final Money priceBought;
	public final Money serviceCost;

	private byte batteryLevel = 0;

	private final StatusPanel statusPanel;
	public final Income income;

	public Vehicle(String ID, String manufacturer, String model, Money priceBought, double serviceCostCoefficient) {
		this.ID = ID;
		this.manufacturer = manufacturer;
		this.model = model;
		this.priceBought = priceBought;
		this.serviceCost = Money.mul(priceBought, serviceCostCoefficient);
		this.statusPanel = new StatusPanel(ID);
		this.income = new Income(ID);
	}

	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	public void recharge() {
		batteryLevel = 100;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean drive(MapField[][] mapGrid, User user, Coordinates start, Coordinates destination,
			int duration, boolean hasPromotionDiscount, boolean hasBrokenDown) throws Exception {
		statusPanel.setBusy();
		LocalDateTime rentDateTime = RentalService.getDateTime();
		boolean hasPassedWideArea = false;
		int drivingInterval = (duration * 1000) / (ProgramProperties.SPEED_COEFFICIENT * start.distance(destination));
		boolean destinationReached = false;
		try {
			int i = start.X, j = start.Y;
			while (true) {
				if (mapGrid[i][j].IS_INNER == false)
					hasPassedWideArea = true;
				mapGrid[i][j].occupy(this);
				sleep(drivingInterval);
				mapGrid[i][j].leave(this);
				if (batteryLevel == 0)
					throw new Exception("Battery is empty! Veicle : " + ID);
				if (i > destination.X)
					i--;
				else if (i < destination.X)
					i++;
				else if (j > destination.Y)
					j--;
				else if (j < destination.Y)
					j++;
				else {
					destinationReached = true;
					break;
				}
				batteryLevel--;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		RentalService.finishRent(user, rentDateTime, this, duration, hasBrokenDown, hasPromotionDiscount,
				hasPassedWideArea);
		statusPanel.setWaiting();
		return destinationReached;
	}

	public abstract String getType();

	public static String[] toTableRow(Vehicle veh) {
		return new String[] { veh.getType(), veh.ID, veh.manufacturer, veh.model, veh.priceBought.toString() };
	}

	public static final String[] TABLE_HEADER = new String[] { "Type", "ID", "Manufacturer", "Model", "Price" };

	@Override
	public void printDuplicateError() {
		System.out.println("Vehicle " + ID + " already exist in a program");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (obj instanceof Vehicle)
			return ((Vehicle) obj).ID.equals(ID);
		return false;
	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder(ID).append(' ').append(batteryLevel).append('%').toString();
	}

	@Override
	public int compareTo(Vehicle vehicle2) {
		return ID.compareTo(vehicle2.ID);
	}

}
