/**
 * A simulation event that sets an elevator's mode and calls its tick() method.
 */

public class ElevatorModeEvent extends SimulationEvent {
	private Elevator.ElevatorState mNewState;
	private OperationMode mNewOperatingMode;
	private Elevator mElevator;
	
	public ElevatorModeEvent(long scheduledTime, Elevator.ElevatorState newState, 
			Elevator elevator, OperationMode newOperationMode) {
		super(scheduledTime);
		mNewState = newState;
		mElevator = elevator;
		mNewOperatingMode = newOperationMode;

	}
	@Override
	public void execute(Simulation sim) {
		mElevator.setMode(mNewOperatingMode);
		mElevator.setState(mNewState);
		mElevator.tick();
		
	}
	
	@Override
	public String toString() {
		return super.toString() + mElevator;
	}
}
