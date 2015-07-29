/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;

/**
 *	<code>LWTabbedPane</code> is the kind of Container which lets the
 *	user to switch between a group of components by clicking on a tab
 *	with a given title.
 *	<p>
 *	Also supports the 'common component'.
 *	
 *	@version 1.05f1
 */

public class LWTabbedPane extends Container implements ItemSelectable {
	
	private String[] titles;
	private TabTitlesComponent tab;
	private Component[] components;
	private Component commonComponent;
	private int selectedIndex;
	private int previousSelectedIndex;
	
	// ---------------------------------------------------------------------------
	
	public LWTabbedPane() {
		this(null);
	}
	
	public LWTabbedPane(Component common) {
		super.setLayout(new BorderLayout());
		
		this.titles = null;
		this.tab = new TabTitlesComponent(this);
		this.components = null;
		this.selectedIndex = -1;
		this.previousSelectedIndex = -1;
		
		this.setCommonComponent(common);
	}
	
	public void doLayout() {
		if (previousSelectedIndex != selectedIndex) {
			super.removeAll();
			this.previousSelectedIndex = this.selectedIndex;
			
			if (selectedIndex >= 0) {
				super.add("North", tab);
				
				BorderBox pp = new BorderBox(
					1,
					BorderBox.BORDER_BOTTOM | BorderBox.BORDER_LEFT | BorderBox.BORDER_RIGHT
				);
				pp.setShadow(true);
				
				Component selected = this.getSelectedComponent();
				if (selected != null) {
					pp.add(selected);
				}
				super.add("Center", pp);
				
				if (commonComponent != null) {
					super.add("South", commonComponent);
				}
			}
		}
		
		super.doLayout();
	}
	
// 	public void keyTyped(KeyEvent e) {}
// 	public void keyPressed(KeyEvent e) {}
// 	
// 	// Key combination [Alt(Option) + Control + Tab]
// 	// switches between the tabs
// 	public void keyReleased(KeyEvent evt) {
// 		if ((evt.getKeyCode() == KeyEvent.VK_TAB) &&
// 				(evt.isAltDown() && evt.isControlDown())) {
// 			if (components != null) {
// 				if ((selectedIndex + 1) < components.length) {
// 					setSelectedIndex(selectedIndex + 1);
// 				} else {
// 					setSelectedIndex(0);
// 				}
// 			}
// 		}
// 	}
	
	/**
	 *	Returns the selected items or null if no items are selected.
	 */
	public Object[] getSelectedObjects() {
		if ((this.selectedIndex >= 0) &&
				(getSelectedComponent() != null) &&
				(this.selectedIndex < getTabsCount())) {
			Component[] ret = new Component[1];
			ret[0] = getSelectedComponent();
			
			return ret;
		} else {
			return null;
		}
	}
	
	public void addItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		tab.setItemListener(AWTEventMulticaster.add(tab.getItemListener(), listener));
	}
	
	public void removeItemListener(ItemListener listener) {
		if (listener == null) {
			return;
		}
		AWTEventMulticaster.remove(tab.getItemListener(), listener);
		tab.setItemListener(null);
	}
	
	public void setCommonComponent(Component common) {
		this.commonComponent = common;
		this.doLayout();
	}
	
	public Component getCommonComponent() {
		return this.commonComponent;
	}
	
	/**
	 *	Returns the currently selected component for this tabbed pane.
	 *	Returns null if there is no currently selected tab.
	 */
	public Component getSelectedComponent() {
        return (this.selectedIndex == -1) ?
        	null :
        	this.getComponentAt(this.selectedIndex);
    }
    
    public String getSelectedTitle() {
        if (this.selectedIndex == -1) {
        	return null;
        }
        
        return getTitleAt(this.selectedIndex);
    }
    
	/**
	 *	Returns the currently selected index for this tabbedpane.
	 *	Returns -1 if there is no currently selected tab.
	 */
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	
	/**
	 * Sets the selected index for this tabbedpane.
	 */
	public void setSelectedIndex(int index) {
		if (this.selectedIndex != index) {
			this.previousSelectedIndex = this.selectedIndex;
			this.selectedIndex = index;
			this.doLayout();
		}
	}
	
	/**
	 * Adds a <i>component</i> represented by a <i>title</i>.
	 */
	public void addTab(String title, Component comp) {
		int len = 0;
		if (components != null) {
			len = this.getTabsCount();
		}
		
		Component[] newPanels = new Component[len+1];
		String[] newTitles = new String[len+1];
		
		if (components != null && titles != null) {
			System.arraycopy(components, 0, newPanels, 0, len);
			System.arraycopy(titles, 0, newTitles, 0, len);
		}
		newPanels[len] = comp;
		newTitles[len] = title;
		
		this.components = newPanels;
		this.titles = newTitles;
		
		this.setSelectedIndex(len);
	}
	
	public void removeTabAt(int index) {
		if (components == null) {
			throw new ArrayIndexOutOfBoundsException("no tabs");
		}
		
		int len = this.getTabsCount();
		if (index < 0 || index >= len) {
			throw new ArrayIndexOutOfBoundsException("out of range");
		}
		
		if (len == 1) {
			removeTabs();
		} else {
			Component[] newPanels = new Component[len-1];
			String[] newTitles = new String[len-1];
			
			System.arraycopy(components, 0, newPanels, 0, index);
			System.arraycopy(titles, 0, newTitles, 0, index);
			System.arraycopy(components, index+1, newPanels, index, len-index-1);
			System.arraycopy(titles, index+1, newTitles, index, len-index-1);
			
			this.components = newPanels;
			this.titles = newTitles;
			setSelectedIndex(0);
		}
	}
	
	public void removeTabs() {
		this.components = null;
		this.titles = null;
		this.setSelectedIndex(-1);
	}
	
	public int getTabsCount() {
		return components.length;
	}
	
	public Component getComponentAt(int index) {
		return this.components[index];
	}
	
	public String getTitleAt(int index) {
		return this.titles[index];
	}
	
	public Dimension getMinimumSize() {
		Dimension currentMax = new Dimension(32, 32);
		Dimension current;
		
		if ((components != null) && (getTabsCount() != 0)) {
			int len = components.length;
			for (int i = 0; i < len; i++) {
				current = components[i].getMinimumSize();
				if (current.width > currentMax.width) {
					currentMax = new Dimension(current.width, currentMax.height);
				}
				if (current.height > currentMax.height) {
					currentMax = new Dimension(currentMax.width, current.height);
				}
			}
		}
		
		return currentMax;
	}
	
	public Dimension getPreferredSize() {
		Dimension currentMax = new Dimension(32, 32);
		Dimension current;
		
		if ((components != null) && (getTabsCount() != 0)) {
			int len = components.length;
			for (int i = 0; i < len; i++) {
				current = components[i].getPreferredSize();
				if (current.width > currentMax.width) {
					currentMax = new Dimension(current.width, currentMax.height);
				}
				if (current.height > currentMax.height) {
					currentMax = new Dimension(currentMax.width, current.height);
				}
			}
		}
		
		return currentMax;
	}
	
}


