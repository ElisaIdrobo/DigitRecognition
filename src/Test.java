import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Elisa Idrobo
 * CSC 380-AI
 * Handwritten Digit Recognition
 *
 * Classify test examples with a neural network
 *
 */
public class Test {

	String testSetFile;
	NeuralNetwork network;
	String labelsFile;

	/*
	 * Constructor.
	 * Parameters
	 * testSetFile- the path of the file with the test set paths
	 * labelsFile- filepath of the labels file
	 * networkFilename- filepath of the file with the saved network
	 */
	public Test(String testSetFile,String labelsFile, String networkFilename) {
		this.testSetFile = testSetFile;
		this.labelsFile = labelsFile;
		network = new NeuralNetwork(networkFilename);
	}

	/*
	 * 1. read in weights/create neural net
	 * 2. read in test images
	 * 3. for each run through network
	 */
	public double test(){
		int start = 0;
		int end = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(testSetFile));
			start = Integer.parseInt(br.readLine());
			end = Integer.parseInt(br.readLine());
			br.close();
		} catch (NumberFormatException | IOException e) {
			System.err.println("Testing file not found or is in wrong format");
			System.exit(1);
		}
		String[] images = Image.readFilepaths(testSetFile);
		int[] labels = Image.readLabelList(labelsFile, start-1, end-1);//0-10,000
		System.out.println("Starting testing");
		int error = 0;
		for(int i =0; i < images.length;i++){
			double[] input = Image.readImage(images[i]);
			int label = labels[i];
			int classification = network.h(input);
			if(label != classification)
				error++;
		}
		double errorRate = (double)error/images.length;
		System.out.println("Testing complete.\nError rate: " + errorRate);
		return errorRate;

	}

	/**
	 * @return the testSetFile
	 */
	public String getTestSetFile() {
		return testSetFile;
	}

	/**
	 * @param testSetFile the testSetFile to set
	 */
	public void setTestSetFile(String testSetFile) {
		this.testSetFile = testSetFile;
	}
	
}
