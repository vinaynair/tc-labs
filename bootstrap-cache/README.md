# Capabilities covered
* **Bootstrap cache**: Ability to load local L1 cache with key+values present from a previous run. Note that the values are read back from L2 TSA on a re-start

Sample consists of a [Put](src/main/java/demo/ehcache/bootstrap/Put.java) application that loads 10000 keys into L2 TSA. 
Note that  snapshots of local keys are taken locally on disk auto-magically.

`$> mvn clean compile exec:exec -Dapp=demo.ehcache.bootstrap.Put` 


Now re-run the [Put](src/main/java/demo/ehcache/bootstrap/Put.java)  application or maybe better, run the [Get](src/main/java/demo/ehcache/bootstrap/Get.java) application. 

You will notice that on start-up it reads the keys present on the application from the previous run and loads those keys+values in the local L1. 

`$> mvn clean compile exec:exec -Dapp=demo.ehcache.bootstrap.Get` 

# Pre-requisities
* Copy terracotta-license.key to root folder
* Start TSA server using [tc-config](src/main/resources/tc-config.xml) such as the one provided 