/**
 *	<code>TabTitlesComponent</code> represents a tab with a given title.
 *	(Used in <code>LWTabbedPane</code>.)
 */

class TabTitlesComponent extends Component implements MouseListener {
	
	static final Font SELECTED_TAB_TITLE_FONT = new Font("Geneva", Font.BOLD, 9);
	static final Font TAB_TITLE_FONT = new Font("Geneva", Font.PLAIN, 9);
	static final Dimension TAB_TITLE_DIMENSION = new Dimension(75, 18);
	
	private LWTabbedPane tabbedPane;
	private transient ItemListener itemListener;
	
	TabTitlesComponent(LWTabbedPane tabbedPane) {
		super();
		this.tabbedPane = tabbedPane;
		
		super.setSize(this.getPreferredSize());
		super.addMouseListener(this);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(tabbedPane.getSize().width, TAB_TITLE_DIMENSION.height + 2);
	}
	
	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {
		int index = (int)(e.getX()/75.0d);
		if (index < 0) {
			index = 0;
		}
		
		int count = tabbedPane.getTabsCount();
		if ((index < count) && (index != tabbedPane.getSelectedIndex())) {
			// 1. change selected index
			// 2. repaint
			// 3. fire change
			tabbedPane.setSelectedIndex(index);
			this.repaint();
			this.fireItemStateChanged();
		}
	}
	
	public void setItemListener(ItemListener listener) {
		this.itemListener = listener;
	}
	
	public ItemListener getItemListener() {
		return this.itemListener;
	}
	
	private void fireItemStateChanged() {
		if (itemListener != null) {
			itemListener.itemStateChanged(
				new ItemEvent(
					tabbedPane,
					ItemEvent.ITEM_STATE_CHANGED,
					tabbedPane.getSelectedComponent(),
					tabbedPane.getSelectedIndex()
				)
			);
		}
	}
	
	public void paint(Graphics g) {
		int w = super.getSize().width;
		int h = super.getSize().height;
		
		g.setColor(super.getForeground());
		g.drawLine(0, h-1, w-1, h-1);
		
		int count = tabbedPane.getTabsCount();
		for (int i = 0; i < count; i++) {
			int currentX = TAB_TITLE_DIMENSION.width*i;
			int nextX = TAB_TITLE_DIMENSION.width*(i+1) - 1;
			
			Font f = TAB_TITLE_FONT;
			if (i == tabbedPane.getSelectedIndex()) {
				f = SELECTED_TAB_TITLE_FONT;
			} else {
				g.setColor(super.getBackground().darker());
				g.fillRect(currentX, 2, nextX - currentX, h-3);
				g.drawLine(currentX + 1, 1, nextX - 1, 1);
				g.setColor(super.getForeground());
			}
			
			// vertical lines
			g.drawLine(
				currentX, 2,
				currentX, TAB_TITLE_DIMENSION.height
			);
			g.drawLine(
				nextX, 2,
				nextX, TAB_TITLE_DIMENSION.height
			);
			
			// horizontal lines
			g.drawLine(currentX+2, 0, nextX-2, 0);
			if (i == tabbedPane.getSelectedIndex()) {
				g.setColor(super.getBackground());
				g.drawLine(currentX+1, h-1, nextX-1, h-1);
				g.setColor(super.getForeground());
			}
			
			// diagonal lines
			g.drawLine(currentX, 2, currentX+2, 0);
			g.drawLine(nextX, 2, nextX-2, 0);
			
			// shadow
			g.setColor(super.getBackground().darker().darker());
			g.drawLine(nextX-1, 2, nextX-1, TAB_TITLE_DIMENSION.height);
			g.setColor(super.getForeground());
			
			// title
			FontMetrics fm = super.getFontMetrics(f);
			String title = tabbedPane.getTitleAt(i);
			int stringWidth = fm.stringWidth(title);
			int maxHeight = fm.getMaxAscent() + fm.getMaxDescent();
			
			g.setFont(f);
			g.drawString(
				title, 
				(int)(TAB_TITLE_DIMENSION.width*i + (TAB_TITLE_DIMENSION.width - stringWidth)/2),
				(int)(h - maxHeight/2)
			);
		}
	}
	
}