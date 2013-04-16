# Lab objectives
* Basic put and get operations on cache: See [sample code](src/test/java/org/terracotta/labs/lab1/SimplePutAndGetScenarioTest.java)
* Understand locks : See [sample code](src/test/java/org/terracotta/labs/lab5/LockingScenarioTest.java) & [docs](http://terracotta.org/documentation/bigmemorygo/api/explicitlocking)
* Searching elements within a cache: See [sample code]()
# Setup
* Copy terracotta-license.key to src/test/resources folder
* Set DirectMemory capacity for maven runtime using `MAVEN_OPTS="-XX:MaxDirectMemorySize=4g -Xmx1024MB"`

# Running all labs and associated scenarios
`$> mvn test`


