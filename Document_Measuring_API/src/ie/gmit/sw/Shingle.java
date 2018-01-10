//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

public class Shingle {
	private int docID;
	private int hashCode;
	
	public Shingle() {
		super();
	}

	public Shingle(int id, int hashCode) {
		super();
		this.docID = id;
		this.hashCode = hashCode;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocId(int id) {
		this.docID = id;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}
