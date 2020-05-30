package com.ib.quest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.output.FileWriterWithEncoding;

import com.ib.quest.Constants.OffData;
import com.ib.quest.Loader.QType;
import com.ib.quest.Loader.Question;

/**
 * Handles Offline stuff
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class OfflineBuilder {

	/**
	 * The directory that will hold the offline server.
	 */
	private File dir;
	
	/**
	 * Initializes the Offline Database
	 */
	public OfflineBuilder() {
		// Starts the Directory
		dir = new File(OffData.DIR);
		// Creates if needed
		createDir(dir);
		// TODO Make something here
	}
	
	/**
	 * Adds a question to the offline database
	 * 
	 * @param subj
	 * The question's subject
	 * @param topic
	 * The question's topic
	 * @param questID
	 * The question's ID
	 * @param qP
	 * The question parts
	 * 
	 * @return
	 * Whether the question was added to the database successfully
	 */
	public boolean addQuest(String subj, String topic, String questID, ArrayList<Question> qP) {
		// Create all necessary Files
		File main = new File(dir.getPath() + "/main.html" ),
			 subjDir = new File(dir.getPath() + "/" + subj.toLowerCase()),
			 subject = new File(subjDir.getPath() + "/" + subj.toLowerCase() + ".html"),
			 top = new File(subjDir.getPath() + "/" + topic.toLowerCase() + ".html"),
			 quest = new File(subjDir.getPath() + "/" + questID + ".html");
		// Verify if question already exist
		if(quest.exists())
			return false;
		// Injects subject links
		injFile(main, "subj", "<a href='./" + subjDir.getName() + "/" + subject.getName() + "'>\n"
								+ subj + "\n"
							+ "</a>");
		// Creates Subject Directory
		createDir(subjDir);
		// Injects topics things
		injFile(subject, "topic", "<a style='s' href='./" + top.getName() + "'>\n"
									+ topic + "\n"
								+ "</a>");
		// Inject questions again
		// TODO Determine whether this is required
		injFile(top, "list", "<li>\n"
							   + questID + ":\n"
						       + "<a href='./" + quest.getName() + "'>\n"
							       + qP.get(0).getText() + "\n"
						       + "</a>\n"
					       + "</li>");
		// Generate text to inject into the question file
		String txt = "<h2>Question</h2>",
			   ans = "\n<h2>Markscheme</h2>";
		// Parse the questions parts
		for(Question q : qP)
			if(q.getType().equals(QType.SPEC))
				txt += "\n<div class='specification'>\n" + 
						   "<p>" + q.getText() + "</p>\n" + 
					   "</div>";
			else if(q.getType().equals(QType.QUEST))
				txt += "\n<div class='question'>\n" + 
						   "<p>" + q.getText() + "</p>\n" + 
					       "<div class='question_part_label'>\n" + 
					       	   q.getLabel() + "\n" + 
					       "</div>\n" + 
					       "<div class='marks'>\n" + 
					       	   "[" + q.getMark() + "]\n" + 
					       "</div>\n" + 
					   "</div>";
			else
				ans += "\n<div class='question'>\n" + 
						   "<p>" + q.getText() + "</p>\n" + 
					       "<div class='question_part_label'>\n" + 
					       	   q.getLabel() + "\n" + 
					       "</div>\n" + 
					   "</div>";
		// Inject the question itself
		injFile(quest, "question", txt + ans);
		// Finished
		return true;
	}
	
	/**
	 * Removes a question from the offline database
	 * 
	 * @param subj
	 * The Question's Subject
	 * @param topic
	 * The question's topic
	 * @param questID
	 * The question's ID
	 * @param qP
	 * The question's parts
	 * 
	 * @return
	 * Whether the removal was successful
	 */
	public boolean removeQuest(String subj, String topic, String questID, ArrayList<Question> qP) {
		// Create all necessary Files
		File main = new File(dir.getPath() + "/main.html" ),
			 subjDir = new File(dir.getPath() + "/" + subj.toLowerCase()),
			 subject = new File(subjDir.getPath() + "/" + subj.toLowerCase() + ".html"),
			 top = new File(subjDir.getPath() + "/" + topic.toLowerCase() + ".html"),
			 quest = new File(subjDir.getPath() + "/" + questID + ".html");
		// Remove Question File
		if(!quest.delete())
			return false;
		// Remove entry from topic 
		extFile(top, "<li>\n"
					   + questID + ":\n"
				       + "<a href='./" + quest.getName() + "'>\n"
					       + qP.get(0).getText() + "\n"
				       + "</a>\n"
			       + "</li>");
		// Stop if file is empty
		if(!isFileEmpty(top, "list"))
			return true;
		else
			top.delete();
		// Check the subj file to remove 
		extFile(subject, "<a style='s' href='./" + top.getName() + "'>\n"
						   + topic + "\n"
					   + "</a>");
		// Stop if file is empty
		if(!isFileEmpty(subject, "topic"))
			return true;
		else {
			subject.delete();
			subjDir.delete();
		}
		// Check the main file
		extFile(main, "<a href='./" + subjDir.getName() + "/" + subject.getName() + "'>\n"
						+ subj + "\n"
					+ "</a>");
		// Stop if file is empty
		if(isFileEmpty(main, "subj"))
			main.delete();
		return true;
	}
	
	/**
	 * Determines whether the file is empty
	 * 
	 * @param f
	 * The file
	 * @param template
	 * The template to compare to
	 * 
	 * @return
	 * is the file empty?
	 */
	private boolean isFileEmpty(File f, String template) {
		// The Scanners
		Scanner scF = null, 
				scT = null;
		try {
			// Init. The Scanners
			scF= new Scanner(f, "UTF-8");
			scT = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream("file/template/" + template + ".html"), "UTF-8");
			// Scans and Compares Files
			while(scT.hasNextLine())
				if(!scF.nextLine().equals(scT.nextLine())) {
					scF.close();
					scT.close();
					return false;
				}
			// If nothing appears wrong, return true
			scF.close();
			scT.close();
			return true;
		} catch (FileNotFoundException e) {}
		finally {
			if(scF != null)
				scF.close();
			if(scT != null)
				scT.close();
		}
		// If failure occurs, return false
		return false;
	}
	
	/**
	 * Extracts a line of txt from a file
	 * 
	 * @param f
	 * The File
	 * @param extract
	 * The line to extract from the file
	 */
	private void extFile(File f, String extract) {
		// New Scanner Enabled
		Scanner sc = null;
		try {
			sc = new Scanner(f);
			// The data
			String data = "";
			// Gets all of the file's contents
			while(sc.hasNextLine()) 
				data += sc.nextLine() + "\n";
			// Deletes the entry
			data = data.replace(extract + "\n", "");
			// Update file
			BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(f, "UTF-8", false));
			bw.write(data);
			bw.flush();
			bw.close();
		} catch (IOException e) {}
		finally {
			if(sc != null)
				sc.close();
		}
	}
	
	/**
	 * Creates a file from a template if file doesn't exist
	 * Injects lines of text
	 * 
	 * @param f
	 * The file
	 * @param template
	 * The template
	 * @param inject
	 * Text to inject 
	 */
	private void injFile(File f, String template, String inject) {
		// The Scanner Object
		Scanner rd = null;
		try {
			// Existence check
			if(!f.exists() || f.isDirectory()) {
				// Create
				f.createNewFile();
				// Use template
				rd = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream("file/template/" + template + ".html"), "UTF-8");
				BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(f, "UTF-8", false));
				// Copy & Paste
				while(rd.hasNextLine()) {
					String ln = rd.nextLine();
					bw.write(ln + "\n");
					// Inject when inject tag appears
					if(ln.trim().equals("<!--i-->"))
						bw.write(inject + "\n");
				}
				// Flush
				bw.flush();
				// Close the Writer
				bw.close();
			}else{
				rd = new Scanner(f);
				String data = "";
				while(rd.hasNextLine()) {
					// Read Txt
					String ln = rd.nextLine();
					data += ln + "\n";
					// Inject when inject tag appears
					if(ln.trim().equals("<!--i-->"))
						data += inject + "\n";
				}
				// Check if there is only one appearance in the file
				// Ignores first appearance 
				if(data.indexOf(inject, data.indexOf(inject) + inject.length()) != -1){
					rd.close();
					return;
				}
				// Rewrite
				BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(f, "UTF-8", false));
				bw.write(data);
				// Flush
				bw.flush();
				// Close
				bw.close();
			}
		} catch (IOException e) {}
		// Emergency close
		finally {
			if(rd != null) 
				rd.close();
		}
	}
	
	/**
	 * Directory Creation if does not exist
	 * 
	 * @param d
	 * The directory
	 */
	private void createDir(File d) {
		// Existence check
		if(!d.exists() || d.isFile())
			d.mkdir();
	}
	
	/**
	 * Checks if the question is already within the offline database
	 * 
	 * @param subj
	 * The Question's subject
	 * @param ID
	 * The Question's ID
	 * 
	 * @return
	 * Whether it already exist or not
	 */
	public boolean alrdyOff(String subj, String ID) {
		return new File(dir.getPath() + "/" + subj.toLowerCase() + "/" + ID + ".html").exists();
	}
}
