#test that only non-null results are getting added to the caches
echo "get customer(1)-->"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/order/customer/1
echo "\r\nget customer(2)"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/order/customer/2
echo "\r\nget customer(3) should not return any contents"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/order/customer/3
echo "\r\nprint cache contents should show all customer and rest caches poulated"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/debug/cache

# test that puts evicts the entry
curl  -H "Content-Type: application/json" -H "Accept: application/json" -X PUT -d '{"id":"1","productName":"iphone","quantity":1,"status":"submitted"}' http://localhost:8080/spring-ehcache-annotation/rest/services/order/customer/1
echo "\r\n cache entry for REST json payload corresponding to customer(1) must be removed--CHECK BELOW"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/debug/cache
echo "\r\n get the customer must repopulate the REST cache entry for customer(1)"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/order/customer/1
echo "\r\n cache entry for REST json payload corresponding to customer(1) must now be back--CHECK BELOW"
curl  -H "Accept: application/json" http://localhost:8080/spring-ehcache-annotation/rest/services/debug/cache
