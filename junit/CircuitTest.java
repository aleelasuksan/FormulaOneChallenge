import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import model.Circuit;

public class CircuitTest {
	private Circuit formula;

	private String formatFloatingPoint(double value) {
		return String.format("%.4f", value);
	}
	
	/**
	 * test physic formulation
	 * let "u" is a start velocity, "v" is a last velocity, "a" is acceleration, "t" is time, "s" is length
	 * v = u + at use to find a velocity at the end of assessment (assess at 2 second, v is a value of car velocity at 2 second.)
	 * Point is at one time car will reach top speed which is 44.4444 m/s for first team (come from 160*1000/3600)
	 * to calculate moved length we use v*v = u*u + 2*a*s to eliminate time variable which may inconsistent at the point when speed almost reach top speed.
	 * or s = ut + (0.5a)t^2 which use for constant acceleration situation.
	 * after car reach top speed it can use only simple formulation below
	 * s = vt is a very simple mechanics formulation, can use only in constant speed.
	 * 
	 * When using manual calculation result might differ from a result because I always truncate a long floating point 
	 * thus it lead to a big different value
	 * if you interest in more detail information I have my print statement which I use to verify & debug formulation in Circuit.java and Car.java (commented)
	 */
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
	 * no handling event, have nitro event
	 */
	@Test
	public void TwoTeam() {
		int team = 2;
		int length = 1000;
		formula = new Circuit(team, length);
		formula.start();
		HashMap<Integer, double[]> result = formula.race_result();
		double[] result1 = result.get(1);
		double[] result2 = result.get(2);
		// team 1 assert time
		assertEquals("31.2600", formatFloatingPoint(result1[0]));
		// team 1 assert speed 
		assertEquals("44.4444", formatFloatingPoint(result1[1]));

		// team 2 assert time
		assertEquals("29.8228", formatFloatingPoint(result2[0]));
		// team 2 assert speed 
		assertEquals("47.2222", formatFloatingPoint(result2[1]));
	}
	
	/**
	 * handling event and nitro event
	 */
	@Test
	public void ThreeTeam() {
		int team = 3;
		int length = 3000;
		formula = new Circuit(team, length);
		formula.start();
		HashMap<Integer, double[]> result = formula.race_result();
		double[] result1 = result.get(1);
		double[] result2 = result.get(2);
		double[] result3 = result.get(3);
		// team 1 assert time
		assertEquals("78.4980", formatFloatingPoint(result1[0]));
		// team 1 assert speed 
		assertEquals("44.4444", formatFloatingPoint(result1[1]));
		
		// team 2 assert time
		assertEquals("74.1955", formatFloatingPoint(result2[0]));
		// team 2 assert speed 
		assertEquals("47.2222", formatFloatingPoint(result2[1]));
				
		// team 3 assert time
		assertEquals("71.3867", formatFloatingPoint(result3[0]));
		// team 3 assert speed 
		assertEquals("50.0000", formatFloatingPoint(result3[1]));
	}
}
