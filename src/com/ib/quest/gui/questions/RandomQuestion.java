package com.ib.quest.gui.questions;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ib.quest.Loader;
import com.ib.quest.Main;

/**
 * This is a JPanel for a random question
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class RandomQuestion extends JPanel{

	private static final long serialVersionUID = 7521714777296438433L;

	// Tracks Slide count
	private int curSlide = 1;
	// Tracks whether submitted or not
	private boolean submit = false;
	
	public RandomQuestion(Loader ld, int amt, JFrame main, JPanel pre) {
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
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new GridLayout(0, 1, 0, 10));
		
		JLabel titleLbl = new JLabel(String.format(Main.s.getLocal().get("ran.intro"), amt));
		titleLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_2.add(titleLbl);
		
		JLabel curQLbl = new JLabel(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
		curQLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_2.add(curQLbl);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		CardLayout c = new CardLayout(0, 0);
		panel.setLayout(c);
		for(BasicQuestion bQ : selQ)
			panel.add(bQ.getID(), bQ);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 60, 0));
		
		JButton quitBtn = new JButton(Main.s.getLocal().get("gen.quit"));
		quitBtn.addActionListener(e -> {
			main.getContentPane().remove(this);
			main.getContentPane().add(pre, BorderLayout.CENTER);
			main.repaint();
			main.revalidate();
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
				if(nextBtn.getText().equals(Main.s.getLocal().get("gen.submit")) || nextBtn.getText().equals(Main.s.getLocal().get("gen.finish")))
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
					if(!submit)
						nextBtn.setText(Main.s.getLocal().get("gen.submit"));
					else
						nextBtn.setText(Main.s.getLocal().get("gen.finish"));
				}
			// Reveal Answer and Move back to slide 1
			}else if(nextBtn.getText().equals(Main.s.getLocal().get("gen.submit"))) {
				submit = true;
				// Generate Answer Pages
				for(BasicQuestion bQ : selQ)
					bQ.checkAns();
				c.first(panel);
				curSlide = 1;
				// Prevent Btn from being named next even tho there is only 1 question
				if(amt != 1)
					nextBtn.setText(Main.s.getLocal().get("gen.next"));
				else 
					nextBtn.setText(Main.s.getLocal().get("gen.finish"));
				curQLbl.setText(String.format(Main.s.getLocal().get("ran.count"), curSlide, amt));
				bacBtn.setEnabled(false);
				main.repaint();
				main.revalidate();
			}else{
				// Calculate Marks
				int total = 0,
					mks = 0;
				for(BasicQuestion bQ : selQ) {
					mks += bQ.getEarn();
					total += bQ.getTotal();
				}
				Main.h.addEntry(LocalDateTime.now(), "Q" + amt + ".RND", mks, total);
				quitBtn.doClick();
			}
		});
		
		// If More than 1 part of question, horizontal box will exist so add to that
		if(amt != 1)
			horizontalBox.add(nextBtn);
		// Else, add to panel
		else
			panel_1.add(nextBtn);
	}
}
