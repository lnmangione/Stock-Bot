import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Test {
	private Symbol[] trainStocks;
	private Symbol[] testStocks;

    private Features features;
	
    public static void main(String[] args) throws IOException {
    	Symbol[] stocks = {new Symbol("AAPL"), new Symbol("LVS"), new Symbol("GOOG")};
    	
    	Test test = new Test(stocks, stocks);
    	test.test();
    }
    
    public Test (Symbol[] trainStocks, Symbol[] testStocks) throws IOException{
        features = new Features();

        this.trainStocks = trainStocks;
    	this.testStocks = testStocks;
    }

    private void test() throws IOException {
        int FUTURE_DAYS = 10;
        int NUM_POINTS = 30;
        int DAYS_BACK = NUM_POINTS + FUTURE_DAYS + 1;

        // Get training data
        double[][] train = getData(trainStocks, NUM_POINTS, DAYS_BACK);
        double[] trainActual = getActual(trainStocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);
        
        // Get data mean and standard deviation
        double[] mean = GradientDescent.getMean(train);
        double[] stdDev = GradientDescent.getStdDev(train);
        
        // Normalize training data
        train = GradientDescent.normalize(train, mean, stdDev);

        // Get actual mean and standard deviation
        double actualMean = GradientDescent.getMean(trainActual);
        double actualStdDev = GradientDescent.getStdDev(trainActual);

        // Normalize training actuals
        trainActual = GradientDescent.normalize(trainActual, actualMean, actualStdDev);


        // Get test data
        double[][] test = getData(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS);
        double[] testActual = getActual(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS, FUTURE_DAYS);
        
        // Normalize test data using training mean and standard deviation
        test = GradientDescent.normalize(test, mean, stdDev);


        // Train
        double[] theta = new double[train[0].length];

        theta = GradientDescent.train(train, trainActual, theta, .4, 10000000);

        // System.out.println(Arrays.toString(theta));
        // System.out.println("Cost (Try to minimize): " + getCost(train, trainActual, theta));

        for (int i = 0; i < testActual.length; i++) {
            System.out.println("Actual: " + testActual[i]);
            System.out.println("Prediction: " + GradientDescent.getPredictions(theta, test, actualMean, actualStdDev)[i]);
        }
        
        Symbol[] promiseTest = {new Symbol("F"), new Symbol("APC"), new Symbol("CA"), new Symbol("C"), new Symbol("D"), new Symbol("GAS")};
        getPromisingStocks(promiseTest, theta, mean, stdDev, actualMean, actualStdDev, DAYS_BACK + 1, 0.3);
    }

    private static double getCost(double[][] data, double[] actual, double[] theta) {
        int size = actual.length;

        double[] predictions = GradientDescent.getPredictions(theta, data, 0, 1);

        double sumErrors = 0;

        for (int i = 0; i < size; i++) {
            sumErrors += Math.pow(predictions[i] - actual[i], 2);
        }

        return (1.0 / (2 * size)) * sumErrors;
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

    // Get set of actuals with most recent acutals first
    public double[] getActual(Symbol[] stocks, int size, int daysAgo, int futureDays) throws IOException {
        int numStocks = stocks.length;

        double[] actuals = new double[numStocks * size];

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < size; j++) {
                actuals[(i * size) + j] = stocks[i].getAdjClose(j + daysAgo - futureDays).doubleValue();
            }
        }

        return actuals;
    }
    
    public Symbol[] getPromisingStocks(Symbol[] symbols, double[] weights, double[] dataMean, double[] dataStdDev, double actualMean, double actualStdDev, int startDaysAgo, double diversity) throws IOException {
    	//Get price predictions for symbols
    	double[][] data = getData(symbols, 1, startDaysAgo);
    	data = GradientDescent.normalize(data, dataMean, dataStdDev);
    	double[] predictedValues = GradientDescent.getPredictions(weights, data, actualMean, actualStdDev);
    	
		double[] priceRatios = new double[predictedValues.length];
		for (int i = 0; i < symbols.length; i++){
			//We wouldn't necessarily be selling the stock at the exact closing price, but its our best estimation to selling price
			priceRatios[i] = predictedValues[i] / symbols[i].getAdjClose(startDaysAgo).doubleValue();
		}
		
		//Rank sell/buy ratios
		int[] rankedRatioIndices = new int[priceRatios.length];
		double[] tempRatios = new double[priceRatios.length];
		
		//Java was passing by value, so I had to manually copy values into the temporary array
		for (int i = 0; i < priceRatios.length; i++){
			tempRatios[i] = priceRatios[i];
		}
		
		for (int i = 0; i < tempRatios.length; i++){
			double max = 0.0;
			int indexOfMax = 0;
			for (int a = 0; a < tempRatios.length; a++){
				if (tempRatios[a] > max){
					max = tempRatios[a];
					indexOfMax = a;
				}
			}
			
			tempRatios[indexOfMax] = 0.0;
			rankedRatioIndices[i] = indexOfMax;
		}
		
		//Now rankedRatioIndices stores the indices of promising stocks
		//Only take certain percentage of most promising stocks using diversity value
		int numPromising = (int)(diversity * (double)symbols.length + 0.5);
		Symbol[] promisingStocks = new Symbol[numPromising];
		for (int i = 0; i < numPromising; i++){
			promisingStocks[i] = symbols[rankedRatioIndices[i]];
		}
		
		return promisingStocks;
	}
}
