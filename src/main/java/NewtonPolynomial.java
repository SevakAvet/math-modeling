import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by savetisyan on 17/09/15.
 */
public class NewtonPolynomial {
    private static final Map<Double[], Double> cache = new HashMap<>();
    private static final Map<Double, Double> cacheValues = new HashMap<>();

    public static void main(String[] args) {
        List<Double> x = LagrangePolynomial.generate(1, 4, 1);
        Function<Double, Double> func = SumFunctional::f;
        List<Double> fx = x.stream().map(func).collect(Collectors.toList());

        for (int i = 0; i < x.size() - 1; i++) {
            double xCur = (x.get(i) + x.get(i + 1)) / 2.0;
            if (i == x.size() - 2) {
                System.out.printf("%.2f\t", x.get(i));
            } else {
                System.out.printf("%.2f\t%.2f\t", x.get(i), xCur);
            }
        }
        System.out.println();
        for (int i = 0; i < x.size() - 1; i++) {
            double xCur = (x.get(i) + x.get(i + 1)) / 2.0;
            if (i == x.size() - 2) {
                System.out.printf("%.2f\t",
                        Math.abs(fx.get(i) - newtonPolynom(func, x.get(i), (Double[]) x.toArray(new Double[x.size()]))));
            } else {
                System.out.printf("%.2f\t%.2f\t",
                        Math.abs(fx.get(i) - newtonPolynom(func, x.get(i), (Double[]) x.toArray(new Double[x.size()]))),
                        Math.abs(func.apply(xCur) - newtonPolynom(func, xCur, (Double[]) x.toArray(new Double[x.size()]))));
            }
        }
    }

    public static void printTable(Function<Double, Double> f, List<Double> x, List<Double> fx) {
        System.out.println("\n");
        for (int i = 0; i < x.size(); i++) {
            System.out.printf("%.3f |\t", x.get(i));
            if (i + 1 < x.size()) {
                System.out.printf("%.3f |\t", (x.get(i) + x.get(i + 1)) / 2);
            }
        }

        System.out.println();
        for (double aX : fx) {
            System.out.printf("%.3f |\t", aX);
        }

        Double[] values = new Double[2 * x.size() - 1];
        int j = 0;
        for (int i = 0; i < values.length - 1; i += 2) {
            values[i] = x.get(j);
            values[i + 1] = (x.get(j) + x.get(j + 1)) / 2;
            j++;
        }

        System.out.println();
        for (int i = 0; i < values.length; i++) {
            Double fXcur = f.apply(x.get(i));
            double newtonCur = newtonPolynom(f, x.get(i), values);
            System.out.printf("%.3f |\t", Math.abs(fXcur - newtonCur));
        }
    }

    public static double newtonPolynom(Function<Double, Double> f, double x, Double... values) {
        x++;
        double bracket = 1;
        double ans = 0;

        for (int i = 0; i <= values.length - 1; i++) {
            if (i == 0) {
                ans += f.apply(values[0]) * bracket;
                continue;
            }

            bracket *= (x - values[i]);
            Double[] tmpValues = new Double[i + 1];
            System.arraycopy(values, 0, tmpValues, 0, tmpValues.length);
            ans += dividedDifference(f, tmpValues) * bracket;
        }

        return ans;
    }

    public static double dividedDifference(Function<Double, Double> f, Double... x) {
        if (cache.containsKey(x)) {
            return cache.get(x);
        }

        Double fx0 = cacheValues.getOrDefault(x[0], f.apply(x[0]));
        Double fx1 = cacheValues.getOrDefault(x[1], f.apply(x[1]));

        cacheValues.put(x[0], fx0);
        cacheValues.put(x[1], fx1);

        if (x.length == 2) {
            return (fx1 - fx0) / (x[1] - x[0]);
        }

        int n = x.length;
        Double[] firstArg = new Double[n - 1];
        Double[] secondArg = new Double[n - 1];

        System.arraycopy(x, 1, firstArg, 0, n - 1);
        System.arraycopy(x, 0, secondArg, 0, n - 1);

        double res = (dividedDifference(f, firstArg) - dividedDifference(f, secondArg)) / (x[n - 1] - x[0]);
        cache.put(x, res);
        return res;
    }
}
