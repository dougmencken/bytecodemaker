/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.*;
import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.util.WindowUtilities;

/**
 *	<code>ThreadMonitor</code>
 *
 *	@version	1.01f2
 */

public class ThreadMonitor extends Frame implements ActionListener {
	
	protected Thread[] threads;
	protected TextViewSelectableLine threadList;
	protected ThreadGroup threadGroup;
	
	/**
	 *	The default ThreadGroup is the group of current thread.
	 */
	public ThreadMonitor() {
		this(Thread.currentThread().getThreadGroup());
	}
	
	/**
	 *	Initializes a new <code>ThreadMonitor</code> object
	 *	for a specified ThreadGroup.
	 */
	public ThreadMonitor(ThreadGroup group) {
		super("Thread Monitor");
		
		this.threadList = new TextViewSelectableLine();
		this.threadList.setFont(new Font("Monaco", Font.PLAIN, 9));
		this.threadList.useBorder();
		
		this.threadGroup = group;
		super.add(threadList, "Center");
		
		Panel panel = new Panel(new FlowLayout(FlowLayout.CENTER));
		
		LWButton updateButton = new LWButton("Update");
		updateButton.setSize(WindowUtilities.BUTTON_DIMENSION);
		updateButton.setActionCommand("UPDATE");
		updateButton.addActionListener(this);
		panel.add(updateButton);
		
		/* STOP BUTTON IS VERY DANGEROUS */
		// LWButton stopButton = new LWButton("Stop");
		// stopButton.setSize(WindowUtilities.BUTTON_DIMENSION);
		// stopButton.setActionCommand("STOP");
		// stopButton.addActionListener(this);
		// panel.add(stopButton);
		
		/* RESUME BUTTON IN PROGRESS */
		// LWButton resumeButton = new LWButton("Resume");
		// resumeButton.setSize(WindowUtilities.BUTTON_DIMENSION);
		// resumeButton.setActionCommand("RESUME");
		// resumeButton.addActionListener(this);
		// resumeButton.setEnabled(false);
		// panel.add(resumeButton);
		
		super.add("South", panel);
		this.updateList();
		
		// the default size & location
		super.setSize(500, 300);
		super.setLocation(calculateThreadMonitorLocation(this));
	}
	
	/**
	 *	(private static method)
	 */
	private static Point calculateThreadMonitorLocation(Window monitor) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point(
			screenSize.width - monitor.getSize().width,
			15
		);
	}
	
    public void actionPerformed(ActionEvent evt) {
		synchronized (this) {
	    	String command = evt.getActionCommand();
	    	
	    	if (command.equals("UPDATE")) {
				this.updateList();
	    	}/* else if (command.equals("STOP")) {
	    		int selectedIndex = threadList.getSelectedIndex();
				Thread thread = threads[selectedIndex];
				
				try {
					thread.checkAccess();
					thread.setPriority(Thread.MIN_PRIORITY);
					thread.stop();
				} catch (SecurityException exc) {
					System.err.println("No permission to stop thread " + thread.getName());
				}
				
				this.updateList();
				threadList.select(selectedIndex);
			} else if (command.equals("RESUME")) {
				// under construction
				super.getToolkit().beep();
			}*/
		}
	}
	
	/**
	 *	'*name*' - current thread.
	 */
	private synchronized void updateList() {
		ThreadGroup group;
		for (group = this.threadGroup; group.getParent() != null;
				group = group.getParent()) {
			/* empty */
		}
		
		Thread currentThread = Thread.currentThread();
		this.threads = new Thread[group.activeCount()];
		int count = group.enumerate(threads, true);
		this.threadList.clear();
		
		for (int i = 0; i < count; i++) {
			Thread thread = threads[i];
			
			StringBuffer buf = new StringBuffer();
			if (thread == currentThread) {
				buf.append('*').append(thread.getName()).append('*');
			} else {
				buf.append(thread.getName());
			}
			
			buf.append("  P: ").append(thread.getPriority()).append(" G(");
			if (thread.getThreadGroup() != null) {
				buf.append(thread.getThreadGroup().getName());
			}
			buf.append(")").append(" [").append(thread.countStackFrames()).append("] ");
			buf.append(thread.isDaemon() ? "Deamon " : " ");
			buf.append(thread.isInterrupted() ? " " : "Interrupted ");
			buf.append(thread.isAlive() ? " " : "Not alive ");
			
			threadList.appendln(buf.toString());
		}
	}
	
}
