//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.Poison;

/**
 * 
 * In this class all the Shingles will be XOR-ed with <code>noOfHashes</code> which were created in the Document Parser.
 * The Results are stored in a Concurrent Hash Map. Being
 * Concurrent allows multiple threads to access the map at more then one point.
 * The Map doesn't lock.
 * 
 * @author Kevin Delassus
 * @author G00270791
 *
 */
public class Consumer implements Runnable {

	private BlockingQueue<Shingle> queue;
	private int noOfHashes;
	private int[] minhashes;
	private ConcurrentMap<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;

	/**
	 * Consumer Constructor
	 * 
	 * @param q is the Blocking Queue     
	 * @param k is the number of minHashes
	 * @param poolSize is the number of Executor worker threads.
	 */
	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.noOfHashes = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	/**
	 * <code>init</code> creates random numbers and stores them into <code>minhashes</code>. The amount of
	 * random numbers created depends on the <code>noOfHashes</code>.
	 */
	private void init() {
		//Creating a Random object.
		Random random = new Random();
		minhashes = new int[noOfHashes];
		for (int i = 0; i < minhashes.length; i++) {
			//Creates a new random number and assignes it to minhashes.
			minhashes[i] = random.nextInt();
		}
	}

	/**
	 * run starts the worker threads. Compares the <code>minhashes</code>
	 */
	//Starts Thread
	public void run() {
		int docCount = 2;
		//While docCount is greater then 0
		while (docCount > 0) {
			try {
				//Creates a new Shingle and takes from queue
				Shingle s = queue.take();
				//if s is an instance of Poison then decrement docCount
				if (s instanceof Poison) {
					docCount--;
				} else {
					//Creating the executer pool (Worker Threads)
					pool.execute(new Runnable() {
						@Override
						//Starting the worker threads
						public void run() {
							//Creating a list of integer and assigning the shingle id
							List<Integer> list = map.get(s.getDocID());
							//loop runs for whatever the length of minhashes is
							for (int i = 0; i < minhashes.length; i++) {
								int value = s.getHashCode() ^ minhashes[i];
								list = map.get(s.getDocID());
								//if list is equaled to null
								if (list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(noOfHashes, Integer.MAX_VALUE));
									//Assigning s id and list to the map
									map.put(s.getDocID(), list);

								} else {
									if (list.get(i) > value) {
										//Setting list to the value of i and value
										list.set(i, value);
									}
								}
							}
							map.put(s.getDocID(), list);
						}
					});
					
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Shutting down executer pool
		pool.shutdown();
		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Getter Methods
	/**
	 * This method returns <code>map</code>
	 * 
	 * @return this returns <code>map</code>
	 */
	public ConcurrentMap<Integer, List<Integer>> getMap() {
		return map;
	}

	/**
	 * This method returns <code>noOfHashes</code>.
	 * 
	 * @return this returns <code>noOfHashes</code>
	 */
	public int getNoOfHashes() {
		return noOfHashes;
	}
}
