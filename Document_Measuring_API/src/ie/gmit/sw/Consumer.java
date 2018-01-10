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

public class Consumer implements Runnable {

	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes;
	private ConcurrentMap<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;

	public ConcurrentMap<Integer, List<Integer>> getMap() {
		return map;
	}

	public Consumer(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	public void init() {
		Random random = new Random();
		minhashes = new int[k];
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}

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
									list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
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

		List<Integer> intersection = map.get(1);
		intersection.retainAll(map.get(2));
		float jaccard = (float) intersection.size() / (k * 2 - (float) intersection.size());

		System.out.println("Jaccard: " + (jaccard) * 100);
	}
}
