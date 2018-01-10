//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {
	
	public void Launch(String f1, String f2) throws InterruptedException {
		
		//Setting Shingle Size and k which is the number of minHashes
		int shingleSize = 3;
		int noOfHashes = 500;
		int poolSize = 200;
			
		//Setting Blocking Queue
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);	
		
		//Setting Up Threads
		Thread t1 = new Thread(new DocumentParser(f1,noOfHashes,shingleSize, q, 1),"T1");
		Thread t2 = new Thread(new DocumentParser(f2,noOfHashes,shingleSize, q, 2),"T2");
		Thread t3 = new Thread(new Consumer(q, noOfHashes, poolSize),"T3");
		
		//Starting Threads
		t1.start();
		t2.start();
		t3.start();
		
		System.out.println("\n\n*****Processing*****\n\n");
		
		//Ending Threads
		t1.join();
		t2.join();
		t3.join();
			
	}	
}
