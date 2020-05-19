package com.ib.quest;

import com.ib.quest.gui.Error;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is the Class that will house all constants
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class Constants {

	/**
	 * Sizes
	 */
	public static final class Size {
		
		/**
		 * Subject Size<br>
		 * SUB_H is only a base
		 */
		public static final int SUB_W = 600,
								SUB_H = 40,
								INT_SIZE = 60;
		
		/**
		 * Standard Size
		 */
		public static final int STAN_W = SUB_W,
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
				Error.throwError("Internal Error Detected", true);
				return null;
			}
		}
		
		/**
		 * Offline
		 */
		public static final URL IBDBOFF = Constants.class.getResource("/test/main.html");
	}
}
