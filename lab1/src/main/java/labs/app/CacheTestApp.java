package labs.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import labs.app.common.Constants;
import labs.app.common.sequence.FixedSizeElementSequence;
import labs.app.common.sequence.Sequence;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheTestApp {

	private static Logger LOG = LoggerFactory.getLogger(CacheTestApp.class);
	private static final String CACHE_NAME = "testCache";
	private static final int NUMBER_OF_THREADS = 10;
	private static final int NUMBER_OF_PUTS = 100000;
	private static final int SIZE_IN_KB = 1000;
	private static  ExecutorService WRITE_EXECUTOR;

	public static void main(String[] args) {
		CacheTestApp cacheTestApp = new CacheTestApp();
		cacheTestApp.write();
		WRITE_EXECUTOR.shutdown();
		
	}

	void write() {
		WRITE_EXECUTOR = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		Sequence<Element> dataGenerator = new FixedSizeElementSequence(
				NUMBER_OF_PUTS, Constants._1KB * SIZE_IN_KB);

		while (dataGenerator.hasNext()) {
			final Element element = dataGenerator.next();
			WRITE_EXECUTOR.execute(new Runnable() {
				public void run() {
					write(element);
				}
			});
		}
	}

	void write(Element element) {
		LOG.info("writing element="+element);
		getCache().put(element);
	}

	// utility functions

	CacheManager getCacheManager() {
		return CacheManager.create("ehcache.xml");
	}

	Cache getCache() {
		return getCacheManager().getCache(CACHE_NAME);
	}
}
