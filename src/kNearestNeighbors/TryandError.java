package kNearestNeighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TryandError {
	
	
//	double [] c= {4,1,7,5,8};
//	int [] d= {0,1,2,1,1};
//	double [] g;
//	int[] t;
//	Object[] y= knn.getWeightedNeighbors(c);
//	g = (double[]) y[1];
//	t = (int[]) y[0];
//	
//	for (double r: g)
//	System.out.print(r + "---");
//	
//	System.out.println();
//	for (int w: t)
//		System.out.print(w + "---");
//		
//	System.out.println();
//
//	String [] cv = {"po","ab","bg","bg","ab"};
//	String [] op = {"ab","po","bg"};
//	for (String u: cv){
//		System.out.print(u+"---");
//	}
//	System.out.println();
//	System.out.println(knn.highestWeightedVote(cv,g, op));
//
	
	
//	
//	for (int q: trainSetNear[0]){
//		System.out.println(q);
//	}
//	for (String h: trainSetNearString[0]){
//		System.out.println(h);
//	}
//	
	
	
//	for (int x: testSet){
//		for (int y: trainSet){
//			if (x==y){
//				System.out.println(x);
//			}
//		}
//	}
//	
//	for (int x=0; x<trainSet.length; x++){
//		for (int y=0; y<trainSet.length; y++){
//			if (x!=y){
//				if (trainSet[x]==trainSet[y]){
//					System.out.print("What");
//				}
//			}
//		}
//	}
//	

//	kNearestNeighbors knn = new kNearestNeighbors(3);
//	
//	int [] c= {1,1,1,1,0};
//	int [] d= {0,1,1,0,1};
//	double [] e ={1.0,3.0,2.0,4.0};
//	System.out.println(knn.cosineSimilarities(c,c));
//	
//	int [] ind;
//	
//	System.out.println(knn.getNeighbors(e)[0]);
//	
//	String [] cv = {"ab","bg","bg","ab","po"};
//	String [] op = {"ab","po","bg"};
//	
//	System.out.println(knn.highestVote(cv, op));


	
	static Map<Integer,String> map = new HashMap<Integer,String>();
	static int[][] documentMatrix;
	static int[] trainingSet;
	static int[] testingSet;
	static int distinctFiles;
	static double[][]nearestNeighbor;
	static String[] res;
	static String[] nearestNeighborPrediction;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		putResultFileArray("C:\\Users\\aku\\Desktop\\news_data\\news_articles.labels");
		putWordFileArray("C:\\Users\\aku\\Desktop\\news_data\\news_articles.mtx");
		crossVal(1,2);
	}
	
	// function to put resultfile in a hashmap
	public static void putResultFileArray(String resultFile){

		// reading the file
		File file = new File(resultFile);
		BufferedReader reader;
		res = new String[4];
		for (int v=0;v<res.length;v++){
			res[v] = "a";
		}

		try {
			// creating a new buffered reader
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    String [] lineArray = new String[2];
		    int count=0;
		    int done =0;
		    // looping till there is a next line in file
		    while ((text = reader.readLine()) != null) {
		    	lineArray = text.split(",");
		        map.put(Integer.parseInt(lineArray[0]), lineArray[1]);
		        count=0;
		        for (int h=0;h< res.length;h++){
		        	if (!res[h].contains(lineArray[1])){
		        	count++;
		        }
		        }
		        if (count==4){
		        	
		        	res[done]=lineArray[1];
		        	done++;
		        }
		    }
		    
		} 
		// adding file not found exception
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		} 
		// adding catch block emphasised by console 
		catch (IOException e) {
		    e.printStackTrace();
		}

		
	}
	
	public static void putWordFileArray(String wordFile){
		

		File file = new File(wordFile);
		BufferedReader reader;
		Boolean skipFirstLine = true;
		Boolean secondLine = true;

		try {
			// creating a new buffered reader
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    String [] lineArray = new String[3];
		    
		    // looping till there is a next line in file
		    while ((text = reader.readLine()) != null) {
		    	if (skipFirstLine){
		    	    skipFirstLine =false;
		    		continue;
		    	}
		    	else if (secondLine){
		    		lineArray = text.split(" ");	
		    		documentMatrix = new int[Integer.parseInt(lineArray[0])][Integer.parseInt(lineArray[1])];
		    		distinctFiles = Integer.parseInt(lineArray[0]);
		    		
		    		secondLine=false;
		    	}
		    	else{
		    		lineArray = text.split(" ");
		    		documentMatrix[Integer.parseInt(lineArray[0])-1][Integer.parseInt(lineArray[1])-1] = Integer.parseInt(lineArray[2]); 
		    	}
		    }

		} 
		// adding file not found exception
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		} 
		// adding catch block emphasised by console 
		catch (IOException e) {
		    e.printStackTrace();
		}

		
	}
		
	public static void splitInSets(int spli){
		int f=0;
		int lengthLoop = (int)(spli);
		trainingSet = new int [distinctFiles-(distinctFiles/lengthLoop)];
		testingSet = new int [(distinctFiles/lengthLoop)];
//		System.out.println(documentMatrix.length);
//		System.out.println(trainingSet.length);
//		System.out.println(testingSet.length);
		for(int l=0; l<distinctFiles-(distinctFiles/lengthLoop); l++){
			Boolean distinctLoop = true;
			while(distinctLoop){
				int j = (int)(Math.random()*(distinctFiles)+ 0);
			    if (IntStream.of(trainingSet).anyMatch(x -> x == j)){
				    continue;
			    }
			    else{
			        trainingSet[l]= j;
			        distinctLoop=false;
			    }
			}
		}
		for (int q=0; q< documentMatrix.length; q++){
			final int k =q;
			if (IntStream.of(trainingSet).anyMatch(x -> x == k)){
				continue;
			}
			else{
				testingSet[f] = q;
				f+=1;
			}
		}
		
		return;
	}
	
	public static double similarity(int[] trainSet, int[] testSet){
		int dotProd = 0;
		int vectorTrainSet = 0;
		int vectorTestSet = 0;
		
		for (int p=0; p<trainSet.length;p++){
			dotProd += trainSet[p]*testSet[p];
			vectorTrainSet += trainSet[p]*trainSet[p];
			vectorTestSet += testSet[p]*testSet[p];
		}
		return ((dotProd)/((Math.sqrt(vectorTrainSet)*Math.sqrt(vectorTestSet))));
		
	}
	
	public static void kNearestNeighbor(int k){
		
		double [][] similaritySet = new double [testingSet.length][trainingSet.length];
		nearestNeighbor = new double [testingSet.length][k];
		for (int qw=0; qw<testingSet.length;qw++){
			for (int e=0;e<k;e++){
				nearestNeighbor[qw][e] = -1;
			}
		}
		
		
		for (int x=0;x<testingSet.length;x++){
		    for (int w=0; w<trainingSet.length;w++){
		    	
		    	similaritySet[x][w] = similarity(documentMatrix[testingSet[x]], documentMatrix[trainingSet[w]]);
		    }
		    
		    
		}
			
		for(int c=0;c<similaritySet.length;c++){
			double [][] similaritySetCopy = new double [testingSet.length][trainingSet.length];
			similaritySetCopy = similaritySet.clone();
			for (int j=0;j<k;j++){
			    double largest = similaritySetCopy[c][0];
			    int largeInd =0;
			    for (int z=0; z<similaritySetCopy[c].length;z++){
			    	 if(similaritySetCopy[c][z] > largest){
			                largest = similaritySetCopy[c][z];
			                largeInd =z;
			    	 }
			    }
			    nearestNeighbor[c][j]=largeInd;
			    similaritySetCopy[c][largeInd]=-1;
			}
		}
		
		return;
	}

	public static int accuracy(){
		int business =0;
		int politics=0;
		int sport=0;
		int technology = 0;
		int accurate = 0;
		int zero_count=0;
		
		nearestNeighborPrediction = new String [nearestNeighbor.length];

		
		for (int x=0; x<nearestNeighbor.length;x++){
			politics=0;
			sport=0;
			technology=0;
			business=0;
			for (int g=0;g<nearestNeighbor[x].length;g++){
				
				int y = (int)nearestNeighbor[x][g];
				if (testingSet[x]!=0 && y!=0){
					
				    if (map.get(y).equals(map.get(testingSet[g]))){

						switch (map.get(y)){
						case "business":
							business++;
							break;
						case "politics":
							politics++;
							break;
						case "sport":
							sport++;
							break;
						case "technology":
							technology++;
							break;
						}
				
				    }
				}
				else{
					zero_count +=1;
				}
			}
			
			int largest = Collections.max(Arrays.asList(business, politics, sport,technology));
			
			if (business==largest){
				nearestNeighborPrediction[x] = "business";
			}
			else if(politics==largest){
				nearestNeighborPrediction[x] = "politics";
			}
			else if(sport==largest){
				nearestNeighborPrediction[x] = "sport";
			}
			else{
				nearestNeighborPrediction[x] = "technology";
			}

		}

		for (int op=0; op<testingSet.length; op++){
			if (nearestNeighborPrediction[op].equals(map.get(testingSet[op]))){
				accurate +=1;
			}
		}
		System.out.println(zero_count);
		
		return accurate;
	}

	public static void crossVal (int folds, int knn){
		splitInSets(10);
		int accu = 0;
		@SuppressWarnings("unused")
		double[][][] result = new double [folds][testingSet.length][knn];;
		for (int h=0; h<folds;h++){
			splitInSets(10);
			kNearestNeighbor(knn);
//			for (int y=0;y<testingSet.length;y++){
//				result[h][y]=nearestNeighbor[y].clone();
//			}
			accu = accu + accuracy();
		}
		System.out.println(accu);
		System.out.println(testingSet.length);
		System.out.println(trainingSet.length);
//		for (int b=0; b < documentMatrix.length;b++){
//			System.out.println(" printing an array " + b);
			for (int c=0; c<documentMatrix[0].length;c++){
//				System.out.println(documentMatrix[0][c]);
//				System.out.println("check" + c);
//				System.out.println(trainingSet[c]);
//				System.out.print("---------------------------------");
			}
		
	}

}

