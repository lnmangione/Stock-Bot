import java.io.IOException;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Features {
    private static final int PAST_DAYS = 4;

    private Symbol sp500;

    public Features() throws IOException{
        sp500 = new Symbol("^GSPC");
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
            features[1 + PAST_DAYS + i] = sp500.getAdjClose((i * 10) + daysAgo).doubleValue();
        }

        return features;
    }
}
