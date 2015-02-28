// ===========================================================================
//	MultiLineCornerLabel.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>MultiLineCornerLabel</code>
 *
 *	@version 0.2d0
 */

public class MultiLineCornerLabel extends CornerLabel {
	
	// override the superclass' field 'mainLabel'
	protected String[] mainLabel = null;
	
	public MultiLineCornerLabel() {
		this(true, Color.black, BorderBox.BORDER_ALL, 1);
	}
	
	public MultiLineCornerLabel(Color color) {
		this(true, color, BorderBox.BORDER_ALL, 1);
	}
	
	public MultiLineCornerLabel(boolean showCorner, Color color) {
		this(showCorner, color, BorderBox.BORDER_ALL, 1);
	}
	
	public MultiLineCornerLabel(boolean showCorner, int borderType) {
		this(showCorner, Color.black, borderType, 1);
	}
	
	public MultiLineCornerLabel(boolean showCorner, Color color, int borderType) {
		this(showCorner, color, borderType, 1);
	}
	
	public MultiLineCornerLabel(boolean showCorner, int borderType, int borderSize) {
		this(showCorner, Color.black, borderType, borderSize);
	}
	
	public MultiLineCornerLabel(boolean showCorner, Color color, int borderType, int borderSize) {
		super(showCorner, color, borderType, borderSize);
		
	}
	
	protected void paintMainLabel(Graphics g) {
		int all_w = getSize().width;
		int all_h = getSize().height;
		FontMetrics fm = super.getFontMetrics(super.getFont());
		
		int maxWidth = all_w - all_h - 10;
		String label = this.mainLabel[0];
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
	
	public void setLabel(String label) {
		this.mainLabel = new String[] { label };
		super.repaint();
	}
	
	public void setLabels(String[] labels) {
		this.mainLabel = labels;
		super.repaint();
	}
	
	public String[] getLabels() { return this.mainLabel; }
	
	public String getLabel() {
		throw new NoSuchMethodError();
	}
	
}