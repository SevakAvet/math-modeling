import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by savetisyan on 01/10/15.
 */

public class SplineTest {
    public static void main(String[] args) throws ScriptException, IOException, InterruptedException {
        Double[] x = generate(0, 10, 0.1);
        Function<Double, Double> function = Math::sin;
        Double[] y = Arrays.stream(x).map(function).toArray(Double[]::new);
        int n = x.length;

        StringBuilder xs = new StringBuilder("(");
        StringBuilder ys = new StringBuilder("(");

        Spline spline = new Spline(x, y, n);
        for (int i = 0; i < x.length - 1; i++) {
            if (i == x.length - 2) {
                xs.append(String.format("%.4f, %.4f", x[i], x[i + 1]));
            } else {
                xs.append(String.format("%.4f, %.4f, ", x[i], (x[i] + x[i + 1]) / 2.0));
            }

        }
        System.out.println();
        for (int i = 0; i < x.length - 1; i++) {
            if (i == x.length - 2) {
                ys.append(String.format("%.4f, %.4f", spline.f(x[i]), spline.f(x[i + 1])));
            } else {
                ys.append(String.format("%.4f, %.4f, ", spline.f(x[i]), spline.f((x[i] + x[i + 1]) / 2.0)));
            }
        }

        xs.append(")");
        ys.append(")");

        System.out.println("python draw.py \"" + xs + "\" \"" + ys + "\"");

        for (int i = 0; i < x.length - 1; i++) {
            if (i == x.length - 2) {
                System.out.printf("%.5f, %.5f", x[i], x[i + 1]);
            } else {
                System.out.printf("%.5f, %.5f, ", x[i], (x[i] + x[i + 1]) / 2.0);
            }
        }

        System.out.println();

        double max = -1;
        for (int i = 0; i < x.length - 1; i++) {
            if (i == x.length - 2) {
                System.out.printf("%.5f, %.5f", Math.abs(function.apply(x[i]) - spline.f(x[i])),
                        Math.abs(function.apply(x[i + 1]) - spline.f(x[i + 1])));
                max = Math.max(max, Math.abs(function.apply(x[i]) - spline.f(x[i])));
                max = Math.max(max, Math.abs(function.apply(x[i + 1]) - spline.f(x[i + 1])));
            } else {
                double x1 = (x[i] + x[i + 1]) / 2.0;
                System.out.printf("%.5f, %.5f, ", Math.abs(function.apply(x[i]) - spline.f(x[i])),
                        Math.abs(function.apply(x1) - spline.f(x1)));

                max = Math.max(max, Math.abs(function.apply(x[i]) - spline.f(x[i])));
                max = Math.max(max, Math.abs(function.apply(x1) - spline.f(x1)));
            }
        }

        System.out.printf("\n%.10f", max);
    }


    private static Double[] generate(int start, int end, double dx) {
        List<Double> res = new ArrayList<>();
        for (double i = start; i <= end; i += dx) {
            res.add(i);
        }

        return res.toArray(new Double[res.size()]);
    }
}

class Spline {
    private SplineTuple[] splines;
    private Double[] xs, ys;
    private int n;

    public Spline(Double[] xs, Double[] ys, int n) {
        this.xs = xs;
        this.ys = ys;
        this.n = n;
    }

    private void build(Double[] x, Double[] y, int n) {
        splines = new SplineTuple[n];
        for (int i = 0; i < n; i++) {
            splines[i] = new SplineTuple();
        }

        for (int i = 0; i < n; ++i) {
            splines[i].x = x[i];
            splines[i].a = y[i];
        }

        splines[0].c = splines[n - 1].c = 0.0;

        double[] alpha = new double[n - 1];
        double[] beta = new double[n - 1];

        alpha[0] = 0.0;
        beta[0] = 0.0;

        for (int i = 1; i < n - 1; ++i) {
            double hi = x[i] - x[i - 1];
            double hi1 = x[i + 1] - x[i];

            double a = hi;
            double c = 2.0 * (hi + hi1);
            double b = hi1;
            double f = 6.0 * ((y[i + 1] - y[i]) / hi1 - (y[i] - y[i - 1]) / hi);
            double z = (a * alpha[i - 1] + c);

            alpha[i] = -b / z;
            beta[i] = (f - a * beta[i - 1]) / z;
        }

        for (int i = n - 2; i > 0; --i) {
            splines[i].c = alpha[i] * splines[i + 1].c + beta[i];
        }

        for (int i = n - 1; i > 0; --i) {
            double hi = x[i] - x[i - 1];
            splines[i].d = (splines[i].c - splines[i - 1].c) / hi;
            splines[i].b = hi * (2.0 * splines[i].c + splines[i - 1].c) / 6.0 + (y[i] - y[i - 1]) / hi;
        }
    }

    public double f(double x) {
        if (splines == null) {
            build(xs, ys, n);
        }

        SplineTuple s;
        int n = splines.length;

        if (x <= splines[0].x) {
            s = splines[1];
        } else if (x >= splines[n - 1].x) {
            s = splines[n - 1];
        } else {
            int l = 0;
            int r = n - 1;

            while (l + 1 < r) {
                int m = l + (r - l) / 2;

                if (x <= splines[m].x) {
                    r = m;
                } else {
                    l = m;
                }
            }

            s = splines[r];
        }

        double dx = (x - s.x);
        return s.a + (s.b + (s.c / 2.0 + s.d * dx / 6.0) * dx) * dx;
    }

    private class SplineTuple {
        public double a, b, c, d, x;
    }

}
