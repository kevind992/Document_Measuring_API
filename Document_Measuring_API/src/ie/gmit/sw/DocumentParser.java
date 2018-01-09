//Author: Kevin Delassus - G00270791
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
	
	public DocumentParser(String file, BlockingQueue<Shingle>q, int ss, int k) {
		this.queue = q;
		this.file = file;
		this.shingleSize = ss;
		this.k = k;
	}
	
	public void run() {
		try {
		
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
			String line = null;
		
			while((line = br.readLine()) != null) {
				if(line.length()>0) {
					String uLine = line.toUpperCase();
					String[] words = uLine.split(" ");
					addWordsToBuffer(words);
					Shingle s  = getNextShingle();
					queue.put(s);
				}
			}
		
			br.close();
			flushBuffer();
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
