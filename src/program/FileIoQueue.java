package program;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class FileIoQueue {

	private static final BlockingQueue<Runnable> ioJobs = new ArrayBlockingQueue<Runnable>(500);

	private static Runnable poisonPill = () -> {
		System.out.println("Disk writer thread is terminating.");
	};

	private static Thread writerThread = new Thread(() -> {
		Runnable job = null;
		while (true)
			try {
				(job = ioJobs.take()).run();
				if (job == poisonPill)
					break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}, "Disk writing thread");

	static {
		writerThread.setDaemon(true);
		writerThread.start();
	}

	public static void saveAsFile(String folder, String file, String data) {
		try {
			ioJobs.put(() -> {
				Path path = Path.of(folder + File.separatorChar + file + ".txt");
				try {
					Files.createFile(path);
				} catch (IOException e) {
				}
				try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.APPEND)) {
					Charset charset = Charset.forName("UTF-8");
					fileChannel.write(charset.encode(data));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void serializeToFile(String folder, String file, Serializable object) {
		ioJobs.add(() -> {
			try {
				try (FileOutputStream fileOutStream = new FileOutputStream(
						folder + File.separatorChar + file + ".dat")) {
					try (ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutStream)) {
						objOutStream.writeObject(object);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static Object returnObject = null;

	public static Object deserializeFromFile(String folder, String file) {
		Object lock = new Object();
		synchronized (lock) {
			ioJobs.add(() -> {
				try (FileInputStream fileInputStream = new FileInputStream(folder + File.separatorChar + file)) {
					try (ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream)) {
						try {
							returnObject = objInputStream.readObject();
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				synchronized (lock) {
					lock.notify();
				}
			});
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitForOperations();
		return returnObject;
	}

	public static void waitForOperations() {
		Object lock = new Object();
		synchronized (lock) {
			try {
				ioJobs.put(() -> {
					synchronized (lock) {
						lock.notify();
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createDirectory(String pathToFolder) {
		try {
			ioJobs.put(() -> {
				try {
					Files.createDirectory(Path.of(pathToFolder));
				} catch (IOException e) {
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void clearDirectory(String string) {
		try {
			ioJobs.put(() -> {
				System.out.println("Clearing folder : " + string);
				for (File file : Path.of(string).toFile().listFiles())
					if (!file.isDirectory()) {
						file.delete();
						System.out.println("File deleted : " + file.getName());
					}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static File[] files = null;

	public static File[] getFiles(String path) {
		Object lock = new Object();
		try {
			synchronized (lock) {
				ioJobs.put(() -> {
					files = Path.of(path).toFile().listFiles();
					synchronized (lock) {
						lock.notify();
					}
				});
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return files;
	}

	public static void terminateWriterThread() {
		try {
			ioJobs.put(poisonPill);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
