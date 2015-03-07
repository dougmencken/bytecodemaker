// ===========================================================================
// LWLabel.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>LWLabel</code>
 *
 *	@version 1.5
 */

public class LWLabel extends Component {
	
	public static final int CENTER = 0;
	public static final int LEFT = 1;
	
	protected String text;
	protected int alignment;
	
	public LWLabel(String text, Font font, int alignment) {
		this.text = text;
		this.setAlignment(alignment);
		super.setFont(font);
		super.setSize(getPreferredSize());
	}
	
	public LWLabel(String text, Font font) {
		this(text, font, CENTER);
	}
	
	public LWLabel(String text, int alignment) {
		this(text, new Font("Monaco", Font.PLAIN, 9), alignment);
	}
	
	public LWLabel(Font font, int alignment) {
		this("", font, alignment);
	}
	
	public LWLabel(String text) {
		this(text, new Font("Monaco", Font.PLAIN, 9), CENTER);
	}
	
	public LWLabel(Font font) {
		this("", font, CENTER);
	}
	
	public LWLabel(int alignment) {
		this("", new Font("Monaco", Font.PLAIN, 9), alignment);
	}
	
	public LWLabel() {
		this("", new Font("Monaco", Font.PLAIN, 9), CENTER);
	}
	
	public int getAlignment() {
		return this.alignment;
	}
	
	public void setAlignment(int alignment) {
		if ((alignment != CENTER) && (alignment != LEFT)) {
			throw new IllegalArgumentException("unknown alignment (" + alignment + ")");
		}
		
		this.alignment = alignment;
		super.repaint();
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String s) {
		this.text = s;
		super.repaint();
	}
	
	public void setEnabled(boolean v) {
		if (v != super.isEnabled()) {
			super.setEnabled(v);
			super.repaint();
		}
	}
	
	public void paint(Graphics g) {
		Font f = super.getFont();
		FontMetrics fm = super.getFontMetrics(f);
		g.setFont(f);
		
		Color foreground = super.getForeground();
		Color background = super.getBackground();
		if (!super.isEnabled()) {
			foreground = background.darker().darker();
		}
		
		g.setColor(background);
		Dimension d = super.getSize();
		g.fillRect(0, 0, d.width, d.height);
		
		g.setColor(foreground);
		
		int xpos = 5;
		if (this.alignment == CENTER) {
			xpos = (d.width - fm.stringWidth(this.text))/2;
		}
		
		g.drawString(this.text, xpos, fm.getMaxAscent());
	}
	
	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}
	
	public Dimension getPreferredSize() {
		FontMetrics fm = super.getFontMetrics(super.getFont());
		return new Dimension(
			fm.stringWidth(this.text) + 10,
			fm.getMaxAscent() + fm.getMaxDescent()
		);
	}
	
	/**
	 *	Returns the parameter string representing the state of this label.
	 */
	protected String paramString() {
		return super.paramString() + ",text=" + this.text;
	}
    
}
