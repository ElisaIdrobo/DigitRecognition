import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Elisa Idrobo
 * CSC 380-AI
 * Handwritten Digit Recognition
 *
 * Trains and tests a neural network.
 * Interface command line arguments:
 * labelFile- filepath to the label file
 * trainingFile- filepath to a file with the paths of the training images
 * testFile- filepath to a file with the paths of the test images
 * networkFile- the name of the file to save the network weights to
 */
public class DigitRecognition {
	public static void main(String[] args) {
		String labelFile = args[0];
		String trainingFile = args[1];
		String testFile = args[2];
		String networkFile = "nn_a0.75_s0.0001";
		if(args.length ==4){
			networkFile = args[3];
		}

		System.out.println("Neural Network");
		Train t = new Train(trainingFile,labelFile, 0.75,0.0001, networkFile);
		t.train();
		Test te = new Test(testFile, labelFile, networkFile+".csv");
		double performance = (1-te.test())*100;
		System.out.println("Neural network performance: "+ performance+ " %");


	}
}
