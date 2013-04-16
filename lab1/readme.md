# Lab objectives
* Basic put and get operations on cache: See [sample code](src/test/java/labs/lab1/SimplePutAndGetScenarioTest.java)
* Understand locks : See [sample code](src/test/java/labs/lab5/LockingScenarioTest.java) & [docs](http://terracotta.org/documentation/bigmemorygo/api/explicitlocking)
* Searching elements within a cache: See [sample code](src/test/java/labs/lab6/SearchTest.java)

# Setup
* Copy terracotta-license.key to src/test/resources folder
* Set DirectMemory capacity for maven runtime using `MAVEN_OPTS="-XX:MaxDirectMemorySize=4g -Xmx1024MB"`
__(TODO: there is a better way to do this using maven exec plugin. will add in the near/far future)__

# Running all labs and associated scenarios
`$> mvn test`


