/**
 *Requests the destination floor and also the floor that comes before the destination. 
 *For example, if moving down to floor 5, we request both floor 5 and 6.
 */

public class ClumsyEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
			
			elevator.requestFloor(passenger.getDestination());
			elevator.requestFloor(passenger.getDestination() - 1);		
		}
		
		else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
			
			elevator.requestFloor(passenger.getDestination());
			elevator.requestFloor(passenger.getDestination() + 1);		
		}
	}
}
