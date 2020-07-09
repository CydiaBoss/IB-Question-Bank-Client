package com.ib.quest;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ib.quest.gui.Selector;
import com.ib.quest.gui.Setting;
import com.ib.quest.gui.template.Progress;
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
		// Error Prompt
		JOptionPane.showMessageDialog(m, msg, s.getLocal().get("error"), (fatal)? JOptionPane.ERROR_MESSAGE : JOptionPane.WARNING_MESSAGE);
		// Fatal Stop
		if(fatal)
			System.exit(-1);
	}

	/**
	 * The Starter
	 */
	@Override
	public void run() {
		// Settings
		s = new Setting();
		// Setup
		m = new JFrame();
		// Progress
		Progress p = new Progress(m, s.getLocal().get("load.start"), 7);
		// Title
		// Adds an Offline tag if in offline mode
		m.setTitle(Main.s.getLocal().get("gen.title") + 
				(Main.s.getSetting().get("connect").equals("1")? " [" + Main.s.getLocal().get("set.connect.off") + "]" : ""));
		// Sets Image
		m.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/img/IBRRI.png")));
		// Sets Exclusion (idk if this actually is useful)
		m.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		// no resize
		m.setResizable(false);
		// Size
		m.setSize(Constants.Size.STAN_W , Constants.Size.STAN_H);
		// Spawns the JFrame in the middle of the screen
		m.setLocationRelativeTo(null);
		// Adds custom close prompt
		m.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Make Closing Action
		m.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			/**
			 * Close only if yes is pressed
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(m, s.getLocal().get("main.close") + " " + s.getLocal().get("gen.sure"), 
						s.getLocal().get("gen.sure"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		// Layout
		m.getContentPane().setLayout(new BorderLayout(0, 0));
		// Adds a Barrier Border around the contents
		{
			Component horizontalStrut = Box.createHorizontalStrut(20);
			m.getContentPane().add(horizontalStrut, BorderLayout.EAST);
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			m.getContentPane().add(horizontalStrut_1, BorderLayout.WEST);
			
			Component verticalStrut = Box.createVerticalStrut(20);
			m.getContentPane().add(verticalStrut, BorderLayout.NORTH);
			
			Component verticalStrut_1 = Box.createVerticalStrut(20);
			m.getContentPane().add(verticalStrut_1, BorderLayout.SOUTH);
		}
		// Finish Build
		p.progress();
		// Start Up
		// History
		h = new History(p);
		// Loader
		ld = new Loader(p);
		// GUI
		sel = new Selector(ld, p);
		// End
		p.progress();
	}

}
