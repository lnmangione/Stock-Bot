import java.io.IOException;
import java.util.List;
import java.util.Random;

import yahoofinance.histquotes.HistoricalQuote;

public class RegressionAlgorithm {
	
	//LinearLearner using Gradient Descent function
	public static double[] linearLearner(Symbol[] symbolsArray, double learningRate) throws IOException{
		//Inputs:
			//Array of symbols, s0 to sn where n is number of symbols	
			//Learning rate (0 to 1), lets use 0.1

		//Number of features
		int numFeatures = 3;

		//Array of weights, w0 to wn where n is number of features
		double[] weightsArray = new double[numFeatures + 1];

		//Weights randomly initialized from all real numbers
		Random rand = new Random();

		for (int i = 0; i < weightsArray.length; i ++){
			//FIX -- Determine range of generated weights
			weightsArray[i] = (double)1.0;
		}


		//For each symbol si
		for (Symbol symbol: symbolsArray){
			//Create array of features x0 to xn, by calculating each value for si
			double[] featuresArray = symbol.getFeatures(0);

			//y (ratio) is stock end value / starting value, thus 1.3 would be 30% increase
			double actualY = symbol.getPrice().doubleValue();

			//Update weights 50 times
			for (int i = 0; i < 50; i++){

				//Predicted y is w0 * x0 + w1 * x1 + ... + wn * xn
				double predictedY = 0.0;
				for (int b = 0; b < numFeatures + 1; b ++){
					predictedY += weightsArray[b] * featuresArray[b];
				}

				//DeltaY = actualY - predictedY
				double delta = actualY - predictedY;

				System.out.print("Actual: " + actualY + ", Predicted: " + predictedY + ", Delta: " + delta + "\n");

				//for a < # features
				for (int a = 0; a < numFeatures + 1; a++){
					//update weights
					//wa = wa + learningRate * deltaY * si.getFeatureA	
					weightsArray[a] = weightsArray[a] + learningRate * delta / featuresArray[a];
					//System.out.print("\n Weight: " + weightsArray[a]);
				}

			}

		}

		//Return weights
		return weightsArray;
	}

	public static double predict1MonthRatio(Symbol symbol, double[] weightsArray) throws IOException{
	//FIX -- manually set values of array: eps, price, volume, ftWkHigh
			double[] featuresArray = symbol.getFeatures(0);

		double predictedY = 0.0;
		for (int i = 0; i < 4; i ++){
			predictedY += weightsArray[i] * featuresArray[i];

			System.out.print(weightsArray[i] + ", " + featuresArray[i]);		
		}


		System.out.print("Predicted: " + predictedY + ", Actual: " + symbol.getPrice());

		return predictedY;
	}
}


