import java.io.IOException;

/**
 * Created by MichaelBick on 7/29/15.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        test();
    }

    private static void test() throws IOException {
        Symbol[] stocks = {new Symbol("AAPL")};

        int FUTURE_DAYS = 10;
        int NUM_POINTS = 20;
        int DAYS_BACK = 100;

        double[][] train = GradientDescent.getData(stocks, NUM_POINTS, DAYS_BACK);
        double[] trainActual = GradientDescent.getActual(stocks, NUM_POINTS, DAYS_BACK, FUTURE_DAYS);

        double[][] test = GradientDescent.getData(stocks, NUM_POINTS, DAYS_BACK + NUM_POINTS);
        double[] testActual = GradientDescent.getActual(stocks, NUM_POINTS, DAYS_BACK + NUM_POINTS, FUTURE_DAYS);

        double[] theta = new double[train[0].length];

        theta = GradientDescent.train(train, trainActual, theta, .0001, 1000);

        for (int i = 0; i < NUM_POINTS; i++) {
            System.out.println("Actual: " + testActual[i]);
            System.out.println("Prediction: " + GradientDescent.getPredictions(theta, test)[i]);
        }
    }
}
