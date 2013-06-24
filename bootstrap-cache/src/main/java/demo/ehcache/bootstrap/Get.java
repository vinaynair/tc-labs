package demo.ehcache.bootstrap;

import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;

public class Get {
	// metric collectors for put and get calls
	public final static Timer GET_TIMER = Metrics.newTimer(Get.class,
			"get calls", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

	public static void main(String[] args) throws Exception {
		CacheManager cacheManager = CacheManager.newInstance();
		Cache cache = cacheManager.getCache("cacheWithBootstrap");

		// start reporting stats every 5s
		ConsoleReporter.enable(5, TimeUnit.SECONDS);

		// get from remote cache but we are bootstrapping - see ehcache.xml
		get(cache, 10000);

		// lets trigger a snapshot of local keys before we shut
		// ourselves down so that next run can potentially benefit from
		// pre-loaded data on L1
		cache.dispose();
	}

	private static void get(Cache cache, int count) {

		// and now get
		for (int i = 0; i < count; i++) {
			TimerContext time = GET_TIMER.time();
			cache.get(i + "");
			time.stop();
		}

		System.out
				.println("Done performing "
						+ count
						+ " gets  \n(note that stats can report smaller count since it might not have gotten the chance to print the last set of metrics");

	}

}
