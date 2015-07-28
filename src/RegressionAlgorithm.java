import java.io.IOException;
import java.util.List;
import java.util.Random;

import yahoofinance.histquotes.HistoricalQuote;

public class RegressionAlgorithm {
	
	//LinearLearner using Gradient Descent function
	public static double[] linearLearner(Symbol[] symbolsArray, int daysAgo, double learningRate) throws IOException{
		//Inputs:
			//Array of symbols, s0 to sn where n is number of symbols	
			//Learning rate (0 to 1), lets use 0.1

		//Number of features
		int numFeatures = symbolsArray[0].getFeatures(daysAgo).length;

		//Array of weights, w0 to wn where n is number of features
		double[] weightsArray = new double[numFeatures];

		//Weights randomly initialized from all real numbers
		Random rand = new Random();

		for (int i = 0; i < weightsArray.length; i ++){
			//FIX -- Determine range of generated weights
			weightsArray[i] = (double)1.0;
		}


		//Update weights 500 times
		for (int i = 0; i < 500; i++){

			//For each symbol si
			for (Symbol symbol: symbolsArray){
				//Create array of features x0 to xn, by calculating each value for si
				double[] featuresArray = symbol.getFeatures(daysAgo);

				//y (ratio) is stock end value / starting value, thus 1.3 would be 30% increase
				double actualY = symbol.getPrice().doubleValue();


				//for a < # features
				for (int a = 0; a < numFeatures; a++){
					//Predicted y is w0 * x0 + w1 * x1 + ... + wn * xn
					double predictedY = 0.0;
					for (int b = 0; b < numFeatures; b ++){
						predictedY += weightsArray[b] * featuresArray[b];
					}
					//DeltaY = actualY - predictedY
					double delta = predictedY - actualY;
					
					double derivative = 0.0;
					for (int b = 0; b < numFeatures; b ++){
						derivative += delta * featuresArray[b];
					}
					derivative *= 1.0 / (numFeatures);
					
					//update weights
					weightsArray[a] = weightsArray[a] - learningRate * derivative;
				}

			}

		}

		//Return weights
		return weightsArray;
	}

	public static double predictY(Symbol symbol, int daysAgo, double[] weightsArray) throws IOException{
		double[] featuresArray = symbol.getFeatures(daysAgo);

		double predictedY = 0.0;
		for (int i = 0; i < featuresArray.length; i ++){
			predictedY += weightsArray[i] * featuresArray[i];		
		}

		return predictedY;
	}
	
	public static double computeCost(Symbol[] symbolsArray, int daysAgo, double[] weightsArray) throws IOException{
		double totalError = 0.0;
		for (Symbol symbol: symbolsArray){
			totalError += Math.pow(symbol.getPrice().doubleValue() - predictY(symbol, daysAgo, weightsArray), 2);
		}		
		return totalError / symbolsArray.length;
	}
}


