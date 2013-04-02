package demo.app.l1.impl;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import net.sf.ehcache.Element;
import net.sf.ehcache.management.ManagementService;
import demo.app.Config;
import demo.app.l1.RemoteCacheApi;
import demo.common.Filler;

/**
 * Update and read the value within the filler entry
 * 
 * @author vch
 * 
 */
public class L1App implements RemoteCacheApi {

	public L1App() {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(Config.CACHE_MANAGER, mBeanServer,
				false, false, false, true);
	}
	
	/* (non-Javadoc)
	 * @see demo.app.l1.RemoteCacheApi#read(java.lang.String)
	 */
	public String read(String key) {
		Element e = Config.CACHE.get(key);
		if (e == null)
			return null;
		Filler filler = (Filler) e.getObjectValue();
		return filler.getValue().toString();
	}

	
	public boolean update(String key,String value) {
		Filler filler = new Filler(Integer.parseInt(key), Config.SIZE_OF_ENTRY,
				value);
		Config.CACHE.put(new Element(key, filler));
		return true;
	}
}
