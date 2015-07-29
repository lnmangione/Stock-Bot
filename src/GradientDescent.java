import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
	
	// Get set of data with more recent data first
	public static double[][] getData(Symbol[] stocks, int size, int daysAgo) throws IOException {
		double[][] data = new double[size][stocks[0].getFeatures(0).length];
		
		for (Symbol stock : stocks) {
			for (int i = 0; i < size; i++) {
				data[i] = stock.getFeatures(daysAgo + i);
			}
		}
		
		return data;
	}
	
	// Get set of actuals with most recent acutals first
	public static double[] getActual(Symbol[] stocks, int size, int daysAgo, int futureDays) throws IOException {
		double[] actuals = new double[size];
		
		for (Symbol stock : stocks) {
			for (int i = 0; i < size; i++) {
				actuals[i] = stock.getAdjClose(i + daysAgo - futureDays).doubleValue();
			}
		}
		
		return actuals;
	}
	
	// first array of data in data, second is features
	public static double[] getPredictions(double[] coef, double[][] data) {
    	int NUM_DATA = data.length;
    	int NUM_FEATURES = coef.length;
		
		double[] predictions = new double[NUM_DATA];
    	
    	for (int j = 0; j < NUM_DATA; j++) {
    		// multiply each feature of data by its weight, sum, and then put in predictions
    		for (int k = 0; k < NUM_FEATURES; k++) {
    			predictions[j] += data[j][k] * coef[k];
    		}
    	}
    	
    	return predictions;
	}
	
    public static double[] train(double[][] inputs, double[] outputs, double[] theta, double alpha, int numIters) {
        int m = outputs.length;
        int numFeatures = inputs[0].length;
        
        for (int i = 0; i < numIters; i++) {
        	// Calculate predictions
        	double[] predictions = getPredictions(theta, inputs);
        	
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
