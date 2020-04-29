package networking;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client implements Runnable {

	ArrayList<byte[]> commands;
	Queue<byte[]> sendQueue, sendQueueUDP;

	SocketChannel socketTCP;
	DatagramChannel socketUDP;
	SocketAddress address;

	ByteBuffer ibuf, obuf, ubuf;

	public Client() throws IOException {
		address = new InetSocketAddress("127.0.0.1", 9999);
		ibuf = ByteBuffer.allocate(2048);
//		ubuf = ByteBuffer.allocate(1024);
		obuf = ByteBuffer.allocate(1024);

		sendQueue = new LinkedBlockingQueue<byte[]>();

		socketTCP = SocketChannel.open(address);
		socketTCP.configureBlocking(false);
		socketTCP.finishConnect();
		socketTCP.socket().setKeepAlive(true);
//		socketUDP = DatagramChannel.open();
//		socketUDP.connect(address);
//		socketUDP.configureBlocking(false);

	}

	boolean running = false;
	boolean hold;

	@Override
	public void run() {
		try {
			while (running) {

				if (socketTCP.read(ibuf) > 0) {

				}

				if (!sendQueue.isEmpty()) {
					while (!sendQueue.isEmpty()) {
						obuf.put(sendQueue.poll());
					}
					obuf.flip();
					while (obuf.hasRemaining())
						socketTCP.write(obuf);
					obuf.clear();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		Thread t = new Thread(this);
		running = true;
		t.start();
	}

	public void stop() {
		running = false;
	}

	public void writeTCP(byte[] b) {
		sendQueue.add(b);
	}

	public void writeUDP(byte[] b) {
		// sendQueueUDP.add(b);
	}
	
	public byte[] readTCP() {
		ibuf.flip();
		byte[] b = new byte[ibuf.limit()];
		ibuf.get(b);
		ibuf.clear();
		return b;
	}

	public void shutdown() throws IOException {
		socketTCP.close();
	}

}
