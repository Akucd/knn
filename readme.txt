Implementation
To implement K-Nearest Neighbours classification on the sample data provided, I used bag-of-words representation of information. The following section explains the classes I used and the functions in them along with how are they being utilized in program.
The implementation consists of 4 classes:
1)	DocumentRetrieval
2)	ListManipulation
3)	KnearestNeighbors
4)	Classification

DocumentRetrieval Class
The following class contains 2 methods. 
1)	putResultFileArray
2)	putWordFileArray

putResultFileArray
The method converts the file that has pre stored information about the original classification of a document into a map variable. Since the information in file is stored with a formatting of Comma separated values, the method reads a sentence, splitting the line with ‘,’ as the separator and extracting information. 
The format the information is stored in map variable is (integer,string) where integer represents the document number and string represents the class the document is classified as.
The method also retrieves all the possible class distinctly present in the file. This array is later utilized for getting votes from neighbours. 
At the end of execution it returns the map variable and the possible class arrays. 
putWordFileArray
The method reads the file that has information about words in documents. It stores all the information in a bag-of-words style of representation with rows representing the document and columns representing the words in the documents. 
Although not a very efficient way with respect to computing time but since we were implementing from scratch it was important to be able to visualize clearly what is happening. 
The method also retrieves the information about total number of documents from the second line of the file. The following pseudo-code is the crux of how the method is operating:
While (endOfFile):
	Line = read_Line();
	If Line is first Line:
		Skip this loop
	If Line is second Line:
		Documents = Get_total_number_of_documents
		Words = Get_total_number_of_distinct_words
		Initalize array with rows = Documents and Columns = Words
	For every Other Line:
		Store information in Array with rows corresponding document Number
		and columns corresponding to the information about words it represents

The method returns the array representation of the information in the matrix and the total number of documents retrieved from second line.

ListManipulation Class
The following class contains only one method as of now which is splitList that is being used to split the complete bag-of-words representation in training and testing set. 
The class constructor expects an integer value on basis of which it divides the matrix into training and testing set. For example: If the testing and training set are to be divided as 2:1 the n would be 3 and if it has to be divided as 3:1 value of n would be 4. 
It is so since the way it is being implemented is, it divides the total number of documents by the integer value and removes that part of length from training set length and using that as testing set length.
(The current default value for division is set at 10. Thus there are 183 instances in testing set and remaining in training set.)
splitList
The method splitList is used to randomly shuffle data into training and testing sets based on decided length in constructor. The method expects the array representation of the matrix as a parameter since it manipulates its length of rows and columns to perform its operations.


The following pseudo-code gives the crux as to how the method works:
For i in range number_of_rows_of_array_representation:
	Index_array [i] = i
For j = 0 in range index_array:
	Random_value = Randomly pick a value between 0 and index_array length
	//Swap values in index array
	Temp = value stored at j 
value stored at j = random_value
value stored at random_value = temp
For k = 0 in range training_set_length:
	Put values of index_Array[k] in train set

For m = 0 in range test_set_length:
	Put values of index_Array[k+train_set_length] in test set

It returns the training set and testing set as a 1-d array that represents the indexes considered to be contributing as training set or testing set.
kNearestNeighbors
This is the class where all the classification specific activities are happening i.e. to say calculation of cosine similarities, getting k nearest neighbours and getting votes based on either weighted voting scheme or binary voting scheme. The class has a constructor that expects an integer value which represents number of neighbours we are interested in.
The class implements the following methods:
1)	CosineSimilarities
2)	getNeighborsCosine
3)	highestVote
4)	highestWeightedVote
5)	euclideanDistance - // It is an additional method
6)	getEuclideanWeightedNeighbors - // it is an additional method

cosineSimilarities
The method calculates cosine similarities between two 1-d arrays. It expects both these arrays, A and B, as its parameters and then calculates cosine similarity value for them. The method is implemented based upon the following mathematical formula:
Cos (D1, D2) =D1·D2 / (||D1|| ||D2||)
The following pseudo code exhibits the crux of the implementation:
// Since it is a bag of word representation both the arrays have same length
Iterate over array A length: 
	numerator = numerator + element_of_array_A * element_of_array_B 
	Mod_A = Mod_A + element_of_array_A* element_of_array_A
	Mod_B = Mod_B + element_of_array_B* element_of_array_B

