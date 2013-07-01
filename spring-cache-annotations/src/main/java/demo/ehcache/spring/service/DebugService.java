package demo.ehcache.spring.service;

import java.util.List;

import demo.ehcache.spring.model.CacheContents;

public interface DebugService {
	List<CacheContents> getCacheDetails();
}
