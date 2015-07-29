/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.tools;

import java.io.*;
import java.util.*;
import douglas.mencken.util.FileUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.exceptions.InvalidClassFormatError;

/**
 *	<code>PlugInClassLoader</code> is a class loader
 *	that is used by <code>PlugIns</code> class.
 *
 *	@version 1.23f1
 */

public class PlugInClassLoader extends ClassLoader {
	
	private PlugInClassLoader() { super(); }
	
	// Used to remember bytecodes, and local classes
	private Hashtable bytecodes = new Hashtable();
	
	/** <b>The</b> loader instance. */
	private static final PlugInClassLoader theLoader = createLoader();
	
	public static PlugInClassLoader getLoader() {
		return theLoader;
	}
	
	/**
	 *	Creates a new <code>PlugInClassLoader</code> object.
	 */
	public static PlugInClassLoader createLoader() {
		return new PlugInClassLoader();
	}
	
	/**
	 *	@param	file	for example, new File("folder1/folder2/xxx.class")
	 *	@return			fully qualified class name, 'null' in the case of IOException
	 */
	public static String extractClassName(File file) throws InvalidClassFormatError {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			
			// check magic number
			int cafebabe = in.readInt();
			if (cafebabe != 0xCAFEBABE) {
				throw new InvalidClassFormatError("Not a java class");
			}
			
			in.readUnsignedShort();	// minor version
			in.readUnsignedShort();	// major version
			
			// constant pool
			int constantCount = in.readUnsignedShort();
			Hashtable constantPool = new Hashtable(constantCount);
			
			for (int i = 1; i < constantCount; i++) {
				int tag = in.readUnsignedByte();
				switch (tag) {
					case /* UTF8 */ 1:
						int length = in.readUnsignedShort();
						byte[] chars = new byte[length];
						in.read(chars);
						
						constantPool.put(
							/* key */ String.valueOf(i),
							/* value */ StringUtilities.toJavaString(new String(chars))
						);
						break;
					
					case /* Unicode */ 2:
						throw new InvalidClassFormatError("Unicode constant (2)");
					
					case /* Integer */ 3:
						in.readInt();
						break;
					case /* Float */ 4:
						in.readFloat();
						break;
					case /* Long */ 5:
						in.readLong();
						i++;
						break;
					case /* Double */ 6:
						in.readDouble();
						i++;
						break;
					
					case /* Classref */ 7:
						constantPool.put(
							/* key */ String.valueOf(i),
							/* value */ String.valueOf(in.readUnsignedShort())
						);
						break;
					
					case /* Stringref */ 8:
						in.readUnsignedShort();
						break;
					
					case /* Fieldref */ 9:
					case /* Methodref */ 10:
					case /* InterfaceMethodref */ 11:
					case /* NameAndType */ 12:
						in.readUnsignedShort();
						in.readUnsignedShort();
						break;
					
					default:
						throw new InvalidClassFormatError("Unknown constant: " + tag);
				}
			}
			
			in.readUnsignedShort();	// access flags
			int className_Classref = in.readUnsignedShort();
			int superclassName_Classref = in.readUnsignedShort();
			
			String name = (String)constantPool.get(String.valueOf(className_Classref));
			name = ClassUtilities.toCommas((String)constantPool.get(name));
			
			return name;
		} catch (IOException ioe) {
			return null;
		}
	}
	
	/**
	 *	Load classes from files.
	 */
	public static Class[] loadFromFiles(File[] files) {
		int count = files.length;
		
		String[] names = new String[count];
		for (int i = 0; i < count; i++) {
			try {
				names[i] = extractClassName(files[i]);
			} catch (InvalidClassFormatError err) { names[i] = null; }
		}
		
		Vector cc = new Vector(count); // vector of loaded classes
		
		for (int i = 0; i < count; i++) {
			try {
				if (names[i] != null) {
					byte[] buf = FileUtilities.getByteArray(files[i].getPath());
					theLoader.setDefinition(names[i], buf);
				}
			} catch (IOException ioe) {}
		}
		
		for (int i = 0; i < count; i++) {
			try {
				if (names[i] != null) {
					cc.addElement(theLoader.loadClass(names[i], true));
				}
			} catch (ClassFormatError err) {
			} catch (ClassNotFoundException exc) { }
		}
		
		count = cc.size();
		if (count > 0) {
			Class[] classes = new Class[count];
			for (int i = 0; i < count; i++) {
				classes[i] = (Class)cc.elementAt(i);
			}
			
			return classes;
		}
		
		return null;
	}
	
	/**
	 *	Load a class from a file.
	 */
	// public static Class loadFromFile(String path) {
	//	return (loadFromFiles(new File[] { new File(path) }))[0];
	// }
	
	/**
	 *	Public version of <code>findLoadedClass()</code>.
	 */
	public Class findClass(String name) {
		return super.findLoadedClass(name);
	}
	
	
	/**
	 *	This is the main method for ClassLoaders.
	 */
	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		// already loaded?
		Class cl = super.findLoadedClass(name);
		if (cl == null) {
			try {
				Class kl = super.findSystemClass(name);
				return kl;
			} catch (ClassNotFoundException e) {}
			
			// This may be a forward reference to a class in the JAR...
			return this.applyDefinition(name, resolve);
		}
		
		if (cl == null) {
			throw new ClassNotFoundException(name);
		}
		if (resolve) {
			super.resolveClass(cl);
		}
		
		return cl;
	}
	
	/**
	 *	Set some bytecodes as a definition for a class.
	 *	Do not actually define the class until later.
	 */
	public void setDefinition(String name, byte[] buf) {
		bytecodes.put(name, buf);
	}
	
	/**
	 *	Define a class from the bytecodes that were collected early...
	 */
	public synchronized void applyDefinitions(Vector classList) {
		// Go through the bytecode arrays defining.
		// NOTE: Sometimes a forward reference forces a class
		// to get defined early; in that case, skip it!
		for (Enumeration k = classList.elements(); k.hasMoreElements(); ) {
			String classname = (String)k.nextElement();
			Class c = this.findLoadedClass(classname);
			if (c == null) {
				// Not yet defined, do so.
				this.applyDefinition(classname, true);
			}
		}
	}
	
	private Class applyDefinition(String name, boolean resolve) {
		byte[] buf = (byte[])this.bytecodes.get(name);
		
		if (buf == null) {
			return null;
		} else {
			Class c = super.defineClass(null, buf, 0, buf.length);
			if ((c != null) && resolve) {
				super.resolveClass(c);
			}
			
			return c;
		}
	}
	
}
