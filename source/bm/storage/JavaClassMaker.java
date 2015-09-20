/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.lang.reflect.Modifier;

import douglas.mencken.util.ByteTransformer;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.ClassUtilities;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.bm.storage.constants.BMErrorStrings;

/**
 *	<code>JavaClassMaker</code>
 *
 *	@version	0.9
 *	@since		Bytecode Maker A.6
 */

public class JavaClassMaker extends Object {

	/**
	 *	This utility class cannot be instantiated.
	 */
	private JavaClassMaker() { super(); }
	
	/**
	 *	(static method)
	 */
	public static JavaClass makeJavaClassByDeclaration(String declaration) {
		String[] tokens = StringUtilities.tokenize(declaration);
		int tokenCount = tokens.length;
		
		int keyword_class_idx = -1;
		for (int i = 0; i < tokenCount; i++) {
			if (tokens[i].equals("class") || tokens[i].equals("interface")) {
				keyword_class_idx = i;
			}
		}
		
		if (keyword_class_idx == -1) {
			UsefulMessageDialogs.sayAboutError(1, BMErrorStrings.getErrorString(1));
			throw new IllegalArgumentException("Error #1: " + BMErrorStrings.getErrorString(1));
		}
		
		for (int i = 0; i < keyword_class_idx; i++) {
			for (int j = i+1; j < keyword_class_idx; j++) {
				if (tokens[i].equals(tokens[j])) {
					UsefulMessageDialogs.sayAboutError(3, BMErrorStrings.getErrorString(3));
					throw new IllegalArgumentException("Error #3: " + BMErrorStrings.getErrorString(3));
				}
			}
		}
		
		// class access
		int mod = 0;
		boolean isInterface = false;
		
		// less or equal ( <= ) doesn't skip the 'interface' keyword
		for (int i = 0; i <= keyword_class_idx; i++) {
			if (tokens[i].equals("public")) {
				mod = mod^Modifier.PUBLIC;
			} else if (tokens[i].equals("final")) {
				mod = mod^Modifier.FINAL;
			} else if (tokens[i].equals("abstract")) {
				mod = mod^Modifier.ABSTRACT;
			} else if (tokens[i].equals("synchronized")) {
				mod = mod^Modifier.SYNCHRONIZED;
			} else if (tokens[i].equals("class")) {
				if (isInterface) {
					UsefulMessageDialogs.sayAboutError(6, BMErrorStrings.getErrorString(6));
					throw new IllegalArgumentException("Error #6: " + BMErrorStrings.getErrorString(6));
				}
			} else if (tokens[i].equals("interface")) {
				isInterface = true;
				// ...
			} else {
				if (tokens[i].equals("private") ||
						tokens[i].equals("protected") ||
						tokens[i].equals("static") ||
						tokens[i].equals("volatile") ||
						tokens[i].equals("transient") ||
						tokens[i].equals("native") ||
						tokens[i].equals("strict")) {
					UsefulMessageDialogs.sayAboutError(2, BMErrorStrings.getErrorString(2));
					throw new IllegalArgumentException("Error #2: " + BMErrorStrings.getErrorString(2));
				} else {
					UsefulMessageDialogs.sayAboutError(4, BMErrorStrings.getErrorString(4));
					throw new IllegalArgumentException("Error #4: " + BMErrorStrings.getErrorString(4));
				}
			}
		}
		
		String className = tokens[keyword_class_idx+1];
		String superclassName = "";
		if (isInterface) {
			superclassName = "java.lang.Object";
		} else {
			if ((keyword_class_idx + 3) < tokenCount) {
				if (!isInterface && tokens[keyword_class_idx+2].equals("extends")) {
					superclassName = tokens[keyword_class_idx+3];
				} else {
					if (!tokens[keyword_class_idx+2].equals("implements")) {
						UsefulMessageDialogs.sayAboutError(5, BMErrorStrings.getErrorString(5));
						throw new IllegalArgumentException("Error #5: " + BMErrorStrings.getErrorString(5));
					}
				}
			}
		}
		
		// new JavaClass
		JavaClass clazz = JavaClassMaker.makeJavaClass(mod, className, superclassName);
		
		if (isInterface) {
			// set 'interface' modifier flag
			clazz.setInterface(true);
		}
		
		int implementsIndex = -1;
		for (int i = keyword_class_idx; i < tokenCount; i++) {
			if (tokens[i].equals("implements") ||
					(isInterface && (tokens[i].equals("extends")))) {
				implementsIndex = i;
				break;
			}
		}
		
		if (implementsIndex > 0) {
			int interfaceCount = tokenCount - implementsIndex - 1;
			String[] interfaces = new String[interfaceCount];
			System.arraycopy(tokens, implementsIndex+1, interfaces, 0, interfaceCount);
			clazz.setInterfaces(interfaces);
		}
		
		return clazz;
	}
	
