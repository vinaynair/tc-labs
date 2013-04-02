package demo.app;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import net.sf.ehcache.management.ManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.common.Constants;
import demo.common.sequence.FixedSizeElementSequence;
import demo.workers.PutWorker;

public class Put {
	private static Logger LOG = LoggerFactory.getLogger(Put.class);

	public static void main(String[] args) throws Exception {
		// Cache cache = getCache("tsa-ehcache.xml");
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(Config.CACHE_MANAGER, mBeanServer, false,
				false, false, true);

		
		// cache data generator sequence
		// 10MB cache entry sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);
		
		// start putting data into the cache
		PutWorker putWorker = new PutWorker(Config.CACHE, elementSequence);
		// TODO: using metrics measure time
		putWorker.start();
		// for now, waiting for put to complete
		putWorker.join();

		
		// just wait for return
		// for now, waiting for user input to close the
		// program
		System.out.println("Enter to exit!");
		System.in.read();
	}

	

}
