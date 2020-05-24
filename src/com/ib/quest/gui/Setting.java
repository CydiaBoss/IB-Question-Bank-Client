package com.ib.quest.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
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

/**
 * Host all the necessary code for the settings
 * 
 * @author Andrew Wang
 * @version 1.0.4.8
 */
public class Setting extends JPanel {

	private static final long serialVersionUID = -5859669926512205632L;

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
	private File defConfig,
				 config;
	
	/**
	 * Init. the setting manager
	 * @wbp.parser.entryPoint
	 */
	public Setting() {
		// Verify if custom defConfig exist
		defConfig = new File(ClassLoader.getSystemClassLoader().getResource("file/config.ibqb").getFile());
		config = new File("config.ibqb");
		// IF not exist, copy new one from storage
		if(!config.exists())
			try {
				Files.copy(defConfig.toPath(), config.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {}
		// Read File
		parseFile();
		// Language Import
		readLang();
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
	 * Reads the correct Language File
	 */
	private void readLang() {
		// Scanner
		Scanner rd = null;
		try {
			rd = new Scanner(new File(ClassLoader.getSystemClassLoader().getResource("file/" + setting.get("lang") + ".lang").getFile()), "UTF-8");
		} catch (FileNotFoundException e) {}
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
	
	/**
	 * Creates The JPanel that hosts the setting panel
	 * @param main
	 * the mainframe
	 * @param pre
	 * the previous panel
	 * @wbp.parser.entryPoint
	 */
	public void init(JFrame main, JPanel pre) {
		// Setup
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 120, 0));
		
		JButton canBtn = new JButton("Cancel");
		canBtn.addActionListener(e -> {
			main.remove(this);
			main.add(pre, BorderLayout.CENTER);
			main.repaint();
			main.revalidate();
		});
		panel.add(canBtn);
		
		JButton applyBtn = new JButton("Apply");
		panel.add(applyBtn);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel langLbl = new JLabel("Language:");
		GridBagConstraints gbc_langLbl = new GridBagConstraints();
		gbc_langLbl.insets = new Insets(0, 0, 0, 5);
		gbc_langLbl.anchor = GridBagConstraints.EAST;
		gbc_langLbl.gridx = 0;
		gbc_langLbl.gridy = 0;
		panel_1.add(langLbl, gbc_langLbl);
		
		// Gets all lang files
		File[] langs = new File(ClassLoader.getSystemClassLoader().getResource("file").getFile())
			.listFiles((FilenameFilter) (dir, name) -> {
				return name.toLowerCase().endsWith(".lang");
			});
		
		// Valid Languages
		String[] langName = new String[langs.length];
		
		// Build Names (Remove .lang & Upper)
		for(int i = 0; i < langName.length; i++)
			langName[i] = langs[i].getName().replace(".lang", "").toUpperCase();
		
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<String>(langName));
		comboBox.setSelectedItem(setting.get("lang").toUpperCase());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel_1.add(comboBox, gbc_comboBox);
		
		applyBtn.addActionListener(e -> {
			// Update Language
			setting.replace("lang", ((String) comboBox.getSelectedItem()).toLowerCase());
			// Update file
			updateSetting();
			// Reload everything
			// TODO Actually Restart
			Main.sel.restart();
		});
	}
}
