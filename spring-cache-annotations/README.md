# Caching services & associated implementation using shared Terracotta BigMemory EhCache


[Sample servicePut](src/main/java/demo/ehcache/spring/service/impl/CustomerOrderServiceImpl.java) makes use of both jaxrs and spring cache annotations

To start jetty with opensource ehcache: - 
`$> mvn clean compile -P opensource jetty:run` 

To start jetty with **BigMemory server array**, start server array, replace [ehcache.xml](src/main/resources/ehcache.xml) with [terracotta-ehcache.xml](src/main/resources/terracotta-ehcache.xml), and start jetty with the following maven profile: -
`$> mvn clean compile -P terracotta jetty:run` 

## Pre-requisities for using BigMemory
* Copy terracotta-license.key to root folder. One can download trial license for Terracotta from Terracotta.org
* Start TSA server using [tc-config](src/main/resources/tc-config.xml) such as the one provided 





