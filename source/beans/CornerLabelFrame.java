// ===========================================================================
// CornerLabelFrame.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;

/**
 *	<code>CornerLabelFrame</code>
 *
 *	@version 1.58f
 */

public class CornerLabelFrame extends Frame implements WindowListener {
	
	private CornerLabel[] labels;
	
	public CornerLabelFrame(String title, int labelCount) {
		super(title);
		
		if (labelCount < 1) {
			labelCount = 1;
		}
		CornerLabel[] labels = new CornerLabel[labelCount];
		
		for (int i = 0; i < labelCount; i++) {
			labels[i] = new CornerLabel();
		}
		this.setLabels(labels);
		
		super.pack();
		super.setLocation(200, 200);
	}
	
	public CornerLabelFrame(String title, CornerLabel[] labels) {
		super(title);
		this.setLabels(labels);
		
		super.pack();
		super.setLocation(200, 200);
	}
	
	public int getLabelCount() {
		return this.labels.length;
	}
	
	protected void update() {
		super.removeAll();
		
		if (labels != null) {
			int labelCount = labels.length;
			super.setLayout(new java.awt.GridLayout(labelCount, 1));
			for (int i = 0; i < labelCount; i++) {
				super.add(labels[i]);
			}
		}
	}
	
	public void setLabels(CornerLabel[] labels) {
		if (labels != null) {
			new CornerLabelGroup(labels);
		}
		
		this.labels = labels;
		update();
	}
	
	public void changeLabel(int index, String mainText, String cornerText) {
		if (labels != null) {
			labels[index].setLabel(mainText);
			labels[index].setCornerLabel(cornerText);
			update();
		} else {
			getToolkit().beep();
		}
	}
	
	public void setBorderColor(Color color) {
		if (labels != null) {
			int labelCount = labels.length;
			for (int i = 0; i < labelCount; i++) {
				labels[i].setBorderColor(color);
			}
		}
	}
	
	public void setBorderSize(int newSize) {
		if (labels != null) {
			int labelCount = labels.length;
			for (int i = 0; i < labelCount; i++) {
				labels[i].setBorderSize(newSize);
			}
		}
	}
	
	public void toggleCorner(int index) {
		if (labels != null) {
			labels[index].setUpperLeftCorner(!labels[index].isUpperLeftCorner());
			update();
		} else {
			getToolkit().beep();
		}
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		super.dispose();
	}
	
}
