package il.ac.haifa.is.datacomms.hw1;

import java.util.ArrayList;

/**
 * Class representation of a race's RouteMarker. 
 * Basically a checkpoint where teams get RouteInfo on next RouteMarker, 
 * and might need to perform tasks to be able to continue.
 */
public final class RouteMarker {
	
	//-------------------------------------------------------------------
	//-----------------------------fields--------------------------------
	//-------------------------------------------------------------------
	
	/**route marker's id.*/
	private final byte id; 
	
	/**route marker's location name.*/
	private String locationName;
	
	/**distance from previous route marker or race starting point in KM.*/
	private double distance;
	
	/**route marker's clue.*/
	private E_ClueType clue;
	
	/**teams visited marker in the past.*/
	private volatile ArrayList<Team> visitedTeams;
	
	/**teams visiting marker currently.*/
	private volatile ArrayList<Team> visitingTeams;
	
	//-------------------------------------------------------------------
	//-------------------------constructors------------------------------
	//-------------------------------------------------------------------
	
	/**
	 * @param id router marker's id.
	 * @param locationName location name.
	 * @param distance distance from previous marker or race starting point (if no previous marker exists).
	 * @param clue route marker's clue type.
	 */
	public RouteMarker(byte id, String locationName, double distance, E_ClueType clue) {
		this.id = id;
		this.locationName = locationName;
		this.distance = distance;
		this.clue = clue;
		visitedTeams = new ArrayList<>();
		visitingTeams = new ArrayList<>();
	}
	
	//-------------------------------------------------------------------
	//-------------------------functionality-----------------------------
	//-------------------------------------------------------------------
	
	/**
	 * handles a team's arrival. 
	 * <p>if game conditions are met, registers team as active & 
	 * hands out the marker's clue. 
	 * <br>otherwise, forces team to wait until they are met.
	 * @param team arriving team.
	 * @return route marker's clue type.
	 * @throws InterruptedException 
	 */
	public E_ClueType handleArrivalOf(Team team) throws InterruptedException {
		Main.Log("Team "+team.getName()+" arrived to marker "+getId()+". Checking for approval..");
		//Check if there is space in the RM
		while(visitingTeams.size()>2){
			team.sleep(4000);
			Main.Log("Team "+team.getName()+" is still waiting (Checkpoint: "+this.toString());
			
		}
	
	
		
		Main.Log("Team "+team.getName()+" approved to enter marker "+getId());
		visitingTeams.add(team);
		
		return clue;

	}
	
	/**
	 * handles a team's departure.
	 * <p>unregisters team and adds it to marker's history.
	 * @param team team leaving marker.
	 * @return next marker to travel to, null if this is the final pit stop.
	 */
	public synchronized RouteMarker handleDepartureOf(Team team) {
		
		Main.Log("Team "+team.getName()+" at RM "+getLocationName()+" is leaving..");
		
		visitingTeams.remove(team);
		visitedTeams.add(team);
		//Remove the team from the marker
		return getNextRouteMarker(this);
	}
	
	//-------------------------------------------------------------------
	//----------------------------utility--------------------------------
	//-------------------------------------------------------------------	
	
	//-------------------------------------------------------------------
	//----------------------------getters--------------------------------
	//-------------------------------------------------------------------
	
	/**
	 * @return route marker's id.
	 */
	public byte getId() {
		return id;
	}
	
	/**
	 * @return route marker's location name.
	 */
	public String getLocationName() {
		return locationName;
	}
	
	/**
	 * @return route marker's distance from previous marker (or starting point).
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * @return first RouteMarker in race.
	 */
	public static RouteMarker getFirstRouteMarker() {
		return AmazingRace.getInstance().getRouteMarkers().get(0);
	}
	
	/**
	 * @return next RouteMarker in race, or null if this is the final pit stop.
	 * @see AmazingRace#getRouteMarkers()
	 */
	private static RouteMarker getNextRouteMarker(RouteMarker marker) {
		RouteMarker toReturn = null;
		Boolean flag = false; //True when marker is found
		for(RouteMarker tmpRM : AmazingRace.getInstance().getRouteMarkers()){
			if(flag){
				toReturn = tmpRM;
				break;
			}
			if(tmpRM.equals(marker))
				flag=true;
		}
		
		return toReturn;
	}
	
	/**
	 * @return route marker's standings. 
	 * <p>a string of teams in departure time order. 
	 * first to leave is first in list. 
	 * last to leave is last in list.
	 */
	public String getStandings() {
		String out = "";
		for (int i = 0; i < visitedTeams.size(); i++)
			out += (i+1) + ". " + visitedTeams.get(i);
		return out;
	}
	
	//-------------------------------------------------------------------
	//----------------------------setters--------------------------------
	//-------------------------------------------------------------------
	
	/**
	 * @param type clue type to be set.
	 * @return reference to this instance.
	 * @see <a href='https://en.wikipedia.org/wiki/Fluent_interface'>Fluent Interface Pattern</a>
	 */
	public RouteMarker setClue(E_ClueType type) {
		this.clue = type;
		return this;
	}
	
	//-------------------------------------------------------------------
	//---------------------------overrides-------------------------------
	//-------------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		final RouteMarker other = (RouteMarker) obj;
		return (this.id == other.id);
	}
	
	@Override
	public String toString() {
		return String.format("RouteMarker [ id=%d, location=%s, distance=%.2f, clue=%s ]\n", 
				id, locationName, distance, clue);
	}
}
