package coursework2;

import java.io.IOException;

public class Main {
	

	// TODO
	// Create dataset structure with methods to display correct guesses, stores all digits from a set...

	public static void main(String[] args) throws IOException {
		
		// Used to load data into different classes
		Load loader = new Load();
		
		
		// Runs K-NN manually.
		// Creates a knn object (requires loader object and Integer k)
		KNN knn = new KNN(loader, 10);
		// Loading data
		knn.loadTrainingSet("cw2DataSet2.csv");
		knn.loadTestSet("cw2DataSet1.csv");
		// Running KNN (parameters: boolean to print progression of the algorithm)
		knn.findKNN(true);
		// Printing accuracy results (parameter set to true to actually print, otherwise only returns the value)
		knn.printAccuracy(true);
		// Printing confidence results (parameter set to true to actually print, otherwise only returns the value)
		knn.printConfidence(true);
		
		// Runs K-Means manually.
		// Creates a KMeansObject object (requires loader object)
		KMeansSupervised kms = new KMeansSupervised(loader);
		// Loading data
		kms.loadTrainingSet("cw2DataSet2.csv");
		kms.loadTestSet("cw2DataSet1.csv");
		// Finding 10 average bitmaps based on the data sets
		kms.meanFinder();
		// Guessing values of test set
		kms.kMS();
		// Printing accuracy results (parameter set to true to actually print, otherwise only returns the value)
		kms.printAccuracy(true);
		
		
		// Creates a "folder" object used to run K-NN and K-Means with a specific N-Fold
		// addToDataSet is used to populate the data set with all our data
		// knn() takes k and n as arguments, kMeans() only requires n
		Folder folder = new Folder(loader, knn, kms);
		// Adding both data set to the folder object
		folder.addToDataSet("cw2DataSet1.csv");
		folder.addToDataSet("cw2DataSet2.csv");
		
		// DataSets can be shuffled randomly (could be useful to get average values over several runs)
		folder.randomizeSet();
		
		
		System.out.println("              ----------");
		// Running knn with (k, n, printing progress during run, printing confusion matrix)
		folder.knn(1, 2, true, true);


		// Used for collecting K-NN results with 100 runs
//		Integer[] ks = { 1, 2, 3, 4, 5, 10, 50, 100};
//		Integer[] ns = { 2, 3, 4, 5, 10, 50, 100};
//		
//		Double acc = 0.0;
//		Double bestAcc = 0.0;
//		Double currentAcc = 0.0;
//		for(int q = 0; q < ks.length; q++) {
//			for(int w = 0; w < ns.length; w++) {
//				
//				for(int e = 0; e < 100; e++) {
//					currentAcc = folder.knn(ks[q], ns[w], false, false);
//					acc += currentAcc;
//					if(currentAcc > bestAcc) {
//						bestAcc = currentAcc;
//					}
//					folder.randomizeSet();
//				}
//				acc = acc / 100;
//				System.out.println("KNN => k = " + ks[q] + ", n = " + ns[w] + ", acc = " + acc + ", best acc = " + bestAcc);
//				acc = 0.0;
//				bestAcc = 0.0;
//				currentAcc = 0.0;
//			}
//		}
		
		System.out.println("              ----------");
		// Running kMeans with (n, printing progress during run, printing confusion matrix)
		folder.kMeans(3, true, true);
		
		
		// Used for collecting K-Means results with 100 runs
//		for(int w = 0; w < ns.length; w++) {
//			for(int e = 0; e < 100; e++) {
//				currentAcc = folder.kMeans(ns[w], false, false);
//				acc += currentAcc;
//				if(currentAcc > bestAcc) {
//					bestAcc = currentAcc;
//				}
//				folder.randomizeSet();
//			}
//			acc = acc / 100;
//			System.out.println("KMEANS => n = " + ns[w] + ", acc = " + acc + ", best acc = " + bestAcc);
//			acc = 0.0;
//			bestAcc = 0.0;
//			currentAcc = 0.0;
//		}
		
		System.out.println("              ----------");
	}

}
