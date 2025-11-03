package program;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ui.panels.MapField;
import ui.panels.StatusPanel;
import ui.panels.StatusSemaphore;
import ui.windows.ProgramDialog;
import world.Bicycle;
import world.Car;
import world.Coordinates;
import world.FinancialReport;
import world.Money;
import world.RentReport;
import world.RentRequest;
import world.Scooter;
import world.User;
import world.Vehicle;

public abstract class RentalService {

	private static final HashMap<String, Integer> usersRentCount = new HashMap<>();
	private static final HashMap<String, User> users = new HashMap<>();
	private static final HashMap<String, Vehicle> vehicles = new HashMap<>();
	private static final HashSet<Car> cars = new HashSet<>();
	private static final HashSet<Bicycle> bicycles = new HashSet<>();
	private static final HashSet<Scooter> scooters = new HashSet<>();
	private static final TreeSet<RentRequest> rentRequests = new TreeSet<>();
	private static final ArrayList<RentReport> rentReports = new ArrayList<>();
	private static final ArrayList<Vehicle.BreakDown> breakDowns = new ArrayList<>();

	private static FinancialReport financialReport = new FinancialReport();
	private static TreeMap<LocalDate, FinancialReport> financialDailyReports = new TreeMap<>();

	private static LocalDateTime currentDateTime;

	private static ProgramDialog programDialog = new ProgramDialog();

