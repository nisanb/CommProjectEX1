package il.ac.haifa.is.datacomms.hw1;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class representation of a team in the amazing race. Consists of 2
 * contestants.
 */
public final class Team extends Thread {

	// -------------------------------------------------------------------
	// -----------------------------fields--------------------------------
	// -------------------------------------------------------------------

	/** maximum members allowed in a team. */
	private static final int MAX_MEMBERS = 2;

	// **team's id.*/
	private final int teamId;

	/** team's members. */
	private ArrayList<Contestant> members;

	// -------------------------------------------------------------------
	// -------------------------constructors------------------------------
	// -------------------------------------------------------------------

	/**
	 * @param id
	 *            team's id.
	 */
	public Team(int id) {
		teamId = id;
		members = new ArrayList<>();
	}

	@Override
	public void run() {
		String name = String.valueOf(this.getTeamId());
		setName(name);
		Main.Log("Team " + getName() + " joined the race!");

		// Handle first RM
		RouteMarker rm = RouteMarker.getFirstRouteMarker();
		while (rm != null) {
			driveTo(rm);
			try {
				handleClue(rm.handleArrivalOf(this));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rm = rm.handleDepartureOf(this);
		}
	}

	// -------------------------------------------------------------------
	// -------------------------functionality-----------------------------
	// -------------------------------------------------------------------

	/**
	 * This method will handle the clue given to the team
	 * 
	 * @param clueType
	 */
	private void handleClue(E_ClueType clueType) {
		Main.Log("Team " + getName() + " received clue: " + clueType);
		switch (clueType) {
		case DETOUR:
			if (getAverageMentalScore() > getAveragePhysicalScore())
				performMentalTask();
			else
				performPhysicalTask();
			break;

		case ROADBLOCK:
			Main.Log("Performing a member roadblock task");
			getRandomMember().performCombinedTask();
			break;

		case ROUTE_INFORMATION:
			// Do nothing so it will proceed to the next RouteMarker
			break;
		}
	}

	/**
	 * drives to destination.
	 * 
	 * @param marker
	 *            route marker to drive to.
	 * @throws InterruptedException
	 */
	private void driveTo(RouteMarker marker) {
		long millis = (long) (marker.getDistance())*100;
		Main.Log("Team " + getName() + " driving to marker " + marker.getId() + "(Dist: " + marker.getDistance()
				+ "km) for " + millis + " milliseconds");
		try {
			sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * performs a physical task.
	 */
	private void performPhysicalTask() {
		long millis = (long) ((100-getAveragePhysicalScore())*100);
		
		try {
			Main.Log("Team " + getName() + " performing physical task for "+millis+" millis");
			sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * performs a mental task.
	 */
	private void performMentalTask() {
		long millis = (long) ((100 - getAverageMentalScore())*100);
		if (millis < 1000)
			millis = 1000;
		try {
			Main.Log("Team " + getName() + " performing mental task for " + millis + " millis.");
			sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Contestant getRandomMember() {
		Random r = new Random();
		Main.Log("Giving random member: " + r);
		return members.get(r.nextInt(2));
	}

	// -------------------------------------------------------------------
	// ----------------------------utility--------------------------------
	// -------------------------------------------------------------------

	/**
	 * adds a member to team.
	 * 
	 * @param contestant
	 *            contestant to be added to team.
	 * @return reference to this instance.
	 * @see <a href='https://en.wikipedia.org/wiki/Fluent_interface'>Fluent
	 *      Interface Pattern</a>
	 */
	public Team addMember(Contestant contestant) {
		if (members.size() == MAX_MEMBERS)
			throw new UnsupportedOperationException("team is full!");
		members.add(contestant);
		return this;
	}

	// -------------------------------------------------------------------
	// ----------------------------getters--------------------------------
	// -------------------------------------------------------------------

	/**
	 * @return team's first member, null if team has no members.
	 */
	public Contestant getMember1() {
		if (members.isEmpty())
			return null;
		return members.get(0);
	}

	/**
	 * @return team's second member, null if team has no members or just one.
	 */
	public Contestant getMember2() {
		if (members.isEmpty() || members.size() < 2)
			return null;
		return members.get(1);
	}

	/**
	 * @return team's id.
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @return team's average physical score.
	 */
	public double getAveragePhysicalScore() {
		return (getMember1().getPhysicalScore() + getMember2().getPhysicalScore()) / 2.0;
	}

	/**
	 * @return team's average mental score.
	 */
	public double getAverageMentalScore() {
		return (getMember1().getMentalScore() + getMember2().getMentalScore()) / 2.0;
	}

	// -------------------------------------------------------------------
	// ----------------------------setters--------------------------------
	// -------------------------------------------------------------------

	// -------------------------------------------------------------------
	// ---------------------------overrides-------------------------------
	// -------------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		final Team other = (Team) obj;
		return (other.teamId == this.teamId);
	}

	@Override
	public String toString() {
		return String.format(
				"Team [ id=%d, avgPhysicalScore=%.2f, avgMentalScore=%.2f, " + "\n\tmember1=%s, \n\tmember2=%s\n",
				teamId, getAveragePhysicalScore(), getAverageMentalScore(), getMember1(), getMember2());
	}
}
