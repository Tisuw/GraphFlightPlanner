package F28DA_CW2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport, Flight>, IFlyingPlannerPartC<Airport, Flight> {
	public Graph<Airport, Flight> flightGraph;
	public DirectedAcyclicGraph<Airport, Flight> dag;

	@Override
	public boolean populate(FlightsReader fr) {
		flightGraph = new SimpleDirectedWeightedGraph<>(Flight.class);
		try {
			// Get HashSet of String arrays of airports
			HashSet<String[]> airports = fr.getAirports();
			// Add all the airports to the graph
			for (String[] a : airports) {
				flightGraph.addVertex(new Airport(a));
			}
			// Get HashSet of String arrays of flights
			HashSet<String[]> flights = fr.getFlights();
			// Add all the flights to the graph, as weighted edges, the weight being the
			// cost
			for (String[] f : flights) {
				Flight tmp = new Flight(f[0], airport(f[3]), airport(f[1]), f[4], f[2], Integer.parseInt(f[5]));
				flightGraph.addEdge(tmp.getFrom(), tmp.getTo(), tmp);
				flightGraph.setEdgeWeight(tmp, tmp.getCost());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		flightGraph = new SimpleDirectedWeightedGraph<>(Flight.class);
		try {
			// Add all the airports to the graph
			for (String[] a : airports) {
				flightGraph.addVertex(new Airport(a));
			}
			// Add all the flights to the graph, as weighted edges, the weight being the
			// cost
			for (String[] f : flights) {
				Flight tmp = new Flight(f[0], airport(f[3]), airport(f[1]), f[4], f[2], Integer.parseInt(f[5]));
				flightGraph.addEdge(tmp.getFrom(), tmp.getTo(), tmp);
				flightGraph.setEdgeWeight(tmp, tmp.getCost());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Airport airport(String code) {
		/**
		 * Find the airport with the airport code, "code" This code was modified from
		 * the website "https://jgrapht.org/guide/UserOverview#graph-accessors" It
		 * iterates over the vertices of flightGraph, filtering out any vertex that does
		 * not have the same airport code as "code", as airport codes are unique there
		 * is no chance of finding the wrong airport.
		 **/
		Airport tmp = flightGraph.vertexSet().stream().filter(Airport -> Airport.getCode().equals(code)).findAny()
				.get();
		return tmp;
	}

	@Override
	public Flight flight(String code) {
		/**
		 * Find the flight with the flight code, "code" This code was modified from the
		 * website "https://jgrapht.org/guide/UserOverview#graph-accessors" It iterates
		 * over the edges of flightGraph, filtering out any edge that does not have the
		 * same flight code as "code", as flight codes are unique there is no chance of
		 * finding the wrong flight.
		 **/
		Flight tmp = flightGraph.edgeSet().stream().filter(Flight -> Flight.getFlightCode().equals(code)).findAny()
				.get();
		return tmp;
	}

	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		DijkstraShortestPath<Airport, Flight> cheap = new DijkstraShortestPath<Airport, Flight>(flightGraph);
		Airport left = airport(from);
		Airport dest = airport(to);

		// Find edges (flights) of shortest path
		List<Flight> flights = cheap.getPath(left, dest).getEdgeList();
		List<String> flightCodes = new ArrayList<>(flights.size());
		// Initialise variables
		int totalCost = 0;
		int airTime = 0;
		int connectingTime = 0;
		int countHops = 0;
		Flight lastFlight = null;
		// For every flight on the list, record the flight code, total the cost, time,
		// and hops
		for (Flight f : flights) {
			flightCodes.add(f.getFlightCode());
			totalCost += f.getCost();
			airTime += getFlightMins(f);
			connectingTime += getConnectingTime(lastFlight, f);
			countHops++;
			lastFlight = f;
		}

		List<Airport> stops = cheap.getPath(left, dest).getVertexList();
		List<String> airports = new ArrayList<>(stops.size());
		// Create list of all airport codes.
		for (Airport a : stops) {
			airports.add(a.getCode());
		}
		// return journey with all details
		return new Journey(airports, flightCodes, countHops, airTime, connectingTime, totalCost);
	}

	/**
	 * Returns how long the flight is in minutes
	 */
	int getFlightMins(Flight f) {
		// Get the time of departure
		int startHours = Integer.parseInt(f.getFromGMTime()) / 100;
		int startMins = (Integer.parseInt(f.getFromGMTime()) - (startHours * 100)) % 60;
		// Get the time of the landing
		int finishHours = Integer.parseInt(f.getToGMTime()) / 100;
		int finishMins = (Integer.parseInt(f.getToGMTime()) - (finishHours * 100)) % 60;

		return getDifference(startMins, startHours, finishMins, finishHours);

	}

	/**
	 * Returns how long the connection is in minutes
	 */
	int getConnectingTime(Flight prev, Flight next) {
		if (prev == null || next == null)
			return 0;
		// Get the time of first departure
		int startHours = Integer.parseInt(prev.getToGMTime()) / 100;
		int startMins = (Integer.parseInt(prev.getToGMTime()) - (startHours * 100)) % 60;
		// Get the time of the final landing
		int finishHours = Integer.parseInt(next.getFromGMTime()) / 100;
		int finishMins = (Integer.parseInt(next.getFromGMTime()) - (finishHours * 100)) % 60;

		return getDifference(startMins, startHours, finishMins, finishHours);
	}

	/**
	 * Returns the difference in time, in minutes
	 */
	int getDifference(int startMins, int startHours, int finishMins, int finishHours) {
		int totalMins = finishMins - startMins;
		// Find the difference in minutes, accounting for change in hours
		if (totalMins < 0) {
			totalMins += 60;
			if (startHours == 23)
				startHours = 0;
			else
				startHours++;
		}
		// Find hour difference, accounting for change of day
		int totalHours = finishHours - startHours;
		if (totalHours < 0) {
			totalHours += 24;
		}
		// Add mins and hours together
		totalMins += totalHours * 60;

		return totalMins;
	}

	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		try {
			// Create breadth first iterator to find the shortest path (hops wise)
			BreadthFirstIterator<Airport, Flight> bfi = new BreadthFirstIterator<>(flightGraph, airport(from));
			Airport a = null;
			// Find the leastHops path, store final airport in "a"
			while (bfi.hasNext()) {
				a = bfi.next();
				if (a.getCode().equals(to)) {
					break;
				}
				if (!bfi.hasNext())
					throw new FlyingPlannerException("The airport is not connected/does not exist");
			}
			// Record number of hops
			int hops = bfi.getDepth(a);

			// Initialise variables
			List<String> airports = new ArrayList<>();
			List<String> flightCodes = new ArrayList<>();

			int airTime = 0;
			int connectingTime = 0;
			int totalCost = 0;

			Flight f = null;
			Flight prev = null;

			airports.add(a.getCode());
			// while not the start airport
			while (!a.getCode().equals(from)) {
				// Get the connecting flight and add to flight list
				f = bfi.getSpanningTreeEdge(a);
				flightCodes.add(f.getFlightCode());
				// Find the times and cost, add to respective totals
				airTime += getFlightMins(f);
				connectingTime += getConnectingTime(f, prev);
				totalCost += f.getCost();
				// Find the next airport, and add to airport list
				a = bfi.getParent(a);
				airports.add(a.getCode());
				prev = f;

			}

			Collections.reverse(airports);
			Collections.reverse(flightCodes);

			// Create a journey to return
			Journey j = new Journey(airports, flightCodes, hops, airTime, connectingTime, totalCost);
			return j;
		} catch (Exception e) {
			throw new FlyingPlannerException("There is no such route");
		}
	}

	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		try {
			// Check if excluding is empty, if not remove airports from the graph
			if (!excluding.contains("")) {
				for (String s : excluding) {
					if (!s.equals(""))
						flightGraph.removeVertex(airport(s));
				}
			}

			// Create Dijkstra object to find shorted weighted path
			DijkstraShortestPath<Airport, Flight> cheap = new DijkstraShortestPath<Airport, Flight>(flightGraph);
			Airport left = airport(from);
			Airport dest = airport(to);

			// Find the shortest path's edges, create list of them
			List<Flight> flights = cheap.getPath(left, dest).getEdgeList();
			List<String> flightCodes = new ArrayList<>(flights.size());
			// Initialise variables
			int totalCost = 0;
			int airTime = 0;
			int connectingTime = 0;
			int countHops = 0;
			Flight lastFlight = null;
			// For every flight in the path, add the code to the list, then add cost, and
			// time to totals
			for (Flight f : flights) {
				flightCodes.add(f.getFlightCode());
				totalCost += f.getCost();
				airTime += getFlightMins(f);
				connectingTime += getConnectingTime(lastFlight, f);
				countHops++;
				lastFlight = f;
			}
			// Get path's airports
			List<Airport> stops = cheap.getPath(left, dest).getVertexList();
			List<String> airports = new ArrayList<>(stops.size());
			// Add all airports to the airport list
			for (Airport a : stops) {
				airports.add(a.getCode());
			}

			return new Journey(airports, flightCodes, countHops, airTime, connectingTime, totalCost);

		} catch (Exception e) {
			throw new FlyingPlannerException("There is no such route or you excluded a non-existent airport");
		}
	}

	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		try {
			// Check if excluding is empty, if not then remove all the airports from the flightGraph
			if (!excluding.contains("")) {
				for (String s : excluding) {
					if (!s.equals(""))
						flightGraph.removeVertex(airport(s));
				}
			}
			// Create breadth first iterator to find path
			BreadthFirstIterator<Airport, Flight> bfi = new BreadthFirstIterator<>(flightGraph, airport(from));
			Airport a = null;
			// Find the leastHop path, store final airport in "a"
			while (bfi.hasNext()) {
				a = bfi.next();
				if (a.getCode().equals(to)) {
					break;
				}
				if (!bfi.hasNext())
					throw new FlyingPlannerException("The airport is not connected/does not exist");
			}
			// Record number of hops
			int hops = bfi.getDepth(a);

			// Initialise variables
			List<String> airports = new ArrayList<>();
			List<String> flightCodes = new ArrayList<>();

			int airTime = 0;
			int connectingTime = 0;
			int totalCost = 0;

			Flight f = null;
			Flight prev = null;

			airports.add(a.getCode());
			// while not the start airport
			while (!a.getCode().equals(from)) {
				// Get the connecting flight and add to flight list
				f = bfi.getSpanningTreeEdge(a);
				flightCodes.add(f.getFlightCode());
				// Find the times and cost, add to respective totals
				airTime += getFlightMins(f);
				connectingTime += getConnectingTime(f, prev);
				totalCost += f.getCost();
				// Find the next airport, and add to airport list
				a = bfi.getParent(a);
				airports.add(a.getCode());
				prev = f;
			}

			Collections.reverse(airports);
			Collections.reverse(flightCodes);

			// Return Journey with details
			return new Journey(airports, flightCodes, hops, airTime, connectingTime, totalCost);

		} catch (Exception e) {
			throw new FlyingPlannerException("There is no such route or you excluded a non-existent airport");
		}
	}

	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		Set<Airport> dConnected = new HashSet<>();
		// For every neighbouring vertex, check if directly connected, if true add to
		// HashSet
		for (Airport a : Graphs.neighborSetOf(flightGraph, airport)) {
			if (flightGraph.containsEdge(airport, a) && flightGraph.containsEdge(a, airport)) {
				dConnected.add(a);
			}

		}
		return dConnected;
	}

	@Override
	public int setDirectlyConnected() {
		int total = 0;
		// Iterate through all airports
		for (Airport a : flightGraph.vertexSet()) {
			// Set the directlyConnected Set variable and directlyConnectedOrder variable
			a.setDirectlyConnected(directlyConnected(a));
			a.setDirectlyConnectedOrder(a.getDirectlyConnected().size());
			total += a.getDirectlyConnectedOrder();
		}
		return total;
	}

	@Override
	public int setDirectlyConnectedOrder() {
		dag = new DirectedAcyclicGraph<Airport, Flight>(Flight.class);
		// Add all the airports in the flightGraph to the DAG
		for (Airport a : flightGraph.vertexSet()) {
			dag.addVertex(a);
		}
		for (Flight f : flightGraph.edgeSet()) {
			// Check if the Origin is directly connect to the Destination
			if (flightGraph.containsEdge(f.getFrom(), f.getTo()) && flightGraph.containsEdge(f.getTo(), f.getFrom())) {
				// Check if the Origin's direct connections is less than the Destination's
				if (f.getFrom().getDirectlyConnectedOrder() < f.getTo().getDirectlyConnectedOrder()) {
					// If true, add the edge to the dag
					dag.addEdge(f.getFrom(), f.getTo(), f);
				}
			}

		}
		// return the number of edges (no of flights) in the DAG
		return dag.edgeSet().size();
	}

	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		Set<Airport> DBConnected = new HashSet<>();
		// Add all of the Airports in the that can be reached by "airport" to the
		// HashSet
		DBConnected.addAll(dag.getDescendants(airport));
		return DBConnected;
	}

	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

}
