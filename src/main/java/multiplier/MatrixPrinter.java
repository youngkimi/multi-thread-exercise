package multiplier;

import java.util.Arrays;

public class MatrixPrinter {
    public static final MatrixPrinter matrixPrinter = new MatrixPrinter();

    protected static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
