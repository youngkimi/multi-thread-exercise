package multiplier.callable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	public int[][] multiplyMatrixByRowThreadPool(int[][] matrixLeft, int[][] matrixRight, int threadPoolSize) {

		int rowLeft = matrixLeft.length;
		int colRight = matrixRight[0].length;

		int[][] matrixMultiplied = new int[rowLeft][colRight];

		List<Future<int[]>> futures = new LinkedList<>();

		ExecutorService pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
		// ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);

			/*
				int corePoolSize,
			 	int maximumPoolSize,
			 	long keepAliveTime,
			 	TimeUnit unit,
			 	BlockingQueue<Runnable> workQueue)
			 */

		for (int r = 0; r < rowLeft; r++) {
			futures.add(pool.submit(new RowMultiplier(matrixLeft, matrixRight, r)));
		}

		try {
			for (int r = 0; r < rowLeft; r++) {
				matrixMultiplied[r] = futures.get(r).get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			pool.shutdown();
		}

		return matrixMultiplied;
	}
}
