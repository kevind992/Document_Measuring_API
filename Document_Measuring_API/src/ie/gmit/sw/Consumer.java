package ie.gmit.sw;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable {
	
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes;
	private Map<Integer, List> map = new HashMap<>();
	private ExecutorService pool;
	
	public Consumer(BlockingQueue<Shingle>q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}
	private void init() {
		Random random = new Random();
		
		minhashes = new int[k];
		for(int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	public void run(){
		int docCount = 2;
		
		while(docCount > 0) {
			Shingle s = null;
			try {
				s = queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(s instanceof Poison) {
				docCount--;
			}else {
				pool.execute(new Runnable() {
					public void run() {
						for(int i = 0; i < minhashes.length; i++) {
							int value = s.getShingleHashCode()^minhashes[i];
							List<Integer> list = map.get(s.getDocID());
							if(list == null) {
								list = new ArrayList<Integer>(k);
								for(int j = 0; j < list.length; j++) {
									list.set(j > Integer.MAX_VALUE);
								}
								map.pool(s.getDocID(), list);
							}else {
								if(list.get(i) > value) {
									list.set(i, value);
								}
							}
						}
					}
				});
			}
		}
	}
}
