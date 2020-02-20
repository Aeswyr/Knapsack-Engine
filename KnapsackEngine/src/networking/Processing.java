package networking;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import networking.Client;

public class Processing {

	static Queue<byte[]> commandBuffer;

	static Client c;
	static Processor p;
	
	public static void InitClient(Processor commandProcessor) {
		commandBuffer = new LinkedBlockingQueue<byte[]>();
		p = commandProcessor;
		try {
			c = new Client();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	byte[] b;
	int read = 0;
	int log = 0;

	public static void update() {
		byte[] temp = c.readTCP();
		p.Process(commandBuffer, temp);
	}

}
