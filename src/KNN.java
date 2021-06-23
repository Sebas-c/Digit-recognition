package coursework2;

import java.util.ArrayList;

import java.text.DecimalFormat;

public class KNN extends Tools{
	
	// Class implementing k-nearest neighbour
	// No implementation of NN as it can be run with k = 1
	// (slightly less efficient than a proper NN implementation but considering the small data sets, should be minimal)
	
	//
	// Variables
	//
	
	private ArrayList<Digit> trainingSet;
	private boolean allDataValid = true;
	private Integer k = -1;
	
	Load loader;
	QuickSort quickSort;
	
	//
	// Constructor
	//
	
	public KNN(Load loader, Integer k) {
		this.loader = loader;
		this.quickSort = new QuickSort();
		this.algorithmName = "K-NN";
		this.k = k;
	}
	
	//
	// Getters and Setters
	//
	
	// Loads a training file
	public void loadTrainingSet(String fileName) {
		
		loader.flushDigits();
		loader.loadFile(fileName);
		this.trainingSet = (ArrayList<Digit>)loader.getAllDigits().clone();
//		System.out.println("Training set size: " + trainingSet.size() + " digits.");
		
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
//		System.out.println("Test set size: " + testSet.size() + " digits.");
		
	}
	
	// Sets a test set
	public void setTestSet(ArrayList<Digit> testSet) {
		this.testSet = testSet;
	}
	
	// Sets k
	public void setK(Integer k) {
		this.k = k;
	}
	
	//
	// Methods
	//
	
	// Performs NN search for k number of neighbours
	public void findKNN(boolean prettyPrint) {
		
		// Checking that we are trying to run the algorithm with a positive k
		if(k < 1) {
			System.out.println();
			System.out.println("----- K-NN can't run with values lower than 1. -----");
			System.out.println();
			return;
		}
		
		// Create an array of size k with max value for the distances
				ArrayList<Integer> orderedNeighboursAttribute;
				ArrayList<Double> orderedNeighboursDistance;
				Integer[] digitsCounter = new Integer[10];
				Double distance = Double.MAX_VALUE;
//				boolean endOfArray = true;
		
		// Setting the name of the algorithm for accuracy printing
		if(k > 1) {
			this.algorithmName = "k-NN (k = " + k.toString() + ")";
		} else {
			this.algorithmName = "NN";
		}
		
				
		
		
		// DEBUGGING AND PRINTING PRETTY INFO
		// Set printing to false to avoid useless visual clutter
		boolean printing = true;
		Integer counter = 0;
		String[] progressBar = { "-", "\\", "|",  "/"};
				
		// Find the distances for each point in the training set
		for(int i = 0; i < testSet.size(); i++) {
			//Resetting the arrays
			orderedNeighboursAttribute = new ArrayList<Integer>();
			orderedNeighboursDistance = new ArrayList<Double>();
	
			
			for(int j = 0; j < trainingSet.size(); j++) {
				
				distance = euclidianDistance(testSet.get(i), trainingSet.get(j));
				
				// DEBUGGING
				if(distance < 0) {
					System.out.println("Error!");
				}
				
				// Storing the distance between the points of the test and training set
				orderedNeighboursDistance.add(distance);
				orderedNeighboursAttribute.add(trainingSet.get(j).getRealAttribute());
				
//				// NOTES: Initial implementation, was replaced by QuickSort. Only left it as it was mentioned that all work done should be shown.
//				// Following bit of code would reorder each element in the array after the distance was calculated
//				// By using QuickSort instead ordering time probably went from aboutO(n2) to O(nLogn)
//				// Another big improvement in both time and space was storing only the attributes in the orderedNeighbours array
//				// Instead of the entire digit. Hard to estimate the improvement for this as it is mostly hardware dependent (writing time mostly)
//				// But I'd say the change only cut down the time by a factor of 3 (I did not time it before hand, this is a vague approximation).
//				
//				// Ordering from smallest to biggest
//				// If the array is empty, add digit and distance to the beginning of their respective arrays
//				if(orderedNeighboursDistance.size() == 0) {
//					
//					orderedNeighboursDistance.add(distance);
//					orderedNeighbours.add(trainingSet.get(j).getRealAttribute());
//					
//				// Otherwise, add digit and distance before the first distance that is bigger than the one we have
//				} else {
//					
//					endOfArray = true;
//					
//					for(int l = 0; l < orderedNeighboursDistance.size(); l++) {
//						
//						if(orderedNeighboursDistance.get(l) > distance) {
//							orderedNeighboursDistance.add(l, distance);
//							orderedNeighbours.add(l, trainingSet.get(j).getRealAttribute());
//							endOfArray = false;
//							break;
//						}
//						
//					}
//					
//					// If the distance is bigger than all those currently in the array, append at the end
//					if(endOfArray) {
//						orderedNeighboursDistance.add(distance);
//						orderedNeighbours.add(trainingSet.get(j).getRealAttribute());
//					}
//				}
			}
			
			// Sorting the distance array (and moving the matching values accordingly)
			quickSort.setArrayLists(orderedNeighboursAttribute, orderedNeighboursDistance);
			quickSort.quickSortDistances(quickSort.getDistances(), 0, orderedNeighboursDistance.size() - 1, quickSort.getAttributes());
			
			
			
			// Retrieving the sorted arrays
			orderedNeighboursDistance = (ArrayList<Double>)quickSort.getDistances().clone();
			orderedNeighboursAttribute = (ArrayList<Integer>)quickSort.getAttributes().clone();
			

			// Resetting the result counters
			for(int m = 0; m < 10; m++) {
				digitsCounter[m] = 0;
			}
			
			// Counting the k closest digits
			for(int j = 0; j < k; j++) {
				digitsCounter[orderedNeighboursAttribute.get(j)]++;
			}
			
			// Storing the guessed attributes in the test set
			testSet.get(i).setGuessedAttribute(closestGuess(digitsCounter));
			
			// Setting the confidence
			Double confidence = highestCount(digitsCounter)/k;
			testSet.get(i).setConfidence(confidence);
			
			
			//DEBUGGING AND PRINTING PRETTY INFO
			if(prettyPrint) {
				counter++;
				if(counter%25 == 0 && printing) {
					Double percentage = counter.doubleValue() * 100 / testSet.size();
					System.out.println("K-NN running.     [" + progressBar[counter / 25 % 4] + "]     Progression: " +  new DecimalFormat("#.##").format(percentage) + "%");
					System.out.flush();
				}
			}			
		}
		
	}
	
	
	
	// Returns the highest occurrence of a digit from the provided array
	// TODO
	// Handle same amount of guesses
	public Integer closestGuess(Integer[] counters) {
		Integer highestCounterIndex = -1;
		Integer highestCount = -1;
		
		for(int i = 0; i < counters.length; i++) {
			
			if (counters[i] > highestCount){
				
				highestCount = counters[i];
				highestCounterIndex = i;
				
			}
		}
		
		return highestCounterIndex;
	}
	
	
	public Double highestCount(Integer[] counters) {
		Integer highestCount = -1;
		
		for(int i = 0; i < counters.length; i++) {
			
			if (counters[i] > highestCount){
				
				highestCount = counters[i];
				
			}
		}
		
		return Double.valueOf(highestCount);
	}
}
