package world;

import java.time.LocalDateTime;

import program.DuplicatePrint;
import program.ProgramParser;
import ui.windows.ToTable;

public class RentRequest implements Comparable<RentRequest>, ToTable, DuplicatePrint {

	public final LocalDateTime dateTime;
	public final String userID;
	public final String vehicleID;
	public final Coordinates start;
	public final Coordinates destination;
	public final int duration;
	public final boolean breakDown;
	public final boolean promotion;

	private final String[] TABLE_ROW;

	public RentRequest(LocalDateTime dateTime, String userID, String vehicleID, Coordinates start,
			Coordinates destination, int duration, boolean breakDown, boolean promotion) {
		this.dateTime = dateTime;
		this.userID = userID;
		this.vehicleID = vehicleID;
		this.start = start;
		this.destination = destination;
		this.duration = duration;
		this.breakDown = breakDown;
		this.promotion = promotion;
		this.TABLE_ROW = new String[] { ProgramParser.toString(dateTime), userID, vehicleID, start.toString(),
				destination.toString(), String.valueOf(duration), ProgramParser.toString(breakDown),
				ProgramParser.toString(promotion) };
	}

	public static final String[] TABLE_HEADER = { "Date/Time", "User", "Vehicle ID", "Start location", "Destination",
			"Duration", "Broken down", "Promotion" };

	@Override
	public String[] toTableRow() {
		return TABLE_ROW;
	}

	@Override
	public void printDuplicateError() {
		System.out.println("Vehicle " + vehicleID + " is already reserved at " + ProgramParser.toString(dateTime));
	}

	@Override
	public int compareTo(RentRequest rent2) {
		int cmp = dateTime.compareTo(rent2.dateTime);
		if (cmp == 0)
			cmp = vehicleID.compareTo(rent2.vehicleID);
		return cmp;
	}

}
