# Capabilities covered
* **Bulk loading**: One can bulk load depending upon the need using a basic [put() call](src/main/java/demo/app/Load.java) after setting the cache in bulk mode,  
`$> mvn clean compile exec:exec -Dapp=demo.app.Load`

or [multi-thread these puts/load](src/main/java/demo/app/MultiThreadedLoad.java) to address slow data sources
`$> mvn clean compile exec:exec -Dapp=demo.app.MultiThreadedLoad`

or to gain better performance [batch the puts](src/main/java/demo/app/MultiThreadedBatchLoad.java) as well.
`$> mvn clean compile exec:exec -Dapp=demo.app.MultiThreadedBatchLoad`




* **Consistency models**: Setup consists of two L1s backed by an L2
 

![consistency](https://raw.github.com/vinaynair/tc-labs/master/lab2/src/main/resources/consistency.jpg "consistency model illustration")


 Each [L1 app](src/main/java/demo/app/l1/impl/mina/MinaL1AppServer.java) exposes a telnet friendly interface to query the distributed cache.

 To start one L1 @ port 9123
 
`$> mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.MinaL1AppServer -Dport=9123`

, and to start another L1 @ port 9124
`mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.MinaL1AppServer -Dport=9123`
 
 One can telnet to each of these apps and try basic read & update to cache - local and backing distributed L2 cache
 ```
 telnet localhost 9123
 Trying ::1...
 Connected to localhost.
 Escape character is '^]'.
 read 1
 null
 update 1 a
 true
 read 1
 a
 close
 Connection closed by foreign host.
 ```
 
 Run the client that updates one L1 and instanteously turns around and reads the same key on the other L1. 
 Obviously STRONG consistency guarantees that reads on any L1 gets the latest updates.
 EVENTUAL consistency ensures updates on all L1 in near-real time but doesnt guarenteed real-time. 
 Having said that for all end-user scenarios EVENTUAL should be sufficient. Note that most NoSql solution support only EVENTUAL, where as Terracotta allows one to configure consistency levels.
 
 There is also a provided [client](src/main/java/demo/app/l1/impl/mina/SimpleL1AppClient.java) that updates one L1 and then quickly turn around & read the same key from the other L1. See below as to how to run the same
 
 ```
 mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.SimpleL1AppClient -Dport1=9123 -Dport2=9124 -DchangeTo=NEWVALUE1
 ```
 
 * **Boot strapping from L2**: An L1 backed by an L2 after a restart doesn't auto-magically load old key-values from the backing L2. One can add that magic back in easily by [warming the L1](http://ehcache.org/documentation/configuration/configuration#cache-warming-for-multi-tier-caches) after restart from the backing L2 using a [Cache bootstrap provided by Terracotta](http://ehcache.org/apidocs/net/sf/ehcache/terracotta/TerracottaBootstrapCacheLoaderFactory.html). 
 A [sample](src/main/java/demo/app/PutOnBootStrappedCache.java) that does just that is provided too :-
 
 ```
 mvn clean compile exec:exec -Dapp=demo.app.PutOnBootStrappedCache
 ```
 
# Pre-requisities
* Copy terracotta-license.key to root folder
* Start TSA server using [tc-config](src/main/resources/tc-config.xml) such as the one provided 





