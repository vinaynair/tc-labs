package demo.ehcache.spring.model;

import java.util.HashMap;
import java.util.Map;

public class CacheContents{
	private String name;
	private Map<String,String> keyValues = new HashMap<String,String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(Map<String, String> keyValues) {
		this.keyValues = keyValues;
	}
}
