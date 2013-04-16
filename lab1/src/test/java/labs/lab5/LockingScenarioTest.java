package labs.lab5;

import static org.junit.Assert.*;
import labs.common.AbstractCacheTestScenarioSupport;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO: Barrier <br/>
 * TODO: testing with distributed cache (TSA)
 * 
 * @author vch
 * 
 */
public class LockingScenarioTest extends AbstractCacheTestScenarioSupport {
	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab5-ehcache.xml");
		usesCache("customerCache");
	}

	@Test
	public void gettingWriteLockAgainWillReturnFalse() {
		final Cache customerCache = getCache("customerCache");
		Element customerA = new Element("1", "abc");
		customerCache.put(customerA);
		// 1. start a thread and acquire a write lock
		Thread firstThread = new Thread() {
			public void run() {
				customerCache.acquireWriteLockOnKey("1");
			}
		};
		firstThread.start();
		try {
			firstThread.join();
		} catch (InterruptedException e1) {
			fail("Something went wrong when waiting a thread that got the lock");
		}

		// 2. try getting the lock on the same key and that should fail
		try {
			boolean gotLock = customerCache.tryWriteLockOnKey("1", 10);
			assertFalse(
					"we should not have got the lock since the previous thread took the write lock and died",
					gotLock);
			boolean readLock = customerCache.tryReadLockOnKey("1", 10);
			assertFalse("we should not be able to get the read lock either",
					readLock);
		} catch (InterruptedException e) {
			fail("we should not have timed out");
		}
	}

	@Test
	public void gettingMultipleReadLockShouldBeOk() {
		final Cache customerCache = getCache("customerCache");
		Element customerA = new Element("1", "abc");
		customerCache.put(customerA);
		for (int i = 0; i < 1000; i++) {
			// 1. start a thread and acquire a write lock
			Thread firstThread = new Thread() {
				public void run() {
					try {
						boolean readLock = customerCache.tryReadLockOnKey("1",
								10);
						assertTrue(
								"we should have had multiple read locks without any issues",
								readLock);
					} catch (InterruptedException e) {
						fail("something terrible happened within the thread trying to acquire read locls");
					}
				}

			};
			firstThread.start();

		}

	}

	// TODO
	// TODO: what happens when we have a series of READ and WRITES. does write
	// take precedence or is it all time based? (should be latter, but confirm)
	// TODO can we set timeout for how long can the thread hold write locks?
	public void readCommitted() {

	}
}
