package demo.workers;

import com.yammer.metrics.core.TimerContext;

import demo.app.Config;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class PutWork implements Runnable{
	Cache _cache;
	Element _element;
	public PutWork(Cache cache, Element element){
		_cache=cache;
		_element=element;
	}
	public void run(){
		TimerContext time=Config.CALL_TIMER.time();
		_cache.put(_element);
		time.stop();
		System.out.print(".");
	}
}