	private static <T> Stream<T> streamObjectsFromCsvFile(Function<String[], Optional<T>> function, Path path) {
		try {
			return Files.lines(path).map(str -> str.split(",")).map(function).filter(Optional::isPresent)
					.map(Optional::get);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Stream.empty();
	}

	private static Optional<Vehicle> toVehicle(String[] strings) {
		Vehicle vehicle = null;
		try {
			if (strings.length == 9) {
				String id = strings[0];
				String manufacturer = strings[1];
				String model = strings[2];
				LocalDate boughtDate = strings[3].length() > 0 ? ProgramParser.parseInputDate(strings[3]) : null;
				Money price = strings[4].length() > 0 ? new Money(strings[4]) : new Money(0);
				int range = strings[5].length() > 0 ? Integer.valueOf(strings[5]) : 0;
				int maxSpeed = strings[6].length() > 0 ? Integer.valueOf(strings[6]) : 0;
				String description = strings[7];
				String type = strings[8];
				if (type.equals("automobil"))
					vehicle = new Car(id, manufacturer, model, boughtDate, price, description);
				else if (type.equals("bicikl"))
					vehicle = new Bicycle(id, manufacturer, model, range, price);
				else if (type.equals("trotinet"))
					vehicle = new Scooter(id, manufacturer, model, maxSpeed, price);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(vehicle);
	}

	private static Optional<RentRequest> toRentRequest(String[] strings) {
		RentRequest rent = null;
		try {
			rent = new RentRequest(ProgramParser.parseInputDateTime(strings[0]), strings[1], strings[2],
					new Coordinates(strings[3], strings[4]), new Coordinates(strings[5], strings[6]),
					Integer.valueOf(strings[7]), ProgramParser.parseInputBoolean(strings[8]),
					ProgramParser.parseInputBoolean(strings[9]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(rent);
	}

	private static void addVehicle(Vehicle vehicle) {
		vehicles.put(vehicle.ID, vehicle);
		if (vehicle instanceof Car)
			cars.add((Car) vehicle);
		else if (vehicle instanceof Bicycle)
			bicycles.add((Bicycle) vehicle);
		else if (vehicle instanceof Scooter)
			scooters.add((Scooter) vehicle);
		else
			try {
				throw new ClassCastException("Unknown vehicle type in program!");
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
	}

	private static User generateRandomUser(String userId) {
		String documentNumber = Integer.toUnsignedString(RandomNumberGenerator.nextInt(), Character.MAX_RADIX)
				.toUpperCase();
		User.Document document = RandomNumberGenerator.nextBoolean() ? new User.IDCard(documentNumber)
				: new User.Passport(documentNumber);
		String drivingLicenceNumber = Integer.toUnsignedString(RandomNumberGenerator.nextInt(), Character.MAX_RADIX)
				.toUpperCase();
		return new User(userId, document, drivingLicenceNumber);
	}

	static {
		financialDailyReports.put(financialReport.date, financialReport);
		Path pathToVehiclesFile = Path.of(ProgramProperties.PATH_TO_VEHICLES_FILE);
		Path pathToRentRequestsFile = Path.of(ProgramProperties.PATH_TO_RENT_REQUESTS_FILE);
		streamObjectsFromCsvFile(RentalService::toVehicle, pathToVehiclesFile).forEach(RentalService::addVehicle);
		streamObjectsFromCsvFile(RentalService::toRentRequest, pathToRentRequestsFile).forEach(reRe -> {
			if (rentRequests.add(reRe) == false)
				reRe.printDuplicateError();
		});
		for (RentRequest rentRequest : rentRequests)
			if (users.containsKey(rentRequest.userID) == false) {
				User user = generateRandomUser(rentRequest.userID);
				users.put(user.ID, user);
			}
	}

	private static void updateDateTime(LocalDateTime dateTime, Consumer<String> setStatus) {
		currentDateTime = dateTime;
		setStatus.accept("Running : " + ProgramParser.toString(currentDateTime));
	}

	public static synchronized void startSimulation(MapField[][] mapGrid, Consumer<String> setStatus,
			StatusSemaphore semaphore) {
		String message;
		if (rentRequests.isEmpty() == false) {
			Queue<User.UserThread> activeUsers = new LinkedList<>();
			Iterator<RentRequest> rentIter = rentRequests.iterator();
			updateDateTime(rentRequests.first().dateTime, setStatus);
			semaphore.setWaiting();
			while (true) {
				RentRequest rent = rentIter.hasNext() ? rentIter.next() : null;
				if (rent == null || currentDateTime.isEqual(rent.dateTime) == false) {
					for (User.UserThread activeUserThread : activeUsers)
						try {
							activeUserThread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					activeUsers.clear();
					if (rent == null)
						break;
					else {
						semaphore.setQueued();
						int sleepInterval = currentDateTime.toLocalDate().isEqual(rent.dateTime.toLocalDate())
								? ProgramProperties.TIME_INTERVAL
								: ProgramProperties.DAY_INTERVAL;
						try {
							Thread.sleep(sleepInterval);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						semaphore.setWaiting();
						updateDateTime(rent.dateTime, setStatus);
					}
				}
				User user = users.get(rent.userID);
				Vehicle vehicle = vehicles.get(rent.vehicleID);
				if (vehicle == null)
					System.out.println("Vehicle " + rent.vehicleID + " doesn't exist.");
				else {
					vehicle.recharge();
					User.UserThread userThread = user.new UserThread(mapGrid, vehicle, rent.start, rent.destination,
							rent.duration, rent.promotion, rent.breakDown);
					userThread.start();
					activeUsers.add(userThread);
				}
			}
			semaphore.setBusy();
			setStatus.accept("Simulation finished. Calculating financial data...");
			calculateFinancialStats();
			message = "Simulation finished";
		} else
			message = "Renting queue is empty";
		setStatus.accept("Simulation complete");
		programDialog.launchProgramDialog("Simulation", new String[] { message });
	}

	public static synchronized void calculateFinancialStats() {
		for (File file : FileIoQueue.getFiles(ProgramProperties.PATH_TO_FINANCIAL_FOLDER)) {
			RentReport rentReport = (RentReport) FileIoQueue.deserializeFromFile(ProgramProperties.PATH_TO_FINANCIAL_FOLDER,
					file.getName());
			synchronized (rentReports) {
				rentReports.add(rentReport);
			}
			Money serviceCost = new Money(0);
			Vehicle vehicle = vehicles.get(rentReport.vehicleID);
			if (rentReport.isBrokenDown)
				serviceCost = Money.negate(vehicle.serviceCost);
			synchronized (financialReport) {
				financialReport.addValuesToReport(rentReport, serviceCost);
			}
			synchronized (financialDailyReports) {
				LocalDate rentDate = rentReport.rentDateTime.toLocalDate();
				FinancialReport financDayRep;
				if ((financDayRep = financialDailyReports.get(rentDate)) == null)
					financialDailyReports.put(rentDate, financDayRep = new FinancialReport(rentDate));
				financDayRep.addValuesToReport(rentReport, serviceCost);
			}
			vehicle.income.addCosts(rentReport.totalPrice, serviceCost);
		}
	}

	public static String[] generateFinancialSummary() {
		synchronized (financialReport) {
			return financialReport.toStrings();
		}
	}

	public static TreeSet<Vehicle.Income> generateVehicleIncomeData() {
		return vehicles.entrySet().stream().map(me -> me.getValue().income)
				.collect(Collectors.toCollection(TreeSet::new));
	}

	public static TreeSet<Car.Income> generateCarsIncomeData() {
		return cars.stream().map(c -> c.income).collect(Collectors.toCollection(TreeSet::new));
	}

	public static TreeSet<Bicycle.Income> generateBicyclesIncomeData() {
		return bicycles.stream().map(b -> b.income).collect(Collectors.toCollection(TreeSet::new));
	}

	public static TreeSet<Scooter.Income> generateScootersIncomeData() {
		return scooters.stream().map(s -> s.income).collect(Collectors.toCollection(TreeSet::new));
	}

	public static List<FinancialReport> getFinancialDailyReports() {
		return financialDailyReports.entrySet().stream().map(Map.Entry::getValue).toList();
	}

	public static TreeSet<Vehicle> getVehicles() {
		return vehicles.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));
	}

	public static List<StatusPanel> getVehicleStatusPanels() {
		return vehicles.entrySet().stream().map(Map.Entry::getValue).sorted().map(Vehicle::getStatusPanel).toList();
	}

	public static List<StatusPanel> getUsersStatusPanels() {
		return users.entrySet().stream().map(Map.Entry::getValue).sorted().map(User::getStatusPanel).toList();
	}

	public static Collection<Car> getCars() {
		return cars;
	}

	public static Set<Bicycle> getBicycles() {
		return bicycles;
	}

	public static Set<Scooter> getScooters() {
		return scooters;
	}

	public static TreeSet<RentRequest> getRentRequests() {
		return rentRequests;
	}

	public static ArrayList<RentReport> getRentReports() {
		return rentReports;
	}

	public static ArrayList<Vehicle.BreakDown> getBreakDowns() {
		synchronized (breakDowns) {
			breakDowns.sort(null);
		}
		return breakDowns;
	}

	public static TreeSet<User> getUsers() {
		return users.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));
	}

	public static LocalDateTime getDateTime() {
		return currentDateTime;
	}

	public static void finishRent(User user, LocalDateTime rentDateTime, Vehicle vehicle, int timePassed,
			boolean isBrokenDown, boolean isDiscountedPromotion, boolean hasPassedWideZone) throws ClassCastException {
		Integer numberOfRents;
		synchronized (usersRentCount) {
			numberOfRents = usersRentCount.get(user.ID);
			if (numberOfRents == null)
				numberOfRents = 0;
			usersRentCount.put(user.ID, ++numberOfRents);
		}
		Money unitPrice = new Money(0);
		if (vehicle instanceof Car)
			unitPrice = new Money(ProgramProperties.CAR_UNIT_PRICE);
		else if (vehicle instanceof Bicycle)
			unitPrice = new Money(ProgramProperties.BICYCLE_UNIT_PRICE);
		else if (vehicle instanceof Scooter)
			unitPrice = new Money(ProgramProperties.SCOOTER_UNIT_PRICE);
		else
			try {
				throw new ClassCastException("Unknown vehicle type in program!");
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		try {
			RentReport rentReport = new RentReport(rentDateTime, user.ID, user.document.toString(),
					user.drivingLicenceNumber, vehicle.ID, unitPrice, timePassed, isBrokenDown, numberOfRents % 10 == 0,
					isDiscountedPromotion, hasPassedWideZone);
			FileIoQueue.saveAsFile(ProgramProperties.PATH_TO_BILLS_FOLDER,
					ProgramParser.toStringFilename(rentDateTime) + '-' + user.ID + '-' + vehicle.ID,
					rentReport.toBill());
			FileIoQueue.serializeToFile(ProgramProperties.PATH_TO_FINANCIAL_FOLDER,
					ProgramParser.toStringFilename(rentDateTime) + '-' + user.ID + '-' + vehicle.ID, rentReport);
//			synchronized (rentReports) {
//				rentReports.add(rentReport);
//			}
//			long serviceCost = 0;
			if (isBrokenDown) {
				String[] breakDownDescriptions = { "Engine failure", "Battery failure", "Electronics failure" };
				Vehicle.BreakDown breakDown = vehicle.new BreakDown(rentDateTime,
						RandomNumberGenerator.takeRandomString(breakDownDescriptions));
				synchronized (breakDowns) {
					breakDowns.add(breakDown);
				}
//				serviceCost = breakDown.getServiceCost();
			}
//			synchronized (financialReport) {
//				financialReport.addValuesToReport(rentReport, serviceCost);
//			}
//			synchronized (financialDailyReports) {
//				LocalDate rentDate = rentDateTime.toLocalDate();
//				FinancialReport financDayRep;
//				if ((financDayRep = financialDailyReports.get(rentDate)) == null)
//					financialDailyReports.put(rentDate, financDayRep = new FinancialReport(rentDate));
//				financDayRep.addValuesToReport(rentReport, serviceCost);
//			}
//			vehicle.income.addCosts(rentReport.totalPrice, serviceCost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
