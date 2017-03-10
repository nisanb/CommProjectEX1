package il.ac.haifa.is.datacomms.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class representation of the Amazing Race.
 */
public final class AmazingRace {
	
	//-------------------------------------------------------------------
	//-----------------------------fields--------------------------------
	//-------------------------------------------------------------------
	private volatile static AmazingRace initiatedRace = null;
	/**teams participating in the race.*/
	private volatile List<Team> teams = new ArrayList<Team>();
	
	/**race's route markers.*/
	private volatile List<RouteMarker> markers = new ArrayList<RouteMarker>();

	//-------------------------------------------------------------------
	//-------------------------constructors------------------------------
	//-------------------------------------------------------------------
	
	/**
	 * singleton instance getter.
	 * @return instance of AmazingRace.
	 */
	public static AmazingRace getInstance() {
		if(initiatedRace==null)
			initiatedRace = new AmazingRace();
		
		return initiatedRace;
	}
	
	//-------------------------------------------------------------------
	//-------------------------functionality-----------------------------
	//-------------------------------------------------------------------
	
	/**
	 * simulates an amazing race: initializes it, starts it and prints its results.
	 * @throws InterruptedException 
	 */
	public void simulate() throws InterruptedException {
		Main.Log("Starting simulating race");
		initTeams();
		initRouteMarkers();
		
		//Init threads
		for(Team t : teams){
			t.start();
		}
		
		//Make thread wait for all teams to finish the race
		for(Team t : teams){
			t.join();
		}
		
		System.out.println("===================================");
		System.out.println("Race is finished. Printing standings.");	
		System.out.println("===================================");
		System.out.println(markers.get(markers.size()-1).getStandings());
		
		  
	}

	//-------------------------------------------------------------------
	//----------------------------utility--------------------------------
	//-------------------------------------------------------------------	
	
	/**
	 * initializes teams. reads data from teams json file.
	 */
	@SuppressWarnings("unchecked")
	private void initTeams() {
		Main.Log("Importing teams from teams.json");
		try (InputStream is = getClass().getResourceAsStream("/teams.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> outerIterator = 
					((JSONArray) new JSONParser().parse(reader)).iterator();
			while (outerIterator.hasNext()) {
				JSONObject obj = (JSONObject) outerIterator.next();
				Team team = new Team(((Number) obj.get("id")).intValue());
				Iterator<JSONObject> innerIterator = 
						((JSONArray) obj.get("Members")).iterator();
				while (innerIterator.hasNext()) {
					JSONObject member = innerIterator.next();
					team.addMember(new Contestant().setName(member.get("firstName") + " " + member.get("lastName"))
												.setPhysicalScore(((Number) member.get("physicalScore")).intValue())
												.setMentalScore(((Number) member.get("mentalScore")).intValue()));
				}
				
				teams.add(team);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(LocalTime.now() + 
				" teams data fetched from file:\n\n" + teams + "\n"); // XXX
	}
	
	/**
	 * initializes route markers. reads data from markers json file.
	 */
	@SuppressWarnings("unchecked")
	private void initRouteMarkers() {
		Main.Log("Importing Route Markers from markers.json");
		try (InputStream is = getClass().getResourceAsStream("/markers.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			Iterator<JSONObject> outerIterator = 
					((JSONArray) new JSONParser().parse(reader)).iterator();
			while (outerIterator.hasNext()) {
				JSONObject obj = (JSONObject) outerIterator.next();
				byte rmID = ((Number)(obj.get("id"))).byteValue();
				String rmLocation = obj.get("location").toString();
				double rmDistance = Double.parseDouble(obj.get("distance").toString());
				Integer rmClue = ((Number)(obj.get("clue"))).intValue();
				E_ClueType rmClueType = E_ClueType.getClueType(rmClue);
				RouteMarker rm = new RouteMarker(rmID, rmLocation, rmDistance, rmClueType);
				markers.add(rm);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(LocalTime.now() + 
				" markers data fetched from file:\n\n" + markers + "\n"); // XXX
	}
	
	//-------------------------------------------------------------------
	//----------------------------getters--------------------------------
	//-------------------------------------------------------------------
	
	/**
	 * @return an unmodifiable list of route markers.
	 */
	public List<RouteMarker> getRouteMarkers() {
		return Collections.unmodifiableList(markers);
	}
	
	//-------------------------------------------------------------------
	//----------------------------setters--------------------------------
	//-------------------------------------------------------------------
	
	//-------------------------------------------------------------------
	//---------------------------overrides-------------------------------
	//-------------------------------------------------------------------
}
