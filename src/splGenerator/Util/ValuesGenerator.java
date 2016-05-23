package splGenerator.Util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;

import com.sun.xml.internal.ws.api.pipe.NextAction;

public class ValuesGenerator {

	private static double minReliability = 0.800, maxReliability = 0.999;
	private static HashSet<Double> reliabilityValues;
	private static int numberOfDecimals = 3;

	/**
	 * This method is responsible for creating a given number of different
	 * reliability values, according to the value informed by the input
	 * parameter.
	 * 
	 * @param numberOfValues
	 *            number of different reliability values to be created
	 */
	public static void generateRandomReliabilityValues(int numberOfValues) {
		reliabilityValues = new HashSet<Double>();
		Random r = new Random();

		int idxValues = 0;
		while (idxValues < numberOfValues) {
			Double value = minReliability + (maxReliability - minReliability)
					* r.nextDouble();
			Double randomValue = BigDecimal.valueOf(value)
					.setScale(numberOfDecimals, BigDecimal.ROUND_HALF_DOWN)
					.doubleValue();
			boolean answer = reliabilityValues.add(randomValue);
			if (answer == true)
				idxValues++;
		}
	}

	public static double getReliabilityValue() {
		Random r = new Random();

		int idx = r.nextInt(reliabilityValues.size());
		Object[] answer = reliabilityValues.toArray();
		return (double) answer[idx];
	}

}
