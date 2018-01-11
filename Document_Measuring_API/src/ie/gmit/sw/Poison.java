//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

/**
 * 
 * Poison places a known data item on the queue and when the consumer reads this item  it closes down.
 * Poison extends Shingle
 * 
 * @author Kevin Delassus 
 * @author G00270791
 * @extends Shingle
 */
public class Poison extends Shingle{
	
	public Poison(int docId, int hashCode) {
		super(docId, hashCode);
	}
}
