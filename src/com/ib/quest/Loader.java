package com.ib.quest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;

import com.ib.quest.gui.Error;

/**
 * The Loader will Load the Questions off the website or the offline database
 * 
 * @author Andrew Wang
 * @version 1.0.4.5
 */
public class Loader {

	// Webclient
	private WebClient c;
	private HtmlPage pg;
	
	// Web Database
	private String ibDB = "https://www.ibdocuments.com/IB%20QUESTIONBANKS/4.%20Fourth%20Edition";
	
	// Offline
	private URL ibDBOff = Loader.class.getResource("/test/main.html");
	
	/**
	 * Creates the Loader Object
	 * @throws MalformedURLException 
	 */
	public Loader(){
		// Remove Error Msg
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		// Create Objs
		c = new WebClient();
		// Settings
		c.getOptions().setUseInsecureSSL(false);
		c.getOptions().setCssEnabled(false);
		c.getOptions().setAppletEnabled(false);
		c.getOptions().setJavaScriptEnabled(false);
		c.getOptions().setPrintContentOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnScriptError(false);
		// Get Online Page
		try {
			pg = c.getPage(ibDB);
		// Switch to Offline
		}catch (FailingHttpStatusCodeException | IOException e) {
			Error.throwError("Connection to Website Failed. Check Internet Connection. Switching to Offline.", false);
			offline();
		}
		// Loads the Links
		loadLinks();
	}
	
	/**
	 * Switch to Offline
	 */
	public void offline() {
		// Try for Offline
		try {
			pg = c.getPage(ibDBOff);
		// Nothing works
		} catch (FailingHttpStatusCodeException | IOException e1) {
			Error.throwError("Offline Database cannot be found.", true);
		}
	}
	
	//- Links to the Subject Topics-//
	private final ArrayList<HtmlAnchor> links = new ArrayList<>();
	
	/**
	 * Gives the Links from the Front Page
	 * 
	 * @return
	 * The Links to the Databases
	 */
	public ArrayList<HtmlAnchor> getDBs() {
		return links;
	}
	
	/**
	 * Load all links
	 */
	private void loadLinks() {
		// Clears the Link List
		links.clear();
		// The Link Location
		HtmlDivision div = pg.getFirstByXPath("//div[@class='row services']");
		// Error 403 Forbidden (DMCA Takedown)
		// Auto switch to offline
		if(div == null) {
			Error.throwError("A DMCA Takedown order has been issued. The Databases are down. Switching to Offline.", false);
			offline();
			div = pg.getFirstByXPath("//div[@class='row services']");
		}
		// Copy the Links down
		for(HtmlElement a : div.getHtmlElementDescendants()) {
			if(!(a instanceof HtmlAnchor))
				continue;
			// Adds the links to a array
			links.add((HtmlAnchor) a);
		}
	}
	
	//- Current Database -//
	private final ArrayList<HtmlAnchor> subj = new ArrayList<>();
	
	/**
	 * Gets the Topics for the current database
	 * 
	 * @return
	 * The Topics
	 */
	public ArrayList<HtmlAnchor> getTopics() {
		return subj;
	}
	
	/**
	 * Entering a Database<br>
	 * Use loadLinks() before this.
	 * 
	 * @param a
	 * The desired subject database 
	 */
	public void parseDatabase(HtmlAnchor a) {
		// Clears the List to host the new Database
		subj.clear();
		// Loads the Page
		HtmlPage dbPage = null;
		// Verify Anchor
		if(!links.contains(a))
			Error.throwError("Internal Error Detected", true);
		// Proceed
		try {
			dbPage = a.click();
		} catch (IOException e) {
			Error.throwError("Invalid Links. Please check to make sure you have the latest software.", true);
		}
		// Grabs all the topics
		HtmlTableBody body = dbPage.getFirstByXPath("//table[@class='table']/tbody");
		for(HtmlElement item : body.getHtmlElementDescendants()) {
			// Ignores titles
			if(item.getAttribute("style").equals(HtmlElement.ATTRIBUTE_NOT_DEFINED))
				continue;
			// Stores the Topics
			// TODO Check if still compatible with online
			subj.add((HtmlAnchor) item);
		}
	}
	
	//- Questions -//
	private final HashMap<String, HtmlAnchor> questions = new HashMap<>();
	
	/**
	 * Returns Questions from the selected Topic
	 * 
	 * @return
	 * The Questions
	 */
	public HashMap<String, HtmlAnchor> getQues() {
		return questions;
	}
	
	/**
	 * Loads all the questions for the given topic<br>
	 * Use parseDatabase() before this.
	 * 
	 * @param a
	 * The desired questions for a given topic
	 */
	public void loadQuest(HtmlAnchor a) {
		// Clear List
		questions.clear();
		// Start Assembling
		HtmlPage quests = null;
		// Verify Anchor
		if(!subj.contains(a))
			Error.throwError("Internal Error Detected", true);
		// Continue
		try {
			quests = a.click();
		} catch (IOException e) {
			Error.throwError("Invalid Links. Please check to make sure you have the latest software.", true);
		}
		// Detection
		List<HtmlElement> rawQues = quests.getByXPath("//div[@class='module' and h3='Directly related questions']/ul/li");
		// Question Filter
		String lastQuestion = "";
		for(HtmlElement item : rawQues) {
			// Records latest Question ID
			String curQuestion = item.asText().split(":")[0];
			// Upper the Level ID
			curQuestion = curQuestion.replaceFirst("hl", "HL");
			curQuestion = curQuestion.replaceFirst("sl", "SL");
			// Removing the 6th chunk
			int curC = 1;
			String newQuest = "";
			for(String ch : curQuestion.split("\\.")) {
				newQuest += ch;
				curC++;
				if(curC >= 6)
					break;
				newQuest += '.';
			}
			// Removal of all Lower
			curQuestion = newQuest;
			curQuestion = curQuestion.replaceAll("[a-z]", "");
			// Compare (Move on if Repeated)
			if(lastQuestion.equals(curQuestion))
				continue;
			// Update LastQuestion
			lastQuestion = curQuestion;
			// Stores the Topics
			questions.put(curQuestion, item.getFirstByXPath("a"));
		}
	}
	
