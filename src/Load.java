package coursework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Load {
	
	// Variables
	private ArrayList<Digit> digits = new ArrayList<Digit>();
	private Double[] bitmap;
		
	// Constructor
	public Load() {
		
	}
	
	// Uses the string passed as argument to read a file and extract the digits from it.
	public void loadFile(String fileName) {
		
		// Variable to know what size of bitmaps we are handling
		Integer bitmapSize = 0;
		
		try{			
			
			// Retrieving the file (feel free to override the location)
			File digitsFile = new File("D:\\OneDrive\\MDX\\CST 3170 - Artificial Intelligence\\Coursework 2\\Original data files\\" + fileName);
			
			// Creating a scanner to read the file and adding delimiter
			Scanner scanner = new Scanner(digitsFile);
			scanner.useDelimiter("[ ,\r\n]");
			
			// Storing the first line as a string
			String firstLine = scanner.nextLine();
			
			// Reading only the first line
			scanner = new Scanner(firstLine);
			scanner.useDelimiter("[ ,\r\n]");
			
			// Counting the number of input per line
			while (scanner.hasNext()) {
				scanner.next();
				bitmapSize++;
			}
			
			// Removing one from the size of bitmap (last value is the attribute)
			bitmapSize--;
			
			
			//Resetting the scanner to the beginning of the file
			scanner = new Scanner(digitsFile);
			scanner.useDelimiter("[ ,\r\n]");
			
			// Retrieving every digit
			while(scanner.hasNextLine()) {
				// Creating an array based on the size of the first input
				bitmap = new Double[bitmapSize];
				// Storing the values for the bitmap
				for(int i = 0; i < bitmapSize; i++) {
					bitmap[i] = scanner.nextDouble();
				}
				
				// Storing the attribute
				Integer attribute = scanner.nextInt();
				
				// Placing the scanner to the next line
				scanner.nextLine();
				
				// Creating a new digit to store
				Digit newDigit = new Digit(bitmap, attribute);
				
				//Adding the new digit to the arrayList.
				digits.add(newDigit);
				
			}
			
		} catch(FileNotFoundException e) {
			
			// prints error if no matching file was found
			System.out.println("***Error encountered loading the file:***");
			e.printStackTrace();
			
		} catch(Exception e) {
			
			// prints any other error encountered
			System.out.println("***Error encountered loading the file:***");
			e.printStackTrace();
			
		}
		
	}
	
	// Returns an ArrayList of digits
	public ArrayList<Digit> getAllDigits(){
		return digits;
	}
	
	// Removes all digits from the Load object
	public void flushDigits() {
		digits.clear();
	}
	
	// Print all digits in the console (impractical due to large number of digits in the file, mostly useful for debugging)
	public void printAllDigits(boolean withGuess) {
		System.out.println("Number of digits loaded: " + digits.size());
		for(int i = 0; i < digits.size(); i++) {
			System.out.println("Digit #" + (i + 1) + ": Bitmap = " + Arrays.toString(digits.get(i).getBitmap()) + ".");
			System.out.println("Attribute: " + digits.get(i).getRealAttribute());
			if(withGuess) {
				System.out.println("Guess: " + digits.get(i).getGuessedAttribute());
			}
		}
	}
}