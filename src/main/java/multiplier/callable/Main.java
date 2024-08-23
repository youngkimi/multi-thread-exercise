package multiplier.callable;

import static multiplier.MatrixGenerator.*;
import static multiplier.runnable.MatrixMultiplier.*;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		int[][] left = matrixGenerator.genIntMatrix(5, 3, 10, false);
		int[][] right = matrixGenerator.genIntMatrix(3, 5, 10, false);

		printMatrix(left);
		printMatrix(right);

		printMatrix(matrixMultiplier.multiplyMatrix(left, right));
	}

	private static void printMatrix(int[][] matrix) {
		System.out.println("=================");
		for (int[] row : matrix) {
			System.out.println(Arrays.toString(row));
		}
	}
}
