import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent extends Algorithm {
    public GradientDescent(Symbol[] trainStocks, int NUM_POINTS, int DAYS_BACK, int FUTURE_DAYS) throws IOException {
		super(trainStocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);
    }
	
	// first array of data in data, second is features
	private static double[] getPredictions(double[] coef, double[][] data, double mean, double stdDev) {
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
	
	public double[] getPredictions(double[] coef, double[][] data) {
		return getPredictions(coef, data, actualMean, actualStdDev);
	}
	
	public double getCost(double[] theta) {
        int size = trainActual.length;

        double[] predictions = getPredictions(theta, train, 0, 1);

        double sumErrors = 0;

        for (int i = 0; i < size; i++) {
            sumErrors += Math.pow(predictions[i] - trainActual[i], 2);
        }

        return (1.0 / (2 * size)) * sumErrors;
    }
	
    public double[] train(double alpha, int numIters) {
    	double[] theta = new double[train[0].length];
    	
    	int m = trainActual.length;
        int numFeatures = train[0].length;
        
        for (int i = 0; i < numIters; i++) {
        	// Calculate predictions
        	double[] predictions = getPredictions(theta, train, 0.0, 1.0);
        	
        	// Calculate error
        	double[] errorSums = new double[numFeatures];
        	
        	for (int j = 0; j < numFeatures; j++) {
        		for (int k = 0; k < m; k++) {
        			errorSums[j] += (predictions[k] - trainActual[k]) * train[k][j];
        		}
        	}
        	
        	for (int j = 0; j < numFeatures; j++) {
        		theta[j] -= alpha * (1.0 / m) * errorSums[j];
        	}
        	
        	// System.out.println(Arrays.toString(theta));
        	System.out.println(getCost(theta));
        }
        
        return theta;
    }
}
