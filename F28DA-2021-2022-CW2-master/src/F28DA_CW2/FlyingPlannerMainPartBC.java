package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class FlyingPlannerMainPartBC {

	public static void main(String[] args) {

		// Your implementation should be in FlyingPlanner.java, this class is only to
		// run the user interface of your programme.

		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());

			// Take in start and end airports
			Scanner sc = new Scanner(System.in);
			
			System.out.print("\nPlease enter start airport (code): ");
			String start = sc.nextLine().toUpperCase().trim();
			while (!fi.flightGraph.containsVertex(fi.airport(start))) {
				System.out.print("Please input a valid airport code: ");
				start = sc.nextLine().toUpperCase().trim();
			}
			System.out.print("Please enter end airport (code): ");
			String end = sc.nextLine().toUpperCase().trim();
			while (!fi.flightGraph.containsVertex(fi.airport(end))) {
				System.out.print("Please input a valid airport code: ");
				end = sc.nextLine().toUpperCase().trim();
			}
			System.out.println("Would you like the least number of hops or the cheapest flight? (hops/cost)");
			String method = sc.nextLine().toLowerCase().trim();
			while (!(method.equals("hops") || method.equals("cost"))){
				System.out.println("invalid input! (hops/cost)");
				method = sc.nextLine().toLowerCase().trim();
			}
			System.out.println("If you would like to exclude any airports, please enter each airport code separated by a comma only. Otherwise press enter:");
			String excludeString = sc.nextLine().toUpperCase().replace(" " , "");
			List<String> exclude = new ArrayList<String>(Arrays.asList(excludeString.split(",")));
			Journey j = null;
			switch (method) {
			case "hops":
				j = fi.leastHop(start, end, exclude);
				break;
			case "cost":
				j = fi.leastCost(start, end, exclude);
			}
			
			ListIterator<String> flightsIter = j.getFlights().listIterator();
			
			String title = String.format("Journey for %s (%s) to %s (%s)\n", fi.airport(start).getCity(), fi.airport(start).getCode(), fi.airport(end).getCity(), fi.airport(end).getCode());
			System.out.println(title);
			System.out.printf("%-4s %-36s %-4s %-6s %-36s %-4s\n", "Leg", "Leave", "At", "On", "Arrive", "At");
			
			Flight f;

			for (int i = 1; i <= j.totalHop(); i++) {
				f = fi.flight(flightsIter.next());
				String s = String.format("%-4d %-30s (%-3s) %-4s %-6s %-30s (%-3s) %-4s", i, f.getFrom().getCity(), f.getFrom().getCode(), f.getFromGMTime(), f.getFlightCode(), f.getTo().getCity(), f.getTo().getCode(), f.getToGMTime());
				System.out.println(s);
			}
			
			System.out.println("Total Journey Cost = £" + j.totalCost());
			System.out.println("Total Time in the Air: " + j.airTime() + "mins or "+ converter(j.airTime()));
			System.out.println("Total Connecting Time: " + j.connectingTime()+"mins or " +converter(j.connectingTime()));
			

			System.out.println("Total Travel Time: " + j.totalTime() + "mins or " + converter(j.totalTime()));
			sc.close();
			
			
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}

	}
	
	private static String converter(int time) {
		int hours = time/60;
		int mins = time % 60;
		String formatTime = hours + "hrs " +mins+ "min(s)";
		return formatTime;
	}

}
