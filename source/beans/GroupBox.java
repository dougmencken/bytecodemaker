// ===========================================================================
//	GroupBox.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;

/**
 *	The <code>GroupBox</code> is a Container and may be used
 *	to visually group a set of Components inside a bordered
 *	and titled region.
 *	<i>Note: supports 'null' layouts.</i>
 *
 *	@version 1.02f
 */

public class GroupBox extends Container {
	
	private String captionText = "";
	private Font captionFont = new Font("Geneva", Font.BOLD, 9);
	
	public GroupBox() {
		this(new FlowLayout(FlowLayout.CENTER));
	}
	
	public GroupBox(LayoutManager layout) {
		super();
		super.setLayout(layout);
		
		super.setForeground(Color.black);
		super.setBackground(Color.white);
	}
	
	public void doLayout() {
		if (super.getLayout() == null) {
			int stringWidth = super.getFontMetrics(captionFont).stringWidth(captionText);
			int max_x = stringWidth + 30;
			int max_y = 1;
			
			int nmembers = super.getComponentCount();
			for (int i = 0; i < nmembers; i++) {
				Component m = super.getComponent(i);
				
				Dimension d = m.getPreferredSize();
				m.setSize(d.width, d.height);
				
				Rectangle mbounds = m.getBounds();
				max_x = Math.max(max_x, (mbounds.x + mbounds.width));
				max_y = Math.max(max_y, (mbounds.y + mbounds.height));
			}
			
			Insets insets = this.getInsets();
			super.setSize(max_x + insets.right, max_y + insets.bottom);
		} else {
			super.doLayout();
		}
	}
	
	public Insets getInsets() {
    	return new Insets(
    		super.getFontMetrics(this.captionFont).getMaxAscent() + 3,
    		4, 4, 4
    	);
    }
    
	public String getCaptionText() {
		return this.captionText;
	}
	
	public void setCaptionText(String captionText) {
		this.captionText = (captionText == null) ? "" : captionText;
	}
	
	public Font getCaptionFont() {
		return this.captionFont;
	}
	
	public void setCaptionFont(Font captionFont) {
		if (captionFont == null) {
			throw new IllegalArgumentException(
				"douglas.mencken.beans.GroupBox.setCaptionFont(Font): captionFont cannot be null"
			);
		}
		this.captionFont = captionFont;
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		FontMetrics fm = super.getFontMetrics(this.captionFont);
		int linePos_top = fm.getMaxAscent()/2 + 3;
		int captionWidth = fm.stringWidth(this.captionText) + 10;
		
		g.setColor(Color.gray);
		int w = super.getSize().width;
		int h = super.getSize().height;
		
		// top
		g.drawLine(0, linePos_top, 10, linePos_top);
		g.drawLine(10 + captionWidth, linePos_top, w-1, linePos_top);
		
		g.drawLine(0, h-1, w-1, h-1);					// bottom
		g.drawLine(0, linePos_top, 0, h-1);				// left
		g.drawLine(w-1, linePos_top, w-1, h-1);			// right
		
		g.setColor(super.getForeground());
		g.setFont(this.captionFont);
		g.drawString(this.captionText, 15, fm.getMaxAscent());
	}
	
}