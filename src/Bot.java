import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bot {
	static final String ds = "$";
	
	public static void main(String[] args) throws IOException {
		/*
		Symbol symbol;
		Scanner sc = new Scanner(System.in);
		
		
		// Get a stock symbol to check
		System.out.print("Stock: ");
		String stock = sc.next();
		symbol = new Symbol(stock);
		

		symbol = new Symbol("AAPL");


		System.out.println("Symbol: " + symbol.getSymbol());
		System.out.println("Price: " + ds + symbol.getPrice());
		// System.out.println("Shares: " + symbol.getNumberOfShares());
		System.out.println("Moving Average: " + symbol.getMA(0, 200));
		System.out.println("Moving Average: " + symbol.getMA(0, 50));
		// System.out.println("Closing Price: " + symbol.getAdjClose(30));
		*/

		
		Symbol[] trainingSet = getRandomStocks(5);

		double[] weightsArray = RegressionAlgorithm.linearLearner(trainingSet, 10, 0.0001);
		
		Symbol appleSymbol = new Symbol("AAPL");
		double prediction = RegressionAlgorithm.predictY(appleSymbol, 10, weightsArray);
		System.out.print("Actual: " + appleSymbol.getPrice().doubleValue() + ", Predicted: " + prediction);

		//sc.close();
	}
	
	private static Symbol[] getRandomStocks(int num) throws IOException {
		String fileName = "stocks.txt";
		String line = null;
		FileReader fileReader;
		ArrayList<String> stocks = new ArrayList<String>();
		Symbol[] randomStocks = new Symbol[num];

		try { // Gets all stocks and adds them to ArrayList<String> stocks
			fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				stocks.add(line);
			}
			bufferedReader.close();
		} catch (Exception e) {}

		int stocksSize = stocks.size();

		//NOTE - This method may return duplicates
		for (int f = 0; f < num; f++) { // Adds 30 random stocks to Symbol[]
			// randomStocks
			int random = (int) (Math.random() * (stocksSize));
			randomStocks[f] = new Symbol(stocks.remove(random));
		}
		return randomStocks;
	}
}
