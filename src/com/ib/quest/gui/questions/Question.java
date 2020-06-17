package com.ib.quest.gui.questions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.ib.quest.Constants;
import com.ib.quest.Main;

/**
 * Question Object
 * 
 * @author andre
 */
public class Question{
	
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
	 * Texts
	 */
	private HtmlParagraph[] p; 
	
	/**
	 * Images
	 */
	private ArrayList<HtmlImage> imgs;
	
	/**
	 * Labels
	 */
	private String label;
	
	/**
	 * Mark
	 */
	private int mark;
	
	/**
	 * Question Type
	 */
	private QType qType;
	
	/**
	 * Main Constructor
	 */
	private Question(String ID, String label, String mark, QType qType, HtmlParagraph... p) {
		this.p = p;
		this.label = label.trim();
		this.mark = Integer.parseInt(mark.trim().substring(1, mark.length() - 1));
		this.qType = qType;
		// Assets
		assetGen(ID);
	}
	
	/**
	 * Repairs, Generates and Downloads all assets for the question
	 */
	private void assetGen(String ID) {
		// Create Array
		imgs = new ArrayList<>();
		// Looks through Paragraphs
		for(HtmlParagraph pg : p) {
			// Retrieves all images
			List<HtmlImage> img = pg.getByXPath("img");
			int imgC = 0;
			for(HtmlImage i : img) {
				// Get SRC
				String src = i.getSrcAttribute();
				// Img Type
				String fileType = (i.getAttribute("data-type").equals(HtmlImage.ATTRIBUTE_NOT_DEFINED))? "png" : i.getAttribute("data-type");
				// Checks for Base64 Image Data
				if(src.contains("data") && src.contains("image") && src.contains("base64"))
					try {
						// Decodes Data
						byte[] data = Base64.getDecoder().decode(src.split(",")[1]);
						BufferedImage datImg = ImageIO.read(new ByteArrayInputStream(data));
						// Create File in Temp Folder
						fileType = src.split("[\\/|;]")[1];
						File datImgFile = new File(Main.TEMP.getPath() + "/IMG-" + ID + "-" + imgC + "." + fileType);
						ImageIO.write(datImg, fileType, datImgFile);
						datImgFile.deleteOnExit();
						imgC++;
						// Update <img>
						i.setAttribute("src", datImgFile.toURI().toURL().toString());
					} catch (IOException e) {}
				// Changes any relative paths to absolute paths
				else if(src.contains("../")) {
					// Fixes Link
					i.setAttribute("src", src.replaceAll("(\\.\\.\\/)+", "https://"));
					// Adds File type
					String[] chunks = src.split("\\.");
					fileType = chunks[chunks.length - 1];
				}
				// Update ALT to error message
				i.setAttribute("alt", Main.s.getLocal().get("error.img"));
				// Change Size if too large
				try {
					if(i.getWidth() > Constants.Size.STAN_W - 80) {
						double ratio = i.getHeight() / i.getWidth();
						i.setAttribute("width", "" + (Constants.Size.STAN_W - 80));
						i.setAttribute("height", "" + (int) ((Constants.Size.STAN_W - 80) * ratio));
					}
				} catch (IOException e) {}
				// Add file type
				i.setAttribute("data-type", fileType);
				// Add to Image Bank
				imgs.add(i);
			}
		}
	}
	
	/**
	 * Creates a Question Object
	 * 
	 * @param ID
	 * Question ID
	 * @param p
	 * The Text
	 * @param label
	 * The Label
	 * @param mark
	 * The Mark
	 */
	public Question(String ID, String label, String mark, HtmlParagraph... p) {
		this(ID, label, mark, QType.QUEST, p);
	}
	
	/**
	 * Creates a Question Object
	 * 
	 * @param ID
	 * Question ID
	 * @param p
	 * The Text
	 * @param label
	 * The Label
	 */
	public Question(String ID, String label, HtmlParagraph... p) {
		this(ID, label, "[-1]", QType.ANS, p);
	}
	
	/**
	 * Creates a Question Object
	 * 
	 * @param ID
	 * Question ID
	 * @param p
	 * The Text
	 */
	public Question(String ID, HtmlParagraph... p) {
		this(ID, "", "[-1]", QType.SPEC, p);
	}

	/**
	 * Get Text as XML
	 * 
	 * @return
	 * The Text
	 */
	public String getText() {
		// Combines all Paragraphs
		String txt = "";
		for(HtmlParagraph pg : p)
			txt += pg.asXml()
				// TODO Fix this
				.replaceAll("<\\/?span([^>]+)?>", "")
				.replaceAll("<\\/?script([^>]+)?>", "")
				.replaceAll("\\/\\/\\<\\!\\[CDATA\\[\n(?<lat>[^\n]+)\n\\/\\/]]>", "\\\\[${lat}\\\\]")
				.replaceAll("\\s{2,}", " ")
				.trim();
		return txt;
	}
	
	/**
	 * Get Raw Text
	 * 
	 * @return
	 * Raw Text
	 */
	public String getRaw() {
		// Combines all Paragraphs
		String txt = "";
		for(HtmlParagraph pg : p)
			txt += pg.asText().trim();
		return txt;
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
	
	/**
	 * Gets all {@link HtmlImage}
	 * 
	 * @return
	 * The {@link HtmlImage}s
	 */
	public ArrayList<HtmlImage> getImgs() {
		return imgs;
	}
}