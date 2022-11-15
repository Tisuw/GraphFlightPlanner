package F28DA_CW2;

import java.util.List;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {

	private List<String> stops;
	private List<String> flights;
	private int hops;
	private int cost;
	private int airTime;
	private int connectingTime;
	private int totalTime;
	
	Journey(List<String> stops, List<String> flights, int hops, int airTime, int connectingTime, int cost){
		this.stops = stops;
		this.flights = flights;
		this.hops = hops;
		this.airTime = airTime;
		this.connectingTime = connectingTime;
		this.totalTime = airTime + connectingTime;
		this.cost = cost;
	}
	
	
	@Override
	public List<String> getStops() {
		return stops;
	}

	@Override
	public List<String> getFlights() {
		return flights;
	}

	@Override
	public int totalHop() {
		return hops;
	}

	@Override
	public int totalCost() {
		return cost;
	}

	@Override
	public int airTime() {
		return airTime;
	}

	@Override
	public int connectingTime() {
		return connectingTime;
	}

	@Override
	public int totalTime() {
		return totalTime;
	}

}
