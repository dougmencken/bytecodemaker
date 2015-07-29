/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.util.MenuUtilities;
import douglas.mencken.util.WindowTracker;
import douglas.mencken.tools.MemoryMonitor;
import douglas.mencken.tools.ThreadMonitor;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.BMEnvironment;
import douglas.mencken.bm.frames.*;

/**
 *	Menu <b>Window</b> for Bytecode Maker.
 *
 *	@version 1.1
 */

public final class WindowMenu extends Menu
implements BMMenu, WindowListener {
	
	private WindowTracker windowTracker = new WindowTracker();
	
	private Menu currentClass;
	private CheckboxMenuItem constantPoolWindow;
	private CheckboxMenuItem fieldsWindow;
	private CheckboxMenuItem methodsWindow;
	private CheckboxMenuItem attributesWindow;
	private CheckboxMenuItem classDump;
	private CheckboxMenuItem classSource;
	
	private MenuItem memoryMonitor;
	private MenuItem threadMonitor;
	
	private MenuItem cycleThroughWindows;
	
	private static final String[] MENU_DESCR = {
		">No class", null, "", "f",
		"-", null, null, null,
		"Show/Hide Memory Monitor", "", "MEMORY_MONITOR", "",
		"Show/Hide Thread Monitor", "", "THREAD_MONITOR", "",
		"-", null, null, null,
		"Cycle Through Windows", "`", "CYCLE_WINDOWS", "f"
	};
	
	private static final String[] CLASS_SUBMENU_DESCR = {
		"Constant Pool", "1", "C_POOL_W", "x",
		"Fields", "2", "FIELDS_W", "x",
		"Methods", "3", "METHODS_W", "x",
		"Attributes", "4", "ATTRS_W", "x",
		"-", null, null, null,
		"Class Dump", "D", "CLASS_DUMP", "x",
		"Decompiled Source", "R", "CLASS_SOURCE", "x"
	};
	
	public WindowMenu() {
		super("Window");
		MenuUtilities.fillMenuByDesc(WindowMenu.MENU_DESCR, this, this);
		
		this.currentClass = (Menu)MenuUtilities.findItemByLabel(this, WindowMenu.MENU_DESCR[0*4]);
		this.fillClassSubmenu(this.currentClass, this);
		
		this.memoryMonitor = MenuUtilities.findItemByLabel(this, WindowMenu.MENU_DESCR[2*4]);
		this.threadMonitor = MenuUtilities.findItemByLabel(this, WindowMenu.MENU_DESCR[3*4]);
		
		this.cycleThroughWindows = MenuUtilities.findItemByLabel(this, WindowMenu.MENU_DESCR[5*4]);
		
		this.updateMenu();
	}
	
	public void updateMenu() {
		JavaClass current = BMEnvironment.getCurrentClass();
		String className = (current == null) ?
			WindowMenu.MENU_DESCR[0*4].substring(1) : current.getClassName();
		if (!className.equals(this.currentClass.getLabel())) {
			this.currentClass.setLabel(className);
		}
		
		this.currentClass.setEnabled(current != null /* true */);
		this.updateClassSubmenu();
		
		this.memoryMonitor.setLabel(
			this.windowTracker.isVisible("memory monitor") ?
					"Hide Memory Monitor" :
					"Show Memory Monitor"
		);
		this.threadMonitor.setLabel(
			this.windowTracker.isVisible("thread monitor") ?
					"Hide Thread Monitor" :
					"Show Thread Monitor"
		);
		
		//if (less than two windows are here) {
		this.cycleThroughWindows.setEnabled(false);
		//} else ...
	}
	
	private void updateClassSubmenu() {
		if (BMEnvironment.getCurrentClass() == null) {
			this.constantPoolWindow.setState(false);
			this.fieldsWindow.setState(false);
			this.methodsWindow.setState(false);
			this.attributesWindow.setState(false);
			this.classDump.setState(false);
			this.classSource.setState(false);
			
			this.constantPoolWindow.setEnabled(false);
			this.fieldsWindow.setEnabled(false);
			this.methodsWindow.setEnabled(false);
			this.attributesWindow.setEnabled(false);
			this.classDump.setEnabled(false);
			this.classSource.setEnabled(false);
		} else {
			this.constantPoolWindow.setState(this.windowTracker.isVisible("pool window"));
			this.fieldsWindow.setState(this.windowTracker.isVisible("fields window"));
			this.methodsWindow.setState(this.windowTracker.isVisible("methods window"));
			this.attributesWindow.setState(this.windowTracker.isVisible("attributes window"));
			this.classDump.setState(this.windowTracker.isVisible("class dump window"));
			this.classSource.setState(this.windowTracker.isVisible("class source window"));
			
			this.constantPoolWindow.setEnabled(true);
			this.fieldsWindow.setEnabled(true);
			this.methodsWindow.setEnabled(true);
			this.attributesWindow.setEnabled(true);
			this.classDump.setEnabled(true);
			this.classSource.setEnabled(true);
		}
	}
	
	private void fillClassSubmenu(Menu submenu, ActionListener actionListener) {
		MenuUtilities.fillMenuByDesc(WindowMenu.CLASS_SUBMENU_DESCR, submenu, actionListener);
		
		this.constantPoolWindow = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[0*4]);
		this.fieldsWindow = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[1*4]);
		this.methodsWindow = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[2*4]);
		this.attributesWindow = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[3*4]);
		
		this.classDump = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[5*4]);
		this.classSource = (CheckboxMenuItem)MenuUtilities.findItemByLabel(submenu, WindowMenu.CLASS_SUBMENU_DESCR[6*4]);
	}
	
	private void showHideConstantPoolWindow() {
		boolean visible = this.windowTracker.isVisible("pool window");
		if (visible) {
			this.windowTracker.closeWindow("pool window");
		} else {
/*************
			JavaConstantPoolCustomizer window = new JavaConstantPoolCustomizer(this);
			window.setLocation(100, 5);
			window.addWindowListener(this);
			window.setClass(BMEnvironment.getCurrentClass());
			this.windowTracker.showWindow("pool window", window, false);
*******************/
		}
		
		this.updateMenu();
	}
	
	private void showHideFieldsWindow() {
		boolean visible = this.windowTracker.isVisible("fields window");
		if (visible) {
			this.windowTracker.closeWindow("fields window");
		} else {
			FieldsFrame window = new FieldsFrame(this);
			window.setLocation(180, 5);
			window.addWindowListener(this);
			window.setClass(BMEnvironment.getCurrentClass());
			this.windowTracker.showWindow("fields window", window, false);
		}
		
		this.updateMenu();
	}
	
	private void showHideMethodsWindow() {
		boolean visible = this.windowTracker.isVisible("methods window");
		if (visible) {
			this.windowTracker.closeWindow("methods window");
		} else {
			MethodsFrame window = new MethodsFrame(this);
			window.setLocation(200, 5);
			window.addWindowListener(this);
			window.setClass(BMEnvironment.getCurrentClass());
			this.windowTracker.showWindow("methods window", window, false);
		}
		
		this.updateMenu();
	}
	
	private void showHideAttributesWindow() {
		boolean visible = this.windowTracker.isVisible("attributes window");
		if (visible) {
			this.windowTracker.closeWindow("attributes window");
		} else {
			//AttributesFrame window = new AttributesFrame(this);
			//window.setLocation(200, 5);
			//window.addWindowListener(this);
			//window.setClass(BMEnvironment.getCurrentClass());
			//this.windowTracker.showWindow("attributes window", window, false);
		}
		
		this.updateMenu();
	}
	
	private void showHideClassDumpWindow() {
		boolean visible = this.windowTracker.isVisible("class dump window");
		if (visible) {
			this.windowTracker.closeWindow("class dump window");
		} else {
			ClassDumpFrame window = new ClassDumpFrame(this);
			window.setLocation(40, 5);
			window.addWindowListener(this);
			window.setClass(BMEnvironment.getCurrentClass());
			this.windowTracker.showWindow("class dump window", window, false);
		}
		
		this.updateMenu();
	}
	
	private void showHideClassSourceWindow() {
		try {
			boolean visible = this.windowTracker.isVisible("class source window");
			if (visible) {
				this.windowTracker.closeWindow("class source window");
			} else {
				ClassSourceFrame window = new ClassSourceFrame(this);
				Dimension screen = window.getToolkit().getScreenSize();
				window.setBounds(3, 3, (int)(screen.width / 1.25f), screen.height - 15);
				window.addWindowListener(this);
				
				window.setClass(BMEnvironment.getCurrentClass());
				this.windowTracker.showWindow("class source window", window, false);
			}
		} catch (Throwable t) {
			UsefulMessageDialogs.doErrorDialog(t, true);
			classSource.setState(false);
		}
		
		this.updateMenu();
	}
	
	public void showHideMemoryMonitor() {
		boolean visible = this.windowTracker.isVisible("memory monitor");
		if (visible) {
			this.windowTracker.closeWindow("memory monitor");
		} else {
			Window monitor = new MemoryMonitor();
			monitor.addWindowListener(this);
			this.windowTracker.showWindow("memory monitor", monitor);
		}
		
		this.updateMenu();
	}
	
	public void showHideThreadMonitor() {
		boolean visible = this.windowTracker.isVisible("thread monitor");
		if (visible) {
			this.windowTracker.closeWindow("thread monitor");
		} else {
			Window monitor = new ThreadMonitor();
			monitor.addWindowListener(this);
			this.windowTracker.showWindow("thread monitor", monitor);
		}
		
		this.updateMenu();
	}
	
	public void hideWindows(boolean includeMonitors) {
		this.windowTracker.closeWindow("pool window");
		this.windowTracker.closeWindow("fields window");
		this.windowTracker.closeWindow("methods window");
		
		this.hideDumpAndSourceWindows();
		
		if (includeMonitors) {
			this.windowTracker.closeWindow("memory monitor");
			this.windowTracker.closeWindow("thread monitor");
		}
		
		this.updateMenu();
	}
	
	public void hideDumpAndSourceWindows() {
		this.windowTracker.closeWindow("class dump window");
		this.windowTracker.closeWindow("class source window");
	}
	
	public void windowClosing(WindowEvent evt) {
		this.windowTracker.closeWindow(evt.getWindow());
		this.updateMenu();
	}
	
	public void windowClosed(WindowEvent evt) {}
	public void windowOpened(WindowEvent evt) {}
	public void windowIconified(WindowEvent evt) {}
	public void windowDeiconified(WindowEvent evt) {}
	public void windowActivated(WindowEvent evt) {}
	public void windowDeactivated(WindowEvent evt) {}
	
	public void actionPerformed(ActionEvent evt) {
		ClassFrame.getCurrentFrame().toFront();
		
		String command = evt.getActionCommand();
		if (command.equals("C_POOL_W")) {
			this.showHideConstantPoolWindow();
		} else if (command.equals("FIELDS_W")) {
			this.showHideFieldsWindow();
		} else if (command.equals("METHODS_W")) {
			this.showHideMethodsWindow();
		} else if (command.equals("ATTRS_W")) {
			this.showHideAttributesWindow();
		} else if (command.equals("CLASS_DUMP")) {
			this.showHideClassDumpWindow();
		} else if (command.equals("CLASS_SOURCE")) {
			this.showHideClassSourceWindow();
		} else if (command.equals("MEMORY_MONITOR")) {
			this.showHideMemoryMonitor();
		} else if (command.equals("THREAD_MONITOR")) {
			this.showHideThreadMonitor();
		} else if (command.equals("CYCLE_WINDOWS")) {
			// **** ... STILL IN PROGRESS ... **** //
		}
	}
	
}
