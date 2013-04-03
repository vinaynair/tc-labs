package demo.app.l1.impl.mina;

import demo.common.Util;
import demo.common.sequence.IntegerSequence;

public class SimpleL1AppClient {
	public static void main(String[] args) throws Throwable {
		// get arguments
		int server1Port = Util.getSystemPropertyWithDefault("port1", -1);
		int server2Port = Util.getSystemPropertyWithDefault("port2", -1);
		String newUpdateValue = Util.getSystemPropertyWithDefault("changeTo",
				"a");

		System.out.println("updating all values to " + newUpdateValue);
		System.out.println("connecting to server1@" + server1Port
				+ ", and server2@" + server2Port);
		Client server1 = new Client("localhost", server1Port);
		System.out.println("client session created-" + server1);
		Client server2 = new Client("localhost", server2Port);
		System.out.println("client session created-" + server2);

		// cache key generator sequence
		IntegerSequence keySequence = new IntegerSequence(100000);

		while (keySequence.hasNext()) {
			Integer key = keySequence.next();
			// update server1
			String value1 = server1.sendAndReceive("update " + key + " "
					+ newUpdateValue + "\r\n");
			// read from server2
			String value2 = server2.sendAndReceive("read " + key + "\r\n");
			System.out.print(".");
			// if update doesnt match read, print the miss
			if (false == value2.equals(newUpdateValue)) {
				System.out.println("MISS. Check if strong consistency is set. [got " + value2 + ", expected "
						+ newUpdateValue + "]");
			}
		}
		// close
		server1.close();
		server2.close();

	}

}
