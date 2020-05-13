
import java.util.*;

public class Building implements ElevatorObserver, FloorObserver {
	private List<Elevator> mElevators = new ArrayList<>();
	private List<Floor> mFloors = new ArrayList<>();
	private Simulation mSimulation;
	private Queue<Integer> mWaitingFloors = new ArrayDeque<>();

	public Building(int floors, int elevatorCount, Simulation sim) {
		mSimulation = sim;

		// Construct the floors, and observe each one.
		for (int i = 0; i < floors; i++) {
			Floor f = new Floor(i + 1, this);
			f.addObserver(this);
			mFloors.add(f);
		}

		// Construct the elevators, and observe each one.
		for (int i = 0; i < elevatorCount; i++) {
			Elevator elevator = new Elevator(i + 1, this);
			elevator.addObserver(this);
			for (Floor f : mFloors) {
				elevator.addObserver(f);
			}
			mElevators.add(elevator);
		}
	}


	public String toString() {
		
		String output = "";
		String x = "|";
		String y = "";
		
		for (int i = this.getFloorCount() - 1; i > -1; i--) {
			for (int j = 0; j < mElevators.size(); j++) {
				if (mElevators.get(j).getCurrentFloor().getNumber() - 1 == i) {
					x += " X |";
				} 
				else {
					x += "   |";
				}
			}
			for (int k = 0; k < mFloors.get(i).getWaitingPassengers().size(); k++) {
				y += mFloors.get(i).getWaitingPassengers().get(k) + "->"+(mFloors.get(i).getWaitingPassengers().get(k).getDestination()) + "  ";
			}
			//Setting up display for return and resetting others
			output += String.format("%2d : %s %s \n", (i + 1), x, y);
			x = "|";
			y = "";
		}
		for (int i = 0; i < mElevators.size(); i++) {
			output += mElevators.get(i).toString() + "\n";
		}
		return output;
	}


	public int getFloorCount() {
		return mFloors.size();
	}

	public Floor getFloor(int floor) {
		return mFloors.get(floor - 1);
	}

	public Simulation getSimulation() {
		return mSimulation;
	}


	@Override
	public void elevatorDecelerating(Elevator elevator) {
		// Have to implement all interface methods even if we don't use them.
	}

	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Don't care.
	}

	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// TODO: if mWaitingFloors is not empty, remove the first entry from the queue and dispatch the elevator to that floor.
		if (!mWaitingFloors.isEmpty()) {
			int firstNum = mWaitingFloors.poll();
			elevator.dispatchTo(mFloors.get(firstNum - 1));
		}
	}

	@Override
	public void elevatorArriving(Floor sender, Elevator elevator) {
		// TODO: add the floor mWaitingFloors if it is not already in the queue.
//		if (!mWaitingFloors.contains(sender)) {
//			mWaitingFloors.add(sender.getNumber());
//		}
	}

	@Override
	public void directionRequested(Floor floor, Elevator.Direction direction) {
		// TODO: go through each elevator. If an elevator is idle, dispatch it to the given floor.
		// TODO: if no elevators are idle, then add the floor number to the mWaitingFloors queue.
		
		boolean dispatchElev = false;
		
		for (Elevator e : mElevators) {
			if (e.isIdle()) {
				e.dispatchTo(floor);
				dispatchElev = true;
				break;
			}
		}
		if (!dispatchElev){
			this.mWaitingFloors.add(floor.getNumber());
		}
	}
}
