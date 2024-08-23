package multiplier.callable;

public class MatrixMultiplier {

	public static MatrixMultiplier matrixMultiplier = new MatrixMultiplier();

	public static int[][] matrixMultiplied;
	protected static int[][] matrixLeft;
	protected static int[][] matrixRight;

	public MatrixMultiplier() {
	}

	public int[][] multiplyMatrix(int[][] matrixLeft, int[][] matrixRight) {

		this.matrixLeft = matrixLeft;
		this.matrixRight = matrixRight;

		int rowLeft = matrixLeft.length;
		int colRight = matrixRight[0].length;

		matrixMultiplied = new int[rowLeft][colRight];

		Thread[] threads = new Thread[rowLeft * colRight];

		for (int r = 0; r < rowLeft; r++) {
			for (int c = 0; c < colRight; c++) {
				threads[r*rowLeft + c] = new Thread(new RowMultiplier(r, c));
				threads[r*rowLeft + c].start();
			}
		}

		for (Thread th : threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted: " + th.getName());
			}
		}

		return matrixMultiplied;
	}
}
