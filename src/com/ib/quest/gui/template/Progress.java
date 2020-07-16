package com.ib.quest.gui.template;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.ib.quest.Main;
import java.awt.Toolkit;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
//import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
//import javax.swing.SwingWorker;

import java.awt.Font;

/**
 * 3rd try at a UI to track progress
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Progress extends JDialog {

	private static final long serialVersionUID = 7382103348733356449L;

	/**
	 * JPanel that holds all progresses
	 */
	private JPanel mPanel = new JPanel();
	
	/**
	 * Description
	 */
	private JLabel desLbl = new JLabel();
	
	/**
	 * The main progress bar
	 */
	private JProgressBar mPB;
	
	/**
	 * Progress Bar Colour
	 */
	private final Color G = new Color(6, 176, 37);
	
	
	public Progress(JFrame owner, String task, int amt) {
		super(owner, Main.s.getLocal().get("load"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Progress.class.getResource("/img/IBRRI.png")));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(350, 120);
		setLocationRelativeTo(owner);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(10);
		getContentPane().add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut_1, BorderLayout.SOUTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		getContentPane().add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		getContentPane().add(horizontalStrut_1, BorderLayout.EAST);
		
		getContentPane().add(mPanel, BorderLayout.CENTER);
		mPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel vBox = new JPanel();
		vBox.setLayout(new GridLayout(0, 1, 0, 0));
		mPanel.add(vBox);
		
		JLabel taskLbl = new JLabel(task);
		taskLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		vBox.add(taskLbl);
		
		desLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		vBox.add(desLbl);
		
		mPB = new JProgressBar(0, amt);
		mPB.setForeground(G);
		mPB.setStringPainted(true);
		vBox.add(mPB);
		
		SwingUtilities.invokeLater(() -> setVisible(true));
	}
	
	/**
	 * Increases the progress bar
	 */
	public void progress() {
		// Change Value
		mPB.setValue(
			(mPB.getValue() < mPB.getMaximum())? 
					mPB.getValue() + 1 : mPB.getMaximum()
		);
		// Close when maxed
		if(mPB.getValue() == mPB.getMaximum())
			SwingUtilities.invokeLater(() -> {
				setVisible(false);
				dispose();
			});
	}
	
	/**
	 * Adds a new progress bar to the popup
	 * 
	 * @param task
	 * The new task
	 * @param amt
	 * The amount of tasks
	 * 
	 * @return
	 * Returns the new progress bar
	 */
	public void addTask(String task, int amt) {
		// Update 
		desLbl.setText(task);
		mPB.setMaximum(mPB.getMaximum() + amt);
	}
}
