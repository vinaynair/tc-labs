package demo.app.l1;

/**
 * Simple remote interface to update and read key/value pair within BM entries
 * especially over a text interface like Telnet
 * 
 * @author vch
 * 
 */
public interface RemoteCacheApi {
	String read(String key);

	boolean update(String key, String value);
}
