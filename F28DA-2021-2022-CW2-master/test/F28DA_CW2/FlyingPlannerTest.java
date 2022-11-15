package F28DA_CW2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class FlyingPlannerTest {

	FlyingPlanner fi;

	@Before
	public void initialize() {
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}
	}

	// Add your own tests here
	// You can get inspiration from FlyingPlannerProvidedTest

	
	@Test
	public void getDifferenceTestHours() {
		assertEquals(60, fi.getDifference(0, 19, 0, 20));
		
	}
	
	@Test
	public void getDifferenceTestMins() {
		assertEquals(5, fi.getDifference(20, 20, 25, 20));
		
	}

	@Test
	public void getDifferenceTestMins2() {
		assertEquals(59, fi.getDifference(20, 20, 19, 21));
		
	}
	
	@Test
	public void getDifferenceTestMins3() {
		assertEquals(2, fi.getDifference(59, 23, 1, 0));
		
	}
	
	@Test
	public void getFlightMinsTest() {
		Flight f = new Flight("UA0557", fi.airport("SDF"), fi.airport("DEN"), "513" , "654", 113);
		assertEquals(1339, fi.getFlightMins(f));
		
	}

	@Test
	public void getConnectingMinsTest() {
		Flight f1 = new Flight("UA0557", fi.airport("DEN"), fi.airport("SDF"), "654" , "513", 113);
		Flight f2 = new Flight("UA2019", fi.airport("MEX"), fi.airport("DEN"), "2116" , "1849", 166);
		// Check time from 0654 (landed in DEN) - 1849 (leaving DEN), the time in airports
		assertEquals(715, fi.getConnectingTime(f1, f2));
		
	}
	
	



}
