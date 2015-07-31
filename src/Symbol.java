import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class Symbol extends Stock {
	
	private final int DAYS_HISTORY = 2000;
	
	// Keep historical data that we have already pulled to speed up getHistory method
	private List<HistoricalQuote> history = new ArrayList<>(0);
	
	public Symbol(String symbol) throws IOException {
		super(symbol);
		Stock stock = YahooFinance.get(symbol);
		setQuote(stock.getQuote());
		setStats(stock.getStats());
		setDividend(stock.getDividend());
		
		// Set history
		Calendar from = Calendar.getInstance();
		from.add(Calendar.DAY_OF_MONTH, -DAYS_HISTORY);

		// Update list of historical data with new historical data
		history = getHistory(from, Interval.DAILY);
	}
	
	/**
	 * Returns list of historical quotes using days from most to least recent
	 * @author Michael Bick
	 * @param daysAgo days ago the first quote is from
	 * @param days amount of days of historical quotes
	 * @return list of historical quotes from time period
	 * @throws IOException
	 */
	public List<HistoricalQuote> getHistory(int daysAgo, int days) throws IOException {		
		// Create an integer to hold the farthest back day requested
		int fromDay = daysAgo + days;

		// Filter the list down to what we need
		// Only subtract 1 from first parameter because it is inclusive
		return history.subList(daysAgo, fromDay);
	}

	/**
	 * Returns one day of historical information
	 * @author Michael Bick
	 * @param daysAgo amount of days ago to get information from
	 * @return historical quote from a given amount of days ago
	 * @throws IOException
	 */
	public HistoricalQuote getDay(int daysAgo) throws IOException {
		return getHistory(daysAgo, 1).get(0);
	}

	/**
	 * Returns a historical adjusted closing price of a stock
	 * @author Michael Bick
	 * @param daysAgo amount of days ago to get the closing price from
	 * @return historical adjusted closing price
	 * @throws IOException
	 */
	public BigDecimal getAdjClose(int daysAgo) throws IOException {
		return getDay(daysAgo).getAdjClose();
	}
	
	/**
	 * Returns a historical volume of shares traded
	 * @author Michael Bick
	 * @param daysAgo amount of days ago to get the volume from
	 * @return volume of shares traded
	 * @throws IOException
	 */
	public long getVolume(int daysAgo) throws IOException {
		return getDay(daysAgo).getVolume();
	}
	
	/**
	 *  Returns a moving average from a stock's history
	 * @author Michael Bick
	 * @param daysAgo amount of days ago the moving average is from
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
}
