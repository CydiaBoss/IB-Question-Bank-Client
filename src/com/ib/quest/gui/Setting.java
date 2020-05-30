package com.ib.quest.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JPanel;

import com.ib.quest.Main;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;

/**
 * Host all the necessary code for the settings
 * 
 * @author Andrew Wang
 * @version 1.0.4.8
 */
public class Setting{

	/**
	 * Internal Data
	 */
	private HashMap<String, String> internal = new HashMap<>();
 	
	/**
	 * Settings
	 */
	private HashMap<String, String> setting = new HashMap<>();
	
	/**
	 * Setting Getter
	 * 
	 * @return
	 * Settings
	 */
	public HashMap<String, String> getSetting() {
		return setting;
	}
	
	/**
	 * Host all localization 
	 */
	private HashMap<String, String> local = new HashMap<>();
	
	/**
	 * Lang Getter
	 * 
	 * @return
	 * Language
	 */
	public HashMap<String, String> getLocal() {
		return local;
	}
	
	/**
	 * File
	 */
	private File config;
	
	/**
	 * Init. the setting manager
	 */
	public Setting() {
		// Verify if custom defConfig exist
		config = new File("config.ibqb");
		// IF not exist, copy new one from storage
		if(!config.exists())
			try {
				genNewFile();
			} catch (IOException e) {}
		// Read Internal Settings
		parseInternal();
		// Read File
		parseFile();
		// Language Import
		readLang();
	}
	
