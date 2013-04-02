package demo.workers;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.TimerContext;

import demo.app.Config;
import demo.common.sequence.Sequence;

public class GetWorker extends Thread {
	private static Logger LOG = LoggerFactory.getLogger(GetWorker.class);
	Cache _cache;
	Sequence<?> _sequence;

	public GetWorker(Cache cache, Sequence<?> sequence) {
		_cache = cache;
		_sequence = sequence;
		setName("get-worker");
	}

	public void run() {
		while (_sequence.hasNext()) {
			TimerContext time = Config.CALL_TIMER.time();
			Element element = _cache.get(_sequence.next());
			time.stop();
			System.out.print(".");
			/*
			 * try { Thread.sleep(10); } catch (InterruptedException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
		}

	}

}
