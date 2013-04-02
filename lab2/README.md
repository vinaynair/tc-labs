

# Capabilities covered
* Basic [load](src/main/java/demo/app/Load.java) with [multithreaded load](src/main/java/demo/app/MultiThreadedLoad.java)
`$> mvn clean compile exec:exec -Dapp=demo.app.MultiThreadedLoad`

* Basic set of 2 L1s backed by an L2 that can be modified to identify how various consistency model works

 The L1 app is an application that exposes a telnet friendly interface to query the cache, both local as well as the L2.
 
 To start one L1 @ port 9123 
`$> mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.MinaL1AppServer -Dport=9123`

, and to start another L1 @ port 9124
`mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.MinaL1AppServer -Dport=9123`
 
 One can telnet to each of these apps and try basic read & update to cache - local and backing distributed L2 cache
 ``telnet localhost 9123
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
 Connection closed by foreign host.``
 
 Run the client that updates one L1 and instanteously turns around and reads the same key on the other L1. 
 Obviously STRONG consistency guarantees that reads on any L1 gets the latest updates.
 EVENTUAL consistency ensures updates on all L1 in near-real time but doesnt guarenteed real-time. 
 Having said that for all end-user scenarios EVENTUAL should be sufficient. Note that most NoSql solution support only EVENTUAL, where as Terracotta allows one to configure consistency levels
 
 `mvn clean compile exec:exec -Dapp=demo.app.l1.impl.mina.SimpleL1AppClient -Dport1=9123 -Dport2=9124 -DchangeTo=NEWVALUE1`
 
# Pre-requisities
* Copy terracotta-license.key to root folder
* Start [TSA server](src/main/resources/tc-config.xml)




