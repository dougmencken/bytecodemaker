/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.plugins;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import douglas.mencken.util.*;
import douglas.mencken.io.SaveDialog;

import douglas.mencken.beans.TextView;
import douglas.mencken.beans.TextViewFrame;
import douglas.mencken.tools.TextViewPopupMenu;

import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.BMEnvironment;

/**
 *	<code>ImportRevealer</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	@version 1.07f1
 */

public class ImportRevealer extends DefaultPlugIn {
	
	public static String[] extractImports(JavaClass clazz) {
		if (clazz != null) {
			String[] classImports = clazz.obtainClassImports();
			
			if (classImports != null) {
				int len = classImports.length;
				if (len != 0) {
					Vector result = new Vector(len);
					for (int i = 0; i < len; i++) {
						result.addElement("import " + classImports[i]);
					}
					return ArrayUtilities.vectorToStringArray(result);
				}
			}
		}
		
		return null;
	}
	
	
	public ImportRevealer() { super(); }
	
	public String getPlugInName() {
		return "Import Revealer";
	}
	
	/**
	 *	Plug-Ins' main method.
	 */
	public void plugin() {
		if (BMEnvironment.getCurrentClass() != null) {
			this.init();
			this.show();
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	// -----------------------------------------------------------------------
	
	protected String[] imports = null;
	
	public void init() {
		this.imports = extractImports(BMEnvironment.getCurrentClass());
	}
	
	public void show() {
		TextView tp = new TextView();
		tp.setFont(new Font("Monaco", Font.PLAIN, 9));
		tp.append(get());
		
		JavaClass clazz = BMEnvironment.getCurrentClass();
		String title = "Import Revealer";
		if (clazz != null) {
			title += ": " + clazz.getClassName();
		} else {
			title += ": No Class";
		}
		
		TextViewFrame tpf = new TextViewFrame(title, tp, false);
		tpf.getTextView().add(new TextViewPopupMenu(
			tpf.getTextView(),
			ClassUtilities.getClassName(clazz.getClassName()) + ".imports",
			false
		));
		tpf.setBounds(25, 10, 400, 500);
		tpf.setVisible(true);
	}
	
	public String get() {
		if (this.imports != null) {
			StringBuffer buf = new StringBuffer();
			int count =imports.length;
			for (int i = 0; i < count; i++) {
				buf.append(imports[i]);
				buf.append(";\n");
			}
			
			return buf.toString();
		}
		
		return "";
	}
	
}
