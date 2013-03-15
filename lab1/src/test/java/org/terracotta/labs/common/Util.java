package org.terracotta.labs.common;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class Util {
	public static Cache getCache(String cacheName) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache(cacheName);
		assert cache != null;
		return cache;

	}

	public static void cleanup(String cacheName) {
		getCache(cacheName).flush();
		CacheManager.getInstance().shutdown();
	}

	public static void waitForUserInput() throws Exception {
		System.out.println("Enter to exit!");
		System.in.read();
	}
}
