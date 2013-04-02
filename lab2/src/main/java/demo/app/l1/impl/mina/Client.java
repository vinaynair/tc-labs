package demo.app.l1.impl.mina;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket _socket;
	BufferedOutputStream _bos;
	BufferedInputStream _bis;

	Client(String hostname, int port) {
		try {
			_socket = new Socket(hostname, port);
			_bos = new BufferedOutputStream(_socket.getOutputStream());
			_bis = new BufferedInputStream(_socket.getInputStream());

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String sendAndReceive(String o) {
		try {
			_bos.write(o.getBytes());
			_bos.flush();
			return new BufferedReader(new InputStreamReader(_bis)).readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			_bos.close();
			_bis.close();
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return _socket.toString();
	}

}