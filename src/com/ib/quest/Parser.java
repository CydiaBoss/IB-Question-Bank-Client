package com.ib.quest;

/**
 * Parses strings within the html file to get the desired effect
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Parser {

	/**
	 * Parses a string and formats it so it becomes recognizable in HTML
	 * 
	 * TODO Fix this
	 * 
	 * @param txt
	 * The string
	 * 
	 * @return
	 * The new string
	 */
	public static final String parseFormat(String txt) {
		// Check if even need
		if((txt.contains("\\(") && txt.contains("\\)") || (txt.contains("\\[") && txt.contains("\\]")))) {
			// Convert array to <ul>
			txt = txt.replace("\\[\\begin{array}{*{20}{l}}", "<ul>");
			txt = txt.replace("\\end{array}\\]", "</ul>");
			// Remove \char from brackets
			txt = txt.replace("\\(", "(").replace("\\)", ")").replace("\\,", "");
			// Remove all text brackets
			txt = txt.replaceAll("\\{\\\\text\\{(?<in>[^}]+?)\\}\\}", "${in}");
			// Fix Negatives / Positives
			txt = txt.replaceAll("\\{\\s(?<sign>[\\+|-])\\s(?<val>\\d+?)?\\}", "${sign}${val}");
			txt = txt.replaceAll("\\{(?<val>\\d+?)\\s(?<sign>[\\+|-])\\s\\}", "${val}${sign}");
			// Super & Sub
			txt = txt.replaceAll("\\{(?<base>[^(\\^|_|\\{)|\\}]+?)\\^(?<sup>[^\\}]+?)\\}", "${base}<sup>${sup}</sup>");
			txt = txt.replaceAll("\\{(?<base>[^(\\^|_)|\\{|\\}]+?)\\_(?<sub>[^\\}]+?)\\}", "${base}<sub>${sub}</sub>");
			txt = txt.replaceAll("_(?<sub>[^\\^]+?)\\^(?<sup>(-|\\+)?[0-9]+?(-|\\+)?)", "<sub>${sub}</sub><sup>${sup}</sup>");
			// Finish List
			//Cleanup
			txt = txt.replaceAll("\\{(?<internal>[^\\}]+?)\\}", "<li>${internal}</li>");
			txt = txt.replace("\\\\", "");
			txt = txt.replace("\\to", "->");
		}
		// New String
		return txt;
	}
}
