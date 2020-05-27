package com.ib.quest.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dialog.ModalExclusionType;
import java.time.LocalDateTime;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.ib.quest.Loader;
import com.ib.quest.Main;
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
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerListModel;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Selector Window
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Selector {

	// The mainframe
	private JFrame main;
	// The loader
	private Loader ld;
	
	// JPanels
	private JPanel subj, topic, quest, ran;

	/**
	 * Create the application.
	 */
	public Selector(Loader ld) {
		this.ld = ld;
		// Build
		initialize();
		// Display
		EventQueue.invokeLater(() -> {
			try {
				main.setVisible(true);
			} catch (Exception e) {}
		});
	}
	
	/**
	 * Reloads the JFrame to properly display {@link Component} 
	 */
	private void reload() {
		main.repaint();
		main.revalidate();
	}
	
	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		// Initialize
		main = new JFrame(Main.s.getLocal().get("gen.title"));
		main.setIconImage(Toolkit.getDefaultToolkit().getImage(Selector.class.getResource("/img/IBRRI.png")));
		main.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		main.setResizable(false);
		main.setSize(Constants.Size.STAN_W , Constants.Size.STAN_H);
		// Spawns the JFrame in the middle of the screen
		main.setLocationRelativeTo(null);
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
		subj.setLayout(new GridLayout(0, 1, 0, 5));
		
		Box horizontalBox = Box.createHorizontalBox();
		subj.add(horizontalBox);
		
		JLabel titleLbl = new JLabel("<html>" + Main.s.getLocal().get("subj.intro") + "</html>");
		titleLbl.setVerticalAlignment(SwingConstants.BOTTOM);
		titleLbl.setFont(new Font("Tahoma", Font.BOLD, 32));
		titleLbl.setIcon(new ImageIcon(Selector.class.getResource("/img/IBRRLB.png")));
		horizontalBox.add(titleLbl);
		
		JPanel panel_1 = new JPanel();
		horizontalBox.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{83, 0};
		gbl_panel_1.rowHeights = new int[]{64, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton setBtn = new JButton("");
		setBtn.addActionListener(e -> {
			main.remove(subj);
			main.add(Main.s.init(main, subj), BorderLayout.CENTER);
			reload();
		});
		setBtn.setIcon(new ImageIcon(Selector.class.getResource("/img/COG.png")));
		setBtn.setToolTipText(Main.s.getLocal().get("set"));
		GridBagConstraints gbc_setBtn = new GridBagConstraints();
		gbc_setBtn.insets = new Insets(0, 0, 5, 0);
		gbc_setBtn.anchor = GridBagConstraints.NORTHEAST;
		gbc_setBtn.gridx = 0;
		gbc_setBtn.gridy = 0;
		panel_1.add(setBtn, gbc_setBtn);
		
		JButton histBtn = new JButton();
		histBtn.addActionListener(e -> {
			main.remove(subj);
			main.add(Main.h.genHistPanel(main, subj), BorderLayout.CENTER);
			reload();
		});
		histBtn.setIcon(new ImageIcon(Selector.class.getResource("/img/HIST.png")));
		histBtn.setToolTipText(Main.s.getLocal().get("hist"));
		GridBagConstraints gbc_histBtn = new GridBagConstraints();
		gbc_histBtn.anchor = GridBagConstraints.NORTHEAST;
		gbc_histBtn.gridx = 0;
		gbc_histBtn.gridy = 1;
		panel_1.add(histBtn, gbc_histBtn);
		
		JPanel panel = new JPanel();
		subj.add(panel);
		panel.setLayout(new GridLayout(0, 2, 10, 20));
		
		// Button Addition
		for(HtmlAnchor a : ld.getDBs()) {
			JButton bt = new JButton(Main.s.getLocal().get("main." + a.asText().trim().toLowerCase()));
			bt.addActionListener(e -> 
				topicSelection(a)
			);
			panel.add(bt);
		}
	}

	/**
	 * Regenerating the GUI for subject
	 * 
	 * @param anc
	 * The subject to generate from
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
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		panel_1.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(1, 0, 120, 0));
		
		// A back button
		JButton backBtn = new JButton(Main.s.getLocal().get("gen.back"));
		backBtn.addActionListener(e -> {
			main.getContentPane().remove(topic);
			main.getContentPane().add(subj, BorderLayout.CENTER);
			reload();
		});
		panel_2.add(backBtn);
		
		JButton nextBtn = new JButton(Main.s.getLocal().get("gen.next"));
		nextBtn.addActionListener(e -> {
			// Throw if No Select
			if(list.getSelectedIndex() < 0) {
				Error.throwError(Main.s.getLocal().get("error.top.sel"), false);
				return;
			}
			// Gen
			quesSelection(list.getSelectedIndex());
		});
		
		Component verticalStrut = Box.createVerticalStrut(40);
		panel_2.add(verticalStrut);
		panel_2.add(nextBtn);
		
		// Updates the Frame
		main.getContentPane().remove(subj);
		main.getContentPane().add(topic, BorderLayout.CENTER);
		
		JLabel instruLbl = new JLabel(Main.s.getLocal().get("topic.intro"));
		instruLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		topic.add(instruLbl, BorderLayout.NORTH);
		reload();
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
		quest.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		quest.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		// Create the 2D Array from HashMap
		String[] titles = {Main.s.getLocal().get("quest.sel.id"), Main.s.getLocal().get("quest.sel.qu")};
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
			public boolean editCellAt(int row, int column, EventObject e) {
             	return false;
          	}
		};
		// Customize Table
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
		
		JLabel instrLbl = new JLabel(Main.s.getLocal().get("quest.intro"));
		instrLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(instrLbl);
		
		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);
		
		JScrollPane scrollPane = new JScrollPane(tab);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		quest.add(scrollPane);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerListModel(Constants.QData.SIZE));
		// No Typing in JSPinner
		((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
		
		JRadioButton randomRdBtn = new JRadioButton(Main.s.getLocal().get("quest.ran"));
		randomRdBtn.setSelected(true);
		randomRdBtn.setHorizontalAlignment(SwingConstants.CENTER);
		randomRdBtn.addActionListener(e -> {
			if(randomRdBtn.isSelected()) {
				tab.setEnabled(false);
				tab.setRowSelectionAllowed(false);
				spinner.setEnabled(true);
			}
		});
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);
		
		horizontalBox.add(randomRdBtn);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JLabel questAmtLbl = new JLabel(Main.s.getLocal().get("quest.ran.amt") + ": ");
		horizontalBox.add(questAmtLbl);
		
		horizontalBox.add(spinner);
		
		JRadioButton selRdBtn = new JRadioButton(Main.s.getLocal().get("quest.sel"));
		selRdBtn.setHorizontalAlignment(SwingConstants.CENTER);
		selRdBtn.addActionListener(e -> {
			if(selRdBtn.isSelected()) {
				tab.setEnabled(true);
				tab.setRowSelectionAllowed(true);
				spinner.setEnabled(false);
			}
		});
		
		ButtonGroup sel = new ButtonGroup();
		sel.add(randomRdBtn);
		
		sel.add(selRdBtn);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		horizontalBox_1.add(selRdBtn);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_1);
		
		JPanel panel_1 = new JPanel();
		quest.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut_1 = Box.createVerticalStrut(70);
		panel_1.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(1, 0, 160, 0));
		
		// A back button
		JButton backBtn = new JButton(Main.s.getLocal().get("gen.back"));
		backBtn.addActionListener(e -> {
			main.getContentPane().remove(quest);
			main.getContentPane().add(topic, BorderLayout.CENTER);
			reload();
		});
		panel_2.add(backBtn);
		
		JButton stBtn = new JButton(Main.s.getLocal().get("gen.submit"));
		stBtn.addActionListener(e -> {
			if(selRdBtn.isSelected()) {
				if(tab.getSelectedRow() < 0) {
					Error.throwError(Main.s.getLocal().get("error.quest.sel"), false);
					return;
				}
				main.getContentPane().remove(quest);
				main.getContentPane().add(new BasicQuestion(ld, 
						((String) tab.getValueAt(tab.getSelectedRow(), 0)).trim(), main, quest), BorderLayout.CENTER);
				reload();
			}else{
				ranQuestSlide((int) spinner.getValue());
				reload();
			}
		});
		panel_2.add(stBtn);
		
		// Updates the Frame
		main.getContentPane().remove(topic);
		main.getContentPane().add(quest, BorderLayout.CENTER);
		reload();
	}
	
	/* Variables for the random */
	
	// Tracks Slide count
	private int curSlide = 1;
	// Tracks whether submitted or not
	private boolean submit = false;
	
	/**
	 * Creates the random page
	 */
	private void ranQuestSlide(int amt) {
		
		// Reset
		curSlide = 1;
		submit = false;
		// Pick Question
		BasicQuestion[] selQ = new BasicQuestion[amt];
		// A Faster Style of Random for a smaller list
		ArrayList<String> ranID = new ArrayList<>();
		// Set to List
		for(String s : ld.getQues().keySet()) 
			ranID.add(s);
		// Random Questions
		int curQ = 0;
		mainLoop:
		while(curQ < amt){
			// Shuffle array with quest ID
			Collections.shuffle(ranID);
			// Add enough
			for(String qID : ranID) {
				selQ[curQ] = new BasicQuestion(ld, qID, null, null);
				curQ++;
				// Escape Once Reached
				if(curQ == amt)
					break mainLoop;
			}
		}
			
		main.getContentPane().remove(quest);
		
		ran = new JPanel();
		ran.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		ran.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel titleLbl = new JLabel(String.format(Main.s.getLocal().get("ran.intro"), amt));
		titleLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_2.add(titleLbl);
		
		JLabel curQLbl = new JLabel(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
		curQLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_2.add(curQLbl);
		
		JPanel panel = new JPanel();
		ran.add(panel, BorderLayout.CENTER);
		CardLayout c = new CardLayout(0, 0);
		panel.setLayout(c);
		for(BasicQuestion bQ : selQ)
			panel.add(bQ.getID(), bQ);
		
		JPanel panel_1 = new JPanel();
		ran.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 60, 0));
		
		JButton quitBtn = new JButton(Main.s.getLocal().get("gen.quit"));
		quitBtn.addActionListener(e -> {
			main.getContentPane().remove(ran);
			main.getContentPane().add(quest, BorderLayout.CENTER);
			reload();
		});
		panel_1.add(quitBtn);
		
		Box horizontalBox = Box.createHorizontalBox();

		JButton bacBtn = new JButton(Main.s.getLocal().get("gen.pre"));
		
		JButton nextBtn = new JButton(Main.s.getLocal().get("gen.next"));
		
		if(amt != 1) {
			
			panel_1.add(horizontalBox);
		
			bacBtn.setEnabled(false);
			bacBtn.addActionListener(e -> {
				c.previous(panel);
				curSlide--;
				curQLbl.setText(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
				if(nextBtn.getText().equals(Main.s.getLocal().get("gen.submit")))
					nextBtn.setText(Main.s.getLocal().get("gen.next"));
				if(curSlide == 1)
					bacBtn.setEnabled(false);
				nextBtn.setEnabled(true);
			});
			horizontalBox.add(bacBtn);
			
			Component horizontalGlue = Box.createHorizontalGlue();
			horizontalBox.add(horizontalGlue);
			
		}else
			nextBtn.setText(Main.s.getLocal().get("gen.submit"));
		nextBtn.addActionListener(e -> {
			if(nextBtn.getText().equals(Main.s.getLocal().get("gen.next"))) {
				c.next(panel);
				curSlide++;
				curQLbl.setText(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
				bacBtn.setEnabled(true);
				if(curSlide == amt) {
					nextBtn.setText(Main.s.getLocal().get("gen.submit"));
					if(submit)
						nextBtn.setEnabled(false);
				}
			// Reveal Answer and Move back to slide 1
			}else{
				submit = true;
				int total = 0,
					mks = 0;
				for(BasicQuestion bQ : selQ) {
					mks += bQ.checkAns();
					total += bQ.getTotal();
				}
				c.first(panel);
				curSlide = 1;
				// Prevent Btn from being named next even tho there is only 1 question
				if(amt != 1)
					nextBtn.setText(Main.s.getLocal().get("gen.next"));
				else 
					nextBtn.setEnabled(false);
				curQLbl.setText(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
				bacBtn.setEnabled(false);
				Main.h.addEntry(LocalDateTime.now(), "Q" + amt + ".RND", mks, total);
				reload();
			}
		});
		
		// If More than 1 part of question, horizontal box will exist so add to that
		if(amt != 1)
			horizontalBox.add(nextBtn);
		// Else, add to panel
		else
			panel_1.add(nextBtn);
		
		// Adds to the Mainframe
		main.getContentPane().add(ran, BorderLayout.CENTER);
	}
}
