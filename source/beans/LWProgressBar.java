// ===========================================================================
// LWProgressBar.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>LWProgressBar</code>
 *
 *	@version 1.3
 */

public class LWProgressBar extends Component {
	
	protected int minimumValue;
	protected int maximumValue;
	protected int currentValue;
	
	public LWProgressBar(int minValue, int maxValue) {
		this.minimumValue = minValue;
		this.currentValue = minValue;
		this.maximumValue = maxValue;
		
		super.setForeground(new Color(0x444444));
		super.setBackground(new Color(0xCCCCFF));
		
		super.setSize(this.getMinimumSize());
	}
	
	public LWProgressBar() {
		this(0, 100);
	}
	
	public void useDefaultColors() {
		super.setForeground(new Color(0x444444));
		super.setBackground(new Color(0xCCCCFF));
	}
	
	public void paint(Graphics g) {
		Dimension r = super.getSize();
		
		g.setColor(Color.black);
		g.drawRect(0, 0, r.width-1, r.height-1);
		
		g.setColor(getBackground());
		g.fillRect(1, 1, r.width-2, r.height-2);
		
		int completeWidth = (int)(r.width * getPercentComplete());
		g.setColor(getForeground());
		g.fillRect(1, 1, completeWidth, r.height-2);
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(186, 12);
	}
	
	public Dimension getPreferredSize() {
		return super.getSize();
	}
	
	public double getPercentComplete() {
		double percent =
			(double)(this.currentValue - this.minimumValue) /
			(double)(this.maximumValue - this.minimumValue);
		
		return percent;
    }
    
	public void setValue(int newValue) {
		if ((newValue < this.minimumValue) || (newValue > this.maximumValue)) {
			throw new IllegalArgumentException("out of range");
		}
		
		this.currentValue = newValue;
		super.repaint();
	}
	
	public void setValue(int valueCount, int value) {
		this.setValue(this.minimumValue, this.maximumValue, valueCount, value);
	}
	
	public void setValue(int barStart, int barEnd, int valueCount, int value) {
		if ((barStart < this.minimumValue) || (barEnd > this.maximumValue)) {
			throw new IllegalArgumentException("out of range");
		}
		
		float koef = (float)(barEnd - barStart) / (float)valueCount;
		this.setValue((int)(barStart + koef*value));
	}
	
	public void setMinimum(int newMinValue) {
		this.minimumValue = newMinValue;
		super.repaint();
	}
	
	public void setMaximum(int newMaxValue) {
		this.maximumValue = newMaxValue;
		super.repaint();
	}
	
	public int getMinimum() { return this.minimumValue; }
	public int getMaximum() { return this.maximumValue; }
	public int getValue() { return this.currentValue; }
	
}
