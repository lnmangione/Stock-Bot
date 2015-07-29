import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Test {
	private Symbol[] trainStocks;
	private Symbol[] testStocks;
	
    public static void main(String[] args) throws IOException {
    	Symbol[] stocks = {new Symbol("AAPL")};
    	
    	Test test = new Test(stocks, stocks);
    	test.test();
    }
    
    public Test (Symbol[] trainStocks, Symbol[] testStocks) {
    	this.trainStocks = trainStocks;
    	this.testStocks = testStocks;
    }

    private void test() throws IOException {
        int FUTURE_DAYS = 10;
        int NUM_POINTS = 20;
        int DAYS_BACK = 100;

        // Get training data
        double[][] train = GradientDescent.getData(trainStocks, NUM_POINTS, DAYS_BACK);
        double[] trainActual = GradientDescent.getActual(trainStocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);
        
        // Get data mean and standard deviation
        double[] mean = GradientDescent.getMean(train);
        double[] stdDev = GradientDescent.getStdDev(train);
        
        // Normalized training data
        train = GradientDescent.normalize(train, mean, stdDev);

        // Get test data
        double[][] test = GradientDescent.getData(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS);
        double[] testActual = GradientDescent.getActual(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS, FUTURE_DAYS);
        
        // Normalize test data using training mean and standard deviation
        test = GradientDescent.normalize(test, mean, stdDev);

        double[] theta = new double[train[0].length];

        theta = GradientDescent.train(train, trainActual, theta, .01, 1000);
        
       //  System.out.println(Arrays.toString(theta));

        for (int i = 0; i < NUM_POINTS; i++) {
            System.out.println("Actual: " + testActual[i]);
            System.out.println("Prediction: " + GradientDescent.getPredictions(theta, test)[i]);
        }
    }
}
