/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.tools.zip;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.zip.*;

import netscape.util.Sort;
import douglas.mencken.util.FontUtilities;

/**
 *	<code>ZipCatalogFrame</code>
 *
 *	@version 1.01f
 */

public class ZipCatalogFrame extends Frame
implements WindowListener, ActionListener {
	
	private ZipCatalog catalog;
	private String[] files;
	private InputStream selectedStream = null;
	
	private transient ActionListener actionListener;
	private transient String actionCommand;
	
	public ZipCatalogFrame(File zipFile) {
		this(zipFile.getPath());
	}
	
	public ZipCatalogFrame(String name) {
		super("");
		setActionCommand("ZIP_CATALOG");
		
		ZipCatalog catalog = new ZipCatalog(name);
		String[] files = catalog.getNames();
		Sort.sortStrings(files, 0, files.length, true, false);
		
		this.catalog = catalog;
		this.files = files;
		
		int filesCount = 0;
		int flen = files.length;
		for (int i = 0; i < flen; i++) {
			if (!files[i].endsWith("/")) filesCount++;
		}
		
		super.setTitle(
			(new File(name)).getName() + ": " +
			flen + " entries, " +
			filesCount + " files"
		);
		
		setSize(350, 450);
		setLocation(15, 5);
		
		addWindowListener(this);
	}
	
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}
	
	public String getActionCommand() { return actionCommand; }
	
	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
	
	public void doLayout() {
		super.removeAll();
		
		List filesList = new List();
		filesList.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		filesList.addActionListener(this);
		
		if ((files != null) && (files.length != 0)) {
			int flen = files.length;
			for (int i = 0; i < flen; i++) {
				filesList.add(files[i]);
			}
		}
		
		add("Center", filesList);
		super.doLayout();
	}
	
	public InputStream getSelectedInputStream() {
		return selectedStream;
	}
	
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		
		try {
			ZipEntry entry = catalog.findEntryByName(command);
			this.selectedStream = catalog.getInputStream(entry);
		} catch (Exception e) {
			return;
		}
		
		if (actionListener != null) {
			actionListener.actionPerformed(
				new ActionEvent(
					this,
					ActionEvent.ACTION_PERFORMED,
					actionCommand
				)
			);
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