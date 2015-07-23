import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
	 *  Returns a moving average from a stock's history
	 * @author Michael Bick
	 * @param daysAgo days ago the moving average is from
	 * @param days days to use in the moving average
	 * @return moving average
	 * @throws IOException
	 */
	public BigDecimal getMA(int daysAgo, int days) throws IOException {
		// Adds last 200 days into a list of quotes
		Stock t = YahooFinance.get(symbol);
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DAY_OF_MONTH, - daysAgo);
		from.add(Calendar.DAY_OF_MONTH, - (daysAgo + days));
		List<HistoricalQuote> quotes = t.getHistory(from, to, Interval.DAILY);
		
		// Calculate the moving average
		BigDecimal ma = new BigDecimal(0);
		for (HistoricalQuote quote : quotes) {
			ma.add(quote.getAdjClose());
		}
		ma.divide(new BigDecimal(days));
		
		return ma;
	}
	
	/**
	 * Returns a linear prediction of a stock's price using the slope of the moving average
	 * @author Michael Bick
	 * @param daysAgo days ago the moving average is predicted from
	 * @param days days used in calculating the moving averages
	 * @param int futureDays days into the future to predict to
	 * @return returns predicted price
	 */
	public BigDecimal getMAPrediction(int daysAgo, int days, int futureDays) throws IOException {
		// Calculate moving averages
		BigDecimal day1 = getMA(daysAgo, days);
		BigDecimal day2 = getMA(daysAgo - 1, days);
		
		// Calculate slope
		BigDecimal slope = day1.subtract(day2);
		
		// Get day1 closing price
		Stock t = YahooFinance.get(symbol);
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DAY_OF_MONTH, - daysAgo);
		from.add(Calendar.DAY_OF_MONTH, - (daysAgo + 1));
		BigDecimal closePrice = t.getHistory(from, to, Interval.DAILY).remove(0).getAdjClose();
		
		// Return calculated prediction
		return closePrice.add(slope.multiply(new BigDecimal(3)));
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
	 	BigDecimal twoHundredChange = stock.getQuote().getChangeFromAvg200();
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
