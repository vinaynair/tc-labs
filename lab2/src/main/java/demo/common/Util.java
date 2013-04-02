package demo.common;

import java.io.IOException;

public class Util {
	public static void sleepFor(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void waitForInput() {
		System.out.println("Enter to exit");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getSystemPropertyWithDefault(String key, int defaultValue) {
		String portAsString = System.getProperty(key);
		if (portAsString != null) {
			return Integer.parseInt(portAsString);
		} else
			return defaultValue;

	}

	public static String getSystemPropertyWithDefault(String key,
			String defaultValue) {
		String str = System.getProperty(key);
		if (str != null) {
			return str;
		} else
			return defaultValue;

	}
}
