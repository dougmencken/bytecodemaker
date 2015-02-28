// ===========================================================================
//	MemoryMonitor.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.beans.*;
import douglas.mencken.util.StringUtilities;

/**
 *	<code>MemoryMonitor</code>
 *
 *	@version 1.12f1
 */

public class MemoryMonitor extends Frame
implements Runnable, ActionListener, ItemListener {
	
	private LWButton updateButton;
	private Choice updateChoice;
	protected int updateTime = 10000; // 10 * 1000
	
	private LWLabel usedLabel;
	private LWLabel totalLabel;
	private LWLabel percentLabel;
	
	private LWProgressBar pbar;
	private transient Thread updateThread;
	
	public MemoryMonitor() {
		super("Memory Monitor");
		super.setFont(new Font("Geneva", Font.PLAIN, 10));
		
		this.updateButton = new LWButton("Update");
		updateButton.setActionCommand("UPDATE");
		updateButton.addActionListener(this);
		
		LWButton garbage = new LWButton("Collect Garbage");
		garbage.setActionCommand("COLLECT");
		garbage.addActionListener(this);
		
		this.usedLabel = new LWLabel("");
		this.totalLabel = new LWLabel("");
		this.percentLabel = new LWLabel("");
		
		this.pbar = new LWProgressBar(0, 1000);
		pbar.setBackground(Color.white);
		pbar.setForeground(new Color(0x787800));
		pbar.setSize(186, 18);
		
		updateMonitor();
		this.updateChoice = new Choice();
		updateChoice.setFont(new Font("Geneva", Font.ITALIC, 10));
		updateChoice.addItemListener(this);
		
		updateChoice.addItem("1");
		updateChoice.addItem("2");
		updateChoice.addItem("3");
		updateChoice.addItem("5");
		updateChoice.addItem("10");
		updateChoice.addItem("20");
		updateChoice.addItem("30");
		updateChoice.addItem("60");
		updateChoice.addItem("stop");
		updateChoice.select(4);
		
		Panel topPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 5, 3));
		topPanel.add(new LWLabel("Update every"));
		topPanel.add(updateChoice);
		topPanel.add(new LWLabel("sec. "));
		topPanel.add(updateButton);
		
		Panel usedMemoryPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 3));
		usedMemoryPanel.add(usedLabel);
		
		Panel totalMemoryPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 3));
		totalMemoryPanel.add(totalLabel);
		totalMemoryPanel.add(garbage);
		
		Panel bytesPanel = new Panel(new BorderLayout());
		bytesPanel.add("North", usedMemoryPanel);
		bytesPanel.add("South", totalMemoryPanel);
		
		add("North", topPanel);
		add("Center", bytesPanel);
		add("South", pbar);
		
		super.pack();
		super.setResizable(false);
		super.setLocation(calculateMemoryMonitorLocation(this));
	}
	
	public void setVisible(boolean newVisible) {
		if (newVisible) {
			super.setVisible(true);
			this.start();
		} else {
			this.stop();
			super.setVisible(false);
		}
	}
	
	protected void start() {
		this.stop();
		this.updateThread = new Thread(this, "Thread-MemoryMonitor");
		this.updateThread.setPriority(Thread.NORM_PRIORITY - 1);
		this.updateThread.setDaemon(true);
		this.updateThread.start();
	}
	
	protected void stop() {
		if (updateThread != null) {
			updateThread.stop();
			updateThread = null;
		}
	}
	
	public void run() {
		while (updateThread != null) {
			updateMonitor();
			try {
				Thread.sleep(updateTime);
			} catch (InterruptedException e) {}
		}
	}
	
	protected void updateMonitor() {
		Runtime runtime = Runtime.getRuntime();
		long total = runtime.totalMemory();
		long used  = total - runtime.freeMemory();
		
		int percent = (int)(used*1000 / total);
		String percentString = "(" + (percent / 10) + "." + (percent % 10) + "%)";
		
		usedLabel.setText(
			" Used memory:  " +
			StringUtilities.toSeparatedByThousandsNumber(used) + " bytes  " +
			percentString + "  "
		);
		totalLabel.setText(
			" Total memory: " +
			StringUtilities.toSeparatedByThousandsNumber(total) + " bytes  "
		);
		
		pbar.setForeground(new Color((int)(0x48 + percent/10), 0x78, 0x00));
		pbar.setValue(percent);
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("UPDATE")) {
			updateMonitor();
		} else if (command.equals("COLLECT")) {
			Runtime.getRuntime().gc();
			updateMonitor();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.stop();
		String choice = (String)e.getItem();
		
		if (!choice.equals("stop")) {
			this.updateTime = Integer.parseInt(choice) * 1000;
			this.start();
		}
	}
	
	/**
	 *	(private static method)
	 */
	private Point calculateMemoryMonitorLocation(Window monitor) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point(
			screenSize.width - monitor.getSize().width,
			screenSize.height - monitor.getSize().height
		);
	}
	
}