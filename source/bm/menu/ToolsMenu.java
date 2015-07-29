/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.tools.PlugIns;

/**
 *	Menu <b>Tools</b> for Bytecode Maker.
 *	
 *	<pre>
 *	Tools
 *		default
 *		...
 *		----------------
 *		group > plug-in names
 *		group > plug-in names
 *		...
 *		----------------
 *		not grouped (untitled)
 *		...
 *	</pre>
 *
 *	@version 1.4
 */

public class ToolsMenu extends Menu implements BMMenu {
	
	private Class[] plugins;
	
	public ToolsMenu() {
		super("Tools");
		this.plugins = (new PlugIns("PLUG", "ByTe")).getPlugIns();
		this.updateMenu();
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.startsWith("PLUGIN")) {
			int index = Integer.parseInt(command.substring(7));
			PlugIns.executePlugIn(this.plugins[index]);
		}
	}
	
	public void updateMenu() {
		if (plugins == null) {
			super.setEnabled(false); /* may be unsupported */
			super.add(new MenuItem("(Oops, no tools)"));
		} else {
			super.removeAll();
			
			int count = plugins.length;
			
			// get plug-in and group names
			String[][] plugInAndGroupNames = new String[2][count];
			for (int i = 0; i < count; i++) {
				plugInAndGroupNames[0][i] = PlugIns.getPlugInName(plugins[i]);
				plugInAndGroupNames[1][i] = PlugIns.getGroupName(plugins[i]);
			}
			
			// count plug-ins grouped as 'default' and 'untitled'
			int defaultPlugInsCount = 0;
			int notGroupedPlugInsCount = 0;
			for (int i = 0; i < count; i++) {
				if (plugInAndGroupNames[1][i].equals("default")) {
					defaultPlugInsCount++;
				} else if (plugInAndGroupNames[1][i].equals("untitled")) {
					notGroupedPlugInsCount++;
				}
			}
			
			MenuItem[] defaultPlugIns = new MenuItem[defaultPlugInsCount];
			for (int i = 0, pos = 0; i < count; i++) {
				if (plugInAndGroupNames[1][i].equals("default")) {
					defaultPlugIns[pos] = new MenuItem(plugInAndGroupNames[0][i]);
					defaultPlugIns[pos].setActionCommand("PLUGIN_" + i);
					defaultPlugIns[pos].addActionListener(this);
					pos++;
				}
			}
			
			MenuItem[] notGroupedPlugIns = new MenuItem[notGroupedPlugInsCount];
			for (int i = 0, pos = 0; i < count; i++) {
				if (plugInAndGroupNames[1][i].equals("untitled")) {
					notGroupedPlugIns[pos] = new MenuItem(plugInAndGroupNames[0][i]);
					notGroupedPlugIns[pos].setActionCommand("PLUGIN_" + i);
					notGroupedPlugIns[pos].addActionListener(this);
					pos++;
				}
			}
			
			if (defaultPlugInsCount > 0) {
				for (int i = 0; i < defaultPlugInsCount; i++) {
					super.add(defaultPlugIns[i]);
				}
				
				if (count > defaultPlugInsCount) {
					super.addSeparator();
				}
			}
			
			Object[] uniqueGroupNames = ArrayUtilities.unique(plugInAndGroupNames[1]);
			int groupCount = uniqueGroupNames.length;
			
			for (int i = 0; i < groupCount; i++) {
				String groupName = (String)uniqueGroupNames[i];
				if ((!groupName.equals("default")) && (!groupName.equals("untitled"))) {
					Menu menu = new Menu(groupName);
					
					for (int j = 0; j < count; j++) {
						if (plugInAndGroupNames[1][j].equals(groupName)) {
							MenuItem mi = new MenuItem(plugInAndGroupNames[0][j]);
							mi.setActionCommand("PLUGIN_" + j);
							mi.addActionListener(this);
							
							menu.add(mi);
						}
					}
					
					super.add(menu);
				}
			}
			
			if (notGroupedPlugInsCount > 0) {
				if (count > notGroupedPlugInsCount) {
					super.addSeparator();
				}
				
				for (int i = 0; i < notGroupedPlugInsCount; i++) {
					super.add(notGroupedPlugIns[i]);
				}
			}
		}
	}
	
}