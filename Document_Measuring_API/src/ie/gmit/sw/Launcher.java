package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public void Launch(String f1, String f2) {
		
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<>(blockingQueueSize)
		
		Thread t1 = Thread(new DocumentParser(f1, q, shingleSize, k),"T1");
		Thread t2 = Thread(new DocumentParser(f1, q, shingleSize, k),"T2");
		
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		
	}
	
	
}
