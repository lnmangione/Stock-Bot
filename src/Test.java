import java.io.IOException;
import java.util.Arrays;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Test {	
    public static void main(String[] args) throws IOException {
    	int FUTURE_DAYS = 10;
        int NUM_POINTS = 30;
        int DAYS_BACK = NUM_POINTS + FUTURE_DAYS + 1;
    	
    	Symbol[] stocks = {new Symbol("AAPL"), new Symbol("LVS")};
    	
    	// test(stocks, stocks);
    	
    	Symbol[] promiseTest = {new Symbol("F"), new Symbol("APC"), new Symbol("CA"), new Symbol("C"), new Symbol("D"), new Symbol("GAS")};
    	Symbol[] promisingStocks = getPromisingStocks(promiseTest, DAYS_BACK + 1, FUTURE_DAYS, 0.3);
    	
    	Portfolio.simulateTrades(promisingStocks, 10000.0, DAYS_BACK + 1, FUTURE_DAYS);
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
        double[] theta = gd.train(2.0, 10000000);

        
        // Show predictions
        System.out.println(Arrays.toString(theta));
        System.out.println("Cost (Try to minimize): " + gd.getCost(theta));

        for (int i = 0; i < testActual.length; i++) {
            System.out.println("Actual: " + testActual[i]);
			System.out.println("Prediction: " + gd.getPredictions(theta, test)[i]);
        }
    }
    
    public static Symbol[] getPromisingStocks(Symbol[] symbols, int startDaysAgo, int futureDays, double diversity) throws IOException {   	
    	GradientDescent gd = new GradientDescent(symbols, 30, startDaysAgo, futureDays);
    	
    	double[] weights = gd.train(1.0, 1000000);
    
    	
    	//Get price predictions for symbols
    	double[][] data = gd.getData(symbols, 1, startDaysAgo);
    	data = gd.normalize(data);
    	double[] predictedValues = gd.getPredictions(weights, data);
    	
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
