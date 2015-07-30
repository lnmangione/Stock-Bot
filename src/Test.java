import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Test {	
    public static void main(String[] args) throws IOException {
    	Symbol[] stocks = {new Symbol("AAPL"), new Symbol("LVS"), new Symbol("GOOG")};
    	
    	test(stocks, stocks);
    }

    private static void test(Symbol[] trainStocks, Symbol[] testStocks) throws IOException {
        int FUTURE_DAYS = 10;
        int NUM_POINTS = 30;
        int DAYS_BACK = NUM_POINTS + FUTURE_DAYS + 1;
        
        GradientDescent gd = new GradientDescent(trainStocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);

        // Get test data
        double[][] test = gd.getData(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS);
        double[] testActual = GradientDescent.getActual(testStocks, NUM_POINTS, DAYS_BACK + NUM_POINTS, FUTURE_DAYS);
        
        // Normalize test data using training mean and standard deviation
        test = gd.normalize(test);


        // Train
        double[] theta = gd.train(.4, 10000000);

        
        // Show predictions
        System.out.println(Arrays.toString(theta));
        // System.out.println("Cost (Try to minimize): " + gd.getCost(theta));

        for (int i = 0; i < testActual.length; i++) {
            System.out.println("Actual: " + testActual[i]);
            System.out.println("Prediction: " + gd.getPredictions(theta, test)[i]);
        }
    }
}
