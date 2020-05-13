import java.util.ArrayList;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {

	// TODO: implement this class.
	// An active elevator cannot be dispatched, and will ignore direction requests from its current floor. (Only idle
	//    mode elevators observe floors, so an ActiveMode elevator will never observe directionRequested.)
	// The bulk of your Project 2 tick() logic goes here, except that you will never be in IDLE_STATE when active.
	// If you used to schedule a transition to IDLE_STATE, you should instead schedule an operation change to
	//    IdleMode in IDLE_STATE.
	// Otherwise your code should be almost identical, except you are no longer in the Elevator class, so you need
	//    to use accessors and mutators instead of directly addressing the fields of Elevator.
	
	
	
	@Override
	public String toString() {
		return "Active";
	}

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {
		// TODO Auto-generated method stub
//		elevator.getCurrentFloor().removeObserver(elevator);
//		elevator.scheduleModeChange(new DispatchMode(targetFloor, targetDirection),
//				Elevator.ElevatorState.IDLE_STATE, 0);
		
	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Elevator elevator) {
		// TODO Auto-generated method stub


switch (elevator.getCurrentState()){
	case DECELERATING:
//			    boolean a;

				elevator.getFloorsRequested().set(elevator.getCurrentFloor().getNumber()-1,false);

				boolean keepGoing;
				if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP &&
						(elevator.getFloorsRequested().lastIndexOf(true) >= elevator.getCurrentFloor().getNumber()
								|| elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_UP))) {

					keepGoing = true;
				}
				else if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN &&
						((elevator.getFloorsRequested().indexOf(true) < elevator.getCurrentFloor().getNumber()  && elevator.getFloorsRequested().indexOf(true)>-1 )
								||   elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_DOWN)    )) { // finish this, but beware: indexOf returns -1 if not found
					keepGoing = true;
				}
				else {
					keepGoing = false;
				}

				if (!keepGoing){
					if (elevator.getFloorsRequested().indexOf(true)<0){
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
					}else{
						if (elevator.getCurrentDirection()==Elevator.Direction.MOVING_UP){
							elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
						}else if (elevator.getCurrentDirection()==Elevator.Direction.MOVING_DOWN){
							elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
						}
					}
				}

				elevator.announceElevatorDecelerating();

				elevator.scheduleModeChange(this, Elevator.ElevatorState.DOORS_OPENING,3);

				break;


	case DOORS_OPENING:

		elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPEN,2);
		break;

	case DOORS_OPEN:
		int e0 = elevator.getPassengerCount();
		int f0= elevator.getCurrentFloor().getWaitingPassengers().size();
//				System.out.println(f0);

		elevator.announceDoorsOpened();


		int e1 = elevator.getPassengerCount();
		int f1 = elevator.getCurrentFloor().getWaitingPassengers().size();
//				System.out.println(f1);
		int numLeft = (e0-e1) + (f0-f1);
		int numJoined = f0 - f1;
		int peopleThatMoved = numJoined + numLeft;

//                System.out.println(x);
		elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_CLOSING, 1 + peopleThatMoved / 2);

		break;

	case ACCELERATING:

		elevator.getCurrentFloor().removeObserver(elevator);
		elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,3);
		break;


	case MOVING:

		if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP ){
			int temp = elevator.getCurrentFloor().getNumber() +1;
			if (temp <=elevator.getBuilding().getFloorCount())
				elevator.setCurrentFloor(elevator.getBuilding().getFloor(temp));
		}else{
			int temp1 = elevator.getCurrentFloor().getNumber()-1;
			if (temp1>=1) {
				elevator.setCurrentFloor(elevator.getBuilding().getFloor(temp1));
			}
		}

		if (elevator.getFloorsRequested().get(elevator.getCurrentFloor().getNumber()-1)){
//						System.out.println("I decelarate mrequest");
			elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING,2);
		}else if(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber()).directionIsPressed(elevator.getCurrentDirection())){
			elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING,2);
//					System.out.println("I decelarate mBuilding");
		}
		else{
			elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,2);
		}


		break;

	case DOORS_CLOSING:
		boolean a = false;
		if (elevator.getPassengers().isEmpty()) {
			elevator.scheduleModeChange(new IdleMode(), Elevator.ElevatorState.IDLE_STATE, 0);
			elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
		}
		else if (!elevator.getPassengers().isEmpty()) {
			for (int i = 0; i < elevator.getFloorsRequested().size(); i++) {
				if (i>elevator.getCurrentFloor().getNumber()-1 && elevator.getFloorsRequested().get(i) && (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP ||elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING)) {
					elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
					a = true;
					break;
				}else if (i<elevator.getCurrentFloor().getNumber()-1 && elevator.getFloorsRequested().get(i) && (elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN ||elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING)) {
					elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
					a = true;
					break;
				}
				else if (!elevator.getFloorsRequested().get(i)) {
					a = false;
				}
			}
			if (a){
				elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 2);
			}else if (elevator.getFloorsRequested().get(elevator.getCurrentFloor().getNumber()-1)) {
				if (elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
					elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
				} else {
					elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
				}
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING, 2);
			}
		}
		if (elevator.getFloorsRequested().get(0) && elevator.getCurrentFloor().getNumber() ==1 ){
			elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
		}
		if (elevator.getFloorsRequested().get(elevator.getBuilding().getFloorCount()-1) && elevator.getCurrentFloor().getNumber() == elevator.getBuilding().getFloorCount()){
			elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
		}


		break;




}



	}


}

