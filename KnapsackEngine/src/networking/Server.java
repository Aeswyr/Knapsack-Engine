package networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Server implements Runnable {

	Selector sel;
	ServerSocketChannel socketTCP;
	DatagramChannel socketUDP;
	InetSocketAddress address;
	ByteBuffer buf;

	public Server() throws IOException {
		address = new InetSocketAddress(9999);

		socketTCP = ServerSocketChannel.open();
		socketTCP.bind(address);
		socketTCP.configureBlocking(false);

//		socketUDP = DatagramChannel.open();
//		socketUDP.bind(address);
//		socketUDP.configureBlocking(false);

		sel = Selector.open();
		buf = ByteBuffer.allocate(1024);

		socketTCP.register(sel, SelectionKey.OP_ACCEPT);

	}

	boolean running = true;

	@Override
	public void run() {
		try {
			Iterator<SelectionKey> iter;
			SelectionKey key;
			SocketChannel chnl = null;

			while (running) {

				sel.select(0);

				iter = sel.selectedKeys().iterator();
				// System.out.println("ready");

				while (iter.hasNext()) {

					key = iter.next();
					iter.remove();

					if (key.isAcceptable()) {
						SocketChannel socket = socketTCP.accept();
						if (socket != null) {
							socket.configureBlocking(false);
							ByteBuffer buf = ByteBuffer.allocate(4096);
							socket.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE, buf);
							socket.socket().setKeepAlive(true);

							System.out.println(
									"New client attempting to connect at " + socket.getRemoteAddress().toString());
						}
						if (socket.finishConnect()) {
							byte[] b = new byte[8];
							new Random().nextBytes(b);
							System.out.println(
									"Client connected, sending establishing byte package: " + Arrays.toString(b));
							ByteBuffer buf0 = ByteBuffer.wrap(b);
							socket.write(buf0);
						} else
							System.out.println("Connection failed at " + socket.getRemoteAddress().toString());
					}

					if (key.isReadable()) {
						chnl = (SocketChannel) key.channel();
						int t = 0;
						try {
						t = chnl.read(buf);
						} catch (SocketException e) {
							t = -1;
						}
						if (t > 0) {
							buf.flip();
							for (SelectionKey key0 : sel.keys()) {
								if (!key0.equals(key)) {
									if (key0.isWritable()) {
										ByteBuffer buf0 = (ByteBuffer) key0.attachment();
										buf0.put(buf);
										buf.rewind();

										buf0.flip();
										((SocketChannel) key0.channel()).write(buf0);
										if (!buf0.hasRemaining()) {
											buf0.clear();
										} else
											buf0.flip();
									}
								}
							}
							buf.clear();
						} else if (t == -1) {
							System.out.println("Client disconnected, cleaning up connections");
							chnl.close();
							key.cancel();
						}
					}
					// end of key loop
				}

			}

		} catch (IOException e) {
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

}
