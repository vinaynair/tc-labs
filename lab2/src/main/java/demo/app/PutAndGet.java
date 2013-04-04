package demo.app;

import net.sf.ehcache.Cache;
import demo.common.Constants;
import demo.common.sequence.FixedSizeElementSequence;
import demo.common.sequence.IntegerSequence;
import demo.workers.GetWorker;
import demo.workers.PutWorker;

/**
 * Usage: see readme.txt
 * 
 * @author vch
 * 
 */
public class PutAndGet {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		Cache cache = config.getCache();

		// cache data generator sequence
		// 10MB cache entry sequence
		FixedSizeElementSequence elementSequence = new FixedSizeElementSequence(
				Config.NUMBER_OF_ENTRIES, Config.SIZE_OF_ENTRY
						* Constants._1KB);
		IntegerSequence keySequence = new IntegerSequence(
				Config.NUMBER_OF_ENTRIES);

		// start putting data into the cache
		PutWorker putWorker = new PutWorker(cache, elementSequence);
		// TODO: using metrics measure time
		putWorker.start();
		// for now, waiting for put to complete
		putWorker.join();

		// now start getting from cache ( in a separate thread)
		GetWorker getWorker = new GetWorker(cache, keySequence);
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