Mod_A = Square_root_of_Mod_A
Mod_B = Square_root_of_Mod_B
Denominator = Mod_A * Mod_B
Return Numerator/Denominator

getNeighborsCosine
After the cosine similarities have been calculated for each array we want to find the k nearest neighbours to the test case. The following method helps with that. It expects a 1-d array that represents the cosine similarities between a document testing set and all others in training set. The pseudo code explains the core of how the method works:
Initialize an array, NN, with length same as the value of k
For n = 0 in range length of NN:
	Max = 0
	Max index = 0 
   	For m in range Array length:
		If value_at_m > Max:
			Max = value_at_m
			Max index = m
	Nearest Neighbor = Max
	Nearest Neighbor Index = Max index
	Array [ Max index] = -1000

Return Nearest Neighbor, Nearest Neighbor Index
The array returns the index of the document that has highest similarity to the testing document and the cosine similarity of that as well in case we want to implement a weighted implementation later.
highestVote
The following method helps in retrieving the class with highest votes that the neighbours suggest. It takes in a string array which represents the class of the neighbours and another string array that represents the distinct classification options and then returns the class with the highest vote.
The following pseudo code gives an insight as to how the core of method works.

Initialize a counter for each distinct class
Set all counters equal to 0
 for each string in neighbour_class_array:
	for each class-string in distinct_class_array:	
               if string equals class_string:
			Increment counter by 1 for that class

Return class with highest counter

highestWeightedVote
The method works almost similar to the highest vote method. There are 2 differences in them.
1)	This method expects an additional parameter as an array of doubles that represents the weight of the neighbour or the class it votes for.
2)	Instead of incrementing the counter by 1 it increments the counter by weight of the neighbour

The following pseudo code helps in depicting what is happening:
Initialize a counter for each distinct class
Set all counters equal to 0
for each string in neighbour_class_array:
	for each class-string in distinct_class_array:	
               if string equals class_string:
			Increment counter by weight of neighbour for that class

Return class with highest counter
euclideanDistance and getEuclideanWeightedNeighbors
The following methods have been additionally implemented out of curiosity in order to see how the classification works with Euclidean distance as the deciding factor.
Classification Class
The following class is where objects of all other classes are being created and used in order to perform classification. 
The class takes 2 user inputs:
1)	Number of neighbours to consider for classification
2)	If to do a weighted voting scheme or a binary neighbour voting scheme
After this step the class is hardcoded to read files present in the files folder of the project and with the name of the sample files provided. This could be changed to a user input feature but since we had only one set of files we hardcoded that into our program.
After receiving information through DocumentRetrieval class object and from user, the main method passes all the variables as parameters to cosine and Euclidean static methods. 
(Note: Since the Euclidean static method is an extra implementation only the Cosine is being discussed here.)
Cosine
The following static method is used to classify the given bag-of-words representation according to the suggested value of nearest neighbours to consider. The validation technique used is Monte-Carlo cross validation/ random sub-sampling technique.
The following is a step by step explanation of what is happening in the method:
1)	The bag-of-words representation in form of array is broken down into training and testing set. A global variable result is initialized to 0.
2)	An object of kNearestNeighbors is initialized with parameter equal to the value of neighbours specified by the user.
3)	We repeat the following process 10 times to achieve a stable average accuracy result    
	a)	Calculating cosine similarities for testing set against all instances in training set. We store this information in an array where row represents the testing set and columns represents the training set.
	b)	We then get the k neighbours with highest similarity weight through the getNeighborsCosine method of kNearestNeighbor Class. 
	c)	According to the index array, which represents the training sets that the testing set instance is closest to, retrieved from step (b) we create a new array that represents their class through those indexes. 
	d)	Along with the string array formed in step (c) and the distinct_class_list obtained as a parameter we pass both of these string to highestVote method of kNearestNeighborsClass and store the returned value in a new array which becomes the predicted class for the corresponding value in test instance set.
	e)	Compare values in predicted values array and through class retrieved using the map object and test set. 
	f)	Add number of correctly classified instances to a class variable, result.
