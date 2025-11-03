package world;

public class Coordinates {

	public final int X, Y;

	public Coordinates(int x, int y) {
		X = x;
		Y = y;
	}

	public Coordinates(String x, String y) {
//		X = Integer.valueOf(x.charAt(0) == '\"' ? x.substring(1, x.length()) : x);
//		Y = Integer.valueOf(y.charAt(y.length() - 1) == '\"' ? y.substring(0, y.length() - 1) : y);
		X = Integer.valueOf(x.substring(1, x.length()));
		Y = Integer.valueOf(y.substring(0, y.length() - 1));
	}

	@Override
	public String toString() {
		return new StringBuilder("{").append(X).append(',').append(Y).append("}").toString();
	}

	public int distance(Coordinates coord2) {
		return Math.abs(X - coord2.X) + Math.abs(Y - coord2.Y);
	}

}
