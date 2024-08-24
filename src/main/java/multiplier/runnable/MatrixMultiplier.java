package multiplier.runnable;

public class MatrixMultiplier {
	public static final MatrixMultiplier matrixRunnableMultiplier = new MatrixMultiplier();

	public static int[][] matrixMultiplied;
	private MatrixMultiplier() {
	}

	/*
		Calculating by cell unit results in the creation of too many threads.
		-> Use Callable, with Executor. Calculate by row unit.
	 */

	public int[][] multiplyMatrixByCell(int[][] matrixLeft, int[][] matrixRight) {

		int rowLeft = matrixLeft.length;
		int colRight = matrixRight[0].length;

		matrixMultiplied = new int[rowLeft][colRight];

		Thread[] threads = new Thread[rowLeft * colRight];

		for (int r = 0; r < rowLeft; r++) {
			for (int c = 0; c < colRight; c++) {
				threads[r*rowLeft + c] = new Thread(new CellMultiplier(matrixLeft, matrixRight, r, c));
				threads[r*rowLeft + c].start();
			}
		}

		for (Thread th : threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				System.out.println("Error in runnable multiply by cell unit: " + e.getMessage());
				throw new RuntimeException();
			}
		}

		return matrixMultiplied;
	}
}
