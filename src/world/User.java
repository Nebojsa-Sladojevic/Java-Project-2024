package world;

import java.awt.Color;

import ui.panels.MapField;
import ui.panels.StatusPanel;
import ui.windows.ToTable;

public class User implements ToTable, Comparable<User> {

	public static class Document {

		public final String number;

		@Override
		public String toString() {
			return number;
		}

		protected Document(String number) {
			this.number = number;
		}
	}

	public static class Passport extends Document {
		public Passport(String number) {
			super("Passport: " + number);
		}
	}

	public static class IDCard extends Document {
		public IDCard(String number) {
			super("ID card: " + number);
		}
	}

	public final String ID;
	public final Document document;
	public final String drivingLicenceNumber;

	private final StatusPanel statusPanel;

	public User(String userID, Document document, String drivingLicenceNumber) {
		this.ID = userID;
		this.document = document;
		this.drivingLicenceNumber = drivingLicenceNumber;
		this.statusPanel = new StatusPanel(ID, Color.CYAN);
	}

	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

	public synchronized void rentVehicle(MapField[][] mapGrid, Vehicle vehicle, Coordinates start,
			Coordinates destination, int duration, boolean hasPromotionDiscount, boolean hasBrokenDown)
			throws Exception {
		statusPanel.setBusy();
		vehicle.drive(mapGrid, this, start, destination, duration, hasPromotionDiscount, hasBrokenDown);
		statusPanel.setWaiting();
	}

	public class UserThread extends Thread {

		public UserThread(MapField[][] mapGrid, Vehicle vehicle, Coordinates start, Coordinates destination,
				int duration, boolean hasPromotionDiscount, boolean hasBrokenDown) {
			super(() -> {
				try {
					User.this.rentVehicle(mapGrid, vehicle, start, destination, duration, hasPromotionDiscount,
							hasBrokenDown);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, User.this.ID);
		}

	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	@Override
	public String[] toTableRow() {
		return new String[] { ID, document.toString(), drivingLicenceNumber };
	}

	public static final String[] TABLE_HEADER = new String[] { "User ID", "Document", "Driving licence" };

	@Override
	public int compareTo(User user2) {
		return ID.compareTo(user2.ID);
	}

}
