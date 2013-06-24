package demo.ehcache.bootstrap.util;

import java.io.IOException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheStoreHelper;
import net.sf.ehcache.store.TerracottaStore;

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
	
	public static void printLocalKeys(Cache cache) {
		System.out.println("printing local keys");
		final TerracottaStore store = (TerracottaStore) new CacheStoreHelper(
				(Cache) cache).getStore();
		for (Object key : store.getLocalKeys()) {
			System.out.println("local key:" + key);
		}

	}
}
