import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/28/15.
 * first dimension of inputs is the data point, second dimension is the feature number
 */
public class GradientDescent {
    private Features features;
    
    double[][] train;
    double[] trainActual;
    
    private double[] mean;
    private double[] stdDev;
    private double actualMean;
    private double actualStdDev;
    
    public GradientDescent(Symbol[] trainStocks, int NUM_POINTS, int DAYS_BACK, int FUTURE_DAYS) throws IOException {
    	features = new Features();
    	
    	 train = getData(trainStocks, NUM_POINTS, DAYS_BACK);
         trainActual = getActual(trainStocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);
         
         // Get data mean and standard deviation
         mean = getMean(train);
         stdDev = getStdDev(train);
         
         // Normalize training data
         train = normalize(train);

         // Get actual mean and standard deviation
         actualMean = getMean(trainActual);
         actualStdDev = getStdDev(trainActual);

         // Normalize training actuals
         trainActual = normalize(trainActual);
    }
    
	public double[] normalize(double[] data){
		double[] normalizedData = data;

		//Get mean, stdDev, etc for normalization calculations
		int size = data.length;

		for (int i = 0; i < size; i++){
			normalizedData[i] = (data[i] - actualMean) / actualStdDev;
		}

		return normalizedData;
	}

	public double[][] normalize(double[][] data){
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
	
	// Get set of data with more recent data first
   public double[][] getData(Symbol[] stocks, int size, int daysAgo) throws IOException {
        int numStocks = stocks.length;

        double[][] data = new double[numStocks * size][features.getFeatures(stocks[0], 0).length];

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < size; j++) {
                data[(i * size) + j] = features.getFeatures(stocks[i], j + daysAgo);
            }
        }

        return data;
    }

    // Get set of actuals with most recent actuals first
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
	
	public static double[] getPredictions(double[] coef, double[][] data) {
		return getPredictions(coef, data);
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
        	// System.out.println(getCost(theta));
        }
        
        return theta;
    }
}
