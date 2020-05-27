package com.ib.quest.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ib.quest.Main;

import java.awt.Toolkit;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Error Framework
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class Error extends JDialog {

	private static final long serialVersionUID = -4335766057253805362L;
	
	private JPanel contentPane;
	
	/**
	 * Throws Error and pauses all execution
	 * 
	 * @param txt
	 * Message
	 * @param crash
	 * Shutdown?
	 */
	public static void throwError(String txt, boolean crash) {
		Error e = new Error(txt, crash);
		// Branches another thread
		SwingUtilities.invokeLater(() -> {
			e.setVisible(true);
		});
	}
	
	/**
	 * Create the frame to display the error message
	 */
	private Error(String txt, boolean crash) {
		super((JFrame) null, Main.s.getLocal().get("error"), crash);
		setAlwaysOnTop(true);
		setTitle(Main.s.getLocal().get("error"));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Error.class.getResource("/img/IBRRI.png")));

		if(crash)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Spawns the JFrame in the middle of the screen
		setLocationRelativeTo(null);
		setSize(350, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPane);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut, BorderLayout.NORTH);
		
		JLabel msg = new JLabel("<html>" + txt + "</html>");
		msg.setVerticalAlignment(SwingConstants.TOP);
		msg.setFont(new Font("Tahoma", Font.PLAIN, 11));
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
			setVisible(false);
			dispose();
			if (crash) 
				System.exit(0);
		});
		panel.add(okayBtn, BorderLayout.CENTER);
		
		Component verticalStrut_2 = Box.createVerticalStrut(15);
		panel.add(verticalStrut_2, BorderLayout.SOUTH);
		
	}
}
