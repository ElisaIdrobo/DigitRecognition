import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Elisa Idrobo
 * CSC 380-AI
 * Handwritten Digit Recognition
 *
 * This class is used for reading in images, labels and
 * lists of filepaths used to create testing and training sets
 *
 */
public class Image {

	/*
	 * Parameters:
	 * fname- the path of the image file
	 * returns an array of normalized(between 0 and 1) values of an image,
	 * with a -1 added onto the end.
	 */
	public static double[] readImage(String fname){
		double[] image;
		try {
		    FileInputStream fis = new FileInputStream(fname);
		    DataInputStream dis = new DataInputStream(fis);
		    int height = dis.readInt();
		    int width = dis.readInt();
		    image = new double[width*height + 1];
		    for (int i=0; i<width*height; i++) {
			  image[i] = (dis.readInt()/255.0);
		    }
		    image[image.length-1] = -1;
		    fis.close();
		    dis.close();
		} catch(IOException e) {
		    System.err.println("Error reading file " + fname);
		    return null;
		}
		return image;
	}
	/*
	 * Parameters:
	 * fname- the path to the labels file
	 * startline- the number of the first image the label will correspond to
	 * endLine- the number of the last image the labels will correspond to
	 * assumes that all images are in the same order as the labels file
	 * returns the labels of a label file in the specified range
	 */
	public static int[] readLabelList(String fname, int startLine, int endLine){
		int[] subsetOfLabels;
		try {
			int[] labels;
		    FileInputStream fis = new FileInputStream(fname);
		    DataInputStream dis = new DataInputStream(fis);
		    int height = dis.readInt();
		    int width = dis.readInt();
		    int nlabels = width*height;
		    if (width != 1) {
		    	System.err.println("Doesn't look like a list " + fname);
				return null;
		    }
		    labels = new int[nlabels];
		    for (int i=0; i<width*height; i++) {
			labels[i] = dis.readInt();
		    }
		    fis.close();
		    dis.close();
		    //pull out labels in given range
		    subsetOfLabels = new int[endLine - startLine];
			for(int i =0; i < subsetOfLabels.length;i++){
				subsetOfLabels[i] = labels[i+startLine];
			}
		} catch(IOException e) {
		    System.err.println("Error reading file " + fname);
		    return null;
		}
		return subsetOfLabels;
	}
	
	/*
	 * returns a list of filepaths in the file with the given filepath
	 */
	public static String[] readFilepaths(String filename){
		String[] list;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int s = Integer.parseInt(br.readLine());
			int e = Integer.parseInt(br.readLine());
			int n = e-s;
			list = new String[n];
			for(int i =0; i < n;i++){
				list[i] = br.readLine();
			}
			br.close();
			return list;
		} catch (NumberFormatException | IOException e) {
			System.err.println("Training/Test file not found or is in wrong format");
			System.exit(1);
		}
		return null;
	}

	/*
	 * prints the values in the given array
	 */
	public static void printImage(int[] image) {
		System.out.print("\n");
		for (int i=0; i<784; i++) {
		    if (i % 28==0) System.out.print("\n");
		    System.out.print(image[i]+ " \t");
		}
		System.out.print("\n\n");
	    }
}
