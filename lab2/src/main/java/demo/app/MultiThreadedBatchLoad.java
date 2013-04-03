package demo.app;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.management.ManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;

import demo.common.Util;
import demo.common.sequence.FixedSizeElementSequence;

/**
 * Uses batch of puts along with a thread pool to fasten the load process<br/>
 * Basic usage involves <br/>
 * <code>
 * loader = new MultiThreadedBatchLoader(cache,batchsize);<br/>
 * loader.put(element); // note that puts are collected internally till a batch is formed for submission<br/>
 * loader.shutdownAndWaitFor(5000);//milliseconds<br/>
 * </code>
 * This implementation is <b>not threadsafe</b><br/>
 * {@link Config} contains all configuration details behind the load including
 * cache configuration
 * 
 * @author vch
 * 
 */
public class MultiThreadedBatchLoad {
	private static Logger LOG = LoggerFactory
			.getLogger(MultiThreadedBatchLoad.class);

	/**
	 * Broad set of steps performed:-
	 * <ul>
	 * <li>clear cache</li>
	 * <li>set cache in bulk mode</li>
	 * <li>set batch size of 100 and then perform puts</li>
	 * <li>wait for batch loader to gracefully shutdown</li>
	 * <li>reset cache to normal mode</li>
	 * </ul>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Cache cache = getCache("tsa-ehcache.xml");
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(Config.CACHE_MANAGER, mBeanServer,
				false, false, false, true);

		// cache data generator sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);

		// clear the inmemory store before we begin
		Config.CACHE.removeAll();
		LOG.info("cache size should be zero. size=" + Config.CACHE.getSize()
				+ "\n starting load");
		MultiThreadedBatchLoad loader = new MultiThreadedBatchLoad(
				Config.CACHE, 100);

		Util.sleepFor(3);

		// start load routine
		Config.CACHE.setNodeBulkLoadEnabled(true);
		long start = System.currentTimeMillis();
		while (elementSequence.hasNext()) {
			Element element = elementSequence.next();
			loader.put(element);
		}
		boolean allTasksCompleted = loader.shutdownAndWaitFor(60 * 5 * 1000);

		while (!allTasksCompleted) {
			System.out
					.println("All tasks did not complete in 5 minutes.waiting for another 5 minutes");
			allTasksCompleted = loader.shutdownAndWaitFor(60 * 5 * 1000);
		}
		// now remove bulkmode
		long endOfWorkerThread = System.currentTimeMillis();

		Config.CACHE.setNodeBulkLoadEnabled(false);
		// stop the timer, maybe we should be stoping
		// it somewhere else. anyways.
		Config.CALL_TIMER.stop();
		long endOfBulkLoadReset = System.currentTimeMillis();

		int endSize = Config.CACHE.getSize();
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

	//
	// parameters
	//
	int _batchSize = 100;
	int _currentSize = 0;
	ExecutorService _executor;
	ArrayList<Element> _puts = new ArrayList<Element>();
	Cache _cache;

	public MultiThreadedBatchLoad(Cache cache, int batchSize) {
		_batchSize = batchSize;
		// start the thread pool
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		System.out
				.println("Using a threadpool of " + numberOfProcessors
						+ " with batch size of " + batchSize
						+ " to perform batch load");
		_executor = Executors.newFixedThreadPool(numberOfProcessors);
		_cache = cache;
	}

	public void put(Element element) {
		// if batch size is met, submit job, else simply add it to the task
		// queue
		if (_currentSize == _batchSize - 1) {
			_currentSize = 0;
			_puts.add(element);
			_executor.execute(new PutBatchWork(_cache, _puts));
			_puts = new ArrayList<Element>();
		} else {
			_puts.add(element);
			_currentSize++;
		}

	}

	/**
	 * <b> must</b> be called to submit the last batch of jobs
	 */
	public boolean shutdownAndWaitFor(int timeInSeconds) throws Exception {
		_executor.execute(new PutBatchWork(_cache, _puts));
		_executor.shutdown();
		boolean isComplete = _executor.awaitTermination(timeInSeconds,
				TimeUnit.SECONDS);
		return isComplete;
	}

	/**
	 * Put batch worker that uses {@link Ehcache#putAll(java.util.Collection)}
	 * api
	 * 
	 * @author vch
	 * 
	 */
	class PutBatchWork implements Runnable {
		Cache _cache;
		ArrayList<Element> _puts;

		PutBatchWork(Cache cache, ArrayList<Element> puts) {
			_puts = puts;
			_cache = cache;
		}

		public void run() {
			// LOG.info("Submitting a batch of " + _puts.size());
			TimerContext time = Config.CALL_TIMER.time();
			_cache.putAll(_puts);
			time.stop();
			_puts.clear();
		}
	}

}
