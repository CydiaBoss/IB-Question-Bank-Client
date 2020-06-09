package com.ib.quest;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ib.quest.gui.Selector;
import com.ib.quest.gui.Setting;
import com.ib.quest.gui.History;

/**
 * The Main Class
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class Main implements Runnable{
	
	/**
	 * Temporary File Directory
	 */
	public static final File TEMP = new File("temp");
	
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
	 * History
	 */
	public static History h;
	
	/**
	 * Mainframe
	 */
	public static JFrame m;
	
	/**
	 * Main 
	 */
	public static void main(String[] args) {
		// Matches the GUI design to the OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			throwError(s.getLocal().get("error.in"), true);
		}
		// Create TEMP Folder
		TEMP.mkdir();
		TEMP.deleteOnExit();
		// Launches a Thread
		new Thread(new Main()).start();
	}
	
	/**
	 * Throws a error
	 * 
	 * @param parent
	 * The parent frame
	 * @param msg
	 * The message
	 * @param fatal
	 * If the error is fatal
	 */
	public static void throwError(String msg, boolean fatal) {
		JOptionPane.showMessageDialog(m, msg, s.getLocal().get("error"), (fatal)? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE);
		if(fatal)
			System.exit(-1);
	}

	/**
	 * The Starter
	 */
	@Override
	public void run() {
		// Setup
		m = new JFrame();
		s = new Setting();
		h = new History();
		ld = new Loader();
		sel = new Selector(ld);
	}

}
