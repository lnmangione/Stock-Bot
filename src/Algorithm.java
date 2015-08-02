import java.io.IOException;

/**
 * Created by MichaelBick on 8/2/15.
 */
public abstract class Algorithm {
    protected Features features;

    protected double[][] train;
    protected double[] trainActual;

    protected double[] mean;
    protected double[] stdDev;
    protected double actualMean;
    protected double actualStdDev;

    public Algorithm(Symbol[] trainStocks, int NUM_POINTS, int DAYS_BACK, int FUTURE_DAYS) throws IOException {
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

    public abstract double[] getPredictions(double[] coef, double[][] data);

    public abstract double[] train(double alpha, int numIters);
}
