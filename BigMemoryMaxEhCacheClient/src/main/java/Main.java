import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Simple client that put()s and the get()s the data back from the distributed In-memory data store
 */
public class Main {
    public static void main(String[] args) {
        int numberOfElements;
        if (args.length < 1) {
            numberOfElements = 1000;
        } else {
            numberOfElements = Integer.parseInt(args[0]);
        }

        // Get hold of the distributed cache defined within ehcache config xml
        // (defaults to ehcache.xml classpath resource)
        //==================================================================================
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache("distributedInMemoryStoreOne");


        //=======================================================
        //put KeyValue(KV) pairs into distributed in-memory store
        //=======================================================
        for (int i = 0; i < numberOfElements; i++) {
            //construct key+value pair
            String key = "key-" + i;
            String value = "value-" + i;

            //push KV pair into BM
            //=====================
            cache.put(new Element(key, value));
        }
        System.out.println("Put " + cache.getSize() + " KV pairs into the in-memory store");

        //=======================================================
        // get KV pairs from distributed in-memory store
        //======================================================
        System.out.println("Got the following key back from BigMemory");
        for (int i = 0; i < numberOfElements; i++) {
            String key = "key-" + i;

            //get value from BM given the key
            //=================================
            Object value = cache.get(key).getObjectValue();


            // Do something useful with the value

            if (value.equals("value-" + i)) { // check that we got the value we put earlier
                System.out.print(key + ",");
            }

        }

        cacheManager.shutdown();

    }
}
