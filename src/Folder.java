package coursework2;

import java.util.ArrayList;
import java.util.Collections;

public class Folder {
	
	// Class used to run the algorithms with specific cross-validation values
	
	//
	// Variables
	//
	
	private ArrayList<Digit> dataSet;
	private Load loader;
	private KNN knn;
	private KMeansSupervised kMeans;
	
	//
	// Constructor
	//
	
	public Folder(Load loader, KNN knn, KMeansSupervised kMeans) {
		
		this.loader = loader;
		this.knn = knn;
		this.kMeans = kMeans;
		this.dataSet = new ArrayList<Digit>();
		
	}

	//
	// Getters and Setters
	//
	
	// Sets a training file
	public void addToDataSet(String fileName) {
		
		// Variables
		ArrayList<Digit> tempList;
		loader.flushDigits();
		loader.loadFile(fileName);
		tempList = (ArrayList<Digit>)loader.getAllDigits().clone();
		for(Digit digit : tempList) {
			dataSet.add(digit);
		}
	}
	

	//
	// Methods
	//
	
	// Randomising dataSet order
	public void randomizeSet() {
		 Collections.shuffle(dataSet);
	}
	
	// Resetting all guessed attributes in the data set to -1
	// Note: this is superfluous as the attributes are supposed to be overwritten
	public void flushAttributes() {
		for(Digit digit : dataSet) {
			digit.setGuessedAttribute(-1);
		}
	}
	

	// Runs k-nn with a specified k, cross-validating the data set N times
	public Double knn(Integer k, Integer n, boolean printProgress, boolean printConfusionMatrix) {
		
		// Checking validity of fold
		if (n < 2) {
			System.out.println("Cannot run: Fold parameter must be 2 or more");
			return 0.0;
		} else if(dataSet.size() / n < k) {
			System.out.println("Cannot run: Fold parameter too large for this value of k.");
			Integer foldSize = dataSet.size() / n;
			System.out.println("Fold size: " + foldSize + ", k = " + k + ".");
			System.out.println("Trying lower k or n.");
			return 0.0;
		}
		
		// Variables
		Double accuracy = 0.0;
		Double[] confidenceValues = {0.0, 0.0, 0.0};
		Double[][] confusionMatrix = new Double[10][10];
		Double[][] tempConfusionMatrix = new Double[10][10];
		Integer indexFrom = 0;
		Integer indexTo;
		Integer initialIndexTo;
		ArrayList<Digit> testSet;
		ArrayList<Digit> trainingSet;
		
		// Set initial index boundaries
		indexTo = dataSet.size() / n;
		initialIndexTo = dataSet.size() / n;
		
		// Initiating confusion matrix
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = 0.0;
			}
		}
		
		knn.setK(k);
		// Looping n - 1 times to run knn
		for(int i = 0; i < n; i++) {
			
			// Resetting the attributes
			// Superfluous since guessing should overwrite existing value
			// But it might help debug in case some digits are not handled properly during the folding
			flushAttributes();
			
			// Printing to help keep track of progression (K-NN can be a bit long on small n values)
			if(printProgress) {
				System.out.println("K-NN: Processing fold " + ( i + 1) + "/" + n);
			}
			// If this is the last loop, testSet goes from last IndexFrom to end of dataSet
			// (to ensure we use all data points in case dataset.size() % n != 0)
			// and training set goes from 0 to last IndexFrom
			if( i == n - 1) {				
				testSet = new ArrayList<Digit>(dataSet.subList(indexFrom, dataSet.size()));
				trainingSet = new ArrayList<Digit>(dataSet.subList(0, indexFrom));
			}else {
				testSet = new ArrayList<Digit>(dataSet.subList(indexFrom, indexTo));
				trainingSet = new ArrayList<Digit>(dataSet.subList(indexTo, dataSet.size()));
			}
			
			// Setting test and training subsets
			knn.setTestSet(testSet);
			knn.setTrainingSet(trainingSet);
			
			// Running kMeans with specific test and training subsets and storing accuracy
			knn.findKNN(false);
			accuracy += knn.printAccuracy(false);
			Double[] tempConfidenceValues = knn.printConfidence(false);
			
			// Retrieving Confusion Matrix
			tempConfusionMatrix = knn.getConfusionMatrix();
			// Adding values of Confusion Matrix to total
			for(int j = 0; j < 10; j++) {
				for(int l = 0; l < 10; l++) {
					confusionMatrix[j][l] += tempConfusionMatrix[j][l];
				}
			}
			
			// Storing confidence values
			for(int j = 0; j < confidenceValues.length; j++) {
				confidenceValues[j] += tempConfidenceValues[j];
			}
			
			// Sliding indexes forward for the next loop
			indexFrom = indexTo + 1;
			indexTo += initialIndexTo;
		}
		
		// Calculating accuracy and confidence
		accuracy = accuracy / n;
		for(int i = 0; i < confidenceValues.length; i++) {
			confidenceValues[i] = confidenceValues[i] / n;
		}
		
		// Calculating Confusion Matrix
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = confusionMatrix[i][j] / n;
			}
		}
		
		// Printing results
