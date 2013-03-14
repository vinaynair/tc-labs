package org.terracotta.labs.lab1;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
/**
 * <b>Scenario</b>
 * <ul>
 * <li>stores 1-n integers in cache
 * <li>cache is persisted to disk (see ehcache.xml)
 * <li>subsequent runs will load data from disk into cache
 * </ul>
 * @author vch
 *
 */
public class EhcacheStandAloneTest {

	public static void main(String[] args) throws Exception {
		// pass in the number of object you want to generate, default is 100
		int numberOfObjects = Integer.parseInt(args.length == 0 ? "100": args[0]);
		System.out.println(numberOfObjects);
		//create the CacheManager
		CacheManager cacheManager = new CacheManager();
		//get a handle on the Cache
		Cache myCache = cacheManager.getCache("myCache");
		
		//iterate through numberOfObjects and use the iterator as the key, value does not matter at this time
		int hitCounter=0;
		for (int i = 0; i < numberOfObjects; i++) {
			String key = new Integer(i).toString();
			if (!checkInCache(key, myCache)) {
				System.out.println("putting "+key+" in cache");
				//when putting in the cache, it is as an Element, the key and the value must be serializable
				myCache.put(new Element(key, "Value"));
				if (i % 2 == 0) {
					//check ever other one to see if it is in the cache
					if(!checkInCache(key, myCache)) {
						System.out.println("Danger Will Robbins.... " + key + " is not in the cache!!!");
					}
				}	
			} else {
				hitCounter++;
			}
		}
		System.out.println("found "+hitCounter+" elements within the cache");

		//used if you are persisting to disk use only if stand alone Ehcache
		myCache.flush();
		
		//important shutdown hook if your JVM goes down; use only if stand alone Ehcache
		cacheManager.shutdown();
	}
	
	//check to see if the key is in the cache
	private static boolean checkInCache(String key, Cache myCache) throws Exception {
		Element element = myCache.get(key);
		boolean returnValue = false;
		if (element != null) {
			//System.out.println(key + " is in the cache!!!");
			returnValue = true;
		}
		return returnValue;
	}
}