	/**
	 * Generate New config.ibqb file
	 * @throws IOException 
	 */
	private void genNewFile() throws IOException {
		// Creates new blank file
		config.createNewFile();
		// Reads from the default file
		Scanner in = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream("file/config.ibqb"), "UTF-8");
		// Write to new file
		while(in.hasNextLine()) {
			String[] ln = in.nextLine().split("=");
			// Skip bad lines
			if(ln.length < 2)
				continue;
			try {
				setting.put(ln[0], ln[1]);
			}catch(ArrayIndexOutOfBoundsException e) {
				Error.throwError(Main.s.getLocal().get("error.setting"), true);
			}
		}
		in.close();
		// Update new file
		updateSetting();
	}
	
	/**
	 * Reads the setting file
	 */
	private void parseFile() {
		// Scanner
		Scanner rd = null;
		try {
			rd = new Scanner(config, "UTF-8");
		} catch (FileNotFoundException e) {}
		// Scans the File
		while(rd.hasNextLine()) {
			String[] ln = rd.nextLine().split("=");
			// Skip bad lines
			if(ln.length < 2)
				continue;
			try {
				setting.put(ln[0], ln[1]);
			}catch(ArrayIndexOutOfBoundsException e) {
				Error.throwError(Main.s.getLocal().get("error.setting"), true);
			}
		}
		rd.close();
	}
	
	/**
	 * Reads the internal data file
	 */
	private void parseInternal() {
		// Scanner
		Scanner rd = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream("file/internal.ibqb"), "UTF-8");
		// Scans the File
		while(rd.hasNextLine()) {
			String[] ln = rd.nextLine().split("=");
			// Skip bad lines
			if(ln.length < 2)
				continue;
			try {
				internal.put(ln[0], ln[1]);
			}catch(ArrayIndexOutOfBoundsException e) {
				Error.throwError(Main.s.getLocal().get("error.setting"), true);
			}
		}
		rd.close();
	}

	/**
	 * Reads the correct Language File
	 */
	private void readLang() {
		// Scanner
		Scanner rd = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream("file/" + setting.get("lang") + ".lang"), "UTF-8");
		// Scans the File
		while(rd.hasNextLine()) {
			String[] ln = rd.nextLine().split("=");
			// Skip bad lines
			if(ln.length < 2)
				continue;
			try {
				local.put(ln[0], ln[1]);
			}catch(ArrayIndexOutOfBoundsException e) {
				// Oversight: If Lang failed to read, prog dont know what error.in is
				Error.throwError(Main.s.getLocal().get("error.in"), true);
			}
		}
		rd.close();
	}
	
	/**
	 * Updates a value in settings
	 * 
	 * @param key
	 * The Setting's Key
	 * @param value
	 * The New Value
	 * @param main
	 * The mainframe to dispose if reload is required (Make null if not needed)
	 */
	public void changeSetting(String key, String value, JFrame main) {
		setting.replace(key, value);
		updateSetting();
		if(main != null) {
			// Reload everything
			new Thread(new Main()).start();
			// Kill old stuff
			main.setEnabled(false);
			main.dispose();
		}
	}
	
	/**
	 * Update the setting file
	 */
	private void updateSetting() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(config, false));
			// Writes to file
			for(String key : setting.keySet()) 
				bw.write(key + "=" + setting.get(key) + "\n");
			// Close
			bw.close();
		} catch (IOException e) {}
	}
	
	private HashMap<String, Boolean> pendSet = new HashMap<>();
	
	/**
	 * Creates The JPanel that hosts the setting panel
	 * 
	 * @param main
	 * the mainframe
	 * @param pre
	 * the previous panel
	 * 
	 * @return
	 * The Panel
	 * 
	 * @wbp.parser.entryPoint
	 */
	public JPanel init(JFrame main, JPanel pre) {
		// Make
		JPanel set = new JPanel();
		// Setup
		set.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		set.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 120, 0));
		
		JButton canBtn = new JButton(Main.s.getLocal().get("gen.quit"));
		canBtn.addActionListener(e -> {
			main.remove(set);
			main.add(pre, BorderLayout.CENTER);
			main.repaint();
			main.revalidate();
		});
		panel.add(canBtn);
		
		JButton applyBtn = new JButton(Main.s.getLocal().get("gen.apply"));
		applyBtn.setEnabled(false);
		panel.add(applyBtn);
		
		JPanel panel_1 = new JPanel();
		set.add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel langLbl = new JLabel(Main.s.getLocal().get("set.lang") + ": ");
		GridBagConstraints gbc_langLbl = new GridBagConstraints();
		gbc_langLbl.insets = new Insets(0, 0, 5, 5);
		gbc_langLbl.anchor = GridBagConstraints.EAST;
		gbc_langLbl.gridx = 0;
		gbc_langLbl.gridy = 0;
		panel_1.add(langLbl, gbc_langLbl);
		
		// Valid Languages
		String[] langName = internal.get("regLang").split(";");
		for(int i = 0; i < langName.length; i++)
			langName[i] = langName[i].toUpperCase();
		
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<String>(langName));
		comboBox.setSelectedItem(setting.get("lang").toUpperCase());
		pendSet.put("lang", false);
		// Looks for change
		comboBox.addActionListener(e -> {
			// Determine whether the Apply button should be enabled
			if(((String) comboBox.getSelectedItem()).equals(setting.get("lang").toUpperCase())) {
				pendSet.put("lang", false);
				for(boolean b : pendSet.values())
					if(b)
						return;
				applyBtn.setEnabled(false);
			}else{
				pendSet.put("lang", true);
				applyBtn.setEnabled(true);
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel_1.add(comboBox, gbc_comboBox);
		
		JLabel connectLbl = new JLabel(Main.s.getLocal().get("set.connect") + ": ");
		pendSet.put("connect", false);
		GridBagConstraints gbc_connectLbl = new GridBagConstraints();
		gbc_connectLbl.insets = new Insets(0, 0, 0, 5);
		gbc_connectLbl.gridx = 0;
		gbc_connectLbl.gridy = 1;
		panel_1.add(connectLbl, gbc_connectLbl);
		
		JSlider slider = new JSlider();
		slider.setMajorTickSpacing(1);
		
		// Setup Table
		Hashtable<Integer, JLabel> lblTab = new Hashtable<Integer, JLabel>();
		lblTab.put(0, new JLabel(Main.s.getLocal().get("set.connect.on")));
		lblTab.put(1, new JLabel(Main.s.getLocal().get("set.connect.off")));
		
		slider.setLabelTable(lblTab);
		slider.setValue(Integer.parseInt(setting.get("connect")));
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMinimum(0);
		slider.setMaximum(1);
		slider.addChangeListener(e -> {
			if(slider.getValue() == Integer.parseInt(setting.get("connect"))) {
				pendSet.put("connect", false);
				for(boolean b : pendSet.values())
					if(b)
						return;
				applyBtn.setEnabled(false);
			}else{
				pendSet.put("connect", true);
				applyBtn.setEnabled(true);
			}
		});
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 1;
		panel_1.add(slider, gbc_slider);
		
		applyBtn.addActionListener(e -> {
			// Update Language
			setting.replace("lang", ((String) comboBox.getSelectedItem()).toLowerCase());
			// Update Connection Status
			setting.replace("connect", "" + slider.getValue());
			// Update file
			updateSetting();
			// Reload everything
			new Thread(new Main()).start();
			// Kill old stuff
			main.setEnabled(false);
			main.dispose();
		});
		
		// Return the generated Panel
		return set;
	}
}
