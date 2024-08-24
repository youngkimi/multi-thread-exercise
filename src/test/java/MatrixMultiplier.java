import static multiplier.MatrixGenerator.*;
import static multiplier.callable.MatrixMultiplier.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

public class MatrixMultiplier {

	@Test
	public void matrixMultiplierWith10Thread() {

		int[][] source = matrix1000x1000;
		int threadPoolSize = 10;

		System.out.println();

		long startTime = System.currentTimeMillis();

		int[][] matrix = matrixCallableMultiplier.multiplyMatrixByRowThreadPool(source, source, threadPoolSize);

		long endTime = System.currentTimeMillis();

		long timeDiff = endTime - startTime;

		System.out.println("With ThreadPool size " + threadPoolSize + " : " + timeDiff + " millis");

		for (int[] row : matrix) {
			for (int cell : row) {
				assertEquals(source[0][0] * source[0][0] * row.length, cell);
			}
		}
	}
}
