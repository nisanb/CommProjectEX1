package il.ac.haifa.is.datacomms.hw1;

import java.time.LocalTime;

/**
 * Application's main class.
 */
public final class Main {
	private Main() {
	}

	public static void main(String[] args) {
		try {
			AmazingRace.getInstance().simulate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Log(String str) {
		System.out.println(LocalTime.now() + " - " + str);
	}
}
