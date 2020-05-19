package com.ib.quest.gui.questions;

import javax.swing.JScrollPane;

import com.ib.quest.Loader;
import com.ib.quest.Loader.QType;
import com.ib.quest.Loader.Question;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Font;

/**
 * This is a JPanel for a basic question
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class BasicQuestion extends JScrollPane {

	private static final long serialVersionUID = -4638463439038323534L;
	
	/* Main Panel */
	private static JPanel panel;
	
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
	public BasicQuestion(int x, int y, Loader ld, String ID) {
		super(panel = new JPanel());
		// Get Question
		txt = "";
		questions = new ArrayList<Question>();
		answers = new ArrayList<Question>();
		ld.loadQParts(ID);
		parseQParts(ld.getQParts());
		// Setup Panel
		setBounds(x, y, 400, 450);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		setSize(400, 450);
		panel.setLayout(new GridLayout(0, 1, 0, 10));
		panel.setBounds(x, y, 400, 450);
		panel.setMaximumSize(new Dimension(400, 450));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(x, y, 400, 450);
		panel_1.setMaximumSize(new Dimension(400, 450));
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel quesIDLbl = new JLabel(ID);
		quesIDLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(quesIDLbl, BorderLayout.NORTH);
		
		JLabel specLbl = new JLabel(txt);
		specLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(specLbl, BorderLayout.SOUTH);
		
		/* Special Question Construction */
		
		for(Question q : questions) {
			// Create custom JPanel
			JPanel panel_2 = new JPanel();
			panel_2.setBounds(x, y, 400, 450);
			panel_2.setMaximumSize(new Dimension(400, 450));
			panel.add(panel_2);
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			gbl_panel_2.columnWidths = new int[] {0, 0, 0, 3};
			gbl_panel_2.rowHeights = new int[] {0, 0, 2};
			gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			panel_2.setLayout(gbl_panel_2);
			
			JLabel labelLbl = new JLabel(q.getLabel());
			GridBagConstraints gbc_labelLbl = new GridBagConstraints();
			gbc_labelLbl.insets = new Insets(0, 0, 5, 5);
			gbc_labelLbl.gridx = 0;
			gbc_labelLbl.gridy = 0;
			panel_2.add(labelLbl, gbc_labelLbl);
			
			JLabel quesLbl = new JLabel("<html>" + q.getText() + "</html>");
			GridBagConstraints gbc_quesLbl = new GridBagConstraints();
			gbc_quesLbl.fill = GridBagConstraints.HORIZONTAL;
			gbc_quesLbl.insets = new Insets(0, 0, 5, 5);
			gbc_quesLbl.gridx = 1;
			gbc_quesLbl.gridy = 0;
			panel_2.add(quesLbl, gbc_quesLbl);
			
			JLabel markLbl = new JLabel("[" + q.getMark() + "]");
			GridBagConstraints gbc_markLbl = new GridBagConstraints();
			gbc_markLbl.insets = new Insets(0, 0, 5, 0);
			gbc_markLbl.gridx = 2;
			gbc_markLbl.gridy = 0;
			panel_2.add(markLbl, gbc_markLbl);
			
			JTextPane textPane = new JTextPane();
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.gridwidth = 3;
			gbc_textPane.insets = new Insets(0, 0, 0, 5);
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 0;
			gbc_textPane.gridy = 1;
			panel_2.add(textPane, gbc_textPane);
		}
		
		setVisible(true);
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
				txt += qP.getText();
			else if(qP.getType().equals(QType.QUEST))
				questions.add(qP);
			else
				answers.add(qP);
				
	}
}
