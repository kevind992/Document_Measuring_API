//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

public class Shingle {
	
	private int docID;
	private int shingleHashCode;
	
	public Shingle(int id, int hashCode) {
		
		this.docID = id;
		this.shingleHashCode = hashCode;
		
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public int getShingleHashCode() {
		return shingleHashCode;
	}

	public void setShingleHashCode(int shingleHashCode) {
		this.shingleHashCode = shingleHashCode;
	}
	
	
	
}
