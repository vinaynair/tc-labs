package demo.ehcache.bootstrap;

import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;

import demo.ehcache.bootstrap.model.Filler;

public class Put {
	// metric collectors for put and get calls
	public final static Timer PUT_TIMER = Metrics.newTimer(Put.class,
			"put calls", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

	public static void main(String[] args) throws Exception {
		CacheManager cacheManager = CacheManager.newInstance();
		Cache cache = cacheManager.getCache("cacheWithBootstrap");

		// start reporting stats every 5s
		ConsoleReporter.enable(5, TimeUnit.SECONDS);
		// put and get data from off-heap + server array backed store
		put(cache, 10000, Filler._1KB);

		// lets trigger a snapshot of local keys before we shut
		// ourselves down so that next run can potentially benefit from
		// pre-loaded data on L1
		cache.dispose();

	}

	private static void put(Cache cache, int count, int sizeOfData) {

		// put first
		for (int i = 0; i < count; i++) {
			TimerContext time = PUT_TIMER.time();
			cache.put(new Element(i + "", new Filler(sizeOfData)));
			time.stop();
		}

		System.out
				.println("Done performing "
						+ count
						+ " puts \n(note that stats can report smaller count since it might not have gotten the chance to print the last set of metrics");

	}

}
