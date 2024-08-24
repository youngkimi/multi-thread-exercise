package multiplier;

import java.util.Arrays;

public class MatrixPrinter {
    public static final MatrixPrinter matrixPrinter = new MatrixPrinter();

    protected static void printMatrix(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            sb.append(Arrays.toString(row));
            sb.append('\n');
        }
        System.out.println(sb);
    }
}
