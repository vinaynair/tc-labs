package demo.workers;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.core.TimerContext;

import demo.app.Config;
import demo.common.sequence.Sequence;

/**
 * basic cache loader
 * @author vch
 *
 */
public class PutWorker extends Thread{
	private static Logger LOG = LoggerFactory.getLogger(PutWorker.class);
	Cache _cache;
	Sequence<Element> _sequence;
	public PutWorker(Cache cache, Sequence<Element> sequence){
		_cache=cache;
		_sequence=sequence;
		setName("put-worker");
	}
	
	public void run() {
		while(_sequence.hasNext()){
			Element element = _sequence.next();
			System.out.print(".");
			TimerContext time=Config.CALL_TIMER.time();
			_cache.put(element);
			time.stop();
			
		}
		
	}
	void sleep(){
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
