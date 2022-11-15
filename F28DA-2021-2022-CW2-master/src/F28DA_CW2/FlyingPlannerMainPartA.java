package F28DA_CW2;

import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

public class FlyingPlannerMainPartA {
	

	public static void main(String[] args) {
		
//		// The following code is from HelloJGraphT.java of the org.jgrapth.demo package
//		
//		System.err.println("The example code is from HelloJGraphT.java from the org.jgrapt.demo package.");
//		System.err.println("Use similar code to build the small graph from Part A by hand.");
//		System.err.println("Note that you will need to use a different graph class as your graph is not just a Simple Graph.");
//		System.err.println("Once you understand how to build such graph by hand, move to Part B to build a more substantial graph.");
//		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)
//        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
//
//        String v1 = "v1";
//        String v2 = "v2";
//        String v3 = "v3";
//        String v4 = "v4";
//        
//        // add the vertices
//        g.addVertex(v1);
//        g.addVertex(v2);
//        g.addVertex(v3);
//        g.addVertex(v4);
//
//        // add edges to create a circuit
//        g.addEdge(v1, v2);
//        g.addEdge(v2, v3);
//        g.addEdge(v3, v4);
//        g.addEdge(v4, v1);
//
//        // note undirected edges are printed as: {<v1>,<v2>}
//        System.out.println("-- toString output");
//        // @example:toString:begin
//        System.out.println(g.toString());
//        // @example:toString:end
//        System.out.println();
//		// Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)
		
		
//		Create a directed weighted graph
		Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Set strings of airport names
		String edin = "Edinburgh";
		String hth = "Heathrow";
		String dub = "Dubai";
		String syd = "Sydney";
		String kua = "Kuala Lumpur";
		
		
		// Add all airports to the graph
		g.addVertex(edin);
		g.addVertex(hth);
		g.addVertex(dub);
		g.addVertex(syd);
		g.addVertex(kua);
		
		
		// Add weighted edges to the graph
		g.setEdgeWeight(g.addEdge(edin, hth), 80);
		g.setEdgeWeight(g.addEdge(hth, edin), 80);
		
		g.setEdgeWeight(g.addEdge(hth, dub), 130);
		g.setEdgeWeight(g.addEdge(dub, hth), 130);
		
		g.setEdgeWeight(g.addEdge(syd, hth), 570);
		g.setEdgeWeight(g.addEdge(hth, syd), 570);
		
		g.setEdgeWeight(g.addEdge(dub, kua), 170);
		g.setEdgeWeight(g.addEdge(kua, dub), 170);
		
		g.setEdgeWeight(g.addEdge(dub, edin), 190);
		g.setEdgeWeight(g.addEdge(edin, dub), 190);

		g.setEdgeWeight(g.addEdge(kua, syd), 150);
		g.setEdgeWeight(g.addEdge(syd, kua), 150);

		// Print out the graph
		System.out.println(g.toString());
        
		// Take in start and end airports
		Scanner sc = new Scanner(System.in);
		System.out.println("\nThe following airports are used :\n	" + g.vertexSet().toString());

		System.out.print("\nPlease enter start airport: ");
		String start = sc.nextLine();
		while (!g.containsVertex(start)) {
			System.out.print("Please input a valid airport: ");
			start = sc.nextLine();
		}
		System.out.print("Please enter end airport: ");
		String end = sc.nextLine();
		while (!g.containsVertex(end)) {
			System.out.print("Please input a valid airport: ");
			end = sc.nextLine();
		}
		DijkstraShortestPath<String, DefaultWeightedEdge> cheap = new DijkstraShortestPath<String, DefaultWeightedEdge>(g);
		
		System.out.println("\nThe cheapest route to " + end + " from " + start + " is:");
		System.out.println(cheap.getPath(start, end).toString());
		System.out.println(cheap.getPath(start, end).getEdgeList().toString());
		System.out.println(cheap.getPath(start, end).getVertexList().toString());
		System.out.println("at: £" + cheap.getPathWeight(start, end));
		
		sc.close();
		
		
	}
	

}
