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
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Font;

/**
 * JFrame to show Progress
 * 
 * TODO Use this
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Progress extends JFrame {
	
	private static final long serialVersionUID = -5756467726613224559L;
	
	/**
	 * Pane
	 */
	private JPanel contentPane;
	
	/**
	 * Progress Bar
	 */
	private JProgressBar pB;
	
	/**
	 * Process
	 */
	private JLabel progLbl;
	
	/**
	 * Create the frame.
	 */
	public Progress(String process, int total) {
		// Setup
		setTitle(Main.s.getLocal().get("load"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Progress.class.getResource("/img/IBRRI.png")));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 350, 135);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		contentPane.add(verticalStrut_1, BorderLayout.SOUTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut_1, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 10));
		
		progLbl = new JLabel(process);
		progLbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		progLbl.setEnabled(true);
		panel.add(progLbl, BorderLayout.NORTH);
		
		// Make Progress Bar
		pB = new JProgressBar(0, total);
		pB.setStringPainted(true);
		pB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pB.setForeground(Color.GREEN);
		pB.setIgnoreRepaint(true);
		panel.add(pB, BorderLayout.CENTER);
		
		// Show
		setVisible(true);
	}

	/**
	 * Set the process
	 * 
	 * @param process
	 * The new process
	 */
	public void setProcess(String process) {
		progLbl.setText(process);
	}
	
	/**
	 * Progress the progress
	 * 
	 * @param prog
	 * How much to progress
	 */
	public void setProg(int prog) {
		// New Progress
		int newProg = pB.getValue() + prog;
		// Prevent for Overflow
		if(newProg >= pB.getMaximum())
			pB.setValue(pB.getMaximum());
		else
			pB.setValue(newProg);
	}
	
	/**
	 * Returns the current Progress
	 * 
	 * @return
	 * The progress
	 */
	public int getProg() {
		return pB.getValue();
	}
	
	/**
	 * Finishes and disposes of the Frame
	 */
	public void finish() {
		// Maximizes the Bar
		pB.setValue(pB.getMaximum());
		// Close Frame
		this.setVisible(false);
		this.dispose();
	}
}
