package multiplier.cellunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MatrixMultiplier {

	protected static int[][] matrixLeft;
	protected static int[][] matrixRight;
	protected static int[][] matrixMultiplied;

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int rowLeft = Integer.parseInt(st.nextToken());
		int colLeft = Integer.parseInt(st.nextToken());
		int rowRight = Integer.parseInt(st.nextToken());
		int colRight = Integer.parseInt(st.nextToken());

		if (! isValidInput(colLeft, rowLeft, colRight, rowRight)) {
			// 에러 처리.
			throw new InvalidParameterException("Matrix multiplication cannot be executed");
		}

		matrixLeft = new int[rowLeft][colLeft];
		matrixRight = new int[rowRight][colRight];
		matrixMultiplied = new int[rowLeft][colRight];

		initiateMatrix(matrixLeft);
		initiateMatrix(matrixRight);

		printMatrix(matrixLeft);
		printMatrix(matrixRight);

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

		printMatrix(matrixMultiplied);
	}

	private static boolean isValidInput(int colLeft, int rowLeft, int colRight, int rowRight) {
		return (colLeft > 0 && rowLeft > 0 && colRight > 0 && rowRight > 0)
			&& colLeft == rowRight && colRight == rowLeft;
	}

	private static void printMatrix(int[][] matrix) {
		System.out.println("=================");
		for (int[] row : matrix) {
			System.out.println(Arrays.toString(row));
		}
	}

	private static void initiateMatrix(int[][] matrix) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		for (int i = 0; i < matrix.length; i++) {
			int[] row = Arrays.stream(br.readLine().split(" "))
				.mapToInt(Integer::parseInt).toArray();

			if (row.length != matrix[0].length) {
				throw new InvalidParameterException("Error: row count not matches.");
			}

			matrix[i] = row;
		}
	}
}
