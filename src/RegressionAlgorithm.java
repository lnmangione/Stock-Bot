import java.io.IOException;

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
		for (int i = 0; i < weightsArray.length; i ++){
			//FIX -- Determine range of generated weights
			weightsArray[i] = (double)0.0;
		}

		//Update weights 500 times
		for (int i = 0; i < 500; i++){
			
			double[] tempWeights = new double[numFeatures];

			//for a < # features
			for (int feature = 0; feature < numFeatures; feature++){
				double sum = 0.0;
				for (Symbol symbol: symbolsArray){
					sum += (predictY(symbol, daysAgo, weightsArray) - symbol.getPrice().doubleValue()) * symbol.getFeatures(daysAgo)[feature];
					System.out.print("Sum: " + sum + ", FVal: " + symbol.getFeatures(daysAgo)[feature] + "\n");
				}
				
				//DEBUGGING
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.print("\ntempWeights[" + feature+ "] = " + weightsArray[feature] + " - " + (learningRate / symbolsArray.length) * sum + ";\n");
				System.out.print("AKA: tempWeights[" + feature + "] = " + weightsArray[feature] + " - " + (learningRate / symbolsArray.length) + " * " + sum + ";\n\n");
				System.out.print("-----------------------------\n");
				//update weights
				tempWeights[feature] = weightsArray[feature] - (learningRate / symbolsArray.length) * sum;	
			}
			
			weightsArray = tempWeights;
		}

		//Return weights
		return weightsArray;
	}

	public static double predictY(Symbol symbol, int daysAgo, double[] weightsArray) throws IOException {
		double[] featuresArray = symbol.getFeatures(daysAgo);

		double predictedY = 0.0;
		for (int i = 0; i < featuresArray.length; i ++) {
			predictedY += weightsArray[i] * featuresArray[i];		
		}

		//System.out.print("Actual: " + symbol.getPrice().doubleValue() +", Predicted: " + predictedY + "\n");

		return predictedY;
	}

	public static double computeCost(Symbol[] symbolsArray, int daysAgo, double[] weightsArray) throws IOException {
		double totalError = 0.0;
		for (Symbol symbol: symbolsArray){
			totalError += Math.pow(symbol.getPrice().doubleValue() - predictY(symbol, daysAgo, weightsArray), 2);
		}		
		return totalError / symbolsArray.length;
	}
}
