// ===========================================================================
//	ButtonedLabel.java (part of douglas.mencken.beans package)
// ===========================================================================

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;

/**
 *	Multi-line text label with small box buttons.
 *	(Four button placements are supported.)
 *	Also paints one or more lines at the bottom.
 *
 *	@version	1.04f1
 */

public class ButtonedLabel extends Component
implements MouseListener, MouseMotionListener {
	
	/** <pre>([button1][button2]                )</pre> */
	public static final int BUTTONS_LEFT		= 0;
	
	/** <pre>(                [button1][button2])</pre> */
	public static final int BUTTONS_RIGHT		= 1;
	
	/** <pre>([button1]                [button2])</pre> */
	public static final int BUTTONS_LEFT_RIGHT	= 2;
	
	/** <pre>(        [button1][button2]        )</pre> */
	public static final int BUTTONS_CENTER		= 3;
	
	
	protected String[] labels;
	protected String[] buttonLabels;
	protected int buttonPlacement;
	protected Font buttonFont;
	protected Color buttonColor = Color.black;
	
	protected int bottomLinesCount = 1;
	protected int bottomLinesSpace = 1;
	
	private transient ActionListener actionListener;
	private String actionCommand;
	
	private transient int[] buttonLabelWidths = null;
	private transient int[] buttonLabelXPos = null;
	
	private transient boolean pressed = false;
	private transient boolean dragExit = false;
	private transient int lastPressedButtonIndex = -1;
	
	private Image offscreenImage;
	private Graphics offscreenGraphics;
	
	/**
	 *	The default constructor.
	 *	<p>
	 *	Default button placement:	<code>BUTTONS_LEFT</code><br>
	 *	Default labels:				<code>null</code> (no labels)<br>
	 *	Default buttons:			<b>"OK"</b> and <b>"Cancel"</b><br>
	 */
	public ButtonedLabel() {
		this(ButtonedLabel.BUTTONS_LEFT, null, new String[] { "OK", "Cancel" });
	}
	
	public ButtonedLabel(int buttonPlacement) {
		this(buttonPlacement, null, new String[] { "OK", "Cancel" });
	}
	
	public ButtonedLabel(String[] labels, String[] buttons) {
		this(ButtonedLabel.BUTTONS_LEFT, labels, buttons);
	}
	
	public ButtonedLabel(int buttonPlacement, String[] labels, String[] buttons) {
		super();
		this.buttonPlacement = buttonPlacement;
		this.labels = labels;
		this.buttonLabels = buttons;
		this.buttonFont = new Font("Monaco", Font.PLAIN, 9);
		
		super.addMouseListener(this);
		super.addMouseMotionListener(this);
		
		super.setFont(new Font("Geneva", Font.PLAIN, 10));
		super.setSize(this.getPreferredSize());
		super.repaint();
	}
	
	
	public String getActionCommand() { return this.actionCommand; }
	
	/**
	 *	The action command for the button <b>Button</b> will be
	 *	<b>command$Button</b>
	 */
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public void addActionListener(ActionListener listener) {
		this.actionListener = AWTEventMulticaster.add(actionListener, listener);
		super.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeActionListener(ActionListener listener) {
		this.actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public String[] getLabels() {
		return this.labels;
	}
	
	public void setLabels(String[] labels) {
		this.labels = labels;
		super.repaint();
	}
	
	public String[] getButtons() {
		return this.buttonLabels;
	}
	
	public void setButtons(String[] buttons) {
		this.buttonLabels = buttons;
		super.repaint();
	}
	
	public int getButtonPlacementMode() {
		return this.buttonPlacement;
	}
	
	public void setButtonPlacementMode(int buttonPlacement) {
		this.buttonPlacement = buttonPlacement;
		super.repaint();
	}
	
	public Font getButtonFont() {
		return this.buttonFont;
	}
	
	public void setButtonFont(Font newFont) {
		this.buttonFont = newFont;
		super.repaint();
	}
	
	public Color getButtonColor() {
		return this.buttonColor;
	}
	
	public void setButtonColor(Color newColor) {
		this.buttonColor = newColor;
		super.repaint();
	}
	
	/**
     *	Gets the preferred <code>Dimension</code> indicating this
     *	<code>ButtonedLabel</code>'s preferred size.
     */
	public Dimension getPreferredSize() {
		int prefx = 100;
		int prefy = 1;
		
		FontMetrics fmmain = super.getFontMetrics(super.getFont());
		FontMetrics fmbuttons = super.getFontMetrics(this.buttonFont);
		int labelsHeight = fmmain.getMaxAscent() + fmmain.getMaxDescent();
		int buttonsHeight = fmbuttons.getMaxAscent() + fmbuttons.getMaxDescent();
		
		if (this.labels != null) {
			int labelsCount = this.labels.length;
			prefy += labelsHeight*labelsCount;
			
			for (int i = 0; i < labelsCount; i++) {
				int widthi = fmmain.stringWidth(this.labels[i]);
				if (widthi > prefx) {
					prefx = widthi;
				}
			}
		}
		if (this.buttonLabels != null) {
			prefy += 8 + buttonsHeight;
			
			int blabels_width = this.calculateButtonsWidth();
			if (blabels_width > prefx) {
				prefx = blabels_width;
			}
		}
		
		if (bottomLinesCount > 1) {
			// + (bottomLinesCount-1) + (bottomLinesCount-1)*bottomLinesSpace
			prefy += (bottomLinesCount-1) * (bottomLinesSpace+1);
		}
		
		return new Dimension(prefx + 8, prefy + 8);
	}
	
	/**
	 *	Calculates the total width of buttons.
	 */
	public int calculateButtonsWidth() {
		if (this.buttonLabelWidths == null) {
			this.calculateButtonLabels();
		}
		
		int width = -9;
		
		int bl_count = this.buttonLabelWidths.length;
		for (int i = 0; i < bl_count; i++) {
			width += this.buttonLabelWidths[i] + 10;
		}
		
		return width;
	}
	
	public int getBottomLinesSpace() {
		return this.bottomLinesSpace;
	}
	
	public void setBottomLinesSpace(int space) {
		this.bottomLinesSpace = space;
		super.repaint();
	}
	
	public int getBottomLinesCount() {
		return this.bottomLinesCount;
	}
	
	public void addBottomLine() {
		if (this.bottomLinesCount < 10) {
			this.bottomLinesCount++;
			super.repaint();
		}
	}
	
	public void removeBottomLine() {
		if (this.bottomLinesCount > 1) {
			this.bottomLinesCount--;
			super.repaint();
		}
	}
	
	private void calculateButtonLabels() {
		int w = super.getSize().width;
		int h = super.getSize().height;
		
		if (this.buttonLabels != null) {
			int buttonsCount = this.buttonLabels.length;
			this.buttonLabelWidths = new int[buttonsCount];
			this.buttonLabelXPos = new int[buttonsCount];
			
			// buttonLabelWidths
			FontMetrics fm = super.getFontMetrics(this.buttonFont);
			for (int i = 0; i < buttonsCount; i++) {
				this.buttonLabelWidths[i] = fm.stringWidth(this.buttonLabels[i]);
			}
			
			// buttonLabelXPos
			switch (this.buttonPlacement) {
				case BUTTONS_LEFT:
				{
					int pos = 5;
					for (int i = 0; i < buttonsCount; i++) {
						this.buttonLabelXPos[i] = pos;
						pos += this.buttonLabelWidths[i] + 10;
					}
					
					break;
				}
				
				case BUTTONS_RIGHT:
				{
					int pos = w - this.buttonLabelWidths[buttonsCount-1] - 4;
					this.buttonLabelXPos[buttonsCount-1] = pos;
					for (int i = (buttonsCount - 2); i >= 0; i--) {
						pos -= (this.buttonLabelWidths[i] + 10);
						this.buttonLabelXPos[i] = pos;
					}
					
					break;
				}
				
				case BUTTONS_LEFT_RIGHT:
				{
					// left buttons
					int pos = 5;
					for (int i = 0; i < buttonsCount; i += 2) {
						this.buttonLabelXPos[i] = pos;
						pos += this.buttonLabelWidths[i] + 10;
					}
					
					// right buttons
					if (buttonsCount >= 2) {
						int rindex = ((buttonsCount % 2) == 0) ?
													buttonsCount - 1 :
													buttonsCount - 2;
						pos = w - this.buttonLabelWidths[rindex] - 4;
						this.buttonLabelXPos[rindex] = pos;
						for (int i = (rindex - 2); i >= 0; i -= 2) {
							pos -= (this.buttonLabelWidths[i] + 10);
							this.buttonLabelXPos[i] = pos;
						}
					}
					
					break;
				}
				
				case BUTTONS_CENTER:
				{
					int offset = (w - this.calculateButtonsWidth())/2 - 4;
					int pos = 5;
					for (int i = 0; i < buttonsCount; i++) {
						this.buttonLabelXPos[i] = pos + offset;
						pos += this.buttonLabelWidths[i] + 10;
					}
					
					break;
				}
			}
		} else {
			this.buttonLabelWidths = new int[0];
			this.buttonLabelXPos = new int[0];
		}
	}
	
	public boolean isDoubleBuffered() {
		return true;
	}
	
	/**
	 *	@see	#paint
	 */
	public void update(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = createImage(getSize().width, getSize().height);
			offscreenGraphics = offscreenImage.getGraphics();
		}
		
		this.paint(offscreenGraphics);
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	/**
	 *	Paints this ButtonedLabel.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		this.calculateButtonLabels();
		
		int w = super.getSize().width;
		int h = super.getSize().height;
		
		g.setColor(super.getBackground());
		int firstline_y = h - (bottomLinesCount-1)*(bottomLinesSpace+1) - 1;
		g.fillRect(0, 0, w, firstline_y);
		g.setColor(super.getForeground());
		
		// - labels -
		if (this.labels != null) {
			int labelsCount = this.labels.length;
			int max_ascent = super.getFontMetrics(super.getFont()).getMaxAscent();
			g.setFont(super.getFont());
			
			int ypos = 4;
			for (int i = 0; i < labelsCount; i++) {
				g.drawString(this.labels[i], 4, ypos + max_ascent);
				ypos += max_ascent + 2;
			}
		}
		
		if (pressed) {
			this.dragExit = true;
		}
		
		// - buttons -
		if (this.buttonLabels != null) {
			FontMetrics fm = super.getFontMetrics(this.buttonFont);
			int max_ascent = fm.getMaxAscent();
			int max_descent = fm.getMaxDescent();
			int fontHeight = max_ascent + max_descent;
			
			g.setFont(this.buttonFont);
			int buttonsCount = this.buttonLabels.length;
			
			for (int i = 0; i < buttonsCount; i++) {
				Rectangle buttonRect = new Rectangle(
					/* x, y, width, height */
					this.buttonLabelXPos[i] - 5, firstline_y - fontHeight,
					this.buttonLabelWidths[i] + 8, fontHeight
				);
				
				if (pressed && (i == this.lastPressedButtonIndex)) {
					g.setColor(super.getForeground());
					g.fillRect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
					g.setColor(super.getBackground());
				} else {
					g.drawRect(buttonRect.x, buttonRect.y, buttonRect.width, buttonRect.height);
				}
				
				g.drawString(
					this.buttonLabels[i], this.buttonLabelXPos[i],
					firstline_y - max_descent
				);
				g.setColor(super.getForeground());
			}
		}
		
		// - bottom lines -
		g.setColor(super.getForeground());
		g.drawLine(0, firstline_y, w, firstline_y);
		
		int ypos = firstline_y;
		for (int i = 1; i < bottomLinesCount; i++) {
			g.setColor(super.getBackground());
			g.fillRect(0, ypos + 1, w, ypos + bottomLinesSpace);
			
			ypos += bottomLinesSpace + 1;
			
			g.setColor(super.getForeground());
			g.drawLine(0, ypos, w, ypos);
		}
	}
	
	/**
	 *	@return		index of the button or -1 if no button is found.
	 */
	private int findButtonAt(Point where) {
		FontMetrics fm = super.getFontMetrics(this.buttonFont);
		int fontHeight = fm.getMaxAscent() + fm.getMaxDescent();
		int firstline_y = super.getSize().height - (bottomLinesCount-1)*(bottomLinesSpace+1) - 1;
		
		if ((where.y >= (firstline_y - fontHeight)) && (where.y <= firstline_y)) {
			int buttonsCount = this.buttonLabels.length;
			
			for (int i = 0; i < buttonsCount; i++) {
				int xpos = this.buttonLabelXPos[i];
				if ((where.x >= xpos - 4) &&
						(where.x <= xpos + this.buttonLabelWidths[i] + 4)) {
					return i;
				}
			}
		}
		
		// not found
		return -1;
	}
	
	public void mousePressed(MouseEvent evt) {
		if (!super.isEnabled()) return;
		
		this.lastPressedButtonIndex = this.findButtonAt(new Point(evt.getX(), evt.getY()));
		if (this.lastPressedButtonIndex == -1) return;
		
		pressed = true;
		super.repaint();
	}
	
	public void mouseReleased(MouseEvent evt) {
		if (!super.isEnabled()) return;
		
		dragExit = false;
		
		if (pressed) {
			pressed = false;
			super.repaint(); 
			
			if (actionListener != null) {
				actionListener.actionPerformed(
					new ActionEvent(
						this,
						ActionEvent.ACTION_PERFORMED,
						actionCommand + '$' + this.buttonLabels[lastPressedButtonIndex]
					)
				);
			}
		}
	}
	
	public void mouseEntered(MouseEvent evt) {
		if (!super.isEnabled()) return;
		
		if (dragExit) {
			pressed = true;
			super.repaint();
		}
	}
	
	public void mouseExited(MouseEvent evt) {
		if (!super.isEnabled()) return;
		
		if (pressed == true) {
			pressed = false;
			super.repaint();
		}
	}
	
	public void mouseDragged(MouseEvent evt) {
		if (findButtonAt(new Point(evt.getX(), evt.getY())) != lastPressedButtonIndex) {
			this.mouseExited(new MouseEvent(
				this,
				MouseEvent.MOUSE_EXITED,
				-1L, -1,
				evt.getX(), evt.getY(),
				1, false
			));
		} else {
			if (!pressed) {
				this.mouseEntered(new MouseEvent(
					this,
					MouseEvent.MOUSE_ENTERED,
					-1L, -1,
					evt.getX(), evt.getY(),
					1, false
				));
			}
		}
	}
	
	public void mouseClicked(MouseEvent evt) {}
	public void mouseMoved(MouseEvent evt) {}
	
}