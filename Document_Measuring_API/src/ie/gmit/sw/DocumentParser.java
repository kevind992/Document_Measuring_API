package ie.gmit.sw;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	
	private BlockingQueue<Shingle>queue;
	private String file;
	private int shingleSize, k;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;
	
	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k, int dID) {
		this.queue = q;
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
		this.docId = dID
	}
	
	
}
