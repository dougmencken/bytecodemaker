/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.tools;

import java.awt.*;
import java.util.*;

import douglas.mencken.util.StringUtilities;
import douglas.mencken.beans.LWLabel;

/**
 *	<code>LabelPanel</code>
 *
 *	@version 1.03f1
 */

public class LabelPanel extends Panel {
	
	private String[] labels;
	
	public LabelPanel(String bigString, Font font) {
		this(bigString, font, 560);
	}
	
	public LabelPanel(String bigString, Font font, int maxWidth) {
		super.setFont(font);
		super.setBackground(Color.white);
		
		// if (bigString == null) bigString = "";
		
		FontMetrics fm = getFontMetrics(font);
		Dimension size = new Dimension(
			maxWidth + 4,
			fm.getMaxAscent() + fm.getMaxDescent() + 2
		);
		
		int rows = 1;
		int stringWidth = fm.stringWidth(bigString);
		
		if (stringWidth > maxWidth) {
			StringTokenizer tokenizer = new StringTokenizer(bigString, " \n\r");
			StringBuffer buf1 = new StringBuffer();
			Vector vector = new Vector();
			
			while (tokenizer.hasMoreTokens()) {
				buf1.append(tokenizer.nextToken());
				String bs = buf1.toString();
				
				if (fm.stringWidth(bs) > maxWidth) {
					vector.addElement(StringUtilities.getBeforeLast(bs, ' '));
					buf1.setLength(0);
					buf1.append(StringUtilities.getAfter(bs, ' '));
					buf1.append(' ');
					rows++;
				} else {
					buf1.append(' ');
				}
			}
			vector.addElement(buf1.toString());
			
			labels = new String[rows];
			int maxStringWidth = 0;
			for (int i = 0; i < rows; i++) {
				labels[i] = (String)(vector.elementAt(i));
				
				int currentWidth = fm.stringWidth(labels[i]);
				if (currentWidth > maxStringWidth) {
					maxStringWidth = currentWidth;
				}
			}
			
			size.height = size.height*rows + 4;
			size.width = maxStringWidth + 4;
		} else {
			labels = new String[1];
			labels[0] = bigString;
			
			size.width = stringWidth + 4;
		}
		
		super.setLayout(new GridLayout(rows, 1, 10, 1));
		for (int i = 0; i < rows; i++) {
			super.add(new LWLabel(labels[i], font, LWLabel.LEFT));
		}
		
		super.setSize(size);
	}
	
	public String[] getLabels() {
		return this.labels;
	}
	
	public Dimension getPreferredSize() {
		return super.getSize();
	}
	
}