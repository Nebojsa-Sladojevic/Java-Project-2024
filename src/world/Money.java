package world;

import java.io.Serializable;

import program.ProgramParser;
import program.ProgramProperties;

public class Money implements Comparable<Money>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String currencyStr = " " + ProgramProperties.CURRENCY;
	private long cents;

	public Money(long cents) {
		this.cents = cents;
	}

	public Money() {
		this(0);
	}

	public Money(String amount) {
		this.cents = ProgramParser.priceToCents(amount);
	}

	@Override
	public String toString() {
		return String.valueOf(cents / 100) + ',' + String.format("%02d", Math.abs(cents) % 100) + currencyStr;
	}

	public String toString(boolean showValue) {
		return showValue ? this.toString() : "-";
	}

	public boolean isPositive() {
		return cents > 0;
	}

	public void add(Money money) {
		this.cents += money.cents;
	}

	public static Money negate(Money mon) {
		return new Money(-mon.cents);
	}

	public static Money add(Money mon1, Money mon2) {
		return new Money(mon1.cents + mon2.cents);
	}

	public static Money sub(Money mon1, Money mon2) {
		return new Money(mon1.cents - mon2.cents);
	}

	public static Money mul(Money mon1, double multiplier) {
		return new Money((long) (mon1.cents * multiplier));
	}

	public static Money mul(Money mon1, long multiplier) {
		return new Money(mon1.cents * multiplier);
	}

	@Override
	public int compareTo(Money money) {
		return Long.valueOf(this.cents).compareTo(money.cents);
	}
}
