package com.ib.quest.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JPanel;

import com.ib.quest.Constants;
import com.ib.quest.Main;
import com.ib.quest.gui.template.Progress;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.Box;

/**
 * Handles the user's question history
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class History {

	/**
	 * Stores the History
	 * 
	 * key = date
	 * value = question data
	 */
	private HashMap<String, String> history = new HashMap<>();
	
	/**
	 * The history file
	 */
	private File histFile = new File(Constants.HData.LOC);
	
	/**
	 * History Getter
	 * 
	 * @return
	 * History
	 */
	public HashMap<String, String> getHist() {
		return history;
	}
	
	/**
	 * Progress Tracker
	 */
	private Progress p;
	
	/**
	 * Builds history
	 */
	public History(Progress p) {
		// Progress
		this.p = p;
		// Verify History File Existence
		if(!histFile.exists())
			try {
				histFile.createNewFile();
				// Empty file so, no parsing required
				return;
			} catch (IOException e) {}
		// History Verified
		// Otherwise, parse the file
		parseHist();
		p.progress();
	}

	/**
	 * Reads the History File
	 */
	private void parseHist() {
		// Creates the Scanner
		Scanner rd = null;
		try {
			rd = new Scanner(histFile, "UTF-8");
		} catch (FileNotFoundException e) {
			Main.throwError(Main.s.getLocal().get("error.in"), true);
		}
		p.addTask(Main.s.getLocal().get("load.start.hist"), 1);
		// Scans stuff
		while(rd.hasNextLine()) {
			String[] ln = rd.nextLine().split(";");
			// Move if corruption
			if(ln.length < 2)
				continue;
			// dd-MM-YYYY HH:mm:ss;QID%mark/MARK
			history.put(ln[0], ln[1]);
		}
		// History Read
		p.progress();
	}
	
	/**
	 * Adds a history entry into the system
	 * 
	 * @param t
	 * The current time
	 * @param ID
	 * The question id
	 * @param mark
	 * The marks gained
	 * @param total
	 * Total marks
	 */
	public void addEntry(LocalDateTime t, String ID, int mark, int total) {
		// Format the time
		String sT = DateTimeFormatter.ofPattern(Constants.HData.FORM).format(t);
		// Add to HashMap
		String data = ID + "%" + mark + "/" + total;
		history.put(sT, data);
		// Add to File
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(histFile, true));
			bw.write(sT + ";" + data + "\n");
			bw.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Clears all history
	 */
	private void clear() {
		// Clears HashMap
		history.clear();
		// Clear File
		try {
			new FileWriter(histFile, false).close();
		} catch (IOException e) {}
	}
	
	/**
	 * Generates a panel that displays the history.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public JPanel genHistPanel(JFrame main, JPanel pre) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		// TODO add Localization
		String[] headers = {Main.s.getLocal().get("hist.date"), Main.s.getLocal().get("quest.sel.id"), Main.s.getLocal().get("hist.mark")};
		String[][] data = new String[history.size()][3];
		
		int i = 1;
		
		for(String s : history.keySet()) {
			data[history.size() - i][0] = s.substring(0, s.length() - 3);
			String [] questData = history.get(s).split("%");
			data[history.size() - i][1] = questData[0];
			data[history.size() - i][2] = questData[1];
			i++;
		}
		
		JTable table = new JTable(data, headers) {
			private static final long serialVersionUID = 734886694495702651L;

			/**
			 * Disables Editing
			 */
			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
             	return false;
          	}
		};
		table.setShowVerticalLines(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel titleLbl = new JLabel(Main.s.getLocal().get("hist"));
		titleLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(titleLbl, BorderLayout.NORTH);
		
		JLabel questCountLbl = new JLabel(Main.s.getLocal().get("hist.done") + ": " + history.size());
		questCountLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(questCountLbl, BorderLayout.WEST);
		
		// Calculate the Average
		double sum = 0,
			total = 0;
		for(String s : history.values()) {
			String[] marks = s.split("%")[1].split("/");
			sum += Integer.parseInt(marks[0]);
			total += Integer.parseInt(marks[1]);
		}
		
		JLabel avgLbl = new JLabel();
		
		// No dividing by 0
		try {
			avgLbl.setText(Main.s.getLocal().get("hist.avg") + ": " + (int) ((sum / total) * 100) + "%");
		}catch(ArithmeticException e) {
			avgLbl.setText(Main.s.getLocal().get("hist.avg") + ": n/a");
		}
		
		avgLbl.setHorizontalAlignment(SwingConstants.TRAILING);
		avgLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(avgLbl, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton backBtn = new JButton(Main.s.getLocal().get("gen.back"));
		backBtn.addActionListener(e -> {
			main.remove(panel);
			main.add(pre, BorderLayout.CENTER);
			main.repaint();
			main.revalidate();
		});
		panel_3.add(backBtn);
		
		JButton cleBtn = new JButton(Main.s.getLocal().get("gen.reset"));
		// No need for a reset if nothing to reset
		if(history.size() == 0)
			cleBtn.setEnabled(false);
		cleBtn.addActionListener(e -> {
			cleBtn.setEnabled(false);
			clear();
			main.remove(panel);
			main.add(pre, BorderLayout.CENTER);
			main.repaint();
			main.revalidate();
		});
		
		Component verticalStrut = Box.createVerticalStrut(35);
		panel_3.add(verticalStrut);
		panel_3.add(cleBtn);
		
		return panel;
	}
}
