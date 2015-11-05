import org.junit.Test;

import java.util.Arrays;

/**
 * Created by savetisyan on 05/11/15
 */
public class ThreeDiagonalMatrix {
    private double[] b;
    private double[] c;
    private double[] d;
    private double[] r;
    private double[] x;

    private double[] delta;
    private double[] lambda;

    private int n;

    public ThreeDiagonalMatrix(double[] b, double[] c, double[] d, double[] r) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.r = r;
        this.n = r.length;
    }

    public double[] solve() {
        if (x != null) {
            return x;
        }

        x = new double[n];
        delta = new double[n];
        lambda = new double[n];

        delta[0] = -d[0] / c[0];
        lambda[0] = r[0] / c[0];

        for (int i = 1; i < n; i++) {
            delta[i] = -d[i] / (c[i] + b[i] * delta[i - 1]);
            lambda[i] = (r[i] - b[i] * lambda[i - 1]) / (c[i] + b[i] * delta[i - 1]);
        }

        x[n - 1] = lambda[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = delta[i] * x[i + 1] + lambda[i];
        }

        System.out.println(Arrays.toString(delta));
        System.out.println(Arrays.toString(lambda));
        return x;
    }
}

class ThreeDiagonalMatrixTest {

    @Test
    public void test1() {
        double[] b = {0, 1, 1};
        double[] c = {2, 2, 2};
        double[] d = {3, 3, 0};

        double[] r = {1, 1, 1};

        /**
         * (2 3 0),  (1)
         * (1 2 3)   (1)
         * (0 1 2)   (1)
         */

        ThreeDiagonalMatrix matrix = new ThreeDiagonalMatrix(b, c, d, r);
        double[] solve = matrix.solve();
        System.out.println(Arrays.toString(solve));
    }

    @Test
    public void test2() {
        double[] b = {0, 2, 4, 4, 2};
        double[] c = {2, 9, 17, 15, 3};
        double[] d = {1, 2, -4, -8, 0};

        double[] r = {-10, -26, -16, -2, 16};

        ThreeDiagonalMatrix matrix = new ThreeDiagonalMatrix(b, c, d, r);
        double[] solve = matrix.solve();
        System.out.println(Arrays.toString(solve));
    }
}
