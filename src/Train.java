import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Elisa Idrobo
 * CSC 380-AI
 * Handwritten Digit Recognition
 *
 * train a single layer network consisting of 784 input nodes and 1 bias node with
 * 10 output nodes corresponding to digits 0-9
 */
public class Train {
	double learningRate;
	double stopCriterion;
	String trainingSetFile;
	String trainingLabelsFile;
	String networkFilename;
	int numPerceptrons;
	String[] images;
	double[][] trainImages;
	int[] labels;
	NeuralNetwork network;

	/*
	 * Parameters:
	 * double learningRate- learning rate (between 0-1)
	 * double stopCriterion- the amount of weight changes must be below to signal the end of training
	 * String trainingSet- the filename of a file containing the paths to images in training set
	 * String trainingLabels- the filepath to the labels file
	 * String on- the name of an output file. the final weights of the network can be saved to a file.
	 */
	public Train(String trainingSet, String trainingLabels, double lr,double stopCriterion, String on){
		learningRate = lr;
		trainingSetFile = trainingSet;
		trainingLabelsFile = trainingLabels;
		networkFilename = on;
		this.stopCriterion = stopCriterion;
		numPerceptrons = 10;
		int numWeights = 785;
		//read in trainingSetFile and labels
		int start = 0;
		int end = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(trainingSetFile));
			start = Integer.parseInt(br.readLine());
			end = Integer.parseInt(br.readLine());
			br.close();
		} catch (NumberFormatException | IOException e) {
			System.err.println("Training file not found or is in wrong format");
			System.exit(1);
		}
		images = Image.readFilepaths(trainingSetFile);
		trainImages = new double[images.length][numWeights];
		//store images in memory rather than reading in each iteration- much faster!
		for(int i = 0; i < trainImages.length;i++){
			trainImages[i]= Image.readImage(images[i]);
		}
		labels = Image.readLabelList(trainingLabelsFile, start-1, end-1);//0-10,000
		network = new NeuralNetwork(numWeights);
	}

	/*
	 * 2. training loop until weight changes are less than the stopCriterion
	 * 	2a. run through network
	 * 	2b. update weights
	 * 3. print weights of the network to file
	 */
	public void train(){
		int iter = 0;
		System.out.println("Starting training");
		double weightDif = stopCriterion+1;
		while(weightDif > stopCriterion){
			 ++iter;
			weightDif = 0;
			//going through all images once is 1 iteration
			for(int i =0; i < images.length;i++){
				//System.out.println(iter+": "+images[i]);
				//weightDif += learn(Image.readImage(images[i]), labels[i]);
				weightDif += learn(trainImages[i], labels[i]);
			}
			weightDif /= images.length;
			//System.out.println(iter+": Weight difference: "+ weightDif);
		}
		System.out.println("Training Complete with " + iter + " iterations");
		network.printToFile(networkFilename);
	}

	/*
	 * runs a training example and updates the weights of the network
	 * Parameters
	 * inputImage- an array of the values of the image being used
	 * label- the digit in the image
	 * Returns average weight differences of the image after training with it
	 */
	private double learn(double[] inputImage, int label){
		//run image through network
		int numWeights = inputImage.length;
		double weightDif = 0;
		double[] sum = new double[10];
		double[] output = new double[10];
		for(int node = 0; node < 10; node++){
			sum[node] = network.weightedSum(inputImage, node);
			output[node] = network.activationFunction(sum[node]);
		}
		int networkClassification = network.classify(output);
		double error, gPrime;
		int y;
		//update weights
		for(int node = 0; node < 10; node++){
			y = 0;
			if(label == node)
				y = 1;
			error = network.error(y, output[node]);
			gPrime = gPrime(sum[node]);
			for(int w = 0; w < numWeights; w++){
				double oldWeight = network.getWeight(node, w);
				double newWeight = learnWeight(inputImage[w], gPrime, error, oldWeight);
				network.setWeight(node, w, newWeight);
				weightDif += Math.abs(newWeight-oldWeight);
			}
		}
		//calculate avg. difference and return
		return weightDif/(numWeights*10.0);
	}
	
	/*
	 * g'(x) = e^-x / 1+ e^-x
	 * Parameters:
	 * weightedSum- the input into the function
	 * returns g'(weightedSum)
	 */
	private double gPrime(double weightedSum){
		double temp = Math.pow(Math.E, -1*weightedSum);
		return temp/Math.pow(1+temp, 2);
	}

	/*
	 * Weight_new = Weight_old + learningRate * error * g'(in) * input
	 */
	private double learnWeight(double input,double gPrime,double error, double currentWeight ){
		double newWeight = currentWeight + (learningRate * error * gPrime * input);
		return newWeight;
	}
	/**
	 * @return the learningRate
	 */
	public double getLearningRate() {
		return learningRate;
	}
	/**
	 * @param learningRate the learningRate to set
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	/**
	 * @return the trainingSetFile
	 */
	public String getTrainingSetFile() {
		return trainingSetFile;
	}
	/**
	 * @param trainingSetFile the trainingSetFile to set
	 */
	public void setTrainingSetFile(String trainingSetFile) {
		this.trainingSetFile = trainingSetFile;
	}
	/**
	 * @return the networkName
	 */
	public String getNetworkFilename() {
		return networkFilename;
	}
	/**
	 * @param networkName the networkName to set
	 */
	public void setNetworkFilename(String networkName) {
		this.networkFilename = networkName;
	}
}
