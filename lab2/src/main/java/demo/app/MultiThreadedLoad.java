package demo.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;

import demo.common.Util;
import demo.common.sequence.FixedSizeElementSequence;

/**
 * 
 * 
 * @see Config Configuration details behind the load including cache config are
 *      here
 * @author vch
 * 
 */
public class MultiThreadedLoad {
	private static Logger LOG = LoggerFactory
			.getLogger(MultiThreadedLoad.class);

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		Cache cache = config.getCache();

		// cache data generator sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);

		// clear the inmemory store before we begin
		cache.removeAll();
		LOG.info("cache size should be zero. size=" + cache.getSize()
				+ "\n starting load");

		// start the thread pool
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("Using a threadpool of " + numberOfProcessors
				+ " to perform load");
		ExecutorService executor = Executors
				.newFixedThreadPool(numberOfProcessors);
		Util.sleepFor(3);

		// start load routine
		cache.setNodeBulkLoadEnabled(true);
		long start = System.currentTimeMillis();
		while (elementSequence.hasNext()) {
			Element element = elementSequence.next();
			executor.execute(new PutWork(cache, element));
		}
		executor.shutdown();
		boolean allTasksCompleted = executor.awaitTermination(5,
				TimeUnit.MINUTES);
		while (!allTasksCompleted) {
			System.out
					.println("All tasks did not complete in 5 minutes.waiting for another 5 minutes");
			allTasksCompleted = executor.awaitTermination(5, TimeUnit.MINUTES);
		}
		// now remove bulkmode
		long endOfWorkerThread = System.currentTimeMillis();

		cache.setNodeBulkLoadEnabled(false);
		// stop the timer, maybe we should be stoping
		// it somewhere else. anyways.
		Config.CALL_TIMER.stop();
		long endOfBulkLoadReset = System.currentTimeMillis();

		int endSize = cache.getSize();
		LOG.info("load complete. total time for " + Config.NUMBER_OF_ENTRIES
				+ " puts to complete is " + (endOfWorkerThread - start)
				+ "ms, and time it took to reset bulk mode is "
				+ (endOfBulkLoadReset - endOfWorkerThread) + "ms \n End size="
				+ endSize
				+ "\n Spin up the JMX console to view metrics before exiting");

		// just wait for return
		// for now, waiting for user input to close the
		// program. This gives us time to look at the metrics on JMX console or
		// TMC :)

		Util.waitForInput();
		// use console reporter to report metrics.
		ConsoleReporter.enable(1, TimeUnit.SECONDS);
		// a hack to have it print report before exiting
		Util.sleepFor(2);

	}

	/**
	 * lazy , creating static class for compactness of readability :)
	 * 
	 * @author vch
	 * 
	 */
	static class PutWork implements Runnable {
		Cache _cache;
		Element _element;

		public PutWork(Cache cache, Element element) {
			_cache = cache;
			_element = element;
		}

		public void run() {
			TimerContext time = Config.CALL_TIMER.time();
			_cache.put(_element);
			time.stop();
		}
	}

}
