// ===========================================================================
//	LogMonitor.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.beans.NumbersView;
import douglas.mencken.beans.TextView;

/**
 *	<code>LogMonitor</code>
 *
 *	@version 1.11f4
 */

public class LogMonitor extends Dialog
implements ActionListener, KeyListener, WindowListener {
	
	private Log theLog;
	private TextView list;
	
	private static LogMonitor currentMonitor = new LogMonitor(new Log());
	
	/**
	 *	Creates a new <code>LogMonitor</code>.
	 */
	public LogMonitor(Log log) {
		super(new InvisibleFrame(), "log", /* modal */ false);
		
		if (log == null) {
			throw new IllegalArgumentException("can't monitor 'null' log");
		}
		
		this.theLog = log;
		currentMonitor = this;
		
		super.setSize(480, 220);
		super.setResizable(false);
		Dimension screenSize = super.getToolkit().getScreenSize();
		super.setLocation(
			screenSize.width - getSize().width - 1,
			screenSize.height - getSize().height - 1
		);
		
		this.list = new TextView(Color.blue, Color.white);
		list.setFont(new Font("Monaco", Font.PLAIN, 9));
		list.add(new TextViewPopupMenu(list, "log", false));
		list.useBorder();
		super.add("Center", list);
		NumbersView np = new NumbersView(list);
		np.useBorder();
		super.add("West", np);
		
		Panel forButton = new Panel();
		Button button = new Button("    OK    ");
		button.addActionListener(this);
		forButton.add(button);
		super.add("South", forButton);
		
		super.addKeyListener(this);
		super.addWindowListener(this);
	}
	
	protected void updateList() {
		String[] records = this.theLog.getRecords();
		if (records != null) {
			int len = records.length;
			for (int i = 0; i < len; i++) {
				list.appendln(records[i]);
			}
		}
		
		list.invalidate();
	}
	
	public void setVisible(boolean newVisible) {
		// show/hide only if log changed
		if (this.theLog.isChanged()) {
			if (!this.theLog.isEmpty()) {
				if (newVisible) {
					this.updateList();
				} else {
					this.theLog.clear();
					this.list.clear();
				}
			}
			
			super.setVisible(newVisible);
			this.theLog.setChanged(false);
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			super.setVisible(false);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		super.setVisible(false);
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		super.setVisible(false);
	}
	
	/**
	 *	(static method)
	 */
	public static void addToCurrentLog(String record) {
		Log log = currentMonitor.theLog;
		
		if (!log.isEmpty() && !log.isChanged()) {
			log.clear();
			currentMonitor.list.clear();
		}
		
		log.addRecord(record);
	}
	
	/**
	 *	(static method)
	 */
	public static void showCurrent() {
		currentMonitor.setVisible(true);
	}
	
	/**
	 *	(static method)
	 */
	public static void clearCurrent() {
		currentMonitor.theLog.clear();
	}
	
}