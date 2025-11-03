package world;

import java.io.Serializable;
import java.time.LocalDateTime;

import program.ProgramParser;
import program.ProgramProperties;
import ui.windows.ToTable;

public class RentReport implements Comparable<RentReport>, ToTable, Serializable {

	private static final long serialVersionUID = 1L;

	private static final double TARRIF_NARROW = ProgramProperties.TARRIF_NARROW;
	private static final double TARRIF_WIDE = ProgramProperties.TARRIF_WIDE;
	private static final double DISCOUNT = ProgramProperties.DISCOUNT;
	private static final double DISCOUNT_PROM = ProgramProperties.DISCOUNT_PROM;

	public final LocalDateTime rentDateTime;
	public final String userID;
	public final String userDocument;
	public final String userDrivingLicence;
	public final String vehicleID;

	public final Money unitPrice;
	public final int duration;
	public final boolean isBrokenDown;
	public final boolean isDiscounted;
	public final boolean isDiscountedPromotion;
	public final boolean isWideZonePassed;

	public final Money ridePrice;
	public final Money discount;
	public final Money discountPromotion;

	public final Money totalPrice;

	public RentReport(LocalDateTime rentDateTime, String userID, String userDocument, String userDrivingLicence,
			String vehicleID, Money unitPrice, int duration, boolean isBrokenDown, boolean isDiscounted,
			boolean isDiscountedPromotion, boolean isWideZonePassed) {

		this.rentDateTime = rentDateTime;
		this.userID = userID;
		this.userDocument = userDocument;
		this.userDrivingLicence = userDrivingLicence;
		this.vehicleID = vehicleID;

		this.unitPrice = unitPrice;
		this.duration = duration;
		this.isBrokenDown = isBrokenDown;
		this.isDiscounted = isDiscounted;
		this.isDiscountedPromotion = isDiscountedPromotion;
		this.isWideZonePassed = isWideZonePassed;

		this.ridePrice = Money.mul(Money.mul(unitPrice, (isWideZonePassed ? TARRIF_WIDE : TARRIF_NARROW)), duration);
		this.discount = isDiscounted ? Money.negate(Money.mul(ridePrice, DISCOUNT)) : new Money(0);
		this.discountPromotion = isDiscountedPromotion ? Money.negate(Money.mul(ridePrice, DISCOUNT_PROM))
				: new Money(0);

		this.totalPrice = isBrokenDown ? new Money(0) : Money.add(Money.add(ridePrice, discount), discountPromotion);

	}

	@Override
	public int compareTo(RentReport rentReport) {
		return rentDateTime.compareTo(rentReport.rentDateTime);
	}

	public static final String[] TABLE_HEADER = new String[] { "Date/Time", "User", "User document", "Driving licence",
			"Vehicle", "Unit price", "Duration", "Wide area", "Price", "Broken down", "Discount", "Promotion",
			"Total price" };

	@Override
	public String[] toTableRow() {
		return new String[] { ProgramParser.toString(rentDateTime), userID, userDocument, userDrivingLicence, vehicleID,
				unitPrice.toString(), String.valueOf(duration), ProgramParser.toString(isWideZonePassed),
				ridePrice.toString(), ProgramParser.toString(isBrokenDown), discount.toString(isDiscounted).toString(),
				discountPromotion.toString(isDiscountedPromotion), totalPrice.toString() };
	}

	public String toBill() {
		StringBuilder billBuilder = new StringBuilder("================================================\n");
		billBuilder.append("Date/time : ").append(ProgramParser.toString(rentDateTime)).append('\n');
		billBuilder.append("Driving licence : ").append(userDrivingLicence).append('\n');
		billBuilder.append("Document : ").append(userDocument).append('\n');
		billBuilder.append("Vehicle : ").append(vehicleID).append('\n');
		billBuilder.append("Time passed : ").append(duration).append("s\n");
		billBuilder.append("Zone : ").append(isWideZonePassed ? "wide" : "narrow").append('\n');
		billBuilder.append("Ride price : ").append(ridePrice).append('\n');
		billBuilder.append("Discount : ").append(discount.toString(isDiscounted).toString()).append('\n');
		billBuilder.append("Promotion : ").append(discountPromotion.toString(isDiscountedPromotion)).append('\n');
		billBuilder.append("Vehicle broken down : ").append(ProgramParser.toString(isBrokenDown)).append('\n');
		billBuilder.append("Total : ").append(totalPrice).append('\n');
		return billBuilder.toString();
	}

}
