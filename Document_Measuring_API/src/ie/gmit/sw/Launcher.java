//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Launcher contains launch which is used to start three threads and close them when they are finished processing.
 * 
 * @author Kevin Delassus
 * @author G00270791
 *
 */

public class Launcher {
	
	private Consumer c;
	private float jaccard;
	
	/**
	 * Launch creates 3 threads. The first two run the <code>DocumentParser</code> and the third runs the <code>Consumer</code> class.
	 * 
	 * @param f1 is the first file entered by the user.
	 * @param f2 is the second file entered by the user.
	 * 
	 * @throws InterruptedException when there is a problem reading from a TextFile.
	 */
	
	public void Launch(String f1, String f2) throws InterruptedException {
		
		//Setting Shingle Size and k which is the number of minHashes
		int shingleSize = 3;
		int noOfHashes = 300;
		int poolSize = 200;
			
		//Setting Blocking Queue
		BlockingQueue<Shingle> q = new LinkedBlockingQueue<Shingle>(100);	
		
		//Setting Up Threads
		Thread t1 = new Thread(new DocumentParser(f1,noOfHashes,shingleSize, q, 1),"T1");
		Thread t2 = new Thread(new DocumentParser(f2,noOfHashes,shingleSize, q, 2),"T2");
		Thread t3 = new Thread(c = new Consumer(q, noOfHashes, poolSize),"T3");
		
		//Starting Threads
		t1.start();
		t2.start();
		t3.start();
		
		System.out.println("\n\nProcessing.......Please Wait.......\n\n");
		
		//Ending Threads
		t1.join();
		t2.join();
		t3.join();
		
		//Calculating and Displaying Result
		calculateJaccard();
		displayJaccard();
			
	}	
	/**
	 * calculateJaccard calculates the jaccard value variables are taken from the Consumer class. 
	 * 
	 * @return A float value with the calculated jaccard
	 */
	public void calculateJaccard() {
		
		List<Integer> intersection = c.getMap().get(1);
		intersection.retainAll(c.getMap().get(2));
		this.jaccard = (float) intersection.size() / (c.getNoOfHashes() * 2 - (float) intersection.size());
		
	}
	/**
	 * displayJaccard displays the jaccard value which was calculated in calculateJaccard
	 */
	public void displayJaccard() {
		System.out.println("Jaccard: " + (this.jaccard) * 100);
	}
}
