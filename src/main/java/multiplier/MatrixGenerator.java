package multiplier;

import java.util.Random;

public class MatrixGenerator {

	public static final MatrixGenerator matrixGenerator = new MatrixGenerator();

	private MatrixGenerator() {
	}

	/**
	 *
	 * @param row : size of matrix row
	 * @param col : size of matrix col
	 * @param boundary : abs boundary
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
}

