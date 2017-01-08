package kNearestNeighbors;

public class ListManipulation {

	// class variable for the percentage of split in data
	final int split;

	// constructor
	public ListManipulation(int n) {
		split = n;
	}

	// Splitting the provided list into random samples of data
	public Object[] splitList(int[][] A) {
		int len = A.length;
		// determining length of training and testing set based on the value of
		// split
		int trainingLength = len - (len / split);
		int testingLength = len - trainingLength;
		// creating a new index array where shuffling will be carried on
		int[] indexArray = new int[A.length];
		// defining training set and testing set arrays which contains only
		// index values
		int[] trainingSet = new int[trainingLength];
		int[] testingSet = new int[testingLength];

		int temp;
		int idx;
		// putting indexes in index array
		for (int i = 0; i < A.length; i++) {
			indexArray[i] = i;
		}
		// shuffling the array
		for (int i = 0; i < A.length; i++) {
			idx = (int) (Math.random() * A.length + 0);
			temp = indexArray[idx];
			indexArray[idx] = indexArray[i];
			indexArray[i] = temp;
		}
		// based on training set length taking that many elements from shuffled
		// index array and storing it in the train set
		for (int j = 0; j < trainingLength; j++) {
			trainingSet[j] = indexArray[j];
		}
		// putting the remaining sample is testing set
		for (int k = 0; k < testingLength; k++) {
			testingSet[k] = indexArray[k + trainingLength];

		}

		return new Object[] { trainingSet, testingSet };

	}

}
