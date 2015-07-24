import java.io.IOException;
import java.util.List;
import java.util.Random;

import yahoofinance.histquotes.HistoricalQuote;

public class RegressionAlgorithm {
	
	//LinearLearner using Gradient Descent function
	public static double[] LinearLearner(Symbol[] symbolsArray, double learningRate) throws IOException{
		//Inputs:
			//Array of symbols, s0 to sn where n is number of symbols	
			//Learning rate (0 to 1), lets use 0.1
		
		//Number of features
		int numFeatures = 4;
		
		//Array of weights, w0 to wn where n is number of features
		double[] weightsArray = new double[numFeatures + 1];
		
		//Weights randomly initialized from all real numbers
		Random rand = new Random();
		
		for (int i = 0; i < weightsArray.length; i ++){
			//FIX -- Determine range of generated weights
			weightsArray[i] = (double)rand.nextInt(500);
		}
		
		//For each symbol si
		for (Symbol symbol: symbolsArray){
			//Create array of features x0 to xn, by calculating each value for si
			double[] featuresArray = new double[numFeatures];
			//FIX -- manually set values of array: eps, price, volume, ftWkHigh
			//x0 is always initialized to 1
			featuresArray[0] = 1.0;
			featuresArray[1] = symbol.getEPS().doubleValue();
			featuresArray[2] = symbol.getPrice().doubleValue();
			featuresArray[3] = (double)symbol.getVolume();
			featuresArray[4] = symbol.getFtWkHigh().doubleValue();
			
	
			//y (ratio) is stock end value / starting value, thus 1.3 would be 30% increase
			List<HistoricalQuote> historic = symbol.getHistory(1);
			double actualRatio = symbol.getPrice().doubleValue() / historic.get(0).getClose().doubleValue();
			
			//Predicted y is w0 * x0 + w1 * x1 + ... + wn * xn
			double predictedRatio = 0.0;
			for (int i = 0; i < numFeatures; i ++){
				predictedRatio += weightsArray[i] * featuresArray[i];
			}
			
			//DeltaY = actualY - predictedY
			double delta = actualRatio - predictedRatio;
		
			//for a < # features
			for (int i = 0; i < numFeatures; i++){
				//update weights
				//wa = wa + learningRate * deltaY * si.getFeatureA	
				weightsArray[i] = weightsArray[i] + learningRate * delta * featuresArray[i];
			}
			
		}
				
		//Return weights
		return weightsArray;
	}
}
	