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

	private String file;
	private int shingleSize;
	private int noOfHashes;
	private BlockingQueue<Shingle> queue;
	private Deque<String> buffer = new LinkedList<>();
	private int docID;

	public DocumentParser(String file, int shingleSize, int k, BlockingQueue<Shingle> q, int id) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.noOfHashes = k;
		this.queue = q;
		this.docID = id;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String uLine = line.toUpperCase();
					String[] words = uLine.split("\\s+");
					addWordsToBuffer(words);
					Shingle s = getNextShingle();
					queue.put(s);
				}
			}

			br.close();
			flushBuffer();

		} catch (FileNotFoundException e) {
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
		for (String s : words) {
			buffer.add(s);
		}
	}

	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while (counter < shingleSize) {
			if (buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			} else {
				counter = shingleSize;
			}

		}
		if (sb.length() > 0) {
			return (new Shingle(docID, sb.toString().hashCode()));
		} else {
			return null;
		}

	}

	private void flushBuffer() throws InterruptedException {

		while (buffer.size() > 0) {
			Shingle s = getNextShingle();
			if (s != null) {
				queue.put(s);
			}
		}
		queue.put(new Poison(docID, 0));
	}

}
