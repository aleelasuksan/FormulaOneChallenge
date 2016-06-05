package model;

/**
 * Car object for Circuit. consist of top speed, acceleration, nitro status, current speed and team.
 * @author Atit Leelasuksan
 *
 */
public class Car {
	
	//meter per second
	protected double top_speed;
	//meter per second square
	protected double acceleration;
	private static double HANDLING_FACTOR = 0.8;
	//store a boolean value of nitro usage, can use only once.
	private boolean nitroed;
	protected double current_speed;
	protected double previous_speed;
	protected int team;
	
	/**
	 * initialize new Car object with team number as a input.
	 * @param team number, use to calculate top speed and acceleration value.
	 */
	public Car(int team) {
		top_speed = (150.0 + 10.0 * team) * 1000.0 / 3600.0;
		acceleration = 2.0 * team;
		nitroed = false;
		current_speed = 0.0;
		previous_speed = current_speed;
		this.team = team;
	}
	
	/**
	 * Assess car speed
	 * @param second, time pass after last assessment.
	 * @param close_to_other_car, if close to other car then driver need to reduce car's speed.
	 * @return how far car can go within second parameter time, meter unit
	 */
	public double assess(int second) {
		double last_speed = current_speed + (acceleration * second);
		double moved_length;
		if (current_speed < top_speed && last_speed > top_speed) {
			double time_to_reach_top = (top_speed-current_speed)/acceleration;
			double time_left = second - time_to_reach_top;
			double time_to_reach_top_length = ((top_speed*top_speed) - (current_speed*current_speed)) / (2*acceleration);
			double time_left_length = top_speed*time_left;
			moved_length = time_to_reach_top_length + time_left_length;
		} else if (current_speed == top_speed) {
			moved_length = current_speed*second;
		}
		// physics formula for static acceleration, s = ut + (0.5a)t^2
		else moved_length = (current_speed*second)+(0.5*acceleration*second*second);
		previous_speed = current_speed;
		current_speed = last_speed;
		System.out.println("Last Speed: " + last_speed);
		check_current_speed();
		System.out.println("New Current Speed: " + current_speed);
		return moved_length;
	}
	
	/**
	 * Handling close car and nitro trigger, invoke after assess all car position.
	 * @param use_nitro trigger nitro
	 * @param close_to_other_car trigger handling factor
	 */
	public void handling_close_car_and_nitro(boolean use_nitro, boolean close_to_other_car) {
		if (use_nitro && current_speed > 0 && current_speed != top_speed) {
			nitro();
		} else if (close_to_other_car) {
			System.out.println("HANDLING");
			current_speed *= HANDLING_FACTOR;
		}
		check_current_speed();
	}
	
	/**
	 * use nitro, increase current speed to double or top speed.
	 * @return true if nitro activated, false otherwise.
	 */
	private boolean nitro() {
		if (!nitroed) {
			System.out.println("NITRO!");
			nitroed = true;
			current_speed *= 2.0;
			return true;
		}
		return false;
	}
	
	/**
	 * Verify current speed to capped at top speed
	 */
	private void check_current_speed() {
		if(current_speed > top_speed) {
			System.out.println("Reached Top Speed");
			current_speed = top_speed;
		}
	}
}
