package multiplier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static multiplier.MatrixGenerator.*;
import static multiplier.MatrixPrinter.printMatrix;
import static multiplier.callable.MatrixMultiplier.matrixCallableMultiplier;
import static multiplier.runnable.MatrixMultiplier.matrixRunnableMultiplier;

public class Main {

	private static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String filePath = "/Users/youngkim/Desktop/output/multiplier/out.txt";

		FileWriter fw = new FileWriter(filePath, true);

		// int[][] left = genIntMatrix(100, 100, 10, false);
		// int[][] right = genIntMatrix(100, 100, 10, false);

		// printMatrix(matrix1000x1000);
		// printMatrix(left);
		// printMatrix(right);

		// System.out.println("Runnable By Cell : ");
		// printMatrix(matrixRunnableMultiplier.multiplyMatrixByCell(left, right));
		//
		// System.out.println("Callable By Cell : ");
		// printMatrix(matrixCallableMultiplier.multiplyMatrixByCell(left, right));
		//
		// System.out.println("Callable By Row : ");
		// printMatrix(matrixCallableMultiplier.multiplyMatrixByRow(left, right));
		//
		// System.out.println("Callable By Row With Limited ThreadPool : ");

		for (int i = 1; i <= 10; i++) {
			sb.append("=====" +"TEST"+ i + "=====");
			calculateWithThreadPool(1);
			calculateWithThreadPool(2);
			calculateWithThreadPool(5);
			calculateWithThreadPool(10);
			calculateWithThreadPool(20);
			calculateWithThreadPool(25);
			calculateWithThreadPool(50);
			calculateWithThreadPool(75);
			calculateWithThreadPool(100);
			calculateWithThreadPool(150);
			calculateWithThreadPool(200);
			calculateWithThreadPool(300);
			calculateWithThreadPool(500);
			calculateWithThreadPool(750);
			calculateWithThreadPool(1000);
		}

		fw.write(sb.toString());
		fw.close();
	}

	private static void calculateWithThreadPool(int threadPoolSize) {
		long startTime = System.currentTimeMillis();
		matrixCallableMultiplier.multiplyMatrixByRowThreadPool(matrix1000x1000, matrix1000x1000, threadPoolSize);
		long endTime = System.currentTimeMillis();

		long timeDuration = endTime - startTime;

		appendResult(timeDuration, threadPoolSize);
	}

	private static void appendResult(long timeDuration, int threadPoolSize) {
		sb.append(threadPoolSize);
		sb.append(" ");
		sb.append(timeDuration);
		sb.append('\n');
	}
}
