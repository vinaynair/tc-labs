package demo.app;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.common.Util;
import demo.common.sequence.FixedSizeElementSequence;
import demo.workers.PutWorker;

public class PutOnBootStrappedCache {
	private static Logger LOG = LoggerFactory
			.getLogger(PutOnBootStrappedCache.class);

	public static void main(String[] args) throws Exception {
		URL url = PutOnBootStrappedCache.class.getResource("/tsa-ehcache.xml");
		CacheManager cacheManager = CacheManager.newInstance(url);
		Cache cache = cacheManager.getCache("bootStrappedCache");

		System.out
				.println("Putting a few keys to bootstrapped cache. \n"
						+ "for the first run, one would see key snapshots being locally saved on disk"
						+ "and for the second run, one would see keys being loaded locally during cache bootstrap");
		// cache data generator sequence
		// 10MB cache entry sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY);

		// start putting data into the cache
		PutWorker putWorker = new PutWorker(cache, elementSequence);
		// TODO: using metrics measure time
		putWorker.start();
		// for now, waiting for put to complete
		putWorker.join();

		// just wait for return
		// for now, waiting for user input to close the
		// program
		Util.waitForInput();

	}

}
