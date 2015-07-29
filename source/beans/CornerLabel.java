/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>CornerLabel</code>
 *
 *	@version 1.3
 */

public class CornerLabel extends Component {
	
	protected boolean showCorner;
	protected boolean upperLeftCorner = false;
	protected int borderSize;
	protected int borderType;
	
	private transient Image offscreenImage;
	private transient Graphics offscreenGraphics;
	
	protected String cornerLabel = "";
	protected String mainLabel = "";
	private Color borderColor;
	
	// ------------------------------------------------------------------
	
	public CornerLabel() {
		this(true, Color.black, BorderBox.BORDER_ALL, 1);
	}
	
	public CornerLabel(Color color) {
		this(true, color, BorderBox.BORDER_ALL, 1);
	}
	
	public CornerLabel(boolean showCorner, Color color) {
		this(showCorner, color, BorderBox.BORDER_ALL, 1);
	}
	
	public CornerLabel(boolean showCorner, int borderType) {
		this(showCorner, Color.black, borderType, 1);
	}
	
	public CornerLabel(boolean showCorner, Color color, int borderType) {
		this(showCorner, color, borderType, 1);
	}
	
	public CornerLabel(boolean showCorner, int borderType, int borderSize) {
		this(showCorner, Color.black, borderType, borderSize);
	}
	
	public CornerLabel(boolean showCorner, Color color, int borderType, int borderSize) {
		setFont(new Font("Monaco", Font.PLAIN, 9));
		
		setForeground(color);
		setBackground(Color.white);
		
		this.showCorner = showCorner;
		this.borderType = borderType;
		this.borderSize = borderSize;
	}
	
	/**
	 * Returns true because this component is painted to an offscreen image
	 * ("buffer") that's copied to the screen later.
	 */
	public boolean isDoubleBuffered() {
		return true;
	}
	
	public void update(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = createImage(getSize().width, getSize().height);
			offscreenGraphics = offscreenImage.getGraphics();
		}
		
		this.paint(offscreenGraphics);
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	public void paint(Graphics g) {
		int all_w = getSize().width;
		int all_h = getSize().height;
		
		// borders
		int noBorder_w = all_w;
		int noBorder_h = all_h;
		g.setColor(this.borderColor);
		
		if ((borderType & BorderBox.BORDER_LEFT) != 0) {
			noBorder_h -= borderSize;
			for (int i = 0; i < borderSize; i++) {
				g.drawLine(i, 0, i, all_h-1);
			}
		}
		if ((borderType & BorderBox.BORDER_RIGHT) != 0) {
			noBorder_h -= borderSize;
			for (int i = 1; i <= borderSize; i++) {
				g.drawLine(all_w-i, 0, all_w-i, all_h-1);
			}
		}
		if ((borderType & BorderBox.BORDER_TOP) != 0) {
			noBorder_w -= borderSize;
			for (int i = 0; i < borderSize; i++) {
				g.drawLine(0, i, all_w-1, i);
			}
		}
		if ((borderType & BorderBox.BORDER_BOTTOM) != 0) {
			noBorder_w -= borderSize;
			for (int i = 1; i <= borderSize; i++) {
				g.drawLine(0, all_h-i, all_w-1, all_h-i);
			}
		}
		
		Font f = super.getFont();
		FontMetrics fm = super.getFontMetrics(f);
		g.setFont(f);
		
		if (showCorner) {
			if (upperLeftCorner) {
				for (int i = 0; i < borderSize; i++) {
					g.drawLine(i, all_h, all_h + i, 0);
				}
			} else {
				for (int i = 0; i < borderSize; i++) {
					g.drawLine(all_w - i, 0, all_w - all_h - i, all_h);
				}
			}
		}
		
		g.setColor(super.getForeground());
		
		// main label
		this.paintMainLabel(g);
		
		// corner label
		int cornerLabel_x = all_w - all_h/2 + 2;
		int cornerLabel_y = all_h - 4;
		if (upperLeftCorner) {
			cornerLabel_x = all_h/2 - fm.stringWidth(cornerLabel);
			cornerLabel_y = fm.getMaxAscent() + 2;
		}
		
		g.drawString(cornerLabel, cornerLabel_x, cornerLabel_y);
	}
	
	protected void paintMainLabel(Graphics g) {
		int all_w = getSize().width;
		int all_h = getSize().height;
		FontMetrics fm = super.getFontMetrics(super.getFont());
		
		int maxWidth = all_w - all_h - 10;
		String label = this.mainLabel;
		boolean fullLabel = true;
		int len = label.length() - 1;
		
		while (fm.stringWidth(label) >= maxWidth) {
			if (len > 2) {
				label = label.substring(0, --len);
			} else {
				break;
			}
			
			fullLabel = false;
		}
		if (!fullLabel) {
			label = label.substring(0, len) + '\u2026';
		}
		
		int mainLabel_x = 5;
		if (upperLeftCorner) {
			mainLabel_x += all_h;
		}
		g.drawString(label, mainLabel_x, (all_h+fm.getMaxAscent())/2 - 1);
	}
	
	public Dimension getMinimumSize() {
		FontMetrics fm = super.getFontMetrics(super.getFont());
		return new Dimension(
			fm.stringWidth(mainLabel) + 25,
			fm.getMaxAscent() + 5
		);
	}
	
	public Dimension getPreferredSize() {
		FontMetrics fm = super.getFontMetrics(super.getFont());
		int preferredHeight = fm.getMaxAscent()*2 + 4;
		int preferredWidth =
			(mainLabel == null) ?
				100 : fm.stringWidth(mainLabel) + preferredHeight + 20;
		
		return new Dimension(preferredWidth, preferredHeight);
	}
	
	public void setUpperLeftCorner(boolean upperLeft) {
		this.upperLeftCorner = upperLeft;
		repaint();
	}
	
	public boolean isUpperLeftCorner() { return upperLeftCorner; }
	
	public void setBorderColor(Color color) {
		this.borderColor = color;
		repaint();
	}
	
	public Color getBorderColor() { return this.borderColor; }
	
	public void setBorderSize(int newSize) {
		this.borderSize = newSize;
		repaint();
	}
	
	public int getBorderSize() { return this.borderSize; }
	
	public void setCornerLabel(String label) {
		int len = label.length();
		if (len > 2) label = label.substring(0, len-1);
		
		this.cornerLabel = label;
		repaint();
	}
	
	public String getCornerLabel() { return cornerLabel; }
	
	public void setLabel(String label) {
		this.mainLabel = label;
		repaint();
	}
	
	public String getLabel() { return mainLabel; }
	
	public void setBorderType(int newType) {
		this.borderType = newType;
		repaint();
	}
	
	public int getBorderType() { return borderType; }
	
}
