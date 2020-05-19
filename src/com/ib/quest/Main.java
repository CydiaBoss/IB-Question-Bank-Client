package com.ib.quest;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ib.quest.gui.Selector;
import com.ib.quest.gui.Error;

/**
 * The Main Class
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class Main {
	
	/**
	 * The Loader
	 */
	private static Loader ld;
	
	/**
	 * Main 
	 */
	public static void main(String[] args) {
		// Matches the GUI design to the OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Error.throwError("Internal Error Detected", true);
			
		}
		// Setup
		ld = new Loader();	
		new Selector(ld);
	}

}
