package program;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ProgramParser {

	public static String toString(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
	}

	public static String toStringFilename(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH.mm"));
	}

	public static String toString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	public static long priceToCents(String priceDecimal) {
		String[] split = priceDecimal.split("\\.");
		if (split.length == 1) {
			return Long.valueOf(priceDecimal) * 100;
		} else if (split.length == 2) {
			long units = Long.valueOf(split[0]);
			long cents;
			switch (split[1].length()) {
			case 0:
				cents = 0;
				break;
			case 1:
				cents = Long.valueOf(split[1]) * 10;
				break;
			case 2:
				cents = Long.valueOf(split[1]);
				break;
			default:
				cents = Long.valueOf(split[1].substring(0, 1));
			}
			return units * 100 + cents;
		} else {
			System.out.println("Cannot parse string to price : " + priceDecimal);
			return 0;
		}
	}

	public static String toString(boolean value) {
		return value ? "Yes" : "No";
	}

	public static boolean parseInputBoolean(String string) throws Exception {
		if (string.equals("da"))
			return true;
		if (string.equals("ne"))
			return false;
		throw new Exception("String \"" + string + "\" is not a valid boolean value");
	}

	public static LocalDate parseInputDate(String dateString) {
		return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d.M.y."));
	}

	public static LocalDateTime parseInputDateTime(String dateString) {
		return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("d.M.y HH:mm"));
	}

}
