package com.ib.quest;

import java.util.Scanner;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

/**
 * The Main Class
 * 
 * @author andre
 * @version 1.0.4
 */
public class Main {

	/**
	 * The Loader
	 */
	private static final Loader ld = new Loader();
	
	/**
	 * Scanner
	 */
	private static final Scanner in = new Scanner(System.in);
	
	/**
	 * Main 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Intro
		System.out.println("IB Questions Database\n"
						 + "\n"
						 + "Subjects:\n");
		// Counter
		int i = 0;
		for(HtmlAnchor sub : ld.getDBs()) {
			// Skip Unavailable stuff
			if(sub.asText().contains("NOT YET AVAILABLE"))
				continue;
			// Print
			i++;
			System.out.println(i + ") " + sub.asText());
		}
		// Subject Selection
		int subj = in.nextInt();
		if(subj <= 0 || subj > i) {
			System.err.println("Error: Not a valid selection");
			System.exit(-1);
		}
		ld.parseDatabase(subj - 1);
		// Counter
		System.out.println("Topics: \n");
		int j = 0;
		for(HtmlAnchor sub : ld.getTopics()) {
			// Print
			j++;
			System.out.println(j + ") " + sub.asText());
		}
		// Subject Selection
		int q = in.nextInt();
		if(q <= 0 || q > j) {
			System.err.println("Error: Not a valid selection");
			System.exit(-1);
		}
		ld.loadQuest(q - 1);
		int k = 0;
		for(String sub : ld.getQues().keySet()) {
			// Print
			k++;
			System.out.println(k + ") " + sub);
		}
	}

}
