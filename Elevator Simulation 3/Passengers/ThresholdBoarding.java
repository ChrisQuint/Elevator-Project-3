/**
 * A Threshold Boarding is a boarding strategy constructed with an integer threshold; will board if the passenger count has 
 * not exceeded the threshold.
 */

public class ThresholdBoarding implements BoardingStrategy {

	private int mThresHold;
	
	public ThresholdBoarding (int threshold) {
		mThresHold = threshold;
	}
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		return elevator.getPassengerCount() < mThresHold;
	}
}
