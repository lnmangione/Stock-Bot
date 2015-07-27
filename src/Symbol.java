import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Symbol {
	String symbol;
	BigDecimal price, change, dayHigh, dayLow, ftWkHigh, ftWkLow, eps;
	long volume, shares;
	
	public Symbol(String stock) throws IOException{
		setInfo(stock);
	}
	 
	//Returns History
	public List<HistoricalQuote> getHistory(int months) throws IOException{
		Stock t = YahooFinance.get(symbol);
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.MONTH, - months); //4 Months Ago
		List<HistoricalQuote> quotes = t.getHistory(from, to, Interval.DAILY);
		return quotes;
	}
	
	/**
	 * Returns list of historical quotes using days
	 * @author Michael Bick
	 * @param daysAgo days ago the first quote is from
	 * @param days amount of days of historical quotes
	 * @return list of historical quotes from time period
	 * @throws IOException
	 */
	public List<HistoricalQuote> getHistory(int daysAgo, int days) throws IOException {
		Stock t = YahooFinance.get(symbol);
		Calendar from = Calendar.getInstance();
		
		// Grab history of more days than necessary. We'll filter out what we don't need later
		from.add(Calendar.DAY_OF_MONTH, - (2 * (daysAgo + days)));
		List<HistoricalQuote> quotes = t.getHistory(from, Interval.DAILY);
		// Filter the list down to what we need
		quotes = quotes.subList(daysAgo, daysAgo + days); 
		
		return quotes;
	}
	
	/**
	 *  Returns a moving average from a stock's history
	 * @author Michael Bick
	 * @param daysAgo days ago the moving average is from
	 * @param days amount of days to use in the moving average
	 * @return moving average
	 * @throws IOException
	 */
	public BigDecimal getMA(int daysAgo, int days) throws IOException {
		// Gets historical quotes
		List<HistoricalQuote> quotes = getHistory(daysAgo, days);
		
		// Calculate the moving average
		BigDecimal ma = new BigDecimal(0);
		for (HistoricalQuote quote : quotes) {
			ma = ma.add(quote.getAdjClose());
			// System.out.println(quote.getAdjClose());
			// System.out.println(ma);
		}
		ma = ma.divide(new BigDecimal(quotes.size()), 2, RoundingMode.HALF_UP); // Rounds the "regular" way to 2 decimal places
		
		return ma;
	}
		
	//Sets Features
	private void setInfo(String ticker) throws IOException{
		Stock stock = YahooFinance.get(ticker);
		symbol = ticker;
		price = stock.getQuote().getPrice();
		change = stock.getQuote().getChangeInPercent();
		volume =stock.getQuote().getVolume();  
		dayHigh = stock.getQuote().getDayHigh(); 
	 	dayLow = stock.getQuote().getDayLow(); 
	 	ftWkHigh = stock.getQuote().getYearHigh(); 
	 	ftWkLow = stock.getQuote().getYearLow(); 
	 	eps = stock.getStats().getEps();
	 	shares = stock.getStats().getSharesOutstanding();
	 	// BigDecimal twoHundredChange = stock.getQuote().getChangeFromAvg200();
	} 
	
	//Returns Features
	public String getSymbol(){
		return symbol;
	}
	public BigDecimal getDayHigh(){
		return dayHigh;
	}
	public BigDecimal getDayLow(){
		return dayLow;
	}
	public BigDecimal getFtWkHigh(){
		return ftWkHigh;
	}
	public BigDecimal getFtWkLow(){
		return ftWkLow;
	}
	public BigDecimal getPrice(){
		return price;
	}
	public BigDecimal getEPS(){
		return eps;
	}
	public long getNumberOfShares(){
		return shares;
	}
	public long getVolume(){
		return volume;
	}
}
