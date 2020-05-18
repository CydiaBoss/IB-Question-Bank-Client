package com.ib.quest.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ib.quest.Main;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;

/**
 * Error Framework
 * 
 * @author andre
 * @version 1.0.4.3
 */
public class Error extends JFrame {

	private static final long serialVersionUID = -4335766057253805362L;
	
	private JPanel contentPane;
	
	/**
	 * Do not change directly
	 */
	private static boolean controller = false;
	
	/**
	 * Internal Lock Mechanism
	 * 
	 * @param c
	 * Toggle
	 */
	private static synchronized void setCont(boolean c) {
		controller = c;
	}
	
	/**
	 * Throws Error
	 * 
	 * @param txt
	 * Message
	 * @param crash
	 * Shutdown?
	 */
	public static void throwError(String txt, boolean crash) {
		setCont(true);
		Thread t = new Thread(() -> {
			Main.lck.lock();
			new Error(txt, crash)
			.setVisible(true);
			// Stops until reset
			// My questionable locking mechanism requires a 1 tick delay for some reason
			while(controller)
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			Main.lck.unlock();
		});
		t.start();
	}
	
	/**
	 * Create the frame.
	 */
	private Error(String txt, boolean crash) {
		setTitle("Error");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Error.class.getResource("/img/IBRR.png")));
		if(crash)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Add a close action
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {
				setCont(false);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}

			@Override
			public void windowDeiconified(WindowEvent arg0) {}

			@Override
			public void windowIconified(WindowEvent arg0) {}

			@Override
			public void windowOpened(WindowEvent arg0) {}
		});
		setBounds(100, 100, 350, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut, BorderLayout.NORTH);
		
		JLabel msg = new JLabel("<html>" + txt + "</html>");
		msg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		if(crash)
			msg.setIcon(new ImageIcon(Error.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-error.png")));
		else
			msg.setIcon(new ImageIcon(Error.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-warning.png")));
		contentPane.add(msg, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(225);
		panel.add(horizontalStrut_2, BorderLayout.WEST);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_3, BorderLayout.EAST);
		
		JButton okayBtn = new JButton("OK");
		okayBtn.addActionListener(e -> {
			this.setVisible(false);
			this.dispose();
			if (crash) 
				System.exit(0);
			else
				setCont(false);
		});
		panel.add(okayBtn, BorderLayout.CENTER);
		
		Component verticalStrut_2 = Box.createVerticalStrut(15);
		panel.add(verticalStrut_2, BorderLayout.SOUTH);
	}
}
