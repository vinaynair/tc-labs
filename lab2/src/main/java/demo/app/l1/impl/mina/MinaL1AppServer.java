package demo.app.l1.impl.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import demo.app.l1.impl.L1App;
import demo.common.Util;

/**
 * Exposes a telnet interface to {@link L1App} <br/>
 * Telnet interface allows one to perform the something like:-
 * 
 * <pre>
 * telnet localhost 9123
 *  Trying ::1...
 *  Connected to localhost.
 *  Escape character is '^]'.
 *  read 1
 *  null
 *  update 1 a
 *  true
 *  read 1
 *  a
 *  close
 *  Connection closed by foreign host.
 * </pre>
 * 
 * @author vch
 * 
 */
public class MinaL1AppServer extends IoHandlerAdapter {

	private L1App _app;
	private IoAcceptor _acceptor;

	public static void main(String[] args) throws Exception {
		MinaL1AppServer app = new MinaL1AppServer(new L1App());
		app.start();
	}

	public MinaL1AppServer(L1App app) {
		_app = app;
	}

	public void start() throws Exception {
		int port = Util.getSystemPropertyWithDefault("port", 9123);
		_acceptor = new NioSocketAcceptor();
		/*
		 * acceptor.getFilterChain().addLast("codec", new
		 * ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		 */
		_acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		_acceptor.setHandler(this);

		_acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		_acceptor.bind(new InetSocketAddress(port));
		System.out.println("Started listening on " + port);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		if (message instanceof String) {
			System.out.println("received command-" + message);
			StringTokenizer commandTokens = new StringTokenizer(
					message.toString());
			String command = commandTokens.nextToken();
			if ("read".equals(command)) {
				String key = commandTokens.nextToken();
				String value = _app.read(key);
				if (value == null) {
					session.write("null");
					return;
				}
				session.write(value);
			} else if ("update".equals(command)) {
				String key = commandTokens.nextToken();
				String value = commandTokens.nextToken();
				boolean b = _app.update(key, value);
				session.write(b);
			} else if ("changeCache".equals(command)) {
				String cache = commandTokens.nextToken();
				boolean changed = _app.setCache(cache);
				if (changed)
					session.write("changed");
				else
					session.write("cache " + cache + " not found");
			}  else {
				System.out.println("closing");
				session.close(true);
			}
		} else {
			System.out.println("received -" + message.toString()
					+ "- and closing");
			session.close(true);
		}

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("IDLE " + session.getIdleCount(status));
	}

}
