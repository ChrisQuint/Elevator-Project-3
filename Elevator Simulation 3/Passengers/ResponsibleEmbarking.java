/**
 * Only requests the passenger's destination floor
 */

public class ResponsibleEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		
		elevator.requestFloor(passenger.getDestination());		
	}

}
