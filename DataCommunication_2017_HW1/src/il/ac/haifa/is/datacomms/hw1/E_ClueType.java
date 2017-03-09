package il.ac.haifa.is.datacomms.hw1;

/**
 * Task types potentially presented to teams when arriving to RouteMarkers. 
 * Teams must finish the required task to be able to proceed to next RouteMarker.
 */
public enum E_ClueType {
	/** no tasks to perform. just move on to next route marker.*/
	ROUTE_INFORMATION ("Route Information"),
	/** contains 1 physical & 1 mental task to choose from performed by both contestant.*/
	DETOUR ("Detour"),
	/** contains 1 combined task, performed by one contestant.*/
	ROADBLOCK ("Roadblock");
	
	/**for debugging purposes.*/
	private String name;
	
	E_ClueType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
