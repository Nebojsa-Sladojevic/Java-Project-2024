package world;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import program.ProgramParser;
import ui.windows.ToTable;

public class FinancialReport implements ToTable, Comparable<FinancialReport> {

	public final LocalDate date;

	private Money totalDiscountExpenses = new Money();
	private Money totalPromotionExpenses = new Money();
	private Money totalRidesIncome = new Money();
	private int totalWideZoneRides = 0;
	private Money totalWideZoneIncome = new Money();
	private int totalNarrowZoneRides = 0;
	private Money totalNarrowZoneIncome = new Money();

	public Money totalRepairExpenses = new Money();

	public FinancialReport(LocalDate date) {
		this.date = date;
	}

	public FinancialReport() {
		this(LocalDate.MAX);
	}

	public void addValuesToReport(RentReport rentReport, Money repairCost) {
		this.totalDiscountExpenses.add(rentReport.discount);
		this.totalPromotionExpenses.add(rentReport.discountPromotion);
		this.totalRidesIncome.add(rentReport.totalPrice);
		if (rentReport.isWideZonePassed) {
			this.totalWideZoneRides++;
			this.totalWideZoneIncome.add(rentReport.totalPrice);
		} else {
			this.totalNarrowZoneRides++;
			this.totalNarrowZoneIncome.add(rentReport.totalPrice);
		}
		this.totalRepairExpenses.add(repairCost);
	}

	public static final String[] TABLE_HEADER = { "Date", "Discounts", "Promotions", "Wide zone", "Narrow zone",
			"Ride incomes", "Maintenance exenses", "Repair expenses", "Gross income" };

	private Money maintenanceExpenses;
	private Money companyExpenses;
	private Money grossIncome;

	private ArrayList<String> toStringList() {
		ArrayList<String> list = new ArrayList<>();
		list.add(date.compareTo(LocalDate.MAX) == 0 ? "all time" : ProgramParser.toString(date));
		list.add(totalDiscountExpenses.toString());
		list.add(totalPromotionExpenses.toString());
		list.add(new StringBuilder("(").append(Integer.toString(totalWideZoneRides)).append(") ")
				.append(totalWideZoneIncome.toString()).toString());
		list.add(new StringBuilder("(").append(Integer.toString(totalNarrowZoneRides)).append(") ")
				.append(totalNarrowZoneIncome.toString()).toString());
		list.add(totalRidesIncome.toString());
		list.add((maintenanceExpenses = Money.negate(Money.mul(totalRidesIncome, 0.2))).toString());
		list.add(totalRepairExpenses.toString());
		list.add((grossIncome = Money.add(Money.add(totalRidesIncome, totalRepairExpenses), maintenanceExpenses))
				.toString());
		return list;
	}

	@Override
	public String[] toTableRow() {
		return toStringList().toArray(String[]::new);
	}

	public String[] toStrings() {
		ArrayList<String> strings = toStringList();
		Money taxes;
		strings.add((companyExpenses = Money.negate(Money.mul(totalRidesIncome, 0.2))).toString());
		strings.add((taxes = Money.negate((grossIncome.isPositive() ? Money.mul(grossIncome, 0.2) : new Money(0))))
				.toString());
		strings.add(Money.add(Money.add(grossIncome, taxes), companyExpenses).toString());
		Iterator<String> iterColumns = strings.iterator();
		return new String[] { "Date : " + iterColumns.next(), //
				"Total discounts : " + iterColumns.next(), //
				"Total promotions : " + iterColumns.next(), //
				"Wide zone rides income : " + iterColumns.next(), //
				"Narrow zone rides income : " + iterColumns.next(), //
				"Total rides income : " + iterColumns.next(), //
				"Maintenance expenses : " + iterColumns.next(), //
				"Repair expenses : " + iterColumns.next(), //
				"Gross income : " + iterColumns.next(), //
				"Company running expenses : " + iterColumns.next(), //
				"Taxes : " + iterColumns.next(), //
				"Net income : " + iterColumns.next() };
	}

	@Override
	public int compareTo(FinancialReport finRep) {
		return date.compareTo(finRep.date);
	}

}
