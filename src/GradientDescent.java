import java.io.IOException;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
	
	public static void main(String[] args) throws IOException {
		Symbol symbol = new Symbol("AAPL");
		
		int FUTURE_DAYS = 10;
		int NUM_POINTS = 200;
		int TEST_SET = 100;
		
		double[][] data = new double[NUM_POINTS][symbol.getFeatures(0).length];
		double[] future = new double[NUM_POINTS];
		double[] theta = new double[data[0].length];
		
		// Add features to inputs and future price to outputs
		for (int i = 0; i < NUM_POINTS; i++) {
			data[i] = symbol.getFeatures(i + TEST_SET);
			future[i] = symbol.getAdjClose(i + TEST_SET - FUTURE_DAYS).doubleValue();
		}
		
		theta = getWeights(data, future, theta, .01, 1000);
		
		System.out.println(theta);
	}
	
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
