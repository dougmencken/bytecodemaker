// ===========================================================================
//	TextViewSelectableLine.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.StringUtilities;

/**
 *	<code>TextViewSelectableLine</code>
 *
 *	@version 1.22f3
 */

public class TextViewSelectableLine extends TextView implements ItemSelectable {
	
	private int selectedLineIndex;
	private boolean[] highlightedLines = null;
	private transient ItemListener itemListener;
	
	public TextViewSelectableLine() {
		this(2, Color.black, Color.white, 0);
	}
	
	public TextViewSelectableLine(int leading) {
		this(leading, Color.black, Color.white, 0);
	}
	
	public TextViewSelectableLine(Color fg, Color bg) {
		this(2, fg, bg, 0);
	}
	
	public TextViewSelectableLine(int leading, Color fg, Color bg) {
		this(leading, fg, bg, 0);
	}
	
	public TextViewSelectableLine(int leading, Color fg, Color bg, int selIndex) {
		super(leading, fg, bg);
		this.selectedLineIndex = selIndex;
	}
	
	/**
	 *	Returns the selected items or 'null' if no items are selected.
	 *	String[2]:	[0] - string at selected line index,
	 *				[1] - number in NumbersView at selected line index (if present).
	 */
	public Object[] getSelectedObjects() {
		if ((selectedLineIndex >= 0) && (selectedLineIndex < end)) {
			String[] ret = new String[2];
			
			ret[0] = "";
			try {
				ret[0] = StringUtilities.getLineNumber(super.buffer, selectedLineIndex);
			} catch (IndexOutOfBoundsException exc) {}
			
			ret[1] = (numbersView != null) ?
				String.valueOf(numbersView.getValueByIndex(selectedLineIndex)) :
				null;
			
			return ret;
		} else {
			return null;
		}
	}
	
	public int getSelectedIndex() {
		return this.selectedLineIndex;
	}
	
	public void select(int index) {
		if ((index < 0) || (index >= StringUtilities.countLines(super.buffer))) {
			throw new IllegalArgumentException("bad index");
		}
		
		this.selectedLineIndex = index;
		this.repaint();
	}
	
	public void highlightLine(int index) {
		if (highlightedLines == null) {
			highlightedLines = new boolean[StringUtilities.countLines(super.buffer)];
		}
		
		if (index < this.highlightedLines.length) {
			highlightedLines[index] = true;
			this.repaint();
		}
	}
	
	public void dehighlightLine(int index) {
		if ((highlightedLines != null) && (index < highlightedLines.length)) {
			highlightedLines[index] = false;
			this.repaint();
		}
	}
	
	public void clearHighlighting() {
		this.highlightedLines = new boolean[StringUtilities.countLines(super.buffer)];
		this.repaint();
	}
	
	public void addItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		this.itemListener = AWTEventMulticaster.add(itemListener, listener);
	}
	
	public void removeItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		this.itemListener = AWTEventMulticaster.remove(itemListener, listener);
	}
	
	/**
	 *	Paints this TextViewSelectableLine.
	 */
	public void paint(Graphics g) {
		if (super.buffer.length() == 0) return;
		
		g.setFont(getFont());
		g.setColor(getBackground());
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(getForeground());
		
		int fontSize = getFont().getSize();
		int pos = fontSize + leading;
		
		int stringCount = StringUtilities.countLines(super.buffer);
		if (end > stringCount) {
			start -= (end - stringCount);
			end = stringCount;
		}
		if (start < 0) start = 0;
		//this.current = this.start + 1;
		
		boolean paintSelected = false;
		for (int i = start; i < end; i++) {
			try {
				if (paintSelected) {
					g.setColor(getForeground());
					paintSelected = false;
				}
				
				if (i == selectedLineIndex) {
					paintSelected = true;
					g.fillRect(0, pos - fontSize, getSize().width, fontSize + leading);
					g.setColor(getBackground());
				} else if ((highlightedLines != null) && (highlightedLines[i] == true)) {
					g.setColor(getBackground().darker());
					g.fillRect(0, pos - fontSize, getSize().width, fontSize + leading);
					g.setColor(getForeground());
				}
				
				g.drawString(
					(StringUtilities.getLineNumber(super.buffer, i)).substring(xpos),
					4, pos
				);
			} catch (StringIndexOutOfBoundsException ignored) {}
			
			pos += fontSize + leading;
		}
		
		if (focus || useBorder) {
			g.setColor(Color.black);
			g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (!focus) {
			super.requestFocus();
		} else {
			double index = (1.0d / (double)(getFont().getSize() + leading)) * (e.getY() - 1);
			selectedLineIndex = start + (int)index;
			int stringCount = StringUtilities.countLines(super.buffer);
			
			if (selectedLineIndex < 0) {
				// select the first string
				selectedLineIndex = 0;
			} else if (selectedLineIndex >= stringCount) {
				// select the last string
				selectedLineIndex = stringCount - 1;
			}
			
			this.repaint();
			fireItemStateChanged();
		}
	}
	
	public void processKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (e.isShiftDown()) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (selectedLineIndex > 0) {
							selectedLineIndex--;
							fitSelectedLine();
						}
						
						this.repaint();
						fireItemStateChanged();
						break;
					
					case KeyEvent.VK_DOWN:
						int stringCount = StringUtilities.countLines(super.buffer);
						
						if (selectedLineIndex < (stringCount-1)) {
							selectedLineIndex++;
							fitSelectedLine();
						}
						
						this.repaint();
						fireItemStateChanged();
						break;
				}
				
				return;
			}
		}
		
		super.processKeyEvent(e);
	}
	
	private void fitSelectedLine() {
		int top = start - selectedLineIndex;
		if (top > 0) {
			start -= top;
			end -= top;
		}
		
		int bottom = selectedLineIndex - end + 1;
		if (bottom > 0) {
			start += bottom;
			end += bottom;
		}
	}
	
	private void fireItemStateChanged() {
		if (itemListener != null) {
			itemListener.itemStateChanged(
				new ItemEvent(
					this,
					ItemEvent.ITEM_STATE_CHANGED,
					StringUtilities.getLineNumber(super.buffer, selectedLineIndex),
					selectedLineIndex
				)
			);
		}
	}
	
}