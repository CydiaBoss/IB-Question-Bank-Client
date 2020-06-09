package com.ib.quest.gui.questions;

import com.ib.quest.Constants;
import com.ib.quest.Loader;
import com.ib.quest.Loader.QType;
import com.ib.quest.Loader.Question;
import com.ib.quest.Main;
import com.ib.quest.Parser;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.awt.BorderLayout;

import java.awt.Font;
import javax.swing.Box;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.CardLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * This is a JPanel for a basic question
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class BasicQuestion extends JPanel {

	private static final long serialVersionUID = -4638463439038323534L;
	
	/* Question Pieces */
	private String txt;
	ArrayList<Question> questions, 
						answers;
	private String ID;
	private int total = 0;
	
	/* Question Counter */
	private int curSlide = 1;
	
	/* Question Slides */
	private CardLayout c = new CardLayout(0, 0);
	private JPanel displayPanel = new JPanel();
	private HashMap<String, JPanel> questSlide = new HashMap<>();
	private HashMap<String, JTextArea> questSlideAns = new HashMap<>();
	private HashMap<String, JComboBox<String>> questSlideMark = new HashMap<>();
	
	/* Submitted */ 
	private boolean submit = false;
	
	/**
	 * Get Total Marks
	 */
	public int getTotal() {
		return total;
	}
	
	/**
	 * Get Marks Earned
	 */
	public int getEarn() {
		int earn = 0;
		for(JComboBox<String> mks : questSlideMark.values()) 
			earn += Integer.parseInt(((String) mks.getSelectedItem()).replace("[", "").replace("]", ""));
		return earn;
	}
	
	/**
	 * Creates the Panel
	 * 
	 * @param ld
	 * The Loader
	 * @param ID
	 * The question's ID
	 * @param m
	 * The mainframe. If null, pre must be null also
	 * @param pre
	 * The previous {@link JPanel}. If null, assume random feature
	 */
	public BasicQuestion(Loader ld, String ID, JFrame m, JPanel pre) {
		
		// Get Question
		txt = "";
		questions = new ArrayList<Question>();
		answers = new ArrayList<Question>();
		ld.loadQParts(ID);
		parseQParts(ld.getQParts());
		this.ID = ID;
		
		// Builds the Panel
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel idLbl = new JLabel(ID);
		idLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(idLbl);
		
		panel.add(Parser.parseTxt(txt, ID + "-Q"));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 80, 0));

		// A back button
		JButton quitBtn = new JButton(Main.s.getLocal().get("gen.quit"));
		
		// If it is random feature, do not put a quit button
		if(m != null && pre != null) {
			quitBtn.addActionListener(e -> {
				m.getContentPane().remove(this);
				m.getContentPane().add(pre, BorderLayout.CENTER);
				m.repaint();
				m.revalidate();
			});
			panel_1.add(quitBtn);
		
		}

		JScrollPane wrk = new JScrollPane(displayPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		displayPanel.setMaximumSize(new Dimension(Constants.Size.STAN_W - 60, 600));
		displayPanel.setPreferredSize(new Dimension(0, 600));
		wrk.setMaximumSize(new Dimension(Constants.Size.STAN_W - 40, 600));
		wrk.setPreferredSize(new Dimension(0, 600));
		wrk.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(wrk, BorderLayout.CENTER);
		displayPanel.setLayout(c);
		
		/* Generate all Question Panel */
		// TODO FIX THIS SOMEHOW WHAAAA
		
		for(Question q : questions) {
			
			JPanel panel_3 = new JPanel();
			displayPanel.add(q.getLabel(), panel_3);
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
			
			panel_4.add(Parser.parseTxt(q.getText(), ID + "-S"), BorderLayout.CENTER);
			
			JLabel markLbl = new JLabel("[" + q.getMark() + "]");
			total += q.getMark();
			markLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			markLbl.setAlignmentX(RIGHT_ALIGNMENT);
			panel_4.add(markLbl, BorderLayout.EAST);
			
			Component verticalStrut_1 = Box.createVerticalStrut(15);
			panel_4.add(verticalStrut_1, BorderLayout.SOUTH);
			
			JTextArea textPane = new JTextArea();
			textPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
			textPane.setLineWrap(true);
			textPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panel_3.add(textPane, BorderLayout.CENTER);
			
			// Puts Text Box into a HashMap
			questSlideAns.put(q.getLabel(), textPane);
			
			Component verticalStrut_2 = Box.createVerticalStrut(20);
			panel_3.add(verticalStrut_2, BorderLayout.SOUTH);
			
			// Puts all JPanel into a HashMap
			questSlide.put(q.getLabel(), panel_3);
			
		}
		
		// Determine how the buttons should be built
		// If only one question, only submit btn
		// if more, add previous and next btn that changes to submit at the end
		Box horizontalBox = Box.createHorizontalBox();

		JButton bacBtn = new JButton(Main.s.getLocal().get("gen.pre"));
		
		JButton nextBtn = new JButton(Main.s.getLocal().get("gen.next"));
		
		// Should back btn be enabled?
		if(questions.size() != 1) {
			
			panel_1.add(horizontalBox);
		
			bacBtn.setEnabled(false);
			bacBtn.addActionListener(e -> {
				c.previous(displayPanel);
				curSlide--;
				if(nextBtn.getText().equals(Main.s.getLocal().get("gen.submit")) || nextBtn.getText().equals(Main.s.getLocal().get("gen.finish")))
					nextBtn.setText(Main.s.getLocal().get("gen.next"));
				if(curSlide == 1)
					bacBtn.setEnabled(false);
				nextBtn.setEnabled(true);
			});
			horizontalBox.add(bacBtn);
			
			Component horizontalGlue = Box.createHorizontalGlue();
			horizontalBox.add(horizontalGlue);
		
		// Else, just add the submit btn
		}else
			nextBtn.setText(Main.s.getLocal().get("gen.submit"));
		
		// Effects of pressing the nextBtn
		nextBtn.addActionListener(e -> {
			// If Btn is in Next Mode
			if(nextBtn.getText().equals(Main.s.getLocal().get("gen.next"))) {
				c.next(displayPanel);
				curSlide++;
				bacBtn.setEnabled(true);
				if(curSlide == questions.size()) {
					if(m == null && pre == null)
						nextBtn.setEnabled(false);
					else {
						if(!submit)
							nextBtn.setText(Main.s.getLocal().get("gen.submit"));
						else
							nextBtn.setText(Main.s.getLocal().get("gen.finish"));
					}
				}
			// If Btn is in submit mode
			}else if(nextBtn.getText().equals(Main.s.getLocal().get("gen.submit"))) {
				submit = true;
				checkAns();
				c.first(displayPanel);
				curSlide = 1;
				bacBtn.setEnabled(false);
				// Change to Next if need
				if(questions.size() > 1)
					nextBtn.setText(Main.s.getLocal().get("gen.next"));
				// Change to Finish otherwise
				else
					nextBtn.setText(Main.s.getLocal().get("gen.finish"));
				m.repaint();
				m.revalidate();
			// If Btn is in finish mode
			}else{
				Main.h.addEntry(LocalDateTime.now(), ID, getEarn(), total);
				quitBtn.doClick();
			}
		});
		
		// If More than 1 part of question, horizontal box will exist so add to that
		if(questions.size() != 1)
			horizontalBox.add(nextBtn);
		// If part of random prog and only 1 quest, do nothing
		else if(m == null && pre == null)
			nextBtn.setEnabled(false);
		// Else, add to panel
		else
			panel_1.add(nextBtn);
	}
	
	/**
	 * The Question's ID
	 * 
	 * @return
	 * The ID
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * Adds the answers onto the text
	 * 
	 * @return
	 * The total grade
	 * @wbp.parser.entryPoint
	 */
	public void checkAns() {
		
		// Adds answers to all Panels
		for(Question a : answers) {
			// Retrieve the question
			Question qu = null;
			for(Question q : questions)
				if(q.getLabel().equals(a.getLabel())) {
					qu = q;
					break;
				}
			// Removes text area
			questSlide.get(a.getLabel()).remove(questSlideAns.get(a.getLabel()));
			// Replace with a JPanel
			JPanel ansSlide = new JPanel();
			ansSlide.setLayout(new GridLayout(0, 1, 0, 10));
			// User Panel
			JPanel panel = new JPanel();
			ansSlide.add(panel);
			panel.setLayout(new BorderLayout(0, 10));
			
			JLabel urRepLbl = new JLabel(Main.s.getLocal().get("quest.rep") + ":");
			urRepLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel.add(urRepLbl, BorderLayout.NORTH);
			
			JLabel urAnsLbl = new JLabel(questSlideAns.get(a.getLabel()).getText());
			urAnsLbl.setVerticalAlignment(SwingConstants.TOP);
			urAnsLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel.add(urAnsLbl, BorderLayout.CENTER);
			// Answer Panel
			JPanel panel_1 = new JPanel();
			ansSlide.add(panel_1);
			panel_1.setLayout(new BorderLayout(0, 10));
			
			JLabel ansLbl = new JLabel(Main.s.getLocal().get("quest.ans") + ":");
			ansLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_1.add(ansLbl, BorderLayout.NORTH);
			
			panel_1.add(Parser.parseTxt(a.getText(), ID + "-A"), BorderLayout.CENTER);
			
			JPanel panel_2 = new JPanel();
			panel_1.add(panel_2, BorderLayout.SOUTH);
			panel_2.setLayout(new BorderLayout(0, 0));
			
			// Generate all marks
			String[] marks = new String[qu.getMark() + 1];
			for(int i = 0; i < marks.length; i++)
				marks[i] = "[" + i + "]";
			
			JComboBox<String> comboBox = new JComboBox<>();
			comboBox.setModel(new DefaultComboBoxModel<String>(marks));
			comboBox.setSelectedIndex(qu.getMark());
			questSlideMark.put(a.getLabel(), comboBox);
			panel_2.add(comboBox, BorderLayout.EAST);
			
			// Mark some sort of mark calculating system
			
			// TODO Change answer correction
			JLabel markLbl = new JLabel(Main.s.getLocal().get("quest.rew") + ": ");
			markLbl.setHorizontalAlignment(SwingConstants.TRAILING);
			markLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
			panel_2.add(markLbl, BorderLayout.CENTER);
			
			// Add Back
			questSlide.get(a.getLabel()).add(ansSlide, BorderLayout.CENTER);
		}
	}
	
	/**
	 * Parses the Question Parts
	 * 
	 * @param q
	 * The array of Question Bits
	 */
	protected void parseQParts(ArrayList<Question> q) {
		// Split the question parts
		for(Question qP : q)
			if(qP.getType().equals(QType.SPEC))
				txt += qP.getText() + "\n";
			else if(qP.getType().equals(QType.QUEST))
				questions.add(qP);
			else
				answers.add(qP);
				
	}
}
