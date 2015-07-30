import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Features {
    private static final int SPREAD = 10;

    private static final int PAST_DAYS = 4;

    private Symbol sp500;
    private HashMap<Integer, BigDecimal> market = new HashMap<Integer, BigDecimal>();

    public Features() throws IOException{
        sp500 = new Symbol("^GSPC");
    }

    private double getSP500(int daysAgo) throws IOException{
        BigDecimal price = market.get(daysAgo);

        if (price == null) {
            price = sp500.getAdjClose(daysAgo);
            market.put(daysAgo, price);
        }

        return price.doubleValue();
    }

    public double[] getFeatures(Symbol stock, int daysAgo) throws IOException {
        double[] features = new double[PAST_DAYS + PAST_DAYS + 1];

        features[0] = 1.0;

        // Add past days as features
        for (int i = 0; i < PAST_DAYS; i++) {
            features[i + 1] = stock.getAdjClose((i * 10) + daysAgo).doubleValue();
        }

        // Add SP500 past prices as features
        for (int i = 0; i < PAST_DAYS; i++) {
            features[i + PAST_DAYS + 1] = getSP500((i * 10) + daysAgo);
        }

        return features;
    }
}