//		System.out.println();
//		System.out.println("K-NN (k = " + k + ") average accuracy (" + n + "-fold) = " + accuracy);
//		System.out.println("Average confidence: " + confidenceValues[0] + "%");
//		System.out.println("Average confidence (correct guesses): " + confidenceValues[1] + "%");
//		System.out.println("Average confidence (wrong guesses): " + confidenceValues[2] + "%");
		if(printConfusionMatrix) {
			System.out.println();
			System.out.println("K-NN (k = " + k + ") confusion matrix (" + n + "-fold) = " + accuracy);
			System.out.println("Note: results shown in the confusion matrix might be slightly inaccurate due to rounding");
			knn.printConfusionMatrix(confusionMatrix);
		}
		
		
		return accuracy;
	}
	
	
	// Runs k-means, cross-validating the data set N times
	public Double kMeans(Integer n, boolean printProgress, boolean printConfusionMatrix) {
		
		// Checking validity of fold
		if (n < 2) {
			System.out.println("Cannot run: Fold parameter must be 2 or more");
			return 0.0;
		}
		
		// Variables
		Double accuracy = 0.0;
		Double[][] confusionMatrix = new Double[10][10];
		Double[][] tempConfusionMatrix = new Double[10][10];
		Integer indexFrom = 0;
		Integer indexTo;
		Integer initialIndexTo;
		ArrayList<Digit> testSet;
		ArrayList<Digit> trainingSet;
		
		// Set initial index boundaries
		indexTo = dataSet.size() / n;
		initialIndexTo = dataSet.size() / n;
		
		// Initiating confusion matrix
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = 0.0;
			}
		}
		
		
		// Looping n - 1 times to run kMeans
		for(int i = 0; i < n; i++) {
			
			// Resetting the attributes
			// Superfluous since guessing should overwrite existing value
			// But it might help debug in case some digits are not handled properly during the folding
			flushAttributes();
			
			// Printing to help keep track of progression
			if(printProgress) {
				System.out.println("K-Means: Processing fold " + ( i + 1) + "/" + n);
			}
			
			// If this is the last loop, testSet goes from last IndexFrom to end of dataSet
			// (to ensure we use all data points in case dataset.size() % n != 0)
			// and training set goes from 0 to last IndexFrom
			if( i == n - 1) {				
				testSet = new ArrayList<Digit>(dataSet.subList(indexFrom, dataSet.size()));
				trainingSet = new ArrayList<Digit>(dataSet.subList(0, indexFrom));
			}else {
				testSet = new ArrayList<Digit>(dataSet.subList(indexFrom, indexTo));
				trainingSet = new ArrayList<Digit>(dataSet.subList(indexTo, dataSet.size()));
			}
			
			// Setting test and training subsets
			kMeans.setTestSet(testSet);
			kMeans.setTrainingSet(trainingSet);
			
			// Running kMeans with specific test and training subsets and storing accuracy
			kMeans.meanFinder();
			kMeans.kMS();
			
			// Adding accuracy of current fold to total
			accuracy += kMeans.printAccuracy(false);
			
			// Retrieving Confusion Matrix
			tempConfusionMatrix = kMeans.getConfusionMatrix();
			// Adding values of Confusion Matrix to total
			for(int j = 0; j < 10; j++) {
				for(int k = 0; k < 10; k++) {
					confusionMatrix[j][k] += tempConfusionMatrix[j][k];
				}
			}
			// Sliding indexes forward for the next loop
			indexFrom = indexTo + 1;
			indexTo += initialIndexTo;
		}
		
		// Calculating accuracy
		accuracy = accuracy / n;
		
		// Calculating Confusion Matrix
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				confusionMatrix[i][j] = confusionMatrix[i][j] / n;
			}
		}
		
		
		// Printing results
//		System.out.println();
//		System.out.println("K-Means average accuracy (" + n + "-fold) = " + accuracy);
		if(printConfusionMatrix) {
			System.out.println();
			System.out.println("K-Means confusion matrix (" + n + "-fold):");
			System.out.println("Note: results shown in the confusion matrix might be slightly inaccurate due to rounding");
			kMeans.printConfusionMatrix(confusionMatrix);
		}
		
		return accuracy;
	}

	
	// DEBUGGING
	public void showDigit(int index) {
		System.out.println(dataSet.get(index).getRealAttribute());
	}	
}
