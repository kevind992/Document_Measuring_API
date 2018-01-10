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
import ie.gmit.sw.Poison;

/**
 * This class compares the <code>minHashes</code> which were created in the
 * Document Parser. The Results are stored in a Concurrent Hash Map. Being
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
	 * @param q
	 *            is the Blocking Queue
	 * @param k
	 *            is the number of minHashes
	 * @param poolSize
	 *            is the number of Executor worker threads.
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
	public void init() {
		Random random = new Random();
		minhashes = new int[noOfHashes];
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}

	/**
	 * run starts the worker threads. Compares the <code>minhashes</code>
	 */
	public void run() {
		int docCount = 2;
		while (docCount > 0) {
			try {
				Shingle s = queue.take();
				if (s instanceof Poison) {
					docCount--;
				} else {
					pool.execute(new Runnable() {
						@Override
						public void run() {

							List<Integer> list = map.get(s.getDocID());
							for (int i = 0; i < minhashes.length; i++) {
								int value = s.getHashCode() ^ minhashes[i];
								list = map.get(s.getDocID());
								if (list == null) {
									list = new ArrayList<Integer>(Collections.nCopies(noOfHashes, Integer.MAX_VALUE));
									map.put(s.getDocID(), list);

								} else {
									if (list.get(i) > value) {
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
