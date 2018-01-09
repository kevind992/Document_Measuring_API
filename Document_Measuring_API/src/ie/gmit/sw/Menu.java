//Author: Kevin Delassus - G00270791
package ie.gmit.sw;

import java.util.Scanner;

public class Menu {
	
	private String file1;
	private String file2;
	private Scanner scanner;
	
	public Menu() {
		super();
		scanner = new Scanner(System.in);
	}
	public Menu(String f1, String f2) {
		
		this.file1 = f1;
		this.file2 = f2;
		
	}
	public void DisplayMenu() throws InterruptedException {
		
		System.out.println("==========================================================");
		System.out.println("Please Enter the name of 2 files you wish to compare:");
		System.out.println("==========================================================");
		System.out.print("File 1 => ");
		file1 = scanner.nextLine();
		System.out.print("File 2 => ");
		file2 = scanner.nextLine();
		
		new Launcher().Launch(file1, file2);
		
		scanner.close();
	}
	
	
}
