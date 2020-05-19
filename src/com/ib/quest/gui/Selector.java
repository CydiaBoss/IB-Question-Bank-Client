package com.ib.quest.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dialog.ModalExclusionType;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.ib.quest.Loader;
import com.ib.quest.gui.questions.BasicQuestion;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import com.ib.quest.Constants;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * Selector Window
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class Selector {

	// The mainframe
	private JFrame main;
	// The loader
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
		main.setSize(Constants.Size.SUB_W , Constants.Size.SUB_H + Constants.Size.INT_SIZE * ld.getDBs().size());
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
		topic.setLayout(new BorderLayout(0, 10));
		
		JPanel panel = new JPanel();
		topic.add(panel, BorderLayout.CENTER);
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
		topic.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut_1 = Box.createVerticalStrut(30);
		panel_1.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(1, 0, 160, 0));
		
		// A back button
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> {
			main.getContentPane().remove(topic);
			main.getContentPane().add(subj, BorderLayout.CENTER);
			main.setSize(Constants.Size.SUB_W , 
					Constants.Size.SUB_H + Constants.Size.INT_SIZE * ld.getDBs().size());
			main.revalidate();
		});
		panel_2.add(backBtn);
		
		JButton nextBtn = new JButton("Next");
		nextBtn.addActionListener(e -> {
			// Throw if No Select
			if(list.getSelectedIndex() < 0) {
				Error.throwError("Please select a Topic", false);
				return;
			}
			// Gen
			quesSelection(list.getSelectedIndex());
		});
		panel_2.add(nextBtn);
		
		// Updates the Frame
		main.getContentPane().remove(subj);
		main.getContentPane().add(topic, BorderLayout.CENTER);
		
		JLabel instruLbl = new JLabel("Please select a topic.");
		instruLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		topic.add(instruLbl, BorderLayout.NORTH);
		main.setSize(Constants.Size.STAN_W, Constants.Size.STAN_H);
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
		quest.setLayout(new GridLayout(0, 1, 0, 20));
		
		JPanel panel = new JPanel();
		quest.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		// Create the 2D Array from HashMap
		String[] titles = {"Question ID", "Question"};
		String[][] data = new String[ld.getQues().size()][2];
		
		int i = 0;
		
		for(String s : ld.getQues().keySet()) {
			data[i][0] = s;
			data[i][1] = ld.getQues().get(s).asText();
			i++;
		}
		
		JTable tab = new JTable(data, titles) {
			private static final long serialVersionUID = 592523610055291313L;

			/**
			 * Disables Editing
			 */
			@Override
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
             	return false;
          	}
		};
		tab.setShowVerticalLines(false);
		tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		tab.getTableHeader().setResizingAllowed(false);
		tab.getTableHeader().setReorderingAllowed(false);
		tab.getColumnModel().getColumn(0).setMinWidth(115);
		tab.getColumnModel().getColumn(0).setMaxWidth(125);
		// Default Disabled
		tab.setEnabled(false);
		
		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);
		
		JScrollPane scrollPane = new JScrollPane(tab);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		quest.add(scrollPane);
		
		JRadioButton randomRdBtn = new JRadioButton("Random");
		randomRdBtn.setSelected(true);
		randomRdBtn.setHorizontalAlignment(SwingConstants.CENTER);
		randomRdBtn.addActionListener(e -> {
			if(randomRdBtn.isSelected()) {
				tab.setEnabled(false);
				tab.setRowSelectionAllowed(false);
			}
		});
		verticalBox.add(randomRdBtn);
		
		JRadioButton selRdBtn = new JRadioButton("Selection");
		selRdBtn.setHorizontalAlignment(SwingConstants.CENTER);
		selRdBtn.addActionListener(e -> {
			if(selRdBtn.isSelected()) {
				tab.setEnabled(true);
				tab.setRowSelectionAllowed(true);
			}
		});
		verticalBox.add(selRdBtn);
		
		ButtonGroup sel = new ButtonGroup();
		sel.add(randomRdBtn);
		sel.add(selRdBtn);
		
		JPanel panel_1 = new JPanel();
		quest.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut_1 = Box.createVerticalStrut(40);
		panel_1.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(1, 0, 160, 0));
		
		// A back button
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> {
			main.getContentPane().remove(quest);
			main.getContentPane().add(topic, BorderLayout.CENTER);
			main.revalidate();
		});
		panel_2.add(backBtn);
		
		JButton stBtn = new JButton("Start");
		stBtn.addActionListener(e -> {
			if(selRdBtn.isSelected()) {
				if(tab.getSelectedRow() < 0) {
					Error.throwError("Please select a Question", false);
					return;
				}
				main.getContentPane().remove(quest);
				main.getContentPane().add(new BasicQuestion(main.getX(), main.getY(), ld, 
						((String) tab.getValueAt(tab.getSelectedRow(), 0)).trim(), main, quest), BorderLayout.CENTER);
				main.revalidate();
			}
		});
		panel_2.add(stBtn);
		
		// Updates the Frame
		main.getContentPane().remove(topic);
		main.getContentPane().add(quest, BorderLayout.CENTER);
		main.revalidate();
	}
}
