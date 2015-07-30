import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
	public static double[] normalize(double[] data, double mean, double stdDev){
		double[] normalizedData = data;

		//Get mean, stdDev, etc for normalization calculations
		int size = data.length;

		for (int i = 0; i < size; i++){
			normalizedData[i] = (data[i] - mean) / stdDev;
		}

		return normalizedData;
	}

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

	public static double getMean(double[] data) {
		double sum = 0;

		// Calculate the mean
		for (double point : data) {
			sum += point;
		}

		return sum / data.length;
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

	private static double getVariance(double[] data) {
		double variance = 0;

		double mean = getMean(data);

		for (double point : data) {
			variance += Math.pow(point - mean, 2);
		}

		variance /= data.length;

		return variance;
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

	public static double getStdDev(double[] data) {
		return Math.sqrt(getVariance(data));
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
	
	// first array of data in data, second is features
	public static double[] getPredictions(double[] coef, double[][] data, double mean, double stdDev) {
    	int NUM_DATA = data.length;
    	int NUM_FEATURES = coef.length;
		
		double[] predictions = new double[NUM_DATA];
    	
    	for (int j = 0; j < NUM_DATA; j++) {
			// multiply each feature of data by its weight, sum, and then put in predictions
			for (int k = 0; k < NUM_FEATURES; k++) {
				// Calculate prediction using linear regression function
				predictions[j] += data[j][k] * coef[k];
			}

			// Un-normalize the prediction
			predictions[j] = (predictions[j] * stdDev) + mean;
		}
    	
    	return predictions;
	}
	
    public static double[] train(double[][] inputs, double[] outputs, double[] theta, double alpha, int numIters) {
        int m = outputs.length;
        int numFeatures = inputs[0].length;
        
        for (int i = 0; i < numIters; i++) {
        	// Calculate predictions
        	double[] predictions = getPredictions(theta, inputs, 0.0, 1.0);
        	
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
