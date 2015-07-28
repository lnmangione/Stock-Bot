import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
	
	public static void main(String[] args) throws IOException {
		Symbol symbol = new Symbol("LVS");
		
		int FUTURE_DAYS = 10;
		int NUM_POINTS = 10;
		int TEST_SET = 20;
		
		double[][] data = new double[NUM_POINTS][symbol.getFeatures(0).length];
		double[] future = new double[NUM_POINTS];
		double[] theta = new double[data[0].length];
		
		// Add features to inputs and future price to outputs
		for (int i = 0; i < NUM_POINTS; i++) {
			data[i] = symbol.getFeatures(i + TEST_SET);
			future[i] = symbol.getAdjClose(i + TEST_SET - FUTURE_DAYS).doubleValue();
		}
		
		theta = getWeights(data, future, theta, .0001, 1000);
		
		int test = 3;
		System.out.println("Actual: " + future[test]);
		System.out.println("Prediction: " + (theta[0] + (theta[1] * data[test][1])));
	}
	
    public static double[] getWeights(double[][] inputs, double[] outputs, double[] theta, double alpha, int numIters) {
        int m = outputs.length;
        int numFeatures = inputs[0].length;
        
        for (int i = 0; i < numIters; i++) {
        	// Calculate predictions
        	double[] predictions = new double[m];
        	
        	for (int j = 0; j < m; j++) {
        		// multiply each feature of data by its weight, sum, and then put in predictions
        		for (int k = 0; k < numFeatures; k++) {
        			predictions[j] += inputs[j][k] * theta[k];
        		}
        	}
        	
        	// Calculate error
        	double[] errorSums = new double[numFeatures];
        	
        	for (int j = 0; j < numFeatures; j++) {
        		for (int k = 0; k < m; k++) {
        			errorSums[j] += (predictions[k] - outputs[k]) * inputs[k][j];
        		}
        	}
        	
        	for (int j = 0; j < numFeatures; j++) {
        		theta[j] -= alpha * (1.0 / m) * errorSums[j];
        	}
        	
        	// System.out.println(Arrays.toString(theta));
        }
        
        return theta;
    }
}
