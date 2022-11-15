package F28DA_CW2;

import java.util.HashSet;
import java.util.Set;

public class Airport implements IAirportPartB, IAirportPartC {
	
	private String code;
	private String city;
	private String name;
	private Set<Airport> directlyConnected;
	private int directlyConnectedOrder;
	
	Airport(String code, String city, String name){
		this.code = code;
		this.city = city;
		this.name = name;
	}
	
	Airport(String[] info){
		this.code = info[0];
		this.city = info[1];
		this.name = info[2];
	}
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
	}

	@Override
	public void setDirectlyConnected(Set<Airport> directlyConnected) {
		this.directlyConnected = directlyConnected;
	}

	@Override
	public Set<Airport> getDirectlyConnected() {
		return directlyConnected;
	}


	@Override
	public void setDirectlyConnectedOrder(int order) {
		this.directlyConnectedOrder = order;

	}

	@Override
	public int getDirectlyConnectedOrder() {
		return directlyConnectedOrder;
	}

}
