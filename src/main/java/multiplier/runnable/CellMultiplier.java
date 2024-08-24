package multiplier.runnable;

import static multiplier.runnable.MatrixMultiplier.*;

public class CellMultiplier implements Runnable {

	// means left Row, right Column
	int[][] matrixLeft;
	int[][] matrixRight;
	int row;
	int col;

	public CellMultiplier(int[][] matrixLeft, int[][] matrixRight, int row, int col) {
		this.matrixLeft = matrixLeft;
		this.matrixRight = matrixRight;
		this.row = row;
		this.col = col;
	}

	@Override
	public void run() {
		int sum = 0;

		for (int i = 0; i < matrixLeft[0].length; i++) {
			sum += matrixLeft[row][i] * matrixRight[i][col];
		}

		matrixMultiplied[row][col] = sum;
	}
}
