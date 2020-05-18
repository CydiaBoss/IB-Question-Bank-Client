package com.ib.quest.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dialog.ModalExclusionType;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.ib.quest.Loader;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Selector Window
 * 
 * @author andre
 * @version 1.0.4.3
 */
public class Selector {

	private JFrame main;
	private Loader ld;
	
	// JPanels
	private JPanel subj, topic, quest;

	/**
	 * Create the application.
	 */
	public Selector(Loader ld) {
		this.ld = ld;
		initialize();
		EventQueue.invokeLater(() -> {
			try {
				main.setVisible(true);
			} catch (Exception e) {}
		});
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Initialize
		main = new JFrame("IB Question Bank Client");
		main.setIconImage(Toolkit.getDefaultToolkit().getImage(Error.class.getResource("/img/IBRR.png")));
		main.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		main.setResizable(false);
		main.setBounds(100, 100, 300, 40 + 60 * ld.getDBs().size());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.getContentPane().setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		main.getContentPane().add(horizontalStrut, BorderLayout.EAST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		main.getContentPane().add(horizontalStrut_1, BorderLayout.WEST);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		main.getContentPane().add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		main.getContentPane().add(verticalStrut_1, BorderLayout.SOUTH);
		
		subj = new JPanel();
		main.getContentPane().add(subj, BorderLayout.CENTER);
		subj.setLayout(new GridLayout(0, 1, 0, 20));
		
		// Button Addition
		for(HtmlAnchor a : ld.getDBs()) {
			JButton bt = new JButton(a.asText().trim());
			bt.addActionListener(e -> 
				topicSelection(a)
			);
			subj.add(bt);
		}
	}

	/**
	 * Regenerating the GUI for subject
	 * 
	 * @param anc
	 * @wbp.parser.entryPoint
	 */
	private synchronized void topicSelection(HtmlAnchor anc) {
		// Load the new 
		ld.parseDatabase(anc);
		// Create New Panel
		topic = new JPanel();
		topic.setLayout(new GridLayout(0, 1, 0, 20));
		topic.setBounds(0, 0, 360, 160);
		
		JPanel panel = new JPanel();
		topic.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JList<String> list = new JList<>();
		DefaultListModel<String> dis = new DefaultListModel<>();
		for(HtmlAnchor a : ld.getTopics())
			dis.addElement(a.asText());
		list.setModel(dis);
		list.setVisibleRowCount(4);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		JScrollPane scrollPane = new JScrollPane(list);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		topic.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut_1 = Box.createVerticalStrut(25);
		panel_1.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(1, 0, 160, 0));
		
		// A back button
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> {
			main.getContentPane().remove(topic);
			main.getContentPane().add(subj, BorderLayout.CENTER);
			main.setBounds(100, 100, 300, 40 + 60 * ld.getDBs().size());
			main.revalidate();
		});
		panel_2.add(backBtn);
		
		JButton nextBtn = new JButton("Next");
		nextBtn.addActionListener(e -> {
			quesSelection(list.getSelectedIndex());
		});
		panel_2.add(nextBtn);
		
		// Updates the Frame
		main.getContentPane().remove(subj);
		main.getContentPane().add(topic, BorderLayout.CENTER);
		main.setBounds(100, 100, 400, 200);
		main.revalidate();
	}
	
	/**
	 * This generates the question selection page
	 * 
	 * @param index
	 * Selected Topic
	 */
	private synchronized void quesSelection(int index) {
		// Load New Question Bank
		ld.loadQuest(ld.getTopics().get(index));
		// Create Panel
		quest = new JPanel();
	}
}
