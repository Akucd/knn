package kNearestNeighbors;

public class KNearestNeighbors {
	// class variable to define number of neighbors to consider
	int neighbors;
	// constructor
	public KNearestNeighbors(int n){
		neighbors = n;
	}
	// calculating cosine similarities
	public double cosineSimilarities(int [] A, int [] B){
		double cosSim = 0;
		double dotProduct = 0;
		double vectorA = 0;
		double vectorB = 0; 
		for(int i=0; i<A.length;i++){
			dotProduct= dotProduct+ A[i]*B[i];
			vectorA = vectorA + A[i]*A[i];
			vectorB = vectorB + B[i]*B[i];
		}
		vectorA = Math.sqrt(vectorA);
		vectorB = Math.sqrt(vectorB);
		cosSim = cosSim + (dotProduct/(vectorA*vectorB));
		return cosSim;
	}
	// calculating nearest neighbors based on cosine value and number of neighbors to consider
	public Object[] getNeighborsCosine(double[] D){
		// Since we are changing values in the array we would not want that to reflect it in the original array
		double [] C = D.clone();
		double max;
		int maxind;
		// Defining index and weight array with length same as number of neighbors to consider
		int [] indexArray = new int[neighbors];
		double [] weightArray = new double[neighbors];
		// looping over the array for k largest valued neighbors from all instances 
		for (int j=0; j<neighbors; j++){
			max = C[0];
			maxind = 0;
			// finding maximum value and its index		
			for (int k=0; k<C.length; k++){
				if (C[k]>max){
					max = C[k];
					maxind = k;
				}
			}
			// storing values
			indexArray[j] = maxind;
			weightArray[j] = max;
			// putting the highest value to a random very low value so as to avoid its occurrence second time as well
			C[(int) maxind] = -1000;			
		}
		return new Object[]{indexArray,weightArray};
	}
	// Getting highest vote from the neighbors based on binary votes i.e. to say not considering weights
	public String highestVote(String [] X, String [] options){
		//array to count which option has maximum vote
		int [] counter = new int [options.length];
		// initializing array with 0 value
		for (int x=0; x<options.length;x++){
		    counter[x] = 0;	
		}
		// looping over all the neighbors and incrementing counter based on what neighbor depicts
		for(String o: X){
			for (int x=0; x<options.length;x++){
			    if (o.equals(options[x])){
			    	counter[x] = counter[x]+1;
			    }
			}	
		}
		
		// finding the max from the counter
		int max = 0;
		int maxIndex =0;
		
		for (int k=0; k<counter.length; k++){
			if (counter[k]>max){
				max = counter[k];
				maxIndex = k;
			}
		}
		// returning predicted value
		return options[maxIndex];
	}
	// calculating euclidean distance
	public double euclideanDistance(int [] O, int [] P){
		double distance = 0;
		
		for (int z=0; z<O.length; z++){
			distance = distance + (Math.abs(O[z]-P[z])*Math.abs(O[z]-P[z]));
		}
		distance = Math.sqrt(distance);
		return distance;
		
	}
	

	// getting weights and highest weighted array based on number of neighbors to consider and their distance
	public Object[] getEuclideanWeightedNeighbors(double[] E){
		// cloning arra since we manipulate its value
		double [] F = E.clone();
		double min, minind;
		int [] indexArray = new int[neighbors];
		double [] weightArray = new double[neighbors];
		// finding neighbors with minimum distance
		for (int j=0; j<neighbors; j++){
			min = F[0];
			minind = 0;
					
			for (int k=0; k<F.length; k++){
				if (F[k]<min){
					min = F[k];
					minind = k;
				}
			}
			indexArray[j] = (int) minind;
			weightArray[j] = 1/min;
			// setting distance of the minimum value to a random high in order to avoid repetition. 
			F[(int) minind] = 1000;			
		}
		return new Object[]{indexArray,weightArray};
	}
	

	// getting predicted value based on option with highest weight
	public String highestWeightedVote(String [] X, double [] wArray, String [] options){
		double [] counter = new double [options.length];
		for (int x=0; x<options.length;x++){
		    counter[x] = 0;	
		}
		// same as checking for non weighted implementation but instead of incrementing the counter by 1
		// incrementing it by the weight value
		for(int o=0; o< X.length;o++){
			for (int x=0; x<options.length;x++){
			    if (X[o].equals(options[x])){
			    	counter[x] = counter[x]+wArray[o];
			    }
			}	
		}
		
		double max = counter[0];
		int maxIndex =0;
		
		// finding the maximum value from the counter.
		for (int k=0; k<counter.length; k++){
			if (counter[k]>max){
				max = counter[k];
				maxIndex = k;
			}
		}
		
		return options[maxIndex];
	}

}

