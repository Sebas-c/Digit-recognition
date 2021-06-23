package coursework2;

import java.util.ArrayList;

public class KMeansSupervised extends Tools {
	
	// Class implementing k-Means supervised
	
	//
	// Variables
	//
	
	private ArrayList<Digit> trainingSet;
	private Double[][] means;
	private Integer[] counters = new Integer[10];
	
	Load loader;
	
	//
	// Constructor
	//
	
	public KMeansSupervised(Load loader) {
		this.loader = loader;
		setAlgorithmName("K-Means Supervised");
	}
	
	
	//
	// Getters and Setters
	//
	
	// Loads a training file
	public void loadTrainingSet(String fileName) {
		
		loader.flushDigits();
		loader.loadFile(fileName);
		this.trainingSet = (ArrayList<Digit>)loader.getAllDigits().clone();
		System.out.println("Training set size: " + trainingSet.size() + " digits.");
		
	}
	
	// Sets a training set
	public void setTrainingSet(ArrayList<Digit> trainingSet) {
		this.trainingSet = trainingSet;
	}
		
	
	// Loads a test file
	public void loadTestSet(String fileName) {
		
		loader.flushDigits();
		loader.loadFile(fileName);
		this.testSet = (ArrayList<Digit>)loader.getAllDigits().clone();
		System.out.println("Test set size: " + testSet.size() + " digits.");
		
	}
	
	// Sets a test set
	public void setTestSet(ArrayList<Digit> testSet) {
		this.testSet = testSet;
	}
		
	//
	// Methods
	//
	
	// Finds the k-Means for each attribute
	public void meanFinder() {
		Integer attribute;
		
		// Initiating the array holding the means
		// Array has a size of 10 (number of digits whose mean we need to find) by the size of the length of the bitmap
		means = new Double[10][trainingSet.get(0).getBitmap().length];
		
		// Setting all values in the counters array and means to 0 and 0.0 respectively
		for (int i = 0; i < counters.length; i++) {
			counters[i] = 0;
			for (int j = 0; j < trainingSet.get(0).getBitmap().length; j++) {
				means[i][j] = 0.0;
			}
		}
		
		// Looping through the training set
		for(Digit digit : trainingSet) {
			
			// Retrieving the attribute and increasing the matching counter
			attribute = digit.getRealAttribute();
			counters[attribute]++;
			
			// Storing the bitmap from the digit
			Double[] newBitmap = digit.getBitmap();
			
			// Looping through each digit in the bitmap and adding it to the array
			for(int i = 0; i < digit.getBitmap().length; i++) {
				means[attribute][i] += newBitmap[i];
			}
			
			
		}
		
		for (int i = 0; i < counters.length; i++) {
			
			for (int j = 0; j < means[i].length; j++) {
				
				means[i][j] = means[i][j] / counters[i];
				
			}
			
		}
		
	}
	
	// Finds the closest K-Mean for each digit in the test set
	public void kMS() {
		Double distance;
		Integer closestAttribute = -1;
		
		for(Digit digit : testSet) {
			distance = Double.MAX_VALUE;
			
			for (int i = 0; i < 10; i++) {
				
				Digit distanceDigit = new Digit(means[i], -1);
				
				if (distance > euclidianDistance(digit, distanceDigit)) {
					distance = euclidianDistance(digit, distanceDigit);
					closestAttribute = i;
					
				}
				
			}
			
			digit.setGuessedAttribute(closestAttribute);
			
		}
	}
	
}
