package com.ib.quest.gui.template;

import javax.swing.JPanel;

import com.ib.quest.Main;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * A custom {@link JPanel} that will host back and next buttons
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Option extends JPanel {

	private static final long serialVersionUID = -869615564542936814L;

	/**
	 * Create the panel.
	 * 
	 * @param m
	 * The Main Frame
	 * @param curr
	 * The Current Panel
	 * @param pre
	 * The Previous Panel
	 * @param nxt
	 * The Action of the Next Button
	 */
	public Option(JFrame m, JPanel curr, JPanel pre, ActionListener nxt) {
		// Set Layout
		setLayout(new BorderLayout(0, 10));
		// Add Current Container
		add(curr, BorderLayout.CENTER);
		// Add Button Container
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		// First Button (Back)
		JButton backBtn = new JButton(Main.s.getLocal().get("gen.back"));
		backBtn.addActionListener(e -> {
			m.getContentPane().remove(this);
			m.getContentPane().add(pre, BorderLayout.CENTER);
			m.repaint();
			m.revalidate();
		});
		panel.add(backBtn, BorderLayout.WEST);
		// Second Button (Next)
		JButton nextBtn = new JButton(Main.s.getLocal().get("gen.next"));
		nextBtn.addActionListener(nxt);
		panel.add(nextBtn, BorderLayout.EAST);
		
	}

}
