package labs.lab11;

import java.util.Collection;

import labs.common.AbstractCacheTestScenarioSupport;
import labs.common.Util;
import net.sf.ehcache.Cache;
import net.sf.ehcache.cluster.CacheCluster;
import net.sf.ehcache.cluster.ClusterNode;
import net.sf.ehcache.cluster.ClusterScheme;
import net.sf.ehcache.cluster.ClusterTopologyListener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ClusterEventsTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab11-clusterevents-ehcache.xml");
		usesCache("productCache");

	}

	

	@Test
	@Ignore
	public void putProductsAndSeeNonStopBehavior() throws Exception {
		Cache products = getCache("productCache");
		CacheCluster cluster = getCacheManager().getCluster(
				ClusterScheme.TERRACOTTA);

		cluster.addTopologyListener(new ClusterTopologyListener() {
			public void nodeJoined(ClusterNode node) {
				System.out.println("NodeId joined: " + node.getId());
			}

			public void nodeLeft(ClusterNode node) {
				System.out.println("NodeId left: " + node.getId());
			}

			public void clusterOnline(ClusterNode node) {
				System.out.println("ClusterOnline: " + node.getId());
			}

			public void clusterOffline(ClusterNode node) {
				System.out.println("ClusterOffline: " + node.getId());
			}

			public void clusterRejoined(ClusterNode node, ClusterNode newNode) {
				System.out
						.println(node + " rejoined the cluster as " + newNode);
			}
		});
		System.out.println("Stop TSA");
		for (int i = 0; i < 60; i++) {
			Util.sleepFor(1000);
			if (cluster.isClusterOnline()) {
				Collection<ClusterNode> nodes = cluster.getNodes();
				for (ClusterNode node : nodes) {
					System.out.println("NodeId:" + node.getId()
							+ " Node HostName: " + node.getHostname()
							+ " Node IP: " + node.getIp());
				}
			} else {
				System.out.println("Cluster Is Offline: ");
			}
		}

	}

}
