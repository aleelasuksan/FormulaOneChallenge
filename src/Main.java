import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import model.Circuit;

public class Main {
	public static void main(String[] args) {
		boolean error = true;
		System.out.println("Welcome to Formula One Racing!!!");
		do {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Please specify amount of team: ");
				int team = Integer.parseInt(reader.readLine());
				System.out.print("Please specify length of track(meter): ");
				int length = Integer.parseInt(reader.readLine());
				error = false;
				Circuit formula = new Circuit(team, length);
				formula.start();
				HashMap<Integer, double[]> result = formula.race_result();
				if(!result.isEmpty()) print_result(result);
				System.out.println("Race ended.");
			} catch(NumberFormatException e) {
				System.out.println("Input value is not a number, Please try again.");
			} catch (IOException e) {
				System.out.println("Unknown error, Please try again.");
			}
		} while(error);
		
	}
	
	public static void print_result(HashMap<Integer, double[]> result) {
		System.out.println("Formula one racing result.");
		System.out.println(String.format("%4s | %10s     | %10s", "Team", "Last Speed", "Finished Time"));
		System.out.println("----------------------------------------------------------------------");
		for(int i = 0 ; i < result.size() ; i++) {
			//output element 0 - finished time, 1 - last speed
			double[] output = result.get(i+1);
			System.out.println(String.format("%4d | %10.4f m/s | %10.4f second", i+1, output[1], output[0]));
		}
		System.out.println("END OF RESULT");
	}
}
