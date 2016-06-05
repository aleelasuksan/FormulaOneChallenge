package model;

import java.util.HashMap;

/**
 * Circuit class to handle assessment tasks and store information related to racing 
 * (rank, current position of car, cars, result, time)
 * @author Atit Leelasuksan
 *
 */
public class Circuit {

	private Car[] cars;
	//Map team(car) and position
	private HashMap<Integer, Double> car_to_position;
	//Map team(car) and finish time
	private HashMap<Integer, double[]> car_to_finish_time;
	//Map rank with team(car)
	private HashMap<Integer, Integer> rank_to_car;
	//keep a rank of car according to element in array e.g. rank[0] mean rank of team 1's car
	private int[] car_to_rank;
	private double length;
	private int amount_of_team;
	protected boolean finished;
	protected double time_pass;
	
	/**
	 * initialize Circuit object with following parameter
	 * @param team as an amount of team participated cannot be negative value
	 * @param length of track cannot be negative value
	 */
	public Circuit(int team, double length) {	
		this.length = length;
		this.amount_of_team = team;
		cars = new Car[team];
		car_to_rank = new int[team];
		car_to_position = new HashMap<Integer, Double>();
		car_to_finish_time = new HashMap<Integer, double[]>();
		rank_to_car = new HashMap<Integer, Integer>();
		finished = false;
		time_pass = 0;
		initialize();
	}
	
	/**
	 * initialize all car and default value.
	 */
	private void initialize() {
		double position = 0.0;
		for (int i = 1 ; i <= amount_of_team ; i++) {
			cars[i-1] = new Car(i);
			car_to_rank[i-1] = i;
			car_to_position.put(i, position);
			rank_to_car.put(i, i);
			position-=200;
		}
	}
	
	public void start() {
		if( amount_of_team < 1 || length < 1) {
			finished = true;
			System.out.println("This race have less than one team or length of track less than one.");
		} else {
			while(!this.is_finished()) {
				this.assess();
			}
		}
	}
	
	/**
	 * assess all car in racing
	 */
	public void assess() {
		if (!this.is_finished()) {
			for (int i = 0 ; i < amount_of_team ; i++) {
				if (car_to_finish_time.containsKey(i+1)) continue;
				System.out.println("Team: " + (i+1));
				System.out.println("Current Speed: " + cars[i].current_speed);
				double position = car_to_position.get(i+1);
				System.out.println("Current Position: " + position);
				int rank = car_to_rank[i];
				double moved_length = cars[i].assess(2);
				System.out.println("Moved Length: " + moved_length);
				position += moved_length;
				car_to_position.put(i+1, position);
				update_rank(i+1, rank);
				if (position >= length) {
					double[] time_and_speed = calculate_finish_time_and_last_speed(position-moved_length, 
							cars[i].acceleration, cars[i].previous_speed, cars[i].top_speed);
					time_and_speed[0]+=time_pass;
					car_to_finish_time.put(i+1, time_and_speed);
					if (car_to_finish_time.size() == amount_of_team) {
						finished = true;
						break;
					}
				}
			}
			
			for(int i = 0 ; i < amount_of_team ; i++) {
				System.out.println("Team: " + (i+1));
				double position = car_to_position.get(i+1);
				System.out.println("POSITION: " + position);
				int rank = car_to_rank[i];
				boolean nitro = false;
				if (rank == amount_of_team) nitro = true;
				boolean close_to_other_car = false;
				if (amount_of_team > 1) close_to_other_car = check_close_to_car(position, rank);
				Car car = cars[i];
				car.handling_close_car_and_nitro(nitro, close_to_other_car);
				System.out.println("New Current Speed After Handling: " + car.current_speed);
			}
			time_pass += 2;
			System.out.println("TIME: " + time_pass);
			System.out.println();
		} else {
			System.out.println("Racing finished, nothing to assess.");
		}
	}
	
	/**
	 * Calculate finish time and last speed of finished car.
	 * @param position before car pass finish line
	 * @param accelerate of car
	 * @param current_speed of car
	 * @return double array which consist of total time and last speed to finish a track
	 */
	private double[] calculate_finish_time_and_last_speed(double position, double accelerate, double speed, double top_speed) {
		double last_speed, time;
		if (speed == top_speed) {
			last_speed = speed;
			time = (length-position)/speed;
		} else if ( speed+accelerate*2 >= top_speed ) {
			last_speed = top_speed;
			time = (top_speed - speed) / accelerate;
			double intervenve_length = (speed+top_speed)*time/2;
			if (intervenve_length<length-position) {
				double rem_length = length-position-intervenve_length;
				time += rem_length/top_speed;
			}
		} else {
			last_speed = Math.sqrt(speed*speed+2*accelerate*(length-position));
			if (last_speed > top_speed) last_speed = top_speed;
			time = (last_speed-speed)/accelerate;
		}
		return new double[]{time, last_speed};
	}
	
	/**
	 * Determine any closer than 10 meter car
	 * @param position of checking car
	 * @param rank of car
	 * @return true if any car is closer than 10 meter, false otherwise.
	 */
	private boolean check_close_to_car(double position, int rank) {
		boolean close_to_other_car;
		if (rank != amount_of_team && rank != 1 && amount_of_team > 2) {
			double lower_car_position = car_to_position.get(rank_to_car.get(rank+1));
			double diff_lower = position - lower_car_position;
			double upper_car_position = car_to_position.get(rank_to_car.get(rank-1));
			double diff_upper = position - upper_car_position;
			close_to_other_car = (diff_lower <= 10 && diff_lower >= -10) 
					|| (diff_upper <= 10 && diff_upper >= -10);
		} else {
			if (rank == 1) {
				double lower_car_position = car_to_position.get(rank_to_car.get(rank+1));
				double diff_lower = position - lower_car_position;
				close_to_other_car = (diff_lower <= 10 && diff_lower >= -10);
			} else {
				double upper_car_position = car_to_position.get(rank_to_car.get(rank-1));
				double diff_upper = position - upper_car_position;
				close_to_other_car = (diff_upper <= 10 && diff_upper >= -10);
			}
		}
		return close_to_other_car;
	}
	
	/**
	 * Assess rank for team's car, process start from current rank and check for lower rank
	 * @param team to assess
	 * @param rank in present time
	 */
	private void update_rank(int team, int rank) {
		boolean process = true;
		if (rank==1) process = false;
		double position = car_to_position.get(team);
		while(process) {
			int upper_car_team = rank_to_car.get(rank-1);
			double upper_rank_position = car_to_position.get(upper_car_team);
			if( upper_rank_position < position ) {
				rank_to_car.put(rank, upper_car_team);
				rank_to_car.put(rank-1, team);
				car_to_rank[team-1] = rank-1;
				car_to_rank[upper_car_team-1] = rank;
				rank -= 1;
				if (rank == 1) process = false;
			} else {
				process = false;
			}
		}
	}
	
	/**
	 * Is racing finish? All car complete a track
	 * @return
	 */
	public boolean is_finished() {
		return finished;
	}
	
	/**
	 * Result of racing
	 * @return Map of team's car with double array of finished time and last speed
	 */
	public HashMap<Integer, double[]> race_result() {
		return car_to_finish_time;
	}
	
	/**
	 * Time pass since race started.
	 * @return current of use in race
	 */
	public double get_current_time() {
		return time_pass;
	}
}
