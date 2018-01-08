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
	public Menu(String f1, String f2, Scanner s) {
		
		this.file1 = f1;
		this.file2 = f2;
		this.scanner = s;
		
		
	}
	public void DisplayMenu() {
		
		System.out.println("==========================================================");
		System.out.println("Please Enter the name of 2 files you wish to compare:");
		System.out.println("==========================================================");
		System.out.print("File 1 => ");
		file1 = scanner.nextLine();
		System.out.print("File 2 => ");
		file2 = scanner.nextLine();
		
		System.out.println("This is file 1: " + file1 + "\nThis is file 2: " + file2);
	}
	
	
}
