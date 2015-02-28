// ===========================================================================
// PlugIns.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.lang.reflect.*;
import java.io.*;
import java.util.Vector;

import com.apple.mrj.MRJOSType;
import com.apple.mrj.MRJFileUtils;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.exceptions.InvalidClassFormatError;

/**
 *	<code>PlugIns</code>
 *
 *	Example of use:
 *	<p>
 *	<hr><blockquote><pre>
 *	public class PlugInExecutor extends Object {
 *	
 *		private Class[] plugins;
 *		
 *		public PlugInExecutor() {
 *			super();
 *			this.plugins = (new PlugIns("PLUG", "ByTe")).getPlugIns();
 *			PlugIns.executePlugIn(this.plugins[0]);
 *		}
 *	
 *	}
 *	</pre></blockquote><hr>
 *
 *	@version 1.42f6
 */

public class PlugIns extends Object {
	
	protected Class[] plugins;
	
	public PlugIns() {
		this(new File(System.getProperty("user.dir"), "Plug-Ins"));
	}
	
	public PlugIns(File pluginsFolder) {
		this(pluginsFolder, "????", "????");
	}
	
	public PlugIns(String type, String creator) {
		this(new File(System.getProperty("user.dir"), "Plug-Ins"), type, creator);
	}
	
	public PlugIns(File pluginsFolder, String type, String creator) {
		super();
		
		if (pluginsFolder.canRead()) {
			Class[] allClasses = PlugInClassLoader.loadFromFiles(
				FileUtilities.getAllFiles(pluginsFolder)
			);
			this.plugins = extractPlugIns(allClasses, pluginsFolder, type, creator);
		}
	}
	
	public Class[] getPlugIns() {
		return this.plugins;
	}
	
	public static void executePlugIn(Class plugin) {
		try {
			Object p_in = plugin.newInstance();
			Method pluginMethod = plugin.getDeclaredMethod("plugin", null);
			pluginMethod.invoke(p_in, null);
		} catch (Exception e) {
			UsefulModalDialogs.doWarningDialog("Cannot execute plug-in: " + plugin.getName());
		}
	}
	
	/**
	 *	All plug-in must have 'public [void] plugin()' method.
	 */
	private static Class[] extractPlugIns(Class[] allClasses, File pluginsFolder,
											String type, String creator) {
		int count = allClasses.length;
		Vector ep = new Vector(count);
		
		for (int i = 0; i < count; i++) {
			try {
				Method pluginMethod = allClasses[i].getDeclaredMethod("plugin", null);
				if (!Modifier.isAbstract(pluginMethod.getModifiers())) {
					ep.addElement(allClasses[i]);
					
					String filename = ClassUtilities.getClassName(
						allClasses[i].getName()
					) + ".class";
					MRJFileUtils.setFileTypeAndCreator(
						FileUtilities.findFile(pluginsFolder, filename),
						new MRJOSType(type),
						new MRJOSType(creator)
					);
				}
			} catch (NoSuchMethodException ignored) {
			} catch (IOException ioe) {}
		}
		
		count = ep.size();
		if (count > 0) {
			Class[] p_ins = new Class[count];
			for (int i = 0; i < count; i++) {
				p_ins[i] = (Class)ep.elementAt(i);
			}
			
			return p_ins;
		}
		
		return null;
	}
	
	public static String getPlugInName(Class plugin) {
		// default name is the Class name
		String name = plugin.getName();
		
		// search for 'getPlugInName()' method
		Method _getPlugInName_ = null;
		try {
			_getPlugInName_ = plugin.getMethod("getPlugInName", null);
			if (Modifier.isAbstract(_getPlugInName_.getModifiers())) {
				_getPlugInName_ = null;
			}
		} catch (Exception exc) {}
		
		if (_getPlugInName_  != null) {
			try {
				Object pluginInstance = plugin.newInstance();
				name = (String)(_getPlugInName_.invoke(pluginInstance, null));
			} catch (Exception exc) {}
		}
		
		return name;
	}
	
	public static String getGroupName(Class plugin) {
		// default group name
		String groupName = "untitled";
		
		// search for 'getGroupName()' method
		Method _getGroupName_ = null;
		try {
			_getGroupName_ = plugin.getMethod("getGroupName", null);
			if (Modifier.isAbstract(_getGroupName_.getModifiers())) {
				_getGroupName_ = null;
			}
		} catch (Exception exc) {}
		
		if (_getGroupName_  != null) {
			try {
				Object pluginInstance = plugin.newInstance();
				groupName = (String)(_getGroupName_.invoke(pluginInstance, null));
			} catch (Exception exc) {}
		}
		
		return groupName;
	}
	
}