4)	Divide the class variable result by 10 to average the result of 10 iterations and then again divide by length of test set to get proportion of correctly classified instances. Multiply it by 100 to retrieve percentage of correctly classified instances.
5)	Display result.    

Results
After iterating over the cosine method with different values of k for a binary vote scheme the following was the result achieved:
Cosine similarity without Weights with k=1    Accuracy = 96.01092896174863%
Cosine similarity without Weights with k=2    Accuracy = 94.00546448087432%
Cosine similarity without Weights with k=3    Accuracy = 95.40983606557377%
Cosine similarity without Weights with k=4    Accuracy = 95.73770491803279%
Cosine similarity without Weights with k=5    Accuracy = 94.80874316939891%
Cosine similarity without Weights with k=6    Accuracy = 95.13661202185793%
Cosine similarity without Weights with k=7    Accuracy = 95.02732240437159%
Cosine similarity without Weights with k=8    Accuracy = 95.68306010928961%
Cosine similarity without Weights with k=9    Accuracy = 95.35519125683061%
Cosine similarity without Weights with k=10   Accuracy = 95.68306010928961%


 

After iterating over the cosine method with different values of k for weighted vote scheme the following was the result achieved:

Cosine similarity with weights and k=1	 Accuracy = 94.97267759562841%
Cosine similarity with weights and k=2	 Accuracy = 94.97267759562841%
Cosine similarity with weights and k=3	 Accuracy = 95.35519125683061%
Cosine similarity with weights and k=4	 Accuracy = 96.12021857923497%
Cosine similarity with weights and k=5	 Accuracy = 95.62841530054645%
Cosine similarity with weights and k=6	 Accuracy = 95.62841530054645%
Cosine similarity with weights and k=7	 Accuracy = 95.30054644808743%
Cosine similarity with weights and k=8	 Accuracy = 95.68306010928961%
Cosine similarity with weights and k=9	 Accuracy = 94.80874316939891%
Cosine similarity with weights and k=10	 Accuracy = 96.17486338797814%
 

The measure we picked to evaluate our result is accuracy. As we can see the result is very well packed in a roughly 94-96% band. This does mean that even though increasing and decreasing size of neighbours considered is affecting the accuracy, the ups and downs reflect on roughly 10-20 test instances only.  
Another interesting thing that we can notice is that even number of k are performing either equal to or slightly better than their preceding odd numbers except for the first case in cosine similarity without weights. Although the difference is really minor it does put a doubt if ordinal values of possible class labels are playing a part. Although we have not implemented anything to cater the class labels as ordinal features but the way the loop works it does consider the features ordinals inherently as if there is a tie between the 2 classes the first one is picked since the check condition does not get satisfied for the second one. 
The other thing noticeable is that the average result for weighted vote are better clustered than the binary voted. Maybe using weights implementation is helping in very few cases that are exhibiting sparsity problems. Also since we did not use any pre-processing technique, getting such clustered result does convey to some extent that we do not have any major noisy data.
In conclusion, we did manage to achieve a high accuracy result for the problem at hand with random sub-sampling validation but it would be interesting to see how Tf-Idf technique would fare as compared to results we achieve through cosine similarity. Implementing the problem with Euclidean distance also helped realizing the importance of role which different techniques play in classification problems that bring backs the importance of domain knowledge and data understanding as we got a comparatively low accuracy as compared to Cosine similarity technique. 
While evaluating the results I did realize the importance of cross-validation techniques that are being implemented with classification as well. Even though random sub-sampling worked fine, I had a hint of doubt about it since there could be instances in data which have never been tested. Although the probability of such a thing happening is really low due to the numerous iterations we send our data through but always a probability. I think increasing the number of iterations over the process will help there as that way it would converge to the same result as that of leave k out validation technique.

