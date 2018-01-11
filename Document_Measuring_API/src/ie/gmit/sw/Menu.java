package ie.gmit.sw;

import java.util.Scanner;

/**
 * This Class contains a Menu method where the user is asked to enter 2 text files.
 * @author Kevin Delassus
 * @author G00279791
 *
 */

public class Menu {
	
	//Member Variables
	private String file1;
	private String file2;
	private Scanner scanner  = new Scanner(System.in);
	
	public Menu() {
		super();
	}
	/**
	 * 
	 * Creates two files which will contain TextFiles.
	 * 
	 * @param f1 is for File 1.
	 * @param f2 is for File 2.
	 * 
	 */
	//Constructor
	public Menu(String f1, String f2) {
		
		this.file1 = f1;
		this.file2 = f2;
		
	}
	/**
	 * DisplayMenu displays the menu to the user, once the two file inputs are taken Launch is run.
	 * 
	 * @return Displays the menu to the user and runs Launch
	 * 
	 * @throws InterruptedException when there is an error loading TextFiles
	 */
	public void DisplayMenu() throws InterruptedException {
		
		//Header
		System.out.println("==========================================================");
		System.out.println("Please Enter the name of 2 files you wish to compare: ");
		System.out.println("==========================================================");
		
		//Asking User for Textfiles
		System.out.print("File 1 => ");
		file1 = scanner.next();
		System.out.print("File 2 => ");
		file2 = scanner.next();
		
		//Creating Launcher and sending file1 and file2
		new Launcher().Launch(file1, file2);
		
		//Closing scanner.
		scanner.close();
	}
}
