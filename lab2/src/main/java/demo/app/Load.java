package demo.app;

import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.reporting.ConsoleReporter;

import demo.common.Util;
import demo.common.sequence.FixedSizeElementSequence;
import demo.workers.PutWorker;

public class Load {
	private static Logger LOG = LoggerFactory.getLogger(Load.class);

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		Cache cache = config.getCache();

		// cache data generator sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);

		cache.removeAll();
		LOG.info("cache size should be zero. size=" + cache.getSize()
				+ "\n starting load");
		Util.sleepFor(3);
		cache.setNodeBulkLoadEnabled(true);

		long start = System.currentTimeMillis();
		// single threaded for now
		// start putting data into the cache
		PutWorker putWorker = new PutWorker(cache, elementSequence);
		// TODO:use executor to make this multi-threaded
		putWorker.start();
		// for now, waiting for put to complete
		putWorker.join();
		// now remove bulkmode
		long endOfWorkerThread = System.currentTimeMillis();

		cache.setNodeBulkLoadEnabled(false);

		Config.CALL_TIMER.stop();// stop the timer
		long endOfBulkLoadReset = System.currentTimeMillis();

		int endSize = cache.getSize();
		LOG.info("complete. total time for " + Config.NUMBER_OF_ENTRIES
				+ " puts to complete is " + (endOfWorkerThread - start)
				+ "ms, and time it took to reset bulk mode is "
				+ (endOfBulkLoadReset - endOfWorkerThread) + "ms \n End size="
				+ endSize);

		// just wait for return
		// for now, waiting for user input to close the
		// program. This gives us time to look at the metrics on JMX console or
		// TMC :)
		
		Util.waitForInput();
		ConsoleReporter.enable(1, TimeUnit.SECONDS);
		Util.sleepFor(2);

	}

}
