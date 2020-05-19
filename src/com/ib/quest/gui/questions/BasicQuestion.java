package com.ib.quest.gui.questions;

import com.ib.quest.Loader;
import com.ib.quest.Loader.QType;
import com.ib.quest.Loader.Question;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.util.ArrayList;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.Font;
import javax.swing.Box;
import java.awt.Component;
import java.awt.CardLayout;

/**
 * This is a JPanel for a basic question
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class BasicQuestion extends JPanel {

	private static final long serialVersionUID = -4638463439038323534L;
	
	/* Question Pieces */
	private String txt;
	ArrayList<Question> questions, 
						answers;
	
	// TODO Scroll Pane sucks, switch tmrw
	
	/**
	 * Creates the Panel
	 * 
	 * @param x
	 * X Location of the mainframe
	 * @param y
	 * Y Location of the mainframe
	 */
	public BasicQuestion(int x, int y, Loader ld, String ID, JFrame m, JPanel pre) {
		
		// Get Question
		txt = "";
		questions = new ArrayList<Question>();
		answers = new ArrayList<Question>();
		ld.loadQParts(ID);
		parseQParts(ld.getQParts());
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel idLbl = new JLabel(ID);
		idLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(idLbl);
		
		JLabel specLbl = new JLabel("<html>" + txt + "</html>");
		specLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(specLbl);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 80, 0));
		
		// A back button
		JButton quitBtn = new JButton("Quit");
		quitBtn.addActionListener(e -> {
			m.getContentPane().remove(this);
			m.getContentPane().add(pre, BorderLayout.CENTER);
			m.revalidate();
		});
		panel_1.add(quitBtn);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		CardLayout c = new CardLayout(0, 0);
		panel_2.setLayout(c);
		
		Box horizontalBox = Box.createHorizontalBox();
		panel_1.add(horizontalBox);
		
		JButton bacBtn = new JButton("Back");
		bacBtn.addActionListener(e -> 
			c.previous(panel_2)
		);
		horizontalBox.add(bacBtn);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JButton nextBtn = new JButton("Next");
		nextBtn.addActionListener(e -> 
			c.next(panel_2)
		);
		horizontalBox.add(nextBtn);
		
		/* Generate all Question */
		
		for(Question q : questions) {
		
			JPanel panel_3 = new JPanel();
			panel_2.add(q.getLabel(), panel_3);
			panel_3.setLayout(new BorderLayout(0, 0));
			
			JPanel panel_4 = new JPanel();
			panel_3.add(panel_4, BorderLayout.NORTH);
			panel_4.setLayout(new BorderLayout(0, 0));
			
			Component verticalStrut = Box.createVerticalStrut(15);
			panel_4.add(verticalStrut, BorderLayout.NORTH);
			
			JLabel labelLbl = new JLabel(q.getLabel());
			labelLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_4.add(labelLbl, BorderLayout.WEST);
			
			JLabel quesLbl = new JLabel("<html>" + q.getText() + "</html>");
			quesLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_4.add(quesLbl, BorderLayout.CENTER);
			
			JLabel markLbl = new JLabel("[" + q.getMark() + "]");
			markLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			markLbl.setAlignmentX(RIGHT_ALIGNMENT);
			panel_4.add(markLbl, BorderLayout.EAST);
			
			Component verticalStrut_1 = Box.createVerticalStrut(15);
			panel_4.add(verticalStrut_1, BorderLayout.SOUTH);
			
			JTextArea textPane = new JTextArea();
			textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
			textPane.setWrapStyleWord(true);
			panel_3.add(textPane, BorderLayout.CENTER);
			
			Component verticalStrut_2 = Box.createVerticalStrut(20);
			panel_3.add(verticalStrut_2, BorderLayout.SOUTH);
			
		}
	}
	
	/**
	 * Parses the Question Parts
	 * 
	 * @param q
	 * The array of Question Bits
	 */
	protected void parseQParts(ArrayList<Question> q) {
		for(Question qP : q)
			if(qP.getType().equals(QType.SPEC))
				txt += qP.getText() + "\n";
			else if(qP.getType().equals(QType.QUEST))
				questions.add(qP);
			else
				answers.add(qP);
				
	}
}
