/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
    public static double[] getWeights(double[][] inputs, double[] outputs, double[] theta, double alpha, int numIters) {
        int m = outputs.length;
        int numPoints = inputs[0].length;
        
        for (int i = 0; i < numIters; i++) {
        	// Calculate predictions
        	double[] predictions = new double[numPoints];
        	
        	for (int j = 0; j < numPoints; j++) {
        		// multiply each feature of data by its weight, sum, and then put in predictions
        		for (int k = 0; k < inputs[0].length; k++) {
        			predictions[j] += inputs[j][k] * theta[k];
        		}
        	}
        	
        	// Calculate error
        	double[] errorSums = new double[m];
        	
        	for (int j = 0; j < m; j++) {
        		for (int k = 0; k < numPoints; k++) {
        			errorSums[j] += (predictions[k] - outputs[k]) * inputs[k][j];
        		}
        	}
        	
        	for (int j = 0; j < m; j++) {
        		theta[j] -= alpha * (1.0 / m) * errorSums[j];
        	}
        }
        
        return theta;
    }
}
