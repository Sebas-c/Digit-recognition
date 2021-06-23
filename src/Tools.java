package coursework2;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Tools {
	
	// Class to inherit. Used to store the test set and calculate the accuracy of results
	// Also used to calculate euclidian distance
	
	//
	// Variables
	//
	
	protected ArrayList<Digit> testSet;
	protected String algorithmName = "";
	
	//
	// Getters and setters
	//
	
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	//
	// Methods
	//
	
	// Returns the Euclidian distance between two digits
	public Double euclidianDistance(Digit firstDigit, Digit secondDigit) {
		// Variable to store the distance
		Double distance = 0.0;
				
		// Check that the training and testing data have the same number of values for the bitmap
		// Should not be an issue, but best practice in case we important additional data
		if (firstDigit.getBitmap().length == secondDigit.getBitmap().length) {
			
			// Subtracts each dimension from the second digit to the second one, squares the results and adds to total
			for(int i = 0; i < firstDigit.getBitmap().length; i++) {
				distance += Math.pow(firstDigit.getBitmap()[i] - secondDigit.getBitmap()[i], 2);
			}
			
			// Gets the square root of the distance
			distance = Math.sqrt(distance);
			
		}else {
			return -1.0;
		}
		
		
		return distance;
	}
	
	
	//
	// Results analysis
	//
	
	// Prints and returns the accuracy of the algorithm
	public Double printAccuracy(boolean print) {
		
		// Variables
		Double correctGuesses = 0.0;
		Double accuracyPercentage = 0.0;
		
		// Loop through all digits in a set and increments correct guesses counter
		for (Digit digit : testSet) {
			if (digit.getGuessedAttribute() == digit.getRealAttribute()) {
				correctGuesses++;
			}
		}
		
		// Accuracy percentage = number of correct guesses * 100 / size of test set
		accuracyPercentage = correctGuesses * 100 / testSet.size();
		
		// Print results
		if(print) {
			System.out.println("Accuracy for " + algorithmName + ": " + accuracyPercentage + "%");
			System.out.println("Total guesses: " + testSet.size());
			System.out.println("Total correct guesses: " + correctGuesses);
		}
		return accuracyPercentage;
	}
	
	// Prints and returns the confidence of the algorithm
	// Returns Double[] with 3 values: average confidence, average confidence (correct guesses) & average confidence (wrong guesses).
	public Double[] printConfidence(boolean print) {
		
		// Variables
		Double correctGuesses = 0.0;
		Double averageConfidence = 0.0;
		Double averageCorrectConfidence = 0.0;
		Double averageWrongConfidence = 0.0;
		
		// Loop through all digits in a set and increments correct guesses counter
		for (Digit digit : testSet) {
			averageConfidence += digit.getConfidence();
			if (digit.getGuessedAttribute() == digit.getRealAttribute()) {
				averageCorrectConfidence += digit.getConfidence();
				correctGuesses++;
			}else {
				averageWrongConfidence += digit.getConfidence();
			}
		}
		
		averageConfidence = averageConfidence * 100 / testSet.size();
		averageCorrectConfidence = averageCorrectConfidence * 100 / correctGuesses;
		averageWrongConfidence = averageWrongConfidence * 100 / (testSet.size() - correctGuesses);
		
		Double[] confidenceValues = {averageConfidence, averageCorrectConfidence, averageWrongConfidence};
		
		if(print) {
			System.out.println("Average confidence: " + averageConfidence + "%");
			System.out.println("Average confidence (correct guesses): " + averageCorrectConfidence + "%");
			System.out.println("Average confidence (wrong guesses): " + averageWrongConfidence + "%");
		}
		
		return confidenceValues;
	}
	
	// Returns the confusion matrix of a testSet
	// Returns a Double[10][10] with real attributes in the first index and guesses % in the second
	public Double[][] getConfusionMatrix(){
		
		// Variables
		Double[][] confusionMatrix = new Double[10][10];
		Integer[] attributeCounter = new Integer[10];
		
		// Initialising all values in the arrays to 0.0
		for(int i = 0; i < 10; i++) {
			attributeCounter[i] = 0;
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = 0.0;
			}
		}
		
		// Counting each attribute found and each guessed attribute
		for(Digit digit : testSet) {
			attributeCounter[digit.getRealAttribute()]++;
			confusionMatrix[digit.getRealAttribute()][digit.getGuessedAttribute()]++;
		}
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = confusionMatrix[i][j] / attributeCounter[i] * 100;
			}
		}
				
		return confusionMatrix;
	}

	// Prints a given confusion Matrix
	public void printConfusionMatrix(Double[][] matrix) {
		System.out.println("                                     Real Attributes");
		System.out.print("Guesses|");
		for(int i = 0; i < 10; i++) {
			System.out.print("|   " + i + "   ");
		}
		System.out.println("||");
		System.out.println("-------===================================================================================");
		for(int i = 0; i < 10; i++) {
			if(i > 0) {
				System.out.println("------------------------------------------------------------------------------------------");
			}
			System.out.print("   " + i + "   |");
			for(int j = 0; j < 10; j++) {
				System.out.print("| " +  new DecimalFormat("00.00").format(matrix[i][j]) + " ");
			}
			System.out.println("||");
		}
		System.out.println("-------===================================================================================");
	}
}
