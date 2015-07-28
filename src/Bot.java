import java.io.IOException;
import java.util.Scanner;

public class Bot {
	static final String ds = "$";
	
	public static void main(String[] args) throws IOException {
		Symbol symbol;
		Scanner sc = new Scanner(System.in);
		
		/*
		// Get a stock symbol to check
		System.out.print("Stock: ");
		String stock = sc.next();
		symbol = new Symbol(stock);
		*/

		symbol = new Symbol("AAPL");

		/*
		System.out.println("Symbol: " + symbol.getSymbol());
		System.out.println("Price: " + ds + symbol.getPrice());
		// System.out.println("Shares: " + symbol.getNumberOfShares());
		System.out.println("Moving Average: " + symbol.getMA(0, 200));
		System.out.println("Moving Average: " + symbol.getMA(0, 50));
		System.out.println("Closing Price: " + symbol.getAdjClose(30));
		*/


		Symbol[] trainingSet = new Symbol[5];
		trainingSet[0] = new Symbol("^GSPC");
		trainingSet[1] = new Symbol("GOOGL");
		trainingSet[2] = new Symbol("ACE");
		trainingSet[3] = new Symbol("HD");
		trainingSet[4] = new Symbol("HP");

		double[] weightsArray = RegressionAlgorithm.linearLearner(trainingSet, 7, 0.1);
		RegressionAlgorithm.predictY(new Symbol("AAPL"), 10, weightsArray);

		sc.close();
	}
}
