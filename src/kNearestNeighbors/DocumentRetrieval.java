package kNearestNeighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DocumentRetrieval {

	// Method to put result file in a map ADT
	public static Object[] putResultFileArray(String resultFile) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		// reading the file
		File file = new File(resultFile);
		BufferedReader reader;
		// Initializing an array to contain distinct values in result document
		String[] res = new String[4];
		for (int v = 0; v < res.length; v++) {
			res[v] = "a";
		}

		// trying to read the file for information
		try {
			// creating a new buffered reader
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			String[] lineArray = new String[2];
			int count = 0;
			int done = 0;
			// looping till there is a next line in file
			while ((text = reader.readLine()) != null) {
				// Splitting line based on comma and storing those values in
				// array
				lineArray = text.split(",");
				// mapping values on a map
				map.put(Integer.parseInt(lineArray[0]), lineArray[1]);
				count = 0;
				// checking if the new Document classified option is already
				// stored as an option
				for (int h = 0; h < res.length; h++) {
					if (!res[h].contains(lineArray[1])) {
						count++;
					}
				}
				// If not stored as an option, storing it as an option
				if (count == 4) {

					res[done] = lineArray[1];
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

		return new Object[] { map, res };
	}

	// Method to put file with words information into an array representation
	public static Object[] putWordFileArray(String wordFile) {

		int[][] documentMatrix = null;
		int distinctFiles = 0;

		File file = new File(wordFile);
		BufferedReader reader;
		Boolean skipFirstLine = true;
		Boolean secondLine = true;

		try {
			// creating a new buffered reader
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			String[] lineArray = new String[3];

			// looping till there is a next line in file
			while ((text = reader.readLine()) != null) {
				// skipping the header line
				if (skipFirstLine) {
					skipFirstLine = false;
					continue;
				}
				// utilizing information in second row to initialize array
				// container and get number of total files.
				else if (secondLine) {
					lineArray = text.split(" ");
					documentMatrix = new int[Integer.parseInt(lineArray[0])][Integer.parseInt(lineArray[1])];
					distinctFiles = Integer.parseInt(lineArray[0]);

					secondLine = false;
				}
				// retrieving information from the document.
				else {
					lineArray = text.split(" ");
					documentMatrix[Integer.parseInt(lineArray[0]) - 1][Integer.parseInt(lineArray[1]) - 1] = Integer
							.parseInt(lineArray[2]);
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

		return new Object[] { documentMatrix, distinctFiles };
	}

}
