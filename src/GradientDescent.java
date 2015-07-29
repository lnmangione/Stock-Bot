import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
	public static double[][] normalize(double[][] data, double[] mean, double[] stdDev){
		double[][] normalizedData = data;
		
		//Get mean, stdDev, etc for normalization calculations
		int numFeatures = data[0].length;
		
		for (int feature = 1; feature < numFeatures; feature ++){
			for (int point = 0; point < data.length; point ++){
				normalizedData[point][feature] = (data[point][feature] - mean[feature]) / stdDev[feature];
			}
		}
		return normalizedData; 
	}
	
	public static double[] getMean(double[][] data) {
		int numFeatures = data[0].length;
		
		double[] means = new double[numFeatures];
		
		// Calculate the mean
		for (int i = 0; i < numFeatures; i++) {
			// Sum the values for each feature
			for (double[] point : data) {
				means[i] += point[i];
			}
			
			// Divide by the amount of data points
			means[i] /= data.length;
		}
		
		return means;
	}
	
	private static double[] getVariance(double[][] data) {
		int numFeatures = data[0].length;
		
		double[] variance = new double[numFeatures];
		
		double[] mean = getMean(data);
		
		// Calculate the variance for each feature
		for (int i = 0; i < numFeatures; i++) {
			// Sum the squares of the difference from mean
			for (double[] point : data) {
				variance[i] += Math.pow((point[i] - mean[i]), 2);
			}
			
			// Divide by the amount of data points
			variance[i] /= data.length;
		}
		
		return variance;
	}
	
	public static double[] getStdDev(double[][] data) {		
		// Set the standard deviations to the variances
		double[] StdDev = getVariance(data);
		
		// Now square root the variances
		for (double variance : StdDev) {
			variance = Math.sqrt(variance);
		}
		
		return StdDev;
	}
	
	// Get set of data with more recent data first
	public static double[][] getData(Symbol[] stocks, int size, int daysAgo) throws IOException {
		int numStocks = stocks.length;
		
		double[][] data = new double[numStocks * size][stocks[0].getFeatures(0).length];
		
		for (int i = 0; i < numStocks; i++) {
			for (int j = 0; j < size; j++) {
				data[(i * size) + j] = stocks[i].getFeatures(j + daysAgo);
			}
		}
		
		return data;
	}
	
	// Get set of actuals with most recent acutals first
	public static double[] getActual(Symbol[] stocks, int size, int daysAgo, int futureDays) throws IOException {
		int numStocks = stocks.length;
		
		double[] actuals = new double[numStocks * size];
		
		for (int i = 0; i < numStocks; i++) {
			for (int j = 0; j < size; j++) {
				actuals[(i * size) + j] = stocks[i].getAdjClose(j + daysAgo - futureDays).doubleValue();
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
