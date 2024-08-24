package multiplier.callable;

import java.util.concurrent.Callable;

public class RowMultiplier implements Callable<int[]> {

	// need the whole matrix input
	int[][] matrixLeft;
	int[][] matrixRight;
	int row;

	public RowMultiplier(int[][] matrixLeft, int[][] matrixRight, int row) {
		this.matrixLeft = matrixLeft;
		this.matrixRight = matrixRight;
		this.row = row;
	}

	@Override
	public int[] call() throws Exception {
		int[] resultRow = new int[matrixLeft.length];

		for (int i = 0; i < matrixLeft.length; i++) {
			for (int j = 0; j < matrixRight.length; j++) {
				resultRow[i] += matrixLeft[row][j] * matrixRight[j][i];
			}
 		}

		return resultRow;
	}
}
