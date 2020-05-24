package com.ib.quest;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ib.quest.gui.Selector;
import com.ib.quest.gui.Setting;
import com.ib.quest.gui.Error;

/**
 * The Main Class
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class Main implements Runnable{
	
	/**
	 * The Loader
	 */
	private static Loader ld;
	
	/**
	 * Setting
	 */
	public static Setting s;
	
	/**
	 * Selector
	 */
	public static Selector sel;
	
	/**
	 * Main 
	 */
	public static void main(String[] args) {
		// Matches the GUI design to the OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Error.throwError(s.getLocal().get("error.in"), true);
		}
		// Launches a Thread
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// Setup
		s = new Setting();
		ld = new Loader();
		sel = new Selector(ld);
	}

}
