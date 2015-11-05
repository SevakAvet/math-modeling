/**
 * Created by savetisyan on 05/11/15
 */
public class ReverseMatrix {
    public static void main(String[] args) {
        double[][] a = {{1, 2}, {3, 4}};
        Gauss gauss = new Gauss(a, null);
        double[][] reverse = gauss.reverse();
        Gauss.print(reverse);
    }
}
