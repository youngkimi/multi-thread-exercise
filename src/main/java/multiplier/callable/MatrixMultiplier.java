package multiplier.callable;

public class MatrixMultiplier {
	public static final MatrixMultiplier matrixCallableMultiplier = new MatrixMultiplier();

	private MatrixMultiplier() {
	}

	public int[][] multiplyMatrixByCell(int[][] matrixLeft, int[][] matrixRight) {

		int rowLeft = matrixLeft.length;
		int colRight = matrixRight[0].length;

		int[][] matrixMultiplied = new int[rowLeft][colRight];

		for (int r = 0; r < rowLeft; r++) {
			for (int c = 0; c < colRight; c++) {
				try {
					matrixMultiplied[r][c] = new CellMultiplier(matrixLeft, matrixRight, r, c).call();
				} catch (Exception e) {
					System.out.println("Error in callable multiply by cell unit: " + e.getMessage());
					throw new RuntimeException();
				}
			}
		}

		return matrixMultiplied;
	}

	public int[][] multiplyMatrixByRow(int[][] matrixLeft, int[][] matrixRight) {

		int rowLeft = matrixLeft.length;
		int colRight = matrixRight[0].length;

		int[][] matrixMultiplied = new int[rowLeft][colRight];

		for (int r = 0; r < rowLeft; r++) {
			try {
				matrixMultiplied[r] = new RowMultiplier(matrixLeft, matrixRight, r).call();
			} catch (Exception e) {
				System.out.println("Error in callable multiply by row unit: " + e.getMessage());
				throw new RuntimeException();
			}
		}

		return matrixMultiplied;
	}
}
