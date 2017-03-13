package il.ac.haifa.is.datacomms.hw1;

/**
 * Class representation of a contestant in the Amazing Race.
 */
public final class Contestant {

	// -------------------------------------------------------------------
	// -----------------------------fields--------------------------------
	// -------------------------------------------------------------------

	/** contestant's first name & last name. */
	private String name;

	/** contestant's physical score. affecting how he does in physical tasks. */
	private int physicalScore;

	/** contestant's mental score. affecting how he does in mental tasks. */
	private int mentalScore;

	// -------------------------------------------------------------------
	// -------------------------constructors------------------------------
	// -------------------------------------------------------------------

	// -------------------------------------------------------------------
	// -------------------------functionality-----------------------------
	// -------------------------------------------------------------------

	/**
	 * performs a combined (both physical & mental) solo task.
	 */
	public void performCombinedTask() {
		long millis = (long) ((100 - getCombinedScore()) * 100.0);
		if (millis < 1000)
			millis = 1000;
		try {
			Main.Log("Member " + getName() + " performing combined task for " + millis + " millis.");
			Thread.sleep(millis);
			Main.Log("Member " + getName() + " finished the combined task!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// -------------------------------------------------------------------
	// ----------------------------utility--------------------------------
	// -------------------------------------------------------------------

	// -------------------------------------------------------------------
	// ----------------------------getters--------------------------------
	// -------------------------------------------------------------------

	/**
	 * @return contestant's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return contestant's physical score.
	 */
	public int getPhysicalScore() {
		return physicalScore;
	}

	/**
	 * @return contestant's mental score.
	 */
	public int getMentalScore() {
		return mentalScore;
	}

	/**
	 * @return contestant's combined score. an average of physical & mental
	 *         scores.
	 */
	public double getCombinedScore() {
		return (physicalScore + mentalScore) / 2.0;
	}

	// -------------------------------------------------------------------
	// ----------------------------setters--------------------------------
	// -------------------------------------------------------------------

	/**
	 * @param name
	 *            name to be set.
	 * @return reference to this instance.
	 * @see <a href='https://en.wikipedia.org/wiki/Fluent_interface'>Fluent
	 *      Interface Pattern</a>
	 */
	public Contestant setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @param score
	 *            score to be set.
	 * @return reference to this instance.
	 * @see <a href='https://en.wikipedia.org/wiki/Fluent_interface'>Fluent
	 *      Interface Pattern</a>
	 */
	public Contestant setPhysicalScore(int score) {
		physicalScore = score;
		return this;
	}

	/**
	 * @param score
	 *            score to be set.
	 * @return reference to this instance.
	 * @see <a href='https://en.wikipedia.org/wiki/Fluent_interface'>Fluent
	 *      Interface Pattern</a>
	 */
	public Contestant setMentalScore(int score) {
		mentalScore = score;
		return this;
	}

	// -------------------------------------------------------------------
	// ---------------------------overrides-------------------------------
	// -------------------------------------------------------------------

	@Override
	public String toString() {
		return String.format("Contestant [ name=%s, physicalScore=%d, mentalScore=%d ]", name, physicalScore,
				mentalScore);
	}

}
