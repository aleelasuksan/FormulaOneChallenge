import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import model.Circuit;

public class CircuitTest {
	private Circuit formula;

	private String formatFloatingPoint(double value) {
		return String.format("%.4f", value);
	}
	
	@Test
	public void SingleTeam() {
		int team = 1;
		int length = 1000;
		formula = new Circuit(team, length);
		formula.start();
		HashMap<Integer, double[]> result = formula.race_result();
		double[] ans = result.get(team);
		// assert time
		assertEquals("31.8811", formatFloatingPoint(ans[0]));
		// assert speed
		assertEquals("44.4444", formatFloatingPoint(ans[1]));
	}
	
	/**
	 * no handling event
	 */
	@Test
	public void TwoTeam() {
		int team = 2;
		int length = 1000;
		formula = new Circuit(team, length);
		formula.start();
		HashMap<Integer, double[]> result = formula.race_result();
		double[] ans = result.get(team);
		// assert time
		assertEquals("31.8811", formatFloatingPoint(ans[0]));
		// assert speed
		assertEquals("44.4444", formatFloatingPoint(ans[1]));
	}
	
	/**
	 * handling event
	 */
	@Test
	public void ThreeTeam() {
		int team = 3;
		int length = 3000;
		formula = new Circuit(team, length);
		formula.start();
		HashMap<Integer, double[]> result = formula.race_result();
		double[] ans = result.get(team);
		// assert time
		assertEquals("31.8811", formatFloatingPoint(ans[0]));
		// assert speed
		assertEquals("44.4444", formatFloatingPoint(ans[1]));
	}
}
