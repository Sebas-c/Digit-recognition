package coursework2;

import java.util.ArrayList;
import java.util.Collections;

public class QuickSort {
	
	//
	// Variables
	//
	
	private ArrayList<Integer> attributes;
	private ArrayList<Double> distances;
	
	//
	// Constructor
	//
	
	public QuickSort() {
		
	}

	public QuickSort(ArrayList<Integer> attributes, ArrayList<Double> distances) {
		
		//Using clones to avoid shallow copies
		this.attributes = (ArrayList<Integer>)attributes.clone();
		this.distances = (ArrayList<Double>)distances.clone();
		
	}
	
	//
	// Getters and Setters
	//
	
	public ArrayList<Integer> getAttributes() {
		
		return attributes;
		
	}

	public void setAttributes(ArrayList<Integer> attributes) {
		
		//Using clone to avoid shallow copy
		this.attributes = (ArrayList<Integer>)attributes.clone();
		
	}

	public ArrayList<Double> getDistances() {
		
		return distances;
		
	}

	public void setDistances(ArrayList<Double> distances) {
		
		//Using clone to avoid shallow copy
		this.distances = (ArrayList<Double>)distances.clone();
		
	}	
	
	public void setArrayLists(ArrayList<Integer> attributes, ArrayList<Double> distances) {
		//Using clone to avoid shallow copy
		this.attributes = (ArrayList<Integer>)attributes.clone();
		this.distances = (ArrayList<Double>)distances.clone();
		
	}
	
	//
	// Methods
	//
	
	
	// Quicksorts distances and attributes alongside
	public void quickSortDistances(ArrayList<Double> distances, Integer low, Integer hi, ArrayList<Integer> attributes) {
		
		// If 
		if(low < hi) {
			Integer index = quickPartitionDistances(distances, low, hi, attributes);
			quickSortDistances(distances, low, index - 1, attributes);
			quickSortDistances(distances, index + 1, hi, attributes);
		}
		
		
	}
	
	// Swaps elements around the pivot (smaller to the left, bigger to the right) and returns pivots index
	public Integer quickPartitionDistances(ArrayList<Double> distances, Integer low, Integer hi, ArrayList<Integer> attributes) {
				
		// Variables
		Double pivot = distances.get(hi);
		Integer index = low;
				
		// Looping through the partition and swapping smaller elements towards the left
		for(int j = low; j < hi; j++) {
			if(distances.get(j) < pivot) {
				Collections.swap(distances, index, j);
				Collections.swap(attributes, index, j);
				index++;
			}
		}
		
		// Swapping pivot with last element to move
		Collections.swap(distances, index, hi);
		Collections.swap(attributes, index, hi);
		
		// Returning index for next sub-partitioning
		return index;
	}
	
}
