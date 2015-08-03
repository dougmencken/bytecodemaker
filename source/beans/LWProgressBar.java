/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;

/**
 *	<code>LWProgressBar</code>
 *
 *	@version 1.4
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
		if (newValue < this.minimumValue) {
			newValue = this.minimumValue;
		} else if (newValue > this.maximumValue) {
			newValue = this.maximumValue;
		}
		
		this.currentValue = newValue;
		super.repaint();
	}
	
	public void changeValue(int valueCount, int value) {
		this.changeValue(this.minimumValue, this.maximumValue, valueCount, value);
	}
	
	public void changeValue(int barStart, int barEnd, int valueCount, int value) {
		if (barStart < this.minimumValue)
			barStart = this.minimumValue;
		if (barEnd > this.maximumValue)
			barEnd = this.maximumValue;
		
		float koef = (float)(barEnd - barStart) / (float)valueCount;
		this.setValue((int)(barStart + koef*value));
	}

	public void resetValue() {
		this.currentValue = this.minimumValue;
		super.repaint();
	}

	public void advanceValue() {
		if (this.currentValue < this.maximumValue) {
			this.currentValue++;
		}
	}

	public void advanceOnePercent() {
		if (this.currentValue >= this.maximumValue) return;
		double newPercent = this.getPercentComplete() + 0.01d;
		this.setValue((int)Math.round(newPercent*((double)(this.maximumValue - this.minimumValue)) + this.minimumValue));
	}
	
	public void setMinimum(int newMinValue) {
		if (newMinValue >= this.maximumValue)
			newMinValue = this.maximumValue - 1;
		this.minimumValue = newMinValue;
		if (this.currentValue < newMinValue)
			this.currentValue = newMinValue;
		super.repaint();
	}
	
	public void setMaximum(int newMaxValue) {
		if (newMaxValue <= this.minimumValue)
			newMaxValue = this.minimumValue + 1;
		this.maximumValue = newMaxValue;
		if (this.currentValue > newMaxValue)
			this.currentValue = newMaxValue;
		super.repaint();
	}
	
	public int getMinimum() { return this.minimumValue; }
	public int getMaximum() { return this.maximumValue; }
	public int getValue() { return this.currentValue; }
	
}
