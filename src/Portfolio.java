import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {
	public static void simulateTrades(Symbol[] stocks, double balance, int buyDaysAgo, int futureDays) throws IOException{
		//Buy stocks
		Map <String, Integer> boughtShares = buyShares(stocks, balance, buyDaysAgo);
		
		//Update balance based on stocks bought
		for (Map.Entry<String, Integer> entry : boughtShares.entrySet()) {
			double shareValue = (new Symbol(entry.getKey()).getDay(buyDaysAgo).getAdjClose().doubleValue());
			balance -=  shareValue * entry.getValue();
		}
		
		
		System.out.print("Buying stocks " + buyDaysAgo + " days ago\n");
		double portfolioValue = getPortfolioValue(boughtShares, balance, buyDaysAgo);
		System.out.print("\n===========================\n\n");
		
		System.out.print("Value at end date (" + (buyDaysAgo - futureDays) + " days ago)\n");
		double endPortfolioValue = getPortfolioValue(boughtShares, balance, buyDaysAgo - futureDays);

	}
	
	public static double getPortfolioValue(Map <String, Integer> stocks, double balance, int daysAgo) throws IOException{
		double portfolioValue = 0.0;
		portfolioValue += balance;
		
		//Update value based on stocks bought
		for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
			double shareValue = (new Symbol(entry.getKey()).getDay(daysAgo).getAdjClose().doubleValue());
			portfolioValue += shareValue * entry.getValue();
			System.out.print(entry.getValue() + " shares of " + entry.getKey() + " = $" + shareValue * entry.getValue() + "\n");
		}
		
		System.out.print("Remaining: $" + balance + "\n");
		System.out.print("Total Value: $" + portfolioValue + "\n");
		
		return portfolioValue;
	}

	public static Map <String, Integer> buyShares(Symbol[] stocks, double balance, int buyDaysAgo) throws IOException{
		Map <String, Integer> boughtShares = new HashMap <String, Integer>();

		//Evenly divides balance between stocks
		for (Symbol stock: stocks){
			int shares = 0;
			while ((shares + 1) * stock.getDay(buyDaysAgo).getAdjClose().doubleValue() < balance / stocks.length){
				shares++;
			}
			boughtShares.put(stock.getSymbol(), shares);

		}
		
		//Return map of each stock and number of shares bought
		return boughtShares;

	}

}
