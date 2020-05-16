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

/**
 * Selector Window
 * 
 * @author andre
 */
public class Selector {

	private JFrame main;
	private Loader ld;

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
		main = new JFrame("IB Question Bank Client");
		main.setIconImage(Toolkit.getDefaultToolkit().getImage(Error.class.getResource("/img/IBRR.png")));
		main.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		main.setResizable(false);
		// TODO Will Replace
		main.setBounds(100, 100, 300, 40 + 40 * ld.getDBs().size());
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
		
		JPanel panel = new JPanel();
		main.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 20));
		
		// Button Addition
		for(HtmlAnchor a : ld.getDBs()) {
			JButton bt = new JButton(a.asText().trim());
			bt.addActionListener(e -> {/* TODO Rebuild Window */});
			panel.add(bt);
		}
	}

}
