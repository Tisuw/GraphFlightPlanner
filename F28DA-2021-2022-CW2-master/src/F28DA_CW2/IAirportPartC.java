package F28DA_CW2;

import java.util.Set;

public interface IAirportPartC {
	
	Set<Airport> getDirectlyConnected();

	void setDirectlyConnected(Set<Airport> directlyConnected);

	void setDirectlyConnectedOrder(int order);

	int getDirectlyConnectedOrder();


}
