# OBJECTIVE(S)
* *understand locks* : See [test scenario](src/test/java/org/terracotta/labs/lab5/LockingScenarioTest.java) & [docs](http://terracotta.org/documentation/bigmemorygo/api/explicitlocking)

# SETUP
* Copy terracotta-license.key to src/test/resources folder
* Set DirectMemory capacity for maven runtime using `MAVEN_OPTS="-XX:MaxDirectMemorySize=4g -Xmx1024MB"`

# RUNNING scenarios
`$> mvn test`


