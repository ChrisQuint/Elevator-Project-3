import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkerFactory implements PassengerFactory {

	private int mWeight;
	
	public WorkerFactory(int weight) {
		// TODO Auto-generated constructor stub
		mWeight = weight;
	}

	@Override
	public String factoryName() {
		// TODO Auto-generated method stub
		return "Worker";
	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return "W";
	}

	@Override
	public int factoryWeight() {
		// TODO Auto-generated method stub
		return mWeight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ThresholdBoarding(5);
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		
		List <Integer> destinations = new ArrayList<>();
		List<Long> durations = new ArrayList<>();

		Building building = simulation.getBuilding();
		Random r = simulation.getBuilding().getSimulation().getRandom();
		
		int numDestination = r.nextInt(4) + 2;
		
		int last = -1;
		
		for (int i = 0; i < numDestination; i++) {
			int nextFloor = r.nextInt(building.getFloorCount() - 1) + 2;
			
			while (nextFloor == last) {
				nextFloor = r.nextInt(building.getFloorCount() - 1) + 2;
			}
			
			destinations.add(nextFloor);
			last = nextFloor;
		}
		
		for (int i = 0; i < numDestination; i++) {
			durations.add((long) (600 + r.nextGaussian() * 180));
		}
		
		return new MultipleDestinationTravel(destinations, durations);
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new ResponsibleEmbarking();
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		// TODO Auto-generated method stub
		return new AttentiveDebarking();
	}

}
