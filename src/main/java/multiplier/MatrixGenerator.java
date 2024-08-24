package multiplier;

import java.util.Random;

public class MatrixGenerator {

	public static final MatrixGenerator matrixGenerator = new MatrixGenerator();
	public static final int[][] matrix1000x1000 = gen1000x1000IntMatrix();

	private MatrixGenerator() {
	}

	/**
	 *
	 * @param row : size of matrix row
	 * @param col : size of matrix col
	 * @param boundary: absolute boundary of cell number
	 * @param direction : true for both, false for positive
	 *
	 * the cell number does not contain Integer.MIN_VALUE.
	 */

	protected static int[][] genIntMatrix(int row, int col, int boundary, boolean direction) {
		int[][] matrix = new int[row][col];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				matrix[i][j] = new Random().nextInt(boundary);
				if (direction) {
					matrix[i][j] *= -1;
				}
			}
		}

		return matrix;
	}

	protected static int[][] gen1000x1000IntMatrix() {
		int matrixLength = 1000;

		int[][] matrix = new int[matrixLength][matrixLength];

		for (int i = 0; i < matrixLength; i++) {
			for (int j = 0; j < matrixLength; j++) {
				matrix[i][j] = 100;
			}
		}

		return matrix;
	}
}

