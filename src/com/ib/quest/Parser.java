package com.ib.quest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.swing.JMathComponent;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

/**
 * Parses strings within the html file to get the desired effect
 * 
 * @author Andrew Wang
 * @version 1.0.4.9
 */
public class Parser {

	/**
	 * Starts the Snuggle Engine
	 */
	private static final SnuggleEngine SE = new SnuggleEngine();
	
	/**
	 * Custom Layout Context for Image Generation
	 */
	private static final class IBLayoutContext implements LayoutContext{
		
		/**
		 * The Default Instance
		 */
		public static final LayoutContext INSTANCE = new IBLayoutContext();
		
		/**
		 * The Parent
		 */
		public LayoutContext parent = LayoutContextImpl.getDefaultLayoutContext();
		
		/**
		 * Access Denied
		 */
		private IBLayoutContext() {}
		
		/**
		 * Override Settings Here
		 */
		@Override
		public Object getParameter(Parameter which) {
			// Override Font Size
			if(which.equals(Parameter.MATHSIZE))
				return Constants.Size.FMSIZE;
			return parent.getParameter(which);
		}
	}
	
	/**
	 * Parses a string and formats it so it becomes a {@link JMathComponent}
	 * 
	 * @param txt
	 * The Latex
	 * @param name
	 * Name of Image
	 * 
	 * @return
	 * The new image {@link File}
	 */
	private static final File mkMath(String txt, String name) {
		// Image File
		File img = null;
		// Creates New Session
		SnuggleSession sS = SE.createSession();
		// Creates Image
		try {
			// Creation
			sS.parseInput(new SnuggleInput(txt));
			// Make Image
			BufferedImage bi = Converter.getInstance().render(
					MathMLParserSupport.parseString(sS.buildXMLString()), 
					IBLayoutContext.INSTANCE);
			// Save Image
			ImageIO.write(bi, "png", img = new File(Main.TEMP.getPath() + "\\" + name + ".png"));
			// Temp
			img.deleteOnExit();
		} catch (IOException | SAXException | ParserConfigurationException e) {}
		// Give back
		return img;
	}
	
	/**
	 * Reads a line and converts it to a {@link JPanel} of {@link JLabel} and {@link JMathComponent}
	 * 
	 * @param txt
	 * The String in question
	 * @param ID
	 * Question ID
	 * 
	 * @return
	 * The JPanel
	 */
	public static final JScrollPane parseTxt(String txt, String ID) {
		// Size
		Dimension maxSize = new Dimension(Constants.Size.STAN_W - 80, Constants.Size.STAN_H / 2);
		Dimension minSize = new Dimension(Constants.Size.STAN_W - 80, Constants.Size.STAN_H / 6);
		// Container
		JEditorPane lbl = null;
		// If does not contain Latex, return
		if(!txt.contains("\\(") && !txt.contains("\\[") && !txt.contains("$$")) {
			// Text
			lbl = new JEditorPane("text/html", txt);
			lbl.setBackground(new Color(240, 240, 240));
			lbl.setEditable(false);
			// Scroll
			JScrollPane jSP = new JScrollPane(lbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jSP.setMaximumSize(maxSize);
			jSP.setMinimumSize(minSize);
			jSP.setPreferredSize(null);
			// Sets Font as System Font
			// TODO Test if this works
			Font font = UIManager.getFont("Label.font");
	        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
	                "font-size: " + font.getSize() + "pt; }";
	        ((HTMLDocument)lbl.getDocument()).getStyleSheet().addRule(bodyRule);
			// Return
			return jSP;
		}
		// Parse txt
		boolean isMath = false;
		String display = "";
		int imgC = 0;
		for(String chunk : (" " + txt).split("(\\\\\\(|\\\\\\[|\\$\\$|\\\\\\]|\\\\\\))")) {
			// Skip First Block if just space
			if(chunk.equals(" ")) {
				isMath = true;
				continue;
			}
			// Filter the Math stuff
			if(isMath) {
				try {
					File img = mkMath("\\[" + chunk + "\\]", "LAT-" + ID + "-" + imgC);
					display += "<img src='" + img.toURI().toURL() + "'></img>";
					imgC++;
				} catch (MalformedURLException e) {}
			}else
				display += chunk;
			// Switching
			isMath = !isMath;
		}
		// Text Display
		lbl = new JEditorPane("text/html", display);
		lbl.setBackground(new Color(240, 240, 240));
		lbl.setEditable(false);
		// Scroll
		JScrollPane jSP = new JScrollPane(lbl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jSP.setMaximumSize(maxSize);
		jSP.setMinimumSize(minSize);
		jSP.setPreferredSize(null);
		// Sets Font as System Font
		Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; }";
        ((HTMLDocument)lbl.getDocument()).getStyleSheet().addRule(bodyRule);
		// Size
		return jSP;
	}
}
