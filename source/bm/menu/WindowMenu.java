// ===========================================================================
//	WindowMenu.java (part of douglas.mencken.bm.menu package)
// ===========================================================================

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.util.WindowTracker;
import douglas.mencken.tools.MemoryMonitor;
import douglas.mencken.tools.ThreadMonitor;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.BMEnvironment;
import douglas.mencken.bm.frames.*;

/**
 *	<code>WindowMenu</code>
 *
 *	@version	0.73d1
 *	@since		Bytecode Maker 0.6.0
 */

public final class WindowMenu extends Menu
implements BMMenu, WindowListener {
	
	private WindowTracker windowTracker = new WindowTracker();
	
	private MenuItem currentClass;
	private CheckboxMenuItem constantPoolWindow;
	private CheckboxMenuItem fieldsWindow;
	private CheckboxMenuItem methodsWindow;
	private CheckboxMenuItem attributesWindow;
	private CheckboxMenuItem classDump;
	private CheckboxMenuItem classSource;
	
	private MenuItem memoryMonitor;
	private MenuItem threadMonitor;
	
	private MenuItem cycleThroughWindows;
	
	// private MenuItem windows;
	
	public WindowMenu() {
		super("Window");
		
		super.add(new MenuItem("(no class)"));
		super.addSeparator();
		
		// show/hide xxx monitor menu items
		this.memoryMonitor = new MenuItem("Show Memory Monitor");
		this.memoryMonitor.setActionCommand("MEMORY_MONITOR");
		this.memoryMonitor.addActionListener(this);
		super.add(this.memoryMonitor);
		
		this.threadMonitor = new MenuItem("Show Thread Monitor");
		this.threadMonitor.setActionCommand("THREAD_MONITOR");
		this.threadMonitor.addActionListener(this);
		super.add(this.threadMonitor);
		
		super.addSeparator();
		
		this.cycleThroughWindows = new MenuItem(
			"Cycle Through Windows", new MenuShortcut(KeyEvent.VK_BACK_QUOTE)
		);
		this.cycleThroughWindows.setActionCommand("CYCLE_WINDOWS");
		this.cycleThroughWindows.addActionListener(this);
		super.add(this.cycleThroughWindows);
		
		this.updateMenu();
	}
	
	public void updateMenu() {
		super.remove(0);
		JavaClass current = BMEnvironment.getCurrentClass();
		if (current != null) {
			this.currentClass = new Menu(current.getClassName());
			this.currentClass.setEnabled(true);
			
			fillClassSubmenu((Menu)this.currentClass, this);
			this.updateClassSubmenu();
		} else {
			this.currentClass = new MenuItem("(no class)");
			this.currentClass.setEnabled(false);
		}
		super.insert(this.currentClass, 0);
		
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
		
		//if (no one window created) {
		this.cycleThroughWindows.setEnabled(false);
		//} else ...
	}
	
	private void updateClassSubmenu() {
		this.constantPoolWindow.setState(this.windowTracker.isVisible("pool window"));
		this.fieldsWindow.setState(this.windowTracker.isVisible("fields window"));
		this.methodsWindow.setState(this.windowTracker.isVisible("methods window"));
		this.attributesWindow.setState(this.windowTracker.isVisible("attributes window"));
		
		this.classDump.setState(this.windowTracker.isVisible("class dump window"));
		this.classSource.setState(this.windowTracker.isVisible("class source window"));
	}
	
	private void fillClassSubmenu(Menu submenu, ActionListener actionListener) {
		this.constantPoolWindow = new CheckboxMenuItem("Constant Pool");
		this.constantPoolWindow.setShortcut(new MenuShortcut(KeyEvent.VK_1));
		this.constantPoolWindow.setActionCommand("C_POOL_W");
		this.constantPoolWindow.addActionListener(actionListener);
		submenu.add(this.constantPoolWindow);
		
		this.fieldsWindow = new CheckboxMenuItem("Fields");
		this.fieldsWindow.setShortcut(new MenuShortcut(KeyEvent.VK_2));
		this.fieldsWindow.setActionCommand("FIELDS_W");
		this.fieldsWindow.addActionListener(actionListener);
		submenu.add(this.fieldsWindow);
		
		this.methodsWindow = new CheckboxMenuItem("Methods");
		this.methodsWindow.setShortcut(new MenuShortcut(KeyEvent.VK_3));
		this.methodsWindow.setActionCommand("METHODS_W");
		this.methodsWindow.addActionListener(actionListener);
		submenu.add(this.methodsWindow);
		
		this.attributesWindow = new CheckboxMenuItem("Attributes");
		this.attributesWindow.setActionCommand("ATTRS_W");
		this.attributesWindow.addActionListener(actionListener);
		submenu.add(this.attributesWindow);
		
		submenu.addSeparator();
		
		this.classDump = new CheckboxMenuItem("Class Dump");
		this.classDump.setShortcut(new MenuShortcut(KeyEvent.VK_D));
		this.classDump.setActionCommand("CLASS_DUMP");
		this.classDump.addActionListener(actionListener);
		submenu.add(this.classDump);
		
		this.classSource = new CheckboxMenuItem("Decompiled Source");
		this.classSource.setShortcut(new MenuShortcut(KeyEvent.VK_R));
		this.classSource.setActionCommand("CLASS_SOURCE");
		this.classSource.addActionListener(actionListener);
		submenu.add(this.classSource);
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
			// ...
		}
	}
	
}
