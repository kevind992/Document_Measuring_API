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

/**
 * This class is used for reading a textfile, breaking words into specified groups and storing them in a blocking queue.
 * @author Kevin Delassus
 * @author G00270791
 *
 */

public class DocumentParser implements Runnable , FileRead {

	//Member Variables
	private String file;
	private int shingleSize;
	private BlockingQueue<Shingle> queue;
	private Deque<String> buffer = new LinkedList<>();
	private int docID;

	/**
	 * Constructs a DocumentParser Object with the parameters below.
	 * 
	 * @param file is one of the files which will be parsed
	 * @param shingleSize is the amount of words which will be grouped together.
	 * @param k is the number of minhashes.
	 * @param q is the BlockingQueue
	 * @param id is the either file 1 or file 2 depending on which thread is running.
	 */
	//Constructor
	public DocumentParser(String file,int shingleSize, BlockingQueue<Shingle> q, int id) {
		super();
		this.file = file;
		this.shingleSize = shingleSize;
		this.queue = q;
		this.docID = id;
	}
	/**
	 *
	 * Used to read from a text file. The lines are changed to Upper-case and stored in the <code>words</code> array.
	 * Words are added to the Buffer and Shingles are then created .Once it has reached the bottom of the file it stops reading. 
	 * 
	 */
	public void readFile() {
		try {
			//Creating the buffered Reader, InputStreamReader and FileInputStream
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					//Changing the line to uppercase.
					String uLine = line.toUpperCase();
					//Splits and skips spaces.
					String[] words = uLine.split("\\s+");
					//Add the words to the buffer.
					addWordsToBuffer(words);
					//Creating the shingle
					Shingle s = getNextShingle();
					//Adding the shingle to the queue.
					queue.put(s);
				}
			}
			//Flushing the buffer
			flushBuffer();
			//Closing the BufferedReader
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Not Found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Run starts the thread.
	 * Once the thread starts the readFile method is run.
	 * 
	 */
	public void run() {
		readFile();
	}
	
	/**
	 * 
	 * Adds the words from array <code>words</code> to the buffer.
	 * @param words
	 * 
	 */
	private void addWordsToBuffer(String[] words) {
		//Adding all the words from words[] to the buffer.
		for (String s : words) {
			buffer.add(s);
		}
	}
	/**
	 * getNextShingle groups words together to whatever is the specified size. It is presently set at 3.
	 * Once the words have been shingled they are then parsed into hashcode and stored in a list. 
	 * @return Shingle
	 */
	private Shingle getNextShingle() {
		//Creating a StringBuilder.
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		
		//Keep going while counter is less the shingleSize
		while (counter < shingleSize) {
			
			if (buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			} else {
				counter = shingleSize;
			}
		}
		if (sb.length() > 0) {
			//Adding the new hashed Shingle to the map
			return (new Shingle(docID, sb.toString().hashCode()));
		} else {
			return null;
		}

	}
	/**
	 * flushBuffer calls an instanace of Poision if <code>s</code> is equaled to <code>null</code>.
	 * @throws InterruptedException if there is an error reading from the TextFile.
	 */
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
