package demo.app;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

import net.sf.ehcache.Element;
import net.sf.ehcache.management.ManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.reporting.ConsoleReporter;

import demo.common.Util;
import demo.common.sequence.FixedSizeElementSequence;
import demo.workers.PutWork;

public class MultiThreadedLoad {
	private static Logger LOG = LoggerFactory
			.getLogger(MultiThreadedLoad.class);

	public static void main(String[] args) throws Exception {
		// Cache cache = getCache("tsa-ehcache.xml");
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(Config.CACHE_MANAGER, mBeanServer,
				false, false, false, true);

		// cache data generator sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);

		Config.CACHE.removeAll();
		LOG.info("cache size should be zero. size=" + Config.CACHE.getSize()
				+ "\n starting load");
		Util.sleepFor(3);
		Config.CACHE.setNodeBulkLoadEnabled(true);
		ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(
				Config.NUMBER_OF_ENTRIES);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 100,
				TimeUnit.SECONDS, taskQueue);
		executor.prestartAllCoreThreads();

		long start = System.currentTimeMillis();
		while (elementSequence.hasNext()) {
			Element element = elementSequence.next();
			executor.execute(new PutWork(Config.CACHE, element));
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

		Config.CACHE.setNodeBulkLoadEnabled(false);

		Config.CALL_TIMER.stop();// stop the timer
		long endOfBulkLoadReset = System.currentTimeMillis();

		int endSize = Config.CACHE.getSize();
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
