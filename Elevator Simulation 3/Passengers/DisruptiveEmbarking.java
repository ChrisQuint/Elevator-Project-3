/**
 *Requests the destination floor and also all floors that come after the destination. 
 *For example, if moving up to floor 8 (10 floors total), request 8, 9, and 10.
 */

public class DisruptiveEmbarking implements EmbarkingStrategy {


	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
			
			for (int i = passenger.getDestination(); i >= 1; i--) {
				elevator.requestFloor(passenger.getDestination());	
			}
		}
		
		else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
			for (int i = passenger.getDestination(); i <= elevator.getNumFloors(); i++) {

				elevator.requestFloor(passenger.getDestination());
		}	
	}
	}
	
}
