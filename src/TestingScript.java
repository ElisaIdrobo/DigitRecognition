import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Elisa Idrobo
 * CSC 380-AI
 * Handwritten Digit Recognition
 *
 * Tests multiple learning rates and stop criterion
 * Tests multiple sizes of training sets
 * Tests Nearest Neighbor *
 */
public class TestingScript {
	public static void main(String[] args) {
		String labelFile = "train_images\\labels.bin";
		String testFile = "test_paths.txt";
		String trainingPrefix = "training_";
		int trainingSize = 5000;
		BufferedWriter bw = null;

		//find optimal alpha and stop criterion
		double[] alpha = {0.4, 0.6, 0.7,0.75, 0.8, 0.85};
		double[] stopCriterion={0.001,0.005, 0.0001, 0.0003, 0.0005};

		try {
			bw = new BufferedWriter(new FileWriter("neuralNetwork_results.txt"));
		} catch (IOException e) {
			System.err.println("file failed");
			System.exit(1);
		}
		double maxE = -1;
		double maxA = -1;
		double maxS = -1;

		for( int a = 0; a <alpha.length; a++){
			for(int s = 0; s < stopCriterion.length;s++){
				String trainingFile = trainingPrefix + trainingSize + ".txt";
				String networkFile = "nn_"+ trainingSize +"_"+alpha[a]+"_"+stopCriterion[s];
				Train t = new Train(trainingFile,labelFile, alpha[a],stopCriterion[s], networkFile);
				t.train();
				Test te = new Test(testFile, labelFile, networkFile+".csv");
				double performance = (1-te.test())*100;
				System.out.println("Performance: "+ performance+ " %");
				if(performance > maxE){
					maxE = performance;
					maxA= alpha[a];
					maxS = stopCriterion[s];
				}
				try {
					bw.write("alpha: "+alpha[a]+" stopCriterion: "+ stopCriterion[s]+" size: "+ trainingSize+ " performance: "+ performance+"\n");
					System.out.println("---------------------------------------------------------");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//Measure performance with different sizes of training sets
		trainingSize = 0;
		for(int i =1; i <=10; i++){
			trainingSize += 1000;
			String trainingFile = trainingPrefix + trainingSize + ".txt";
			String networkFile = "nn_"+ trainingSize +"_"+maxA+"_"+maxS;
			Train t = new Train(trainingFile,labelFile, maxA,maxS, networkFile);
			t.train();
			Test te = new Test(testFile, labelFile, networkFile+".csv");
			double performance = (1-te.test())*100;
			System.out.println("Performance: "+ performance+ " %");
			try {
				bw.write("alpha: "+maxA+" stopCriterion: "+ maxS+" size: "+ trainingSize+ " performance: "+ performance+"\n");
				System.out.println("---------------------------------------------------------");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
