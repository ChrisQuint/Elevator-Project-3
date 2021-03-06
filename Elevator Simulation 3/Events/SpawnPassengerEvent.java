
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
/**
 * A simulation event that adds a new random passenger on floor 1, and then schedules the next spawn event.
 */
public class SpawnPassengerEvent extends SimulationEvent {
//	private static long SPAWN_MEAN_DURATION = 10_800;
//	private static long SPAWN_STDEV_DURATION = 3_600;

	// After executing, will reference the Passenger object that was spawned.
	private Passenger mPassenger;
	private Building mBuilding;
	
	public SpawnPassengerEvent(long scheduledTime, Building building) {
		super(scheduledTime);
		mBuilding = building;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Adding " + mPassenger.getPassengerName() + " " + mPassenger.getId() + " [-> " + 
				mPassenger.getDestination() + "] to floor 1.";
	}
	
	@Override
	public void execute(Simulation sim) {

		List<PassengerFactory> factories = sim.getPassengerFactories();

		//Take each element's weight from the simulation class and sum the total of weights
		List<Integer> weights = sim.getWeights();
		int totalWeight = 0;

		for (int i = 0; i < weights.size(); i++) {
			totalWeight += weights.get(i);
		}
		System.out.println("Total Weights:"+ totalWeight);

		Random r = sim.getRandom();
		int randomn = r.nextInt(totalWeight);
		int nextCutoff = 0;
		//Iterate over all the factories in the list
		for (PassengerFactory factory: factories) {
			//Find the sum of the weights of the factories
			nextCutoff += factory.factoryWeight();
			//If the random number generated is less than the sum, create that passenger, else break
			if (randomn < nextCutoff) {
				mPassenger = new Passenger(factory.factoryName(), factory.shortName(), factory.createBoardingStrategy(sim),
						factory.createEmbarkingStrategy(sim), factory.createDebarkingStrategy(sim),
						factory.createTravelStrategy(sim));
				break;
			}
		}

		mBuilding.getFloor(1).addWaitingPassenger(mPassenger);

		/*
		 TODO: schedule the new SpawnPassengerEvent with the simulation. Construct a new SpawnPassengerEvent
		 with a scheduled time that is X seconds in the future, where X is a uniform random integer from
		 1 to 30 inclusive.
		*/

		int min = 1;
		int max = 30;
		int scheduledTime = r.nextInt(max) + min;

		SpawnPassengerEvent pEvent = new SpawnPassengerEvent(sim.currentTime() + scheduledTime, mBuilding);

		//Passed in
		sim.scheduleEvent(pEvent);
	}
	
	
//	private Passenger getVisitor() {
//		/*
//		 TODO: construct a VisitorPassenger and return it.
//		 The visitor should have a random destination floor that is not floor 1 (generate a random int from 2 to N).
//		 The visitor's visit duration should follow a NORMAL (GAUSSIAN) DISTRIBUTION with a mean of 1 hour
//		 and a standard deviation of 20 minutes.
//		 */
//
//		Random r = mBuilding.getSimulation().getRandom();
//
//		int minFloor = 2;
//		int maxFloor = mBuilding.getFloorCount();
//		int randomFloor = r.nextInt((maxFloor - minFloor) + 1) + minFloor;
//
//	    // Look up the documentation for the .nextGaussian() method of the Random class.
//	    double val = r.nextGaussian() * 1200 + 3600;
//
////	    VisitorPassenger visitor = new VisitorPassenger(randomFloor, (int) Math.round(val));
//
//	    CapacityBoarding capacityBoarding = new CapacityBoarding();
//	    ResponsibleEmbarking responsibleEmbarking = new ResponsibleEmbarking();
//	    AttentiveDebarking attentiveDebarking = new AttentiveDebarking();
//	    SingleDestinationTravel singleTravel = new SingleDestinationTravel(randomFloor, (int) Math.round(val));
//
//	    Passenger visitor = new Passenger("Visitor", "V", capacityBoarding, responsibleEmbarking, attentiveDebarking, singleTravel);
//
//	    return visitor;
//	}
	
//	private Passenger getWorker() {
//		/*
//		TODO: construct and return a WorkerPassenger. A Worker requires a list of destinations and a list of durations.
//		To generate the list of destinations, first generate a random number from 2 to 5 inclusive. Call this "X",
//		how many floors the worker will visit before returning to floor 1.
//		X times, generate an integer from 2 to N (number of floors) that is NOT THE SAME as the previously-generated floor.
//		Add those X integers to a list.
//		To generate the list of durations, generate X integers using a NORMAL DISTRIBUTION with a mean of 10 minutes
//		and a standard deviation of 3 minutes.
//		 */
//
//		ArrayList<Integer> destinations = new ArrayList<>();
//	    ArrayList<Long> durations = new ArrayList<>();
//
//	    Random r = mBuilding.getSimulation().getRandom();
//
//	    long dur;
//	    int lowFloor = 2;
//	    int highFloor = mBuilding.getFloorCount();
//	    int rfloor = 1;
//	    int x = r.nextInt(4) + 2;
//		int lastFloor = 0;
//
//	    for (int i = 0; i < x; i++) {
//	    	rfloor = r.nextInt((highFloor - lowFloor) + 1) + lowFloor;
//
//	    	if (lastFloor == rfloor){
//				rfloor = r.nextInt((highFloor-lowFloor) + 1) + lowFloor;
//			}
//			lastFloor = rfloor;
//
//			destinations.add(rfloor);
//		}
//
//	    for (int i = 0; i < x; i++) {
//
//	    	dur = (long) r.nextGaussian() * 180 + 600;
//	    	durations.add(dur);
//	    }
//
//	//   WorkerPassenger WorkPass = new WorkerPassenger(destinations, durations);
//
//	    ThresholdBoarding thresholdBoarding = new ThresholdBoarding(3);
//	    ResponsibleEmbarking responsibleEmbarking = new ResponsibleEmbarking();
//	    AttentiveDebarking attentiveDebarking = new AttentiveDebarking();
//	    MultipleDestinationTravel multipleTravel = new MultipleDestinationTravel(destinations, durations);
//
//	    Passenger worker = new Passenger("Worker", "W", thresholdBoarding, responsibleEmbarking, attentiveDebarking, multipleTravel);
//
//	    return worker;
//	   }
	}


