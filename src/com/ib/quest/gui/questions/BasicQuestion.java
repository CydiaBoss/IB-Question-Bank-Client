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
import java.util.HashMap;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.Font;
import javax.swing.Box;
import java.awt.Component;
import java.awt.CardLayout;
import javax.swing.SwingConstants;

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
	
	/* Question Counter */
	private int curSlide = 1;
	
	/* Question Slides */
	private HashMap<String, JPanel> questSlide = new HashMap<>();
	private HashMap<String, JTextArea> questSlideAns = new HashMap<>();
	
	/* Submitted */ 
	private boolean submit = false;
	
	/**
	 * Creates the Panel
	 * 
	 * @param ld
	 * The Loader
	 * @param ID
	 * The question's ID
	 * @param m
	 * The mainframe
	 * @param pre
	 * The previous {@link JPanel}
	 */
	public BasicQuestion(Loader ld, String ID, JFrame m, JPanel pre) {
		
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
			m.repaint();
			m.revalidate();
		});
		panel_1.add(quitBtn);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		CardLayout c = new CardLayout(0, 0);
		panel_2.setLayout(c);
		
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
			
			JLabel labelLbl = new JLabel(q.getLabel() + ") ");
			labelLbl.setVerticalAlignment(SwingConstants.TOP);
			labelLbl.setAlignmentY(TOP_ALIGNMENT);
			labelLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_4.add(labelLbl, BorderLayout.WEST);
			
			JLabel quesLbl = new JLabel("<html>" + q.getText() + "</html>");
			quesLbl.setVerticalAlignment(SwingConstants.TOP);
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
			textPane.setLineWrap(true);
			panel_3.add(textPane, BorderLayout.CENTER);
			
			// Puts Text Box into a HashMap
			questSlideAns.put(q.getLabel(), textPane);
			
			Component verticalStrut_2 = Box.createVerticalStrut(20);
			panel_3.add(verticalStrut_2, BorderLayout.SOUTH);
			
			// Puts all JPanel into a HashMap
			questSlide.put(q.getLabel(), panel_3);
			
		}
		
		Box horizontalBox = Box.createHorizontalBox();

		JButton bacBtn = new JButton("Back");
		
		JButton nextBtn = new JButton("Next");
		
		if(questions.size() != 1) {
			
			panel_1.add(horizontalBox);
		
			bacBtn.setEnabled(false);
			bacBtn.addActionListener(e -> {
				c.previous(panel_2);
				curSlide--;
				if(nextBtn.getText().equals("Submit"))
					nextBtn.setText("Next");
				if(curSlide == 1)
					bacBtn.setEnabled(false);
				nextBtn.setEnabled(true);
			});
			horizontalBox.add(bacBtn);
			
			Component horizontalGlue = Box.createHorizontalGlue();
			horizontalBox.add(horizontalGlue);
			
		}
		
		if(questions.size() == 1)
			nextBtn.setText("Submit");
		nextBtn.addActionListener(e -> {
			if(nextBtn.getText().equals("Next")) {
				c.next(panel_2);
				curSlide++;
				bacBtn.setEnabled(true);
				if(curSlide == questions.size()) {
					nextBtn.setText("Submit");
					if(submit)
						nextBtn.setEnabled(false);
				}
			}else{
				submit = true;
				checkAns();
				m.repaint();
				m.revalidate();
				nextBtn.setEnabled(false);
			}
		});
		
		if(questions.size() != 1)
			horizontalBox.add(nextBtn);
		else
			panel_1.add(nextBtn);
	}
	
	/**
	 * Adds the answers onto the text
	 */
	private void checkAns() {
		for(Question a : answers) {
			
			JLabel ansLbl = new JLabel("<html>" + a.getText() + "</html>");
			ansLbl.setVerticalAlignment(SwingConstants.TOP);
			ansLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			questSlide.get(a.getLabel()).add(ansLbl, BorderLayout.EAST);
			questSlideAns.get(a.getLabel()).setEditable(false);
			
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