	//- Question Parts -//
	
	/**
	 * Question Part Types
	 * 
	 * @author andre
	 */
	public static enum QType {
		/**
		 * Answers
		 */
		ANS,
		/**
		 * Questions
		 */
		QUEST,
		/**
		 * Details
		 */
		SPEC;
	}
	
	/**
	 * Question Object
	 * 
	 * @author andre
	 */
	public class Question{
		
		/**
		 * Texts
		 */
		private String text, 
		/**
		 * Labels
		 */
					   label;
		
		/**
		 * Mark
		 */
		private int mark;
		
		/**
		 * Question Type
		 */
		private QType qType;
		
		private Question(String text, String label, String mark, QType qType) {
			this.text = text.trim();
			this.label = label.trim();
			this.mark = Integer.parseInt(mark.trim().substring(1, mark.length() - 1));
			this.qType = qType;
		}
		
		/**
		 * Creates a Question Object
		 * 
		 * @param text
		 * The Text
		 * @param label
		 * The Label
		 * @param mark
		 * The Mark
		 */
		private Question(String text, String label, String mark) {
			this(text, label, mark, QType.QUEST);
		}
		
		/**
		 * Creates a Question Object
		 * 
		 * @param text
		 * The Text
		 * @param label
		 * The Label
		 */
		public Question(String text, String label) {
			this(text, label, "[-1]", QType.ANS);
		}
		
		/**
		 * Creates a Question Object
		 * 
		 * @param text
		 * The Text
		 */
		public Question(String text) {
			this(text, "", "[-1]", QType.SPEC);
		}

		/**
		 * Get Text
		 * 
		 * @return
		 * The Text
		 */
		public String getText() {
			return text;
		}

		/**
		 * Get Label
		 * 
		 * @return
		 * The Label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Get Mark
		 * 
		 * @return
		 * The Mark
		 */
		public int getMark() {
			return mark;
		}

		/**
		 * Get Type
		 * 
		 * @return
		 * The Type
		 */
		public QType getType() {
			return qType;
		}
	}
	
	/**
	 * The Questions Parts
	 */
	private ArrayList<Question> qParts = new ArrayList<>();
	
	/**
	 * Get the parts of the Question
	 * 
	 * @return
	 * Question Parts
	 */
	public ArrayList<Question> getQParts() {
		return qParts;
	}
	
	/**
	 * Organize Parts of the Question<br>
	 * Use loadQuest() before this.
	 * 
	 * @param ID
	 * The Question Identifier
	 */
	public void loadQParts(String ID) {
		// Clear List
		qParts.clear();
		// Get Page
		HtmlPage questPg = null;
		try {
			questPg = questions.get(ID).click();
		} catch (IOException e) {
			Error.throwError("Invalid Links. Please check to make sure you have the latest software.", true);
		}
		// Verify
		if(questPg == null)
			Error.throwError("Internal Error Detected", true);
		// Iterate through descendants to find data
		boolean isQuest = false,
				isAns = false;
		List<HtmlElement> tempList = questPg.getByXPath("//div[@class='page-content container']/div|"
				  								      + "//div[@class='page-content container']/h2");
		for(HtmlElement e : tempList) {
			// Identifies whether the element is part of whatever
			if(e instanceof HtmlHeading2)
				if(e.asText().contains("Question")) {
					isQuest = true;
					continue;
				}else if(e.asText().contains("Markscheme")) {
					isQuest = false;
					isAns = true;
					continue;
				}else{
					isAns = false;
					break;
				}
			// Records Questions
			if(isQuest)
				// Details
				if(e.getAttribute("class").equals("specification")) {
					List<HtmlParagraph> tList = e.getByXPath("p");
					for(HtmlParagraph p : tList)
						qParts.add(new Question(p.asText().trim()));
				// Actual Question
				}else{
					List<HtmlParagraph> tList = e.getByXPath("p");
					String q = "";
					for(HtmlParagraph p : tList)
						q += p.asText().trim();
					qParts.add(new Question(q, 
							((HtmlDivision) e.getFirstByXPath("div[@class='question_part_label']")).asText().trim(), 
							((HtmlDivision) e.getFirstByXPath("div[@class='marks']")).asText().trim()));
				}
			// Records Answers
			else if(isAns) {
				List<HtmlParagraph> tList = e.getByXPath("p");
				String a = "";
				for(HtmlParagraph p : tList)
					a += p.asText().trim();
				qParts.add(new Question(a, ((HtmlDivision) e.getFirstByXPath("div[@class='question_part_label']")).asText().trim()));
			}
		}
	}
}
