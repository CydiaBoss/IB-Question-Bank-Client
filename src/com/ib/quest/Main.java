package com.ib.quest;

//import java.util.Scanner;
//
//import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
//import com.ib.quest.Loader.QType;
//import com.ib.quest.Loader.Question;
import com.ib.quest.gui.Selector;

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
	private static Loader ld;
	
//	/**
//	 * Scanner
//	 */
//	private static final Scanner in = new Scanner(System.in);
	
	/**
	 * Main 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ld = new Loader();	
		new Selector(ld);
//		// Intro
//		System.out.println("IB Questions Database\n"
//						 + "\n"
//						 + "Subjects:\n");
//		// Counter
//		int i = 0;
//		for(HtmlAnchor sub : ld.getDBs()) {
//			// Skip Unavailable stuff
//			if(sub.asText().contains("NOT YET AVAILABLE"))
//				continue;
//			// Print
//			i++;
//			System.out.println(i + ") " + sub.asText());
//		}
//		// Subject Selection
//		int subj = in.nextInt();
//		if(subj <= 0 || subj > i) {
//			System.err.println("Error: Not a valid selection");
//			System.exit(-1);
//		}
//		ld.parseDatabase(subj - 1);
//		// Counter
//		System.out.println("Topics: \n");
//		int j = 0;
//		for(HtmlAnchor sub : ld.getTopics()) {
//			// Print
//			j++;
//			System.out.println(j + ") " + sub.asText());
//		}
//		// Subject Selection
//		int q = in.nextInt();
//		if(q <= 0 || q > j) {
//			System.err.println("Error: Not a valid selection");
//			System.exit(-1);
//		}
//		ld.loadQuest(q - 1);
//		// Counter
//		int k = 0;
//		for(String sub : ld.getQues().keySet()) {
//			// Print
//			k++;
//			System.out.println(k + ") " + sub);
//		}
//		//Q Selection
//		int qt = in.nextInt();
//		if(qt <= 0 || qt > k) {
//			System.err.println("Error: Not a valid selection");
//			System.exit(-1);
//		}
//		ld.loadQParts(ld.getQues().keySet().toArray(new String[ld.getQues().keySet().size()])[qt - 1]);
//		for(Question sub : ld.getQParts()) {
//			// Print
//			if(sub.getType().equals(QType.SPEC))
//				System.out.println(sub.getText());
//			else if (sub.getType().equals(QType.QUEST))
//				System.out.println(sub.getLabel() + ") " + sub.getText() + " [" + sub.getMark() + "]");
//			else
//				System.out.println("[ANS] " + sub.getLabel() + ") " + sub.getText());
//		}
	}

}
