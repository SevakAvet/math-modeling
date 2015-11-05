/**
 * Created by savetisyan on 04/11/15
 */
public class Determinant {
    public static void main(String[] args) {
        double[][] a = {{1,1},{2,3}};
        double[] b = {3,4};

        Gauss gauss = new Gauss(a, b);
        gauss.solve();
        System.out.println(gauss.det());
    }
}
