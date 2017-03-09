package il.ac.haifa.is.datacomms.hw1;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Application's main class.
 */
public final class Main {
	private Main() {}
	
	public static void main(String[] args) {
		AmazingRace.getInstance().simulate();
	}
	
	public static void Log(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		
		System.out.println(sdf.format(new Date())+" - "+str);
	}
}
