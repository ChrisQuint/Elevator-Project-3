/**
 * An AwkwardBoarding is a boarding strategy constructed with an integer threshold; will board if the passenger count has 
 * not exceeded the threshold. The threshold grows by 2 each time the passenger decides not to board.
 */
public class AwkwardBoarding implements BoardingStrategy{

	private int mThresHold;
	
	public AwkwardBoarding (int threshold) {
		mThresHold = threshold;
	}
	
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		if (elevator.getPassengerCount() < mThresHold) {
			return true;
		}
		else {
			mThresHold += 2;
			return false;
		}
	}
}
