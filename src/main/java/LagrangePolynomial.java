import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x0 | (x0 + x1) / 2   | x1 | ...
 * ------------------------------
 * f0 | L((x0 + x1) / 2)| f1 | ...
 * <p>
 * Created by savetisyan on 10/09/15.
 */
public class LagrangePolynomial {
    public static void main(String[] args) {
        List<Double> x = generate(1, 3, 1);
        List<Double> fx = x.stream()
                .map(SumFunctional::f)
                //.map(z -> z * z)
                .collect(Collectors.toList());

        List<Double> lagrangeValues = new ArrayList<>();
        for (int i = 0; i < x.size() - 1; i++) {
            double xCur = (x.get(i) + x.get(i + 1)) / 2.0;
            lagrangeValues.add(L(xCur, x, fx));
        }

        List<Double> result = merge(fx, lagrangeValues);

        printOldTable(x, fx);
        printNewTable(x, result);
    }

    public static void printOldTable(List<Double> x, List<Double> fx) {
        for (double aX : x) {
            System.out.printf("%.3f |\t", aX);
        }
        System.out.println();
        for (double aX : fx) {
            System.out.printf("%.3f |\t", aX);
        }
    }

    public static void printNewTable(List<Double> x, List<Double> fx) {
        System.out.println("\n");
        for (int i = 0; i < x.size(); i++) {
            System.out.printf("%.3f |\t", x.get(i));
            if(i + 1 < x.size()) {
                System.out.printf("%.3f |\t", (x.get(i) + x.get(i + 1)) / 2);
            }
        }
        System.out.println();
        for (double aX : fx) {
            System.out.printf("%.3f |\t", aX);
        }
    }

    public static double L(double x, List<Double> input, List<Double> values) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            double num = 1;
            double denom = 1;

            for (int j = 0; j < input.size(); j++) {
                if (j == i) {
                    continue;
                }

                num *= (x - input.get(j));
                denom *= (input.get(i) - input.get(j));
            }

            sum += (num / denom) * values.get(i);
        }

        return sum;
    }

    public static List<Double> merge(List<Double> fx, List<Double> lagrangeValues) {
        List<Double> result = new ArrayList<>(fx);
        int index = 1;
        for (double lagrangeValue : lagrangeValues) {
            result.add(index, lagrangeValue);
            index += 2;
        }

        return result;
    }

    public static List<Double> generate(int start, int end, int dx) {
        List<Double> values = new ArrayList<>();
        for (double i = start; i <= end; i += dx) {
            values.add(i);
        }

        return values;
    }
}
