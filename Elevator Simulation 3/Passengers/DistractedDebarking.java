/*
 * will not get of at the destination floor; instead, gets off the next time
the doors open after already missing the destination. This mistake only happens once per
passenger. 

When leaving the elevator, if they got off on the wrong floor, schedule the passenger
to reappear on that floor in 5 seconds without changing their destination. If they got off on the
correct floor, tells the passenger to schedule the next step of their travel.
*/

public class DistractedDebarking implements DebarkingStrategy {

	private boolean mistakeHasHappened = false;
	
	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
		
		if (mistakeHasHappened) {
			return passenger.getDestination() == elevator.getCurrentFloor().getNumber();
		}
			
		else {
			
			if (passenger.getDestination() == elevator.getCurrentFloor().getNumber()) {
				return false;
			}
			
			else {
				return elevator.isDoorOpen();
			}
		}
	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator) {
		// TODO Auto-generated method stub
		if (mistakeHasHappened) {
			
			Simulation s = elevator.getBuilding().getSimulation();
	        PassengerNextDestinationEvent event = new PassengerNextDestinationEvent(s.currentTime() + 5, 
	        		passenger, elevator.getCurrentFloor());
	       
	        s.scheduleEvent(event);
		}
		else {
			passenger.scheduleNextDestination(elevator.getCurrentFloor());
		}
		
	}

}
