import static java.lang.Math.abs;

/**
 * Выполнить вычисление суммы следующего ряда:
 * f(x) = (Vx) - (Vx)^3 / 3! + (Vx)^5 / 5! - (Vx)^7 / 7! + ...
 * f0(x) = Vx
 * f1(x) = (Vx)^3 / 3!
 * <p>
 * f(x) = sum(k=0, inf) fk(x)
 * <p>
 * Сумму ряда будет находить в 10 точках, делящих на равные отрезки некоторый отрезок [a, b] in R
 * a ----------------- b
 * x1 x2 ............. x10
 * <p>
 * Результаты вычислений вывести в виде следующей таблицы значений:
 * <p>
 * x1      | x2      |... | x10
 * Sn1(x1) | Sn2(x2) |... | Sn10(x10)
 * n1      | n2      |... | n10
 * <p>
 * Вычисление частичной суммы проводить до тех пор, пока |fl+1(x)| < eps, eps = 10^-8
 *
 * Created by savetisyan on 03/09/15.
 */
public class SumFunctional {
    private static final double EPS = 1e-8;

    public static void main(String[] args) {
        double V = 2;

        double a = -5;
        double b = 5;
        double dx = 1;

        for (double x = a; x <= b; x += dx) {
            double[] f = f(V, x);
            System.out.printf("%f\t%f\t%d\n", x, f[0], (int) f[1]);
        }
    }

    public static double[] f(double V, double x) {
        double pow = 3;
        double cur = V * x;
        double sum = cur;
        double next = next(cur, V * x, pow);
        int step = 1;

        while (abs(next) >= EPS) {
            step++;
            sum += next;
            pow += 2;
            cur = next;
            next = next(cur, V * x, pow);
        }

        return new double[]{sum, step};
    }

    public static double f(double x) {
        double pow = 3;
        double cur = x;
        double sum = cur;
        double next = next(cur, x, pow);

        while (abs(next) >= EPS) {
            sum += next;
            pow += 2;
            cur = next;
            next = next(cur, x, pow);
        }

        return sum;
    }



    public static double next(double cur, double vx, double pow) {
        double next = cur * vx * vx;
        next = next / (pow * (pow - 1));
        return -1 * next;
    }
}
