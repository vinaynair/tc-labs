# Capabilities covered
* **Bootstrap cache**: Ability to load local cache with key+values present locally from a previous runs. Note that the values are read back from L2 TSA on a re-start
 [Put](src/main/java/demo/ehcache/bootstrap/Put.java) to load 10000 keys into L2 TSA. The program takes key snapshots locally on disk 
`$> mvn clean compile exec:exec -Dapp=demo.ehcache.bootstrap.Put` 


, then  [Get](src/main/java/demo/ehcache/bootstrap/Get.java) to start-up to get those keys. You will note that as the app is loading, it pre-loads the key+values into the L1 asynchronously.
`$> mvn clean compile exec:exec -Dapp=demo.ehcache.bootstrap.Get` 

# Pre-requisities
* Copy terracotta-license.key to root folder
* Start TSA server using [tc-config](src/main/resources/tc-config.xml) such as the one provided 





