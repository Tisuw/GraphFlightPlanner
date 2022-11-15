package F28DA_CW2;

public class Flight implements IFlight {
	
	private String code;
	private Airport to;
	private Airport from;
	private String toTime;
	private String fromTime;
	private int cost;
	
	Flight(String code, Airport to, Airport from, String toTime, String fromTime, int cost){
		this.code = code;
		this.to = to;
		this.from = from;
		this.toTime = toTime;
		this.fromTime = fromTime;
		this.cost = cost;
		
	}
	

	@Override
	public String getFlightCode() {
		return code;
	}

	@Override
	public Airport getTo() {
		return to;
	}

	@Override
	public Airport getFrom() {
		return from;
	}

	@Override
	public String getFromGMTime() {
		return fromTime;
	}

	@Override
	public String getToGMTime() {
		return toTime;
	}

	@Override
	public int getCost() {
		return cost;
	}


}
