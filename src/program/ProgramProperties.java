package program;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.util.Properties;

public abstract class ProgramProperties {

	private static final String PROPERTIES_FILE_PATH = "config.properties";
	private static final String LOCK_FILE_PATH = "program.lock";
	private static Properties properties = new Properties();

	static {
		Path lockFilePath = Path.of(LOCK_FILE_PATH);
		try {
			try {
				Files.delete(lockFilePath);
			} catch (IOException e) {
			}
			Files.createFile(lockFilePath);
		} catch (IOException e) {
			System.out.println("Program is already running");
			System.exit(1);
		}
		try {
			@SuppressWarnings({ "resource", "unused" })
			FileChannel lockFileChannel = new RandomAccessFile(lockFilePath.toFile(), "rw").getChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File propertiesFile = new File(PROPERTIES_FILE_PATH);
		try (FileReader fileReader = new FileReader(propertiesFile)) {
			properties.load(fileReader);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Properties file doesn't exist!");
			System.out.println("Creating new properties file.");
			System.out.println("New file location : " + propertiesFile.getAbsolutePath());
			properties = new Properties();
			properties.put("PATH_TO_VEHICLES_FILE", "Vehicles.csv");
			properties.put("PATH_TO_RENT_REQUESTS_FILE", "RentRequests.csv");
			properties.put("PATH_TO_BILLS_FOLDER", "Bills");
			properties.put("PATH_TO_FINANCIAL_FOLDER", "Financial");
			properties.put("CURRENCY", "KM");
			properties.put("TARRIF_NARROW", "0");
			properties.put("TARRIF_WIDE", "0");
			properties.put("DISCOUNT", "0");
			properties.put("DISCOUNT_PROM", "0");
			properties.put("CAR_UNIT_PRICE", "0");
			properties.put("BICYCLE_UNIT_PRICE", "0");
			properties.put("SCOOTER_UNIT_PRICE", "0");
			properties.put("SPEED_COEFFICIENT", "1");
			properties.put("TIME_INTERVAL", "500");
			properties.put("DAY_INTERVAL", "5000");
			try (FileWriter fileWriter = new FileWriter(propertiesFile)) {
				properties.store(fileWriter, null);
				System.out.println("Set default values in properties file and run program again!");
				System.out.println("Program is exiting");
				System.exit(1);
			} catch (IOException e1) {
				System.out.println("Creating properties file failed!");
				e1.printStackTrace();
			}
		}
	}

	public static final String PATH_TO_VEHICLES_FILE = properties.getProperty("PATH_TO_VEHICLES_FILE");
	public static final String PATH_TO_RENT_REQUESTS_FILE = properties.getProperty("PATH_TO_RENT_REQUESTS_FILE");
	public static final String PATH_TO_BILLS_FOLDER = properties.getProperty("PATH_TO_BILLS_FOLDER");
	public static final String PATH_TO_FINANCIAL_FOLDER = properties.getProperty("PATH_TO_FINANCIAL_FOLDER");
	public static final String CURRENCY = properties.getProperty("CURRENCY");
	public static final double TARRIF_NARROW = Double.valueOf(properties.getProperty("TARRIF_NARROW"));
	public static final double TARRIF_WIDE = Double.valueOf(properties.getProperty("TARRIF_WIDE"));
	public static final double DISCOUNT = Double.valueOf(properties.getProperty("DISCOUNT"));
	public static final double DISCOUNT_PROM = Double.valueOf(properties.getProperty("DISCOUNT_PROM"));
	public static final long CAR_UNIT_PRICE = ProgramParser.priceToCents(properties.getProperty("CAR_UNIT_PRICE"));
	public static final long BICYCLE_UNIT_PRICE = ProgramParser
			.priceToCents(properties.getProperty("BICYCLE_UNIT_PRICE"));
	public static final long SCOOTER_UNIT_PRICE = ProgramParser
			.priceToCents(properties.getProperty("SCOOTER_UNIT_PRICE"));
	public static final int SPEED_COEFFICIENT = Integer.valueOf(properties.getProperty("SPEED_COEFFICIENT"));
	public static final int TIME_INTERVAL = Integer.valueOf(properties.getProperty("TIME_INTERVAL"));
	public static final int DAY_INTERVAL = Integer.valueOf(properties.getProperty("DAY_INTERVAL"));

	static {
		FileIoQueue.createDirectory(PATH_TO_BILLS_FOLDER);
		FileIoQueue.createDirectory(PATH_TO_FINANCIAL_FOLDER);
		FileIoQueue.waitForOperations();
		FileIoQueue.clearDirectory(PATH_TO_BILLS_FOLDER);
		FileIoQueue.clearDirectory(PATH_TO_FINANCIAL_FOLDER);
		FileIoQueue.waitForOperations();
	}

}
