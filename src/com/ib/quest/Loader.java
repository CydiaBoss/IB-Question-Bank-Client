package com.ib.quest;

import java.io.IOException;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;

/**
 * The Loader will Load the Questions off the website
 * 
 * @author andre
 * @version 1.0.4
 */
public class Loader {

	// Webclient
	private WebClient c;
	private HtmlPage pg;
	
	// Web Database
	private String ibDB = "https://www.ibdocuments.com/IB%20QUESTIONBANKS/4.%20Fourth%20Edition/";
	
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
		c.getOptions().setUseInsecureSSL(true);
		c.getOptions().setCssEnabled(false);
		c.getOptions().setAppletEnabled(true);
		c.getOptions().setJavaScriptEnabled(false);
		c.getOptions().setActiveXNative(true);
		c.getOptions().setPrintContentOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnFailingStatusCode(false);
		c.getOptions().setThrowExceptionOnScriptError(false);
		// Get Page
		try {
			pg = c.getPage(ibDB);
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.err.println("Error: Failed to connect to website: " + ibDB);
		}
		c.waitForBackgroundJavaScript(500);
		// Loads the Links
		loadLinks();
	}
	
	//- Links -//
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
	 * Load all links from Main
	 */
	private void loadLinks() {
		// Retrieves the Link Location
		HtmlDivision div = pg.getElementById("services")
						     .getFirstByXPath("//div[@class='row services']");
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
	 * Entering a Database
	 * 
	 * @param index
	 * The database in the ArrayList
	 */
	public void parseDatabase(int index) {
		// Clears the List to host the new Database
		subj.clear();
		// Loads the Page
		HtmlPage dbPage = null;
		try {
			dbPage = links.get(index).click();
		} catch (IOException e) {
			System.err.println("Error: Database cannot be found");
			System.exit(-2);
		}
		HtmlTableBody body = dbPage.getFirstByXPath("//table[@class='table']/tbody");
		for(HtmlElement item : body.getHtmlElementDescendants()) {
			// Ignores titles
			if(item.getAttribute("style").equals(HtmlElement.ATTRIBUTE_NOT_DEFINED))
				continue;
			// Stores the Topics
			subj.add(item.getFirstByXPath("a"));
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
	 * 
	 * @param index
	 */
	public void loadQuest(int index) {
		// Clear List
		questions.clear();
		// Start Assembling
		HtmlPage quests = null;
		try {
			quests = subj.get(index).click();
		} catch (IOException e) {
			System.err.println("Error: Subject cannot be found");
			System.exit(-3);
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
			System.out.println(curQuestion);
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
	private class Question{
		
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
		
		private Question(String text, String label, int mark, QType qType) {
			this.text = text;
			this.label = label;
			this.mark = mark;
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
		private Question(String text, String label, int mark) {
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
			this(text, label, -1, QType.ANS);
		}
		
		/**
		 * Creates a Question Object
		 * 
		 * @param text
		 * The Text
		 */
		public Question(String text) {
			this(text, "-1", -1, QType.SPEC);
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
	 * Organize Parts of the Question
	 */
	public void loadQParts() {
		
	}
	
//	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
//		Loader ld = new Loader();
//		ld.parseDatabase(1);
//		ld.loadQuest(5);
//		
//	}
	
}
