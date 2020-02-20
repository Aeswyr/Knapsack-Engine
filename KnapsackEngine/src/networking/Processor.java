package networking;

import java.util.Queue;

public interface Processor {

	public void Process(Queue<byte[]> queue, byte[] data);	
}
