package demo.app;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import net.sf.ehcache.management.ManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.common.sequence.IntegerSequence;
import demo.workers.GetWorker;

public class Get {
	private static Logger LOG = LoggerFactory.getLogger(Get.class);

	public static void main(String[] args) throws Exception {
		// Cache cache = getCache("tsa-ehcache.xml");
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(Config.CACHE_MANAGER, mBeanServer,
				false, false, false, true);

		// cache key generator sequence
		IntegerSequence keySequence = new IntegerSequence(
				Config.NUMBER_OF_ENTRIES);
		
		// now start getting from cache ( in a separate thread)
		GetWorker getWorker = new GetWorker(Config.CACHE, keySequence);
		getWorker.start();
		// for now waiting for get to complete too
		getWorker.join();

		
		// just wait for return
		// for now, waiting for user input to close the
		// program
		System.out.println("Enter to exit!");
		System.in.read();
	}

}
