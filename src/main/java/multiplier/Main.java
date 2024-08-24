package multiplier;

import java.io.IOException;

import static multiplier.MatrixGenerator.genIntMatrix;
import static multiplier.MatrixPrinter.printMatrix;
import static multiplier.callable.MatrixMultiplier.matrixCallableMultiplier;
import static multiplier.runnable.MatrixMultiplier.matrixRunnableMultiplier;

public class Main {

	public static void main(String[] args) {

		int[][] left = genIntMatrix(5, 3, 10, false);
		int[][] right = genIntMatrix(3, 5, 10, false);

		printMatrix(left);
		printMatrix(right);

		System.out.println("Runnable By Cell : ");
		printMatrix(matrixRunnableMultiplier.multiplyMatrixByCell(left, right));

		System.out.println("Callable By Cell : ");
		printMatrix(matrixCallableMultiplier.multiplyMatrixByCell(left, right));

		System.out.println("Callable By Row : ");
		printMatrix(matrixCallableMultiplier.multiplyMatrixByRow(left, right));
w	}
}
