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
	 * The main progress bar
	 */
	private JProgressBar mPB;
	
	
	public Progress(JFrame owner, String task, int amt) {
		super(owner, Main.s.getLocal().get("load"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(Progress.class.getResource("/img/IBRRI.png")));
		setResizable(false);
		setSize(350, 90);
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
		
		mPB = new JProgressBar(0, amt);
		mPB.setForeground(Color.GREEN);
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
			SwingUtilities.invokeLater(() -> setVisible(false));
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
	public JProgressBar addTask(String task, int amt) {
		
		JPanel vBox = new JPanel();
		vBox.setLayout(new GridLayout(0, 1, 0, 0));
		mPanel.add(vBox);
		
		setSize(350, getHeight() + 60);
		
		JLabel taskLbl = new JLabel(task);
		taskLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		vBox.add(taskLbl);
		
		JProgressBar progressBar = new JProgressBar(0, amt);
		progressBar.setForeground(Color.GREEN);
		progressBar.setStringPainted(true);
		vBox.add(progressBar);
		
		return progressBar;
	}
	
	/**
	 * DEMO
	 */
//	public static void main(String... args) {
//		JFrame f = new JFrame();
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setSize(400,200);
//		
//		JButton b = new JButton("Click");
//		b.addActionListener(e -> {
//			
//			Progress p = new Progress(f, "First Task", 100); 
//			
//			p.setVisible(true);
//			
//			new SwingWorker<Void, Object>() {
//
//				@Override
//				protected Void doInBackground() throws Exception {
//					
//					JProgressBar pB = null;
//					
//					for(int i = 1; i <= 100; i++) {
//						p.progress();
//						
//						if(i == 50)
//							pB = p.addTask("Next Task", 50);
//						
//						if(i >= 50)
//							pB.setValue(i - 50);
//						
//						Thread.sleep(100);
//					}
//					
//					SwingUtilities.invokeLater(() -> 
//						p.setVisible(false)
//					);
//					
//					return null;
//				}
//				
//			}.execute();
//		});
//		f.add(b);
//		
//		SwingUtilities.invokeLater(() -> {
//			f.setVisible(true);
//		});
//	}
}
