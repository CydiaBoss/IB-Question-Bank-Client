package com.ib.quest;

import com.ib.quest.gui.Error;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is the Class that will house all constants
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class Constants {

	/**
	 * Sizes
	 */
	public static final class Size {
		
		/**
		 * Standard Size
		 */
		public static final int STAN_W = 600,
								STAN_H = 450;
	}
	
	/**
	 * Database Location
	 */
	public static final class Database {
		
		/**
		 * Online
		 */
		public static final URL IBDBON = IBONEXP();
		
		// Used to handle the exception
		@Deprecated
		private static final URL IBONEXP() {
			try {
				return new URL("https://www.ibdocuments.com/IB%20QUESTIONBANKS/4.%20Fourth%20Edition");
			} catch (MalformedURLException e) {
				Error.throwError(Main.s.getLocal().get("error.in"), true);
				return null;
			}
		}
		
		/**
		 * Offline
		 */
		public static final URL IBDBOFF = IBOFFEXP();
		
		// Used to handle the exception
		@Deprecated
		private static final URL IBOFFEXP() {
			try {
				return new File("test/main.html").toURI().toURL();
			} catch (MalformedURLException e) {
				Error.throwError(Main.s.getLocal().get("error.in"), true);
				return null;
			}
		}
	}
	
	/**
	 * Question Data
	 */
	public static final class QData {
		
		/**
		 * Random Values
		 */
		public static final Integer[] size = new Integer[]{1, 3, 5, 10, 20, 25, 30};
		
	}
}
