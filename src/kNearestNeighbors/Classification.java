package kNearestNeighbors;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Classification {

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Getting user input for number of neighbors to consider and if to go
		// for weighted or non-weighted scheme
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter the value of k for k-nn classification(minimum 1): ");
		int kNumber = scanner.nextInt();
		if (kNumber>0){
		System.out.print("Please enter 1 if you would like weight implementation, 0 otherwise: ");
		int weightYN = scanner.nextInt();
		System.out.println("\n");
		// Declaring a map to store the real classification value of each
		// document
		Map<Integer, String> classifiedMap = new HashMap<Integer, String>();
		// A string array to store all the options available for the document to
		// be classified in.
		String[] classificationOptions;

		// The array representation of documents and words
		int[][] arrayRepresentation;
		// Total number of files scanned from the file
		int numberOfFiles;

		// New Object arrays that contains information from both the file in an
		// operate-able format
		Object[] obj1 = DocumentRetrieval.putResultFileArray("Files\\news_articles.labels");
		Object[] obj2 = DocumentRetrieval.putWordFileArray("Files\\news_articles.mtx");

		// reading the map and options available from first object
		classifiedMap = (Map<Integer, String>) obj1[0];
		classificationOptions = (String[]) obj1[1];

		// reading array representation of document and words from object 2
		arrayRepresentation = (int[][]) obj2[0];
		numberOfFiles = (int) obj2[1];

		// based on user input calling both the functions to retrieve result
		//Euclidean(arrayRepresentation, classificationOptions, classifiedMap, kNumber, weightYN);
		Cosine(arrayRepresentation, classificationOptions, classifiedMap, kNumber, weightYN);

		// Code snippet to display result for all the k's in range of 1-10.

		// for (int getResults=1; getResults<=10; getResults++){
		// Euclidean(arrayRepresentation,classificationOptions,classifiedMap,getResults,0);
		// }
		//
		// System.out.print("\n \n \n");
		//
		// for (int getResults=1; getResults<=10; getResults++){
		// Euclidean(arrayRepresentation,classificationOptions,classifiedMap,getResults,1);
		// }
		//
		// System.out.print("\n \n \n");
		//
		// for (int getResults=1; getResults<=10; getResults++){
		// Cosine(arrayRepresentation,classificationOptions,classifiedMap,getResults,0);
		// }
		//
		// System.out.print("\n \n \n");
		//
		// for (int getResults=1; getResults<=10; getResults++){
		// Cosine(arrayRepresentation,classificationOptions,classifiedMap,getResults,1);
		// }
		}
		else{
			System.out.println("You need to consider atleast one neighbor for classification");
		}
	}

	// Method to perform classification based on Cosine similarities between the
	// documents
	public static void Cosine(int[][] arrayRepresentation, String[] classificationOptions,
			Map<Integer, String> classifiedMap, int number, int weighted) {

		// Splitting Matrix representation into testing and training sets
		int[] testSet;
		int[] trainSet;
		// Creating an object of ListManipulation class that divides the whole
		// array matrix into random training and testing set.
		ListManipulation lm = new ListManipulation(10);

		Object[] obj3 = lm.splitList(arrayRepresentation);

		trainSet = (int[]) obj3[0];
		testSet = (int[]) obj3[1];

		// Variable to store the average result over 10 iterations
		double finalResult = 0;
		// repeating 10 times since we would like to average the result
		for (int rep = 0; rep < 10; rep++) {
			// defining a 2-d array to store cosine similarities of different
			// documents
			double[][] similaritySet = new double[testSet.length][trainSet.length];

			// creating an object of kNearestNeighbors class
			KNearestNeighbors knn = new KNearestNeighbors(number);

			// finding cosine similarities
			for (int i = 0; i < testSet.length; i++) {
				// int[] A = arrayRepresentation[testSet[i]].clone();
				for (int j = 0; j < trainSet.length; j++) {
					// int[] B = arrayRepresentation[trainSet[j]].clone();
					similaritySet[i][j] = knn.cosineSimilarities(arrayRepresentation[testSet[i]],
							arrayRepresentation[trainSet[j]]);
				}
			}

			// getting neighbors with highest cosine similarity
			int[][] trainSetNearIndex = new int[testSet.length][];
			double[][] trainSetNearCosineWeights = new double[testSet.length][];
			// We define an object since we return both weights and index array
			Object[] obj5;
			for (int y = 0; y < similaritySet.length; y++) {
				obj5 = knn.getNeighborsCosine(similaritySet[y]);
				trainSetNearIndex[y] = (int[]) obj5[0];
				trainSetNearCosineWeights[y] = (double[]) obj5[1];
			}

			// getting string representation/document classification of indexes
			// found to be most similar
			// through cosine representation.
			String[][] trainSetNearString = new String[testSet.length][number];

			for (int z = 0; z < trainSetNearIndex.length; z++) {
				for (int x = 0; x < trainSetNearIndex[z].length; x++) {
					int g = trainSetNearIndex[z][x];
					g = trainSet[g] + 1;
					trainSetNearString[z][x] = classifiedMap.get(g);
				}
			}

			// getting string value for highest votes cosine
			String[] highestVotesString = new String[trainSetNearString.length];
			// If the user wants weighted version
			if (weighted == 1) {

				for (int z = 0; z < trainSetNearString.length; z++) {
					highestVotesString[z] = knn.highestWeightedVote(trainSetNearString[z], trainSetNearCosineWeights[z],
							classificationOptions);
				}
			}
			// if the user is looking for binary vote representation
			else {
				for (int z = 0; z < trainSetNearString.length; z++) {
					highestVotesString[z] = knn.highestVote(trainSetNearString[z], classificationOptions);

				}
			}

			// get result cosine
			int counter = 0;
			for (int h = 0; h < testSet.length; h++) {
				// check if the predicted value is equal to the original value
				// using +1 since map starts from 1 while array starts from 0
				if (classifiedMap.get(testSet[h] + 1).equals(highestVotesString[h])) {
					counter = counter + 1;
				}
			}

			// add the result for iteration to final result
			finalResult = finalResult + counter;

			// Again random sub sampling the array into training and testing set
			Object[] obj4 = lm.splitList(arrayRepresentation);

			trainSet = (int[]) obj4[0];
			testSet = (int[]) obj4[1];
		}
		// Averaging the final result since it represents sum over 10 iterations
		finalResult = finalResult / 10;
		// Multiplying by 100 for percentage value
		finalResult = finalResult * 100;
		// Dividing by total number of documents in testSet to get final result
		finalResult = finalResult / testSet.length;

		// Different output statements used based on if it was weighted or
		// non-weighted representation
		if (weighted == 1) {
			System.out.println("Cosine similarity with Weights and k=" + number + "\t Accuracy = " + finalResult + "%");
		} else {
			System.out.println(
					"Cosine similarity without Weights with k=" + number + "\t Accuracy = " + finalResult + "%");
		}

	}

	// Method based on distance between 2 documents, Euclidean distance
	// repeating similar steps as that during Cosine method.
	public static void Euclidean(int[][] arrayRepresentation, String[] classificationOptions,
			Map<Integer, String> classifiedMap, int number, int weighted) {
		// finding euclidean distance between each document
		int[] testSet;
		int[] trainSet;
		ListManipulation lm = new ListManipulation(20);

		Object[] obj3 = lm.splitList(arrayRepresentation);

		trainSet = (int[]) obj3[0];
		testSet = (int[]) obj3[1];

		double finalResult = 0;

		for (int rep = 0; rep < 10; rep++) {

			KNearestNeighbors knn = new KNearestNeighbors(number);

			double[][] distanceSet = new double[testSet.length][trainSet.length];

			for (int i = 0; i < testSet.length; i++) {
				for (int j = 0; j < trainSet.length; j++) {
					distanceSet[i][j] = knn.euclideanDistance(arrayRepresentation[testSet[i]], arrayRepresentation[trainSet[j]]);
				}
			}

			// euclidean Distance getting neighbors

			int[][] trainSetWeightNearIndex = new int[testSet.length][];
			double[][] trainSetNearWeights = new double[testSet.length][];
			Object[] obj4;
			for (int y = 0; y < distanceSet.length; y++) {
				obj4 = knn.getEuclideanWeightedNeighbors(distanceSet[y]);
				trainSetWeightNearIndex[y] = (int[]) obj4[0];
				trainSetNearWeights[y] = (double[]) obj4[1];
			}

			// euclidean string representation

			String[][] trainSetNearWeightString = new String[testSet.length][number];

			for (int z = 0; z < trainSetWeightNearIndex.length; z++) {
				for (int x = 0; x < trainSetWeightNearIndex[z].length; x++) {
					int g = trainSetWeightNearIndex[z][x];
					g = trainSet[g] + 1;
					trainSetNearWeightString[z][x] = classifiedMap.get(g);
				}
			}

			// getting string value for highest votes euclidean

			String[] highestVotesWeightString = new String[trainSetNearWeightString.length];

			if (weighted == 1) {
				for (int z = 0; z < trainSetNearWeightString.length; z++) {

					highestVotesWeightString[z] = knn.highestWeightedVote(trainSetNearWeightString[z],
							trainSetNearWeights[z], classificationOptions);
				}
			} else {
				for (int z = 0; z < trainSetNearWeightString.length; z++) {
					highestVotesWeightString[z] = knn.highestVote(trainSetNearWeightString[z], classificationOptions);
				}
			}

			// get results euclidean

			int counter1 = 0;
			for (int h = 0; h < testSet.length; h++) {
				// System.out.println(classifiedMap.get(testSet[h]+1) + "-->" +
				// highestVotesString[h]);
				if (classifiedMap.get(testSet[h] + 1).equals(highestVotesWeightString[h])) {
					counter1 = counter1 + 1;
				}
			}

			finalResult = finalResult + counter1;

			Object[] obj5 = lm.splitList(arrayRepresentation);

			trainSet = (int[]) obj5[0];
			testSet = (int[]) obj5[1];
		}
		finalResult = finalResult / 10;
		finalResult = finalResult * 100;
		finalResult = finalResult / testSet.length;

		if (weighted == 1) {
			System.out
					.println("Euclidean distance with Weights and k=" + number + "\t Accuracy = " + finalResult + "%");
		} else {
			System.out.println(
					"Euclidean distance without Weights with k=" + number + "\t Accuracy = " + finalResult + "%");
		}

	}

}
