package labs.timing;

import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Person;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TimeStepsTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab-timing-ehcache.xml");
		usesCache("customerCache");
	}
	@Test
	@Ignore
	public void test(){
		LOG.info("putting");
		getCache("customerCache").put(new Element(1,new Person("abc")));
		LOG.info("put");
	}
}
