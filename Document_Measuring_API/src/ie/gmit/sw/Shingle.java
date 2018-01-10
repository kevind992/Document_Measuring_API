package ie.gmit.sw;

/**
 * 
 * This Class implements a Shingle. A Shingle is a couple of words grouped together.
 * 
 * @author Kevin Delassus
 * @author G00270791
 * 
 */
public class Shingle {
	private int docID;
	private int hashCode;
	
	public Shingle() {
		super();
	}

	/**
	 * 
	 * Constructs a Shingle with a <code>docID</code> and a <code>hashCode</code>.
	 * 
	 * @param id is the identification of the Shingle.
	 * @param hashCode contains the shingled words hashed.
	 * 
	 */
	public Shingle(int id, int hashCode) {
		super();
		this.docID = id;
		this.hashCode = hashCode;
	}

	/**
	 * 
	 * This method Returns <code>docID</code>.
	 * @return this returns <code>doID</code>.
	 * 
	 */
	public int getDocID() {
		return docID;
	}

	/**
	 * This method Sets <code>docID</code>.
	 * @param id sets <code>docID</code> 
	 */
	public void setDocId(int id) {
		this.docID = id;
	}

	/**
	 * This method gets <code>hashCode</code>.
	 * @return this returns <code>hashCode</code>.
	 */
	public int getHashCode() {
		return hashCode;
	}

	/**
	 * This method sets <code>hashCode</code>.
	 * @param hashCode sets <code>hashCode</code>.
	 */
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}
