package multiplier;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static multiplier.MatrixGenerator.*;
import static multiplier.callable.MatrixMultiplier.matrixCallableMultiplier;

public class Main {

	private static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String filePath = "c:/Users/subst/Desktop/outputs/multiplier/bycell.txt";

		FileWriter fw = new FileWriter(filePath, true);

		fw.write(sb.toString());
		fw.close();
	}

	private static void calculateWithThreadPool(int threadPoolSize) {
		long startTime = System.currentTimeMillis();
		matrixCallableMultiplier.multiplyMatrixByRowThreadPool(matrix1000x1000, matrix1000x1000, 10);
		long endTime = System.currentTimeMillis();

		long timeDuration = endTime - startTime;

		appendResult(timeDuration, threadPoolSize);
	}

	private static void calculateWithCellUnit(int threadPoolSize) {
		long startTime = System.currentTimeMillis();
		matrixCallableMultiplier.multiplyMatrixByCell(matrix1000x1000, matrix1000x1000);
		long endTime = System.currentTimeMillis();

		long timeDuration = endTime - startTime;

		appendResult(timeDuration, threadPoolSize);
	}

	private static void calculateWithRowUnit(int threadPoolSize) {
		long startTime = System.currentTimeMillis();
		matrixCallableMultiplier.multiplyMatrixByRow(matrix1000x1000, matrix1000x1000);
		long endTime = System.currentTimeMillis();

		long timeDuration = endTime - startTime;

		appendResult(timeDuration, threadPoolSize);
	}

	private static void appendResult(long timeDuration, int threadPoolSize) {
		sb.append(threadPoolSize);
		sb.append(" ");
		sb.append(timeDuration);
		sb.append("\r\n");
	}
}
