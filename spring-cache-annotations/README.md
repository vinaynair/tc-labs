# Caching services & associated implementation using  Terracotta BigMemory 


[Sample service](src/main/java/demo/ehcache/spring/service/impl/CustomerOrderServiceImpl.java) makes use of both jaxrs and spring cache annotations to manage the lifecycle of domain objects as well as REST json representations that are cached.

To start jetty with opensource ehcache running locally: - 
`$> mvn clean compile -P opensource jetty:run` 

To start jetty with **BigMemory server array**, start server array, replace [ehcache.xml](src/main/resources/ehcache.xml) with [terracotta-ehcache.xml](src/main/resources/terracotta-ehcache.xml), and start jetty with the following maven profile: -
`$> mvn clean compile -P terracotta jetty:run` 

A simple curl test that was lazily put together to access & test the customer order rest resource and its lifecycle is provided too. See [curl_test_for_cache_lifecycle](curl_test_for_cache_lifecycle.sh)


## Pre-requisities for using BigMemory
* Copy terracotta-license.key to root folder. One can download trial license for Terracotta from Terracotta.org
* Start TSA server using [tc-config](src/main/resources/tc-config.xml) such as the one provided 





