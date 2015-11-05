import java.util.Arrays;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;

/**
 * Created by savetisyan on 18/10/15.
 */
public class Gauss {
    private double[][] a;
    private double[] b;
    private double[] x;
    private int n;

    public Gauss(double[][] a, double[] b) {
        this.a = a;
        this.b = b;
        n = a.length;
    }

    public double[][] reverse() {
        double[][] reverse = new double[n][n];
        double[][] identity = getIdentityMatrix(n);

        for (int i = 0; i < n; i++) {
            double[][] clone = clone(a);

            Gauss gauss = new Gauss(clone, identity[i]);
            double[] column = gauss.solve();

            for (int j = 0; j < n; j++) {
                reverse[j][i] = column[j];
            }
        }

        return reverse;
    }

    private double[][] clone(double[][] a) {
        double[][] clone = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = a[i][j];
            }
        }

        return clone;
    }

    private double[][] getIdentityMatrix(int n) {
        double[][] identity = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1.0;
        }
        return identity;
    }

    public double[] solve() {
        if(x != null) {
            return x;
        }
        x = new double[n];

        swapRows();
        directFlow();
        reverseFlow();
        return x;
    }

    public double det() {
        double det = 1;

        for (int i = 0; i < n; i++) {
            det *= a[i][i];
        }

        return det;
    }

    private void directFlow() {
        double[][] t = new double[n][n];

        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                t[i][k] = a[i][k] / a[k][k];
                b[i] -= t[i][k] * b[k];

                for (int j = k + 1; j < n; j++) {
                    a[i][j] -= t[i][k] * a[k][j];
                }
            }
        }
    }

    private void reverseFlow() {
        x[n - 1] = b[n - 1] / a[n - 1][n - 1];

        for (int k = n - 2; k >= 0; k--) {
            x[k] = (b[k] - sum(k, n)) / a[k][k];
        }
    }

    private double sum(int k, int n) {
        double sum = 0;

        for (int j = k; j < n; j++) {
            sum += a[k][j] * x[j];
        }

        return sum;
    }

    public static void print(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void print(double[][] a, double[] b) {
        System.out.println();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(Arrays.toString(b));
        System.out.println();
    }

    public static void swapRows(double[][] a, int i, int row) {
        double[] tmp = a[i];
        a[i] = a[row];
        a[row] = tmp;
    }

    public static void swapRows(double[] b, int i, int row) {
        double tmp = b[i];
        b[i] = b[row];
        b[row] = tmp;
    }

    private void swapRows() {
        for (int i = 0; i < n; i++) {
            double max = -1.0;
            int row = -1;

            for (int j = i; j < n; j++) {
                if (abs(a[j][i]) > max) {
                    max = abs(a[j][i]);
                    row = j;
                }
            }

            Gauss.swapRows(a, i, row);
            Gauss.swapRows(b, i, row);
        }
    }
}

class GaussTest {
    public static void main(String[] args) {
        double v = 1.0;
        double e = 1e-2;

        double[] lv = {v, v + 2, v + 4/*, v + 6, v + 8, v + 10*/};
        double[] le = stream(lv).map(x -> x * e).toArray();

        int n = lv.length;
        double[][] a = new double[n][n];
        double[] b = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    a[i][j] = lv[i];
                } else {
                    a[i][j] = le[i];
                }
            }
        }

        Gauss gauss = new Gauss(a, b);



        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i] += a[j][j] * a[i][j];
            }
        }

        Gauss.print(a, b);

        double[] x = gauss.solve();
        System.out.println("x = " + Arrays.toString(x));
        System.out.println("det = " + gauss.det());
    }
}
