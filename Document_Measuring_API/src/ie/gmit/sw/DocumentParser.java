package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	
	private BlockingQueue<Shingle> queue;
	private String file;
	private int shingleSize;
	private int k;
	private int docId;
	private Deque<String> buffer = new LinkedList<>();
	
	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k, int dID) {
		this.queue = q;
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
		this.docId = dID;
	}
	
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = null;
		
		try {
			while((line = br.readLine()) != null) {
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" ");
				addWordsToBuffer(words);
				Shingle s  = getNextShingle();
				queue.put(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			flushBuffer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void addWordsToBuffer(String[] words) {
		for(String s : words) {
			buffer.add(s);
		}
	}
	private Shingle getNextShingle() {
		
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}
		if(sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}else {
			return(null);
		}
	}// getNextShingle
	
	private void flushBuffer() throws InterruptedException {
		
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if( s != null) {
				queue.put(s);
			}
			else {
				queue.put(new Poison(docId, 0));
			}
		}
	}
}
