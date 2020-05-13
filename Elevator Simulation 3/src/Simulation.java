
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
	private Random mRandom;
	private PriorityQueue<SimulationEvent> mEvents = new PriorityQueue<>();
	private long mCurrentTime;
	
	//Added for simulation to access building, which is used in factory classes for travel methods
	private Building mBuilding;
	
	private List<PassengerFactory> mPassengerFactories = new ArrayList<PassengerFactory>();
	private List<Integer> weights = new ArrayList<Integer>();
	
	/**
	 * Seeds the Simulation with a given random number generator.
	 */
	public Simulation(Random random, int visitorWeight, int workerWeight, int  childw, int deliveryw, int stonerw, int jerkw) {
		mRandom = random;

		mPassengerFactories.add(new VisitorFactory(visitorWeight));
		mPassengerFactories.add(new WorkerFactory(workerWeight));
		mPassengerFactories.add(new ChildFactory(childw));
		mPassengerFactories.add(new DeliveryPersonFactory(deliveryw));
		mPassengerFactories.add(new StonerFactory(stonerw));
		mPassengerFactories.add(new JerkFactory(jerkw));


	}
	
	public List<Integer> getWeights() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mPassengerFactories.size(); i++) {
			weights.add(mPassengerFactories.get(i).factoryWeight());
		}
		return weights;
	}
	
	
	public List<PassengerFactory> getPassengerFactories() {
		// TODO Auto-generated method stub
		return mPassengerFactories;
	}

	//Added to access the building
	public Building getBuilding() {
		return mBuilding;
	}
	
	/**
	 * Gets the current time of the simulation.
	 */
	public long currentTime() {
		return mCurrentTime;
	}
	
	/**
	 * Access the Random object for the simulation.
	 */
	public Random getRandom() {
		return mRandom;
	}
	
	/**
	 * Adds the given event to a priority queue sorted on the scheduled time of execution.
	 */
	public void scheduleEvent(SimulationEvent ev) {
		mEvents.add(ev);
	}
	
	public void startSimulation(Scanner input) {
		
		mBuilding = new Building(10, 1, this);
		SpawnPassengerEvent ev = new SpawnPassengerEvent(0, mBuilding);
		scheduleEvent(ev);
		
		long nextSimLength = -1;
		
		// Set this boolean to true to make the simulation run at "real time".
		boolean simulateRealTime = false;
		// Change the scale below to less than 1 to speed up the "real time".
		double realTimeScale = 1.0;
		
		// TODO: the simulation currently stops at 200s. Instead, ask the user how long they want to simulate.
		//nextSimLength = 200;
		System.out.println("How long do you want to simulate?");
		nextSimLength = input.nextInt();
		
		while (nextSimLength != -1) {
			
			long nextStopTime = mCurrentTime + nextSimLength;
		
			// If the next event in the queue occurs after the requested sim time, then just fast forward to the requested sim time.
			if (mEvents.peek().getScheduledTime() >= nextStopTime) {
				mCurrentTime = nextStopTime;
			}
		
			// As long as there are events that happen between "now" and the requested sim time, process those events and
			// advance the current time along the way.
			while (!mEvents.isEmpty() && mEvents.peek().getScheduledTime() <= nextStopTime) {
				SimulationEvent nextEvent = mEvents.poll();
			
				long diffTime = nextEvent.getScheduledTime() - mCurrentTime;
				if (simulateRealTime && diffTime > 0) {
					try {
						Thread.sleep((long)(realTimeScale * diffTime * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			
			mCurrentTime += diffTime;
			nextEvent.execute(this);
			System.out.println(nextEvent);
		}
		
		// TODO: print the Building after simulating the requested time.
        System.out.println(mBuilding.toString());
		System.out.println("How long do you want to simulate? Enter -1 to end: ");
        nextSimLength = input.nextInt();
	}
		
		/*
		 TODO: the simulation stops after one round of simulation. Write a loop that continues to ask the user
		 how many seconds to simulate, simulates that many seconds, and stops only if they choose -1 seconds.
		*/
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		// TODO: ask the user for a seed value and change the line below.
		System.out.println("Please provide a seed value: ");
		Random seed = new Random(s.nextInt());
		
		System.out.println("Please input weights for each passenger.\n");
		System.out.println("Visitor: ");
		int visitorw = s.nextInt();
		
		System.out.println("Worker: ");
		int workerw = s.nextInt();

		System.out.println("Child : ");
		int childw = s.nextInt();

		System.out.println("Delivery person: ");
		int deliveryw = s.nextInt();

		System.out.println("Stoner: ");
		int stonerw = s.nextInt();

		System.out.println("Jerk: ");
		int jerkw = s.nextInt();

		Simulation sim = new Simulation(seed, visitorw , workerw, childw, deliveryw, stonerw, jerkw);
		
		sim.startSimulation(s);
	}

}
