//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public void Launch(String f1, String f2) throws InterruptedException {
		
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<>();
		
		//Setting Shingle Size and k which is the number of minHashes
		int shingleSize = 3;
		int k = 50;
		int poolSize = 20;
				
		Thread t1 = new Thread(new DocumentParser(f1, q, shingleSize, k),"T1");
		Thread t2 = new Thread(new DocumentParser(f1, q, shingleSize, k),"T2");
		Thread t3 = new Thread(new Consumer(q,k,poolSize),"T3");
		
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		
		//List<Integer> intersection = new ArrayList(a);
		//intersection.retainAll(b);
		
		//float jaccard = ((float)intersection.size()) /((k * 2) + ((float) intersection.size()));
		
	}	
}
