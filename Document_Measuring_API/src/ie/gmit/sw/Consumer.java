package ie.gmit.sw;


import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Consumer implements Runnable {
	
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new HashMap<>();
	private ExecutorService pool;
	private Shingle s;
	
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
						
						List<Integer> list = map.get(s.getDocID());
						
						for(int i = 0; i < minhashes.length; i++) {
							
							int value = s.getShingleHashCode() ^ minhashes[i];
							
							if(list == null) {
								
								list = new ArrayList<Integer>(Collections.nCopies(k, Integer.MAX_VALUE));
								map.put(s.getDocID(), list);
							
							}else {
								if(list.get(i) > value) {
									list.set(i, value);
								}
							}
						}
						map.put(s.getDocID(), list);
					}
				});
			}
		}
	}
}
