package il.ac.haifa.is.datacomms.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Application's main class.
 */
public final class Main {
	static PrintStream prntstrm;
	static File logFolder;
	static Map<String, PrintStream> prntToTeam;
	static Boolean scoreBoard = false;

	private Main() {
	}

	public static void main(String[] args) {
		initiateLogs();
		try {
			AmazingRace.getInstance().simulate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initiates the log files
	 */
	private static void initiateLogs() {
		String dateTime = (new SimpleDateFormat("ddMM_hhmmss")).format(new Date());
		try {
			logFolder = new File("logs_" + dateTime);
			logFolder.mkdir();
			prntstrm = new PrintStream(new File(logFolder.getAbsolutePath() + "/MainLog.log"));
			prntToTeam = new HashMap<String, PrintStream>();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void Log(String str) {
		if(scoreBoard || str.contains("==================================")){
			try {
				scoreBoard = true;
				prntstrm = new PrintStream(new File(logFolder + "/" + "Scoreboard.log"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		if (str.contains("Team ") && !scoreBoard) {
			// Is a team log
			String teamString = str.substring(0, 7);
			if (!prntToTeam.containsKey(teamString))
				try {
					prntToTeam.put(teamString, new PrintStream(new File(logFolder + "/" + teamString + ".log")));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			prntToTeam.get(teamString).println(LocalTime.now() + " - " + str);

		}
		
		//Take care of scoreboard results
		
		prntstrm.println(LocalTime.now() + " - " + str);
		System.out.println(LocalTime.now() + " - " + str);
	}
}
