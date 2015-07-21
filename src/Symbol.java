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
