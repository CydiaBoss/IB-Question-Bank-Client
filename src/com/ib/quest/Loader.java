package com.ib.quest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.ib.quest.gui.questions.Question;

/**
 * The Loader will Load the Questions off the website or the offline database
 * 
 * @author Andrew Wang
 * @version 1.0.4.7
 */
public class Loader {

	// Webclient
	private WebClient c;
	private HtmlPage pg;
	
	// Offline
	private OfflineBuilder o = null;
	
	/**
	 * Creates the Loader Object
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
		c.getOptions().setJavaScriptEnabled(true);
		c.getOptions().setPrintContentOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnScriptError(false);
		// Get Online Page
		try {
			if(Main.s.getSetting().get("connect").equals("0")) {
				pg = c.getPage(Constants.Database.IBDBON);
				// Load OfflineBuilder
				o = new OfflineBuilder();
			}else
				offline();
		// Switch to Offline
		}catch (FailingHttpStatusCodeException | IOException e) {
			Main.throwError(Main.s.getLocal().get("error.in") + " " + Main.s.getLocal().get("offline"), false);
			offline();
		}
		// Loads the Links
		loadLinks();
	}
	
	/**
	 * Switch to Offline
	 */
	public void offline() {
		// Change Settings
		Main.s.changeSetting("connect", "1", null);
		// Try for Offline
		try {
			WebRequest req = new WebRequest(Constants.Database.IBDBOFF);
			req.setCharset(StandardCharsets.UTF_8);
			pg = c.getPage(req);
		// Nothing works
		} catch (FailingHttpStatusCodeException | IOException e1) {
			Main.throwError(Main.s.getLocal().get("error.off.missing"), true);
		}
	}
	
	/**
	 * Get Offline Handler
	 */
	public OfflineBuilder getOffBuild() {
		return o;
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
			// If div = null and setting is not offline, dmca error
			if(Main.s.getSetting().get("connect").equals("0")) {
				Main.throwError(Main.s.getLocal().get("error.dmca") + " " + Main.s.getLocal().get("offline"), false);
				offline();
				div = pg.getFirstByXPath("//div[@class='row services']");
			}
			// Offline Missing / Corruption if still div
			if(div == null) {
				Main.s.changeSetting("connect", "0", null);
				Main.throwError(Main.s.getLocal().get("error.off.missing") + " " + Main.s.getLocal().get("online"), true);
				return;
			}
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
			Main.throwError(Main.s.getLocal().get("error.in"), true);
		// Proceed
		try {
			dbPage = a.click();
		} catch (IOException e) {
			Main.throwError(Main.s.getLocal().get("error.link"), true);
		}
		// Grabs all the topics
		for(Object itemE : dbPage.getByXPath("//table[@class='table']/tbody/tr/td/a")) 
			// Stores the Topics
			subj.add((HtmlAnchor) itemE);
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
			Main.throwError(Main.s.getLocal().get("error.in"), true);
		// Continue
		try {
			quests = a.click();
		} catch (IOException e) {
			Main.throwError(Main.s.getLocal().get("error.link"), true);
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
			Main.throwError(Main.s.getLocal().get("error.link"), true);
		}
		// Verify
		if(questPg == null)
			Main.throwError(Main.s.getLocal().get("error.in"), true);
		// Iterate through descendants to find data
		boolean isQuest = false,
				isAns = false;
		List<HtmlElement> tempList = questPg.getByXPath("//div[@class='page-content container']/div|"
				  								      + "//div[@class='page-content container']/h2");
		// Start
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
					qParts.add(new Question(ID, tList.toArray(new HtmlParagraph[tList.size()])));
				// Actual Question
				}else{
					List<HtmlParagraph> tList = e.getByXPath("p");
					String lbl, mk;
					// Try to get Label
					try{
						lbl = ((HtmlDivision) e.getFirstByXPath("div[@class='question_part_label']")).asText().trim();
					}catch(NullPointerException e1) {
						lbl = Constants.Database.LBL;
					}
					// Try to get Marks
					try{
						mk = ((HtmlDivision) e.getFirstByXPath("div[@class='marks']")).asText().trim();
					}catch(NullPointerException e1) {
						mk = Constants.Database.MK;
					}
					// Add Question
					qParts.add(new Question(ID, lbl, mk, tList.toArray(new HtmlParagraph[tList.size()])));
				}
			// Records Answers
			else if(isAns) {
				List<HtmlParagraph> tList = e.getByXPath("p");
				String lbl;
				// Try to get Label
				try{
					lbl = ((HtmlDivision) e.getFirstByXPath("div[@class='question_part_label']")).asText().trim();
				}catch(NullPointerException e1) {
					lbl = Constants.Database.LBL;
				}
				qParts.add(new Question(ID, lbl, tList.toArray(new HtmlParagraph[tList.size()])));
			}
			// Add to Progress
		}
		// End
	}
}