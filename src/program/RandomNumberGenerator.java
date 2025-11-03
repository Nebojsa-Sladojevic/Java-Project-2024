package program;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class RandomNumberGenerator {

	private static abstract class GeneratorQueue {

		private static BlockingQueue<Integer> randomIntegerQueue = new ArrayBlockingQueue<>(200);
		private static BlockingQueue<Long> randomLongQueue = new ArrayBlockingQueue<>(200);
		private static BlockingQueue<Boolean> randomBooleanQueue = new ArrayBlockingQueue<>(200);

		static {
			new Thread(() -> {
				Random randInt = new Random();
				while (true)
					try {
						randomIntegerQueue.put(randInt.nextInt());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}, "Integer generating thread").start();
			new Thread(() -> {
				Random randLong = new Random();
				while (true)
					try {
						randomLongQueue.put(randLong.nextLong());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}, "Long generating thread").start();
			new Thread(() -> {
				Random randBool = new Random();
				while (true)
					try {
						randomBooleanQueue.put(randBool.nextBoolean());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}, "Boolean generating thread").start();
		}

		public static int nextInt() {
			try {
				return randomIntegerQueue.take().intValue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 0;
		}

		public static long nextLong() {
			try {
				return randomLongQueue.take().longValue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 0;
		}

		public static boolean nextBoolean() {
			try {
				return randomBooleanQueue.take().booleanValue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

	}

	public static int nextInt() {
		return GeneratorQueue.nextInt();
	}

	public static long nextLong() {
		return GeneratorQueue.nextLong();
	}

	public static boolean nextBoolean() {
		return GeneratorQueue.nextBoolean();
	}

	public static String takeRandomString(String[] strings) {
		return strings[Math.abs(nextInt()) % strings.length];
	}

}