	/**
	 *	(static method)
	 */
	public static JavaClass makeJavaClass(int accessMods, String name, String supername) {
		JavaConstantPool pool = new JavaConstantPool(1);
		
		// add class' name constant
		int nameNumber = pool.addBasicTypeConstant(
					/* 1: UTF8 */ JavaConstantPool.CONSTANT_UTF8,
					ClassUtilities.fromCommas(name) );
		int nameRefNumber = pool.addRefTypeConstant(
					/* 7: Classref */ JavaConstantPool.CONSTANT_Classref,
					nameNumber );
		
		int supernameNumber = 0;
		int supernameRefNumber = 0;
		
		// add superclass' name constant
		if ( (supername != null) && (supername.length() > 0) ) {
			supernameNumber = pool.addBasicTypeConstant(
					/* 1: UTF8 */ JavaConstantPool.CONSTANT_UTF8,
					ClassUtilities.fromCommas(supername) );
			supernameRefNumber = pool.addRefTypeConstant(
					/* 7: Classref */ 7,
					supernameNumber );
		}
		
		// construct new class
		JavaClass newJavaClass = new JavaClass(accessMods, nameRefNumber, supernameRefNumber);
		newJavaClass.setConstantPool(pool);
		
		return newJavaClass;
	}
	
	/**
	 *	Converts a lot of parameters into the pretty declaration.
	 *	(static method)
	 */
	public static String
	makeClassDeclaration(int accessMods, String name, String superName, String[] interfaceNames) {
		if ((name == null) || (superName == null)) {
			throw new IllegalArgumentException("class and superclass names cannot be null");
		}
		if (name.length() == 0) {
			throw new IllegalArgumentException("class name cannot be \"\"");
		}
		
		boolean isInterface = Modifier.isInterface(accessMods);
		if (isInterface && Modifier.isAbstract(accessMods)) {
			// 'abstract interface' -> 'interface'
			accessMods = accessMods^Modifier.ABSTRACT;
		}
		
		String accessString = Modifier.toString(accessMods);
		
		StringBuffer declaration = new StringBuffer();
		if (accessString.length() != 0) {
			declaration.append(accessString).append(' ');
		}
		
		if (isInterface) {
			declaration.append(name);
		} else {
			declaration.append("class ");
			declaration.append(name);
			if (superName.length() != 0) {
				declaration.append(" extends " + superName);
			}
		}
		
		if ((interfaceNames != null) && (interfaceNames.length > 0)) {
			declaration.append((interfaceNames.length > 1) ? '\n' : ' ');
			declaration.append((isInterface) ? "extends " : "implements ");
			declaration.append(StringUtilities.stringArrayToString(interfaceNames));
		}
		
		return declaration.toString();
	}
	
	/**
	 *	(static method)
	 */
	public static void addDefaultConstructor(JavaClass clazz) {
		if (!clazz.isInterface()) {
			JavaMethod _init_ = JavaMethodMaker.makeJavaMethod(
				clazz, /* method index */ 0, Modifier.PUBLIC, "<init>", "()V"
			);
			int superclassRef = clazz.getSuperclassReference();
			
			// [177] return
			byte[] code = { (byte)177 };
			
			// IT CAN'T BE CHANGED!!! IT IS RECALCULATED EACH TIME etc.
			// _init_.setMaxStack(0);
			// IT CAN'T BE CHANGED!!! IT IS RECALCULATED EACH TIME etc.
			// _init_.setMaxLocals(1);
			
			if (superclassRef != 0) {
				JavaConstantPool pool = clazz.getConstantPool();
				int nameref = pool.findUTF8ConstantByContents("<init>");
				int typeref = pool.findUTF8ConstantByContents("()V");
				int nameAndTypeRef = pool.findNameAndTypeConstantByReferences(nameref, typeref);
				
				int methodrefNumber = pool.addRefTypeConstant(
					/* Methodref */ 10, superclassRef, nameAndTypeRef
				);
				
				// [ 42] aload_0
				// [183] invokenonvirtual < Methodref (16-bit) >
				// [177] return
				byte[] nums = ByteTransformer.extractBytesFromShort(methodrefNumber);
				code = new byte[] { 42, (byte)183, nums[0], nums[1], (byte)177 };
				
				// IT CAN'T BE CHANGED!!! IT IS RECALCULATED EACH TIME etc.
				// _init_.setMaxStack(1);
			}
			
			_init_.setCode(code);
			clazz.addMethod(_init_);
		}
	}
	
}
