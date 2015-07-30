import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

public class Bot {
	static final String ds = "$";
	
	public static void main(String[] args) throws IOException {
		Symbol stock = new Symbol("GOOG");
		
		List<HistoricalQuote> history = stock.getHistory(10, 1000);
		
		for (HistoricalQuote quote : history) {
			System.out.println(quote.getAdjClose());
		}
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
