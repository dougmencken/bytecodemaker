// ===========================================================================
//	Unpackager.java (part of douglas.mencken.bm.engine package)
// ===========================================================================

package douglas.mencken.bm.engine;

import douglas.mencken.util.ClassUtilities;
import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;

/**
 *	<code>Unpackager</code>
 *
 *	@version 1.07f4
 */

public class Unpackager extends Object {
	
	protected String className;
	protected String superclassName;
	protected String[] packages;
	
	private static Unpackager currentUnpackager;
	
	public Unpackager(JavaClass clazz) {
		super();
		this.setClass(clazz);
		Unpackager.currentUnpackager = this;
	}
	
	public void setClass(JavaClass clazz) {
		this.packages = (clazz == null) ? null : clazz.obtainPackages();
		this.className = clazz.getClassName();
		this.superclassName = clazz.getSuperclassName();
	}
	
	public String unpackage(String fqName) {
		if (!BMPreferencesManager.getUseFullyQualifiedNames()) {
			return ClassUtilities.unpackage(fqName, packages);
		} else {
			return fqName;
		}
	}
	
	/**
	 *	Note:	if fully qualified names aren't preferred,
	 *			then the 'String className' parameter must
	 *			be unpackaged.
	 */
	public String castThis(String name) {
		String thisClassName = unpackage(this.className);
		String thisSuperclassName = unpackage(this.superclassName);
		
		if (name.equals(thisClassName)) {
			return "this";
		} else if (name.equals(thisSuperclassName)) {
			return "super";
		} else {
			return "((" + name + ")this)";
		}
	}
	
	/**
	 *	(static method)
	 */
	public static String toBooleanValue(String value) {
		if (value.equals("0")) {
			return "false";
		} else if (value.equals("1")) {
			return "true";
		} else {
			// variable name
			return value;
		}
	}
	
	public static Unpackager getCurrentUnpackager() {
		return currentUnpackager;
	}
	
}