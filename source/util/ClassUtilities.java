/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.lang.reflect.Modifier;

/**
 *	<code>ClassUtilities</code>
 *
 *	@version 1.27f1
 */

public final class ClassUtilities extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private ClassUtilities() { super(); }
	
	/**
	 *	[0]: type <br>
	 *	[1]: parameters ("" for field's dsc) <p>
	 *
	 *	(static method)
	 */
	public static String[] descriptionToTypeAndParameters(String dsc) {
		if ((dsc == null) || (dsc.length() == 0)) {
			return new String[] { "", "" };
		}
		
		String type = "";
		String parameters = "";
		
		if (dsc.indexOf(')') != -1) /* method's dsc */ {
			String[] parts = StringUtilities.splitByChar(dsc, ')');
			type = ClassUtilities.describeJVMType(parts[1]);
			
			String[] paramsArray = ClassUtilities.describeJVMParameterList(parts[0]);
			if ((paramsArray != null) && (paramsArray.length != 0)) {
				parameters = StringUtilities.stringArrayToString(paramsArray);
			}
		} else /* field's dsc */ {
			type = ClassUtilities.describeJVMType(dsc);
		}
		
		return new String[] { type, parameters };
	}
	
	/**
	 *	[0]: access string <br>
	 *	[1]: return type <br>
	 *	[2]: name <br>
	 *	[3]: parameters <br>
	 *	[4]: throws string <p>
	 *
	 *	(static method)
	 */
	public static String[] splitDeclaration(String declaration) {
		if ((declaration == null) || (declaration.length() == 0)) {
			return new String[] { "", "", "", "", "" };
		}
		
		String throwsString = "";
		int throwsPos = declaration.lastIndexOf(" throws ");
		if (throwsPos != -1) {
			throwsString = declaration.substring(throwsPos);
			declaration = declaration.substring(0, throwsPos);
		}
		
		String[] declaration_params = StringUtilities.splitByChar(declaration, '(');
		String parameters = "(" + declaration_params[1];
		declaration = declaration_params[0];
		
		String[] declaration_name = StringUtilities.splitByChar(
			declaration, ' ', /* from end */ true
		);
		declaration = declaration_name[0];
		String name = declaration_name[1];
		
		String access = "";
		String returnType = declaration;
		if (declaration.indexOf(" ") != -1) {
			String[] access_returnType = StringUtilities.splitByChar(
				declaration, ' ', /* from end */ true
			);
			access = access_returnType[0];
			returnType = access_returnType[1];
		}
		
		return new String[] {
			access, returnType, name, parameters, throwsString
		};
	}
	
	/**
	 *	(static method)
	 */
	public static byte[] createEmptyMethod(String returnType) {
		byte[] opcodes = new byte[0];
		
		if (returnType.equals("void")) {
			opcodes = new byte[] { /* return */ -79 };
		} else if (returnType.equals("int")) {
			opcodes = new byte[] { /* iconst_0 */ 3, /* ireturn */ -84 };
		} else if (returnType.equals("long")) {
			opcodes = new byte[] { /* lconst_0 */ 9, /* lreturn */ -83 };
		} else if (returnType.equals("float")) {
			opcodes = new byte[] { /* fconst_0 */ 11, /* freturn */ -82 };
		} else if (returnType.equals("double")) {
			opcodes = new byte[] { /* dconst_0 */ 14, /* dreturn */ -81 };
		} else {
			opcodes = new byte[] { /* aconst_null */ 1, /* areturn */ -80 };
		}
		
		return opcodes;
	}
	
	public static int getAccessFromString(String accessString) {
		int accessFlags = 0;
		
		if (accessString.indexOf("public") != -1) {
			accessFlags = accessFlags | Modifier.PUBLIC;
		}
		if (accessString.indexOf("protected") != -1) {
			accessFlags = accessFlags | Modifier.PROTECTED;
		}
		if (accessString.indexOf("private") != -1) {
			accessFlags = accessFlags | Modifier.PRIVATE;
		}
		
		if (accessString.indexOf("static") != -1) {
			accessFlags = accessFlags | Modifier.STATIC;
		}
		if (accessString.indexOf("final") != -1) {
			accessFlags = accessFlags | Modifier.FINAL;
		}
		
		// method's access flags
		if (accessString.indexOf("abstract") != -1) {
			accessFlags = accessFlags | Modifier.ABSTRACT;
		}
		if (accessString.indexOf("synchronized") != -1) {
			accessFlags = accessFlags | Modifier.SYNCHRONIZED;
		}
		if (accessString.indexOf("native") != -1) {
			accessFlags = accessFlags | Modifier.NATIVE;
		}
		
		// field's access flags
		if (accessString.indexOf("volatile") != -1) {
			accessFlags = accessFlags | Modifier.VOLATILE;
		}
		if (accessString.indexOf("transient") != -1) {
			accessFlags = accessFlags | Modifier.TRANSIENT;
		}
		
		// other access flags
		if (accessString.indexOf("synchronized") != -1) {
			accessFlags = accessFlags | Modifier.SYNCHRONIZED;
		}
		if (accessString.indexOf("interface") != -1) {
			accessFlags = accessFlags | Modifier.INTERFACE;
		}
		
		return accessFlags;
	}
	
	public static String toCommas(String s) {
		return s.replace('/', '.');
	}
	
	public static String fromCommas(String s) {
		return s.replace('.', '/');
	}
	
	public static boolean hasPackage(String what) {
		what = what.replace('/', '.');
		return (what.indexOf(".") != -1);
	}
	
	public static String getPackage(String what) {
		if (!hasPackage(what)) return "";
		return StringUtilities.getBeforeLast(what, '.');
	}
	
	public static String getClassName(String what) {
		if (!hasPackage(what)) return what;
		return StringUtilities.getAfter(what, '.');
	}
	
	public static String createVariableNameFromType(String type, int n) {
		String t = StringUtilities.getAfter(type, '.');
		if (t == null) t = type;
		
		char nameChar = t.charAt(0);
		nameChar = Character.toLowerCase(nameChar);
		if (n > 0) {
			return (nameChar + String.valueOf(n));
		} else {
			return String.valueOf(nameChar);
		}
	}
	
	public static boolean isJavaIdentifier(String identifier) {
		char[] chars = identifier.toCharArray();
		int len = chars.length;
		
		// assume "" is java identifier
		if (len == 0) return true;
		
		if (!Character.isJavaIdentifierStart(chars[0])) return false;
		for (int i = 1; i < len; i++) {
			if (!Character.isJavaIdentifierPart(chars[i]) && chars[i] != '.') {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 *	Converts the JVM type to the Java type.
	 *	Example: [[I -> int[][]
	 *	
	 *	@see	#parseType
	 */
	public static String describeJVMType(final String JVMtype) {
		int len = JVMtype.length();
		if (len == 0) return "";
		if (JVMtype.equals("jvm_ret_addr")) return JVMtype;
		
		StringBuffer buf = new StringBuffer();
		int st_pos = 0;
		while (JVMtype.charAt(st_pos) == '[') {
			st_pos++;
		}
			
		switch (JVMtype.charAt(st_pos)) {
			case 'B':
				buf.append("byte");
				break;
			case 'C':
				buf.append("char");
				break;
			case 'D':
				buf.append("double");
				break;
			case 'F':
				buf.append("float");
				break;
			case 'I':
				buf.append("int");
				break;
			case 'J':
				buf.append("long");
				break;
			case 'L':
				buf.append(toCommas(JVMtype.substring(st_pos+1, len-1)));
				break;
			case 'S':
				buf.append("short");
				break;
			case 'V':
				buf.append("void");
				break;
			case 'Z':
				buf.append("boolean");
				break;
			
			case '?':
			case '*':
				buf.append("? unknown ?");
				break;
		}
		
		for (int i = 0; i < st_pos; i++) {
			buf.append("[]");
		}
		
		return buf.toString();
	}
	
	/**
	 *	Converts the Java type to the JVM type.
	 *	Example: java.lang.Object -> Ljava/lang/Object;
	 *	
	 *	@see	#describeJVMType
	 */
	public static String parseType(String type) {
		int len = type.length();
		if (len == 0) return "";
		if (type.equals("jvm_ret_addr")) return type;
		
		StringBuffer buf = new StringBuffer();
		
		for (int i = len-1; i >= 0; i--) {
			if (type.charAt(i) == ']') {
				i--;
				buf.append('[');
			}
		}
		
		if (buf.length() > 0 /* if buf contains '[[..' */) {
			type = StringUtilities.getBefore(type, '[');
		}
		
		if (type.equals("byte")) {
			buf.append('B');
		} else if (type.equals("char")) {
			buf.append('C');
		} else if (type.equals("double")) {
			buf.append('D');
		} else if (type.equals("float")) {
			buf.append('F');
		} else if (type.equals("int")) {
			buf.append('I');
		} else if (type.equals("long")) {
			buf.append('J');
		} else if (type.equals("short")) {
			buf.append('S');
		} else if (type.equals("void")) {
			buf.append('V');
		} else if (type.equals("boolean")) {
			buf.append('Z');
		} else {
			buf.append('L');
			buf.append(fromCommas(type));
			buf.append(';');
		}
		
		return buf.toString();
	}
	
	/**
	 *	Converts a JVM list of parameters (like IZLjava.lang.Object;J)
	 *	to a Java list { int, boolean, java.lang.Object, long }.
	 *
	 *	@see	parseParameterList
	 */
	public static String[] describeJVMParameterList(String in) {
		if ((in == null) || (in.length() == 0) || in.equals("(")) {
			return null;
		}
		
		String list = StringUtilities.getAfter(in, '(');
		if (list == null) {
			list = in;
		}
		
		int paramCount = countJVMParameters(list);
		String[] out = new String[paramCount];
		int len = list.length();
		int index = 0;
		int numberOfDimensions = 0;
		
		for (int i = 0; i < len; i++) {
			switch (list.charAt(i)) {
				case '[':
					numberOfDimensions++;
					break;
					
				case 'B':
				case 'C':
				case 'D':
				case 'F':
				case 'I':
				case 'J':
				case 'S':
				case 'Z':
					StringBuffer type = new StringBuffer();
					type.append(describeJVMType(String.valueOf(list.charAt(i))));
					while (numberOfDimensions > 0) {
						type.append("[]");
						numberOfDimensions--;
					}
					
					out[index] = type.toString();
					index++;
					break;
				
				case 'L':
					StringBuffer buf = new StringBuffer();
					
					i++;
					while (list.charAt(i) != ';') {
						buf.append(list.charAt(i));
						i++;
					}
					
					while (numberOfDimensions > 0) {
						buf.append("[]");
						numberOfDimensions--;
					}
					
					out[index] = toCommas(buf.toString());
					index++;
					break;
			}
		}
		
		return out;
	}
	
	/**
	 *	Converts a Java list of parameters to a JVM list.
	 *
	 *	@see	describeJVMParameterList
	 */
	public static String parseParameterList(String[] in) {
		if (in == null) return null;
		if (in.length == 0) return "";
		
		StringBuffer buf = new StringBuffer();
		
		int count = in.length;
		for (int i = 0; i < count; i++) {
			buf.append(parseType(in[i]));
		}
		
		return buf.toString();
	}
	
	public static int countParameters(String in) {
		return countParameters(in, false);
	}
	
	/**
	 *	@param	checkDoubleSize			if true, adds 2 (not 1) for each "long" or
	 *									"double" parameter.
	 *	@see	countJVMParameters
	 */
	public static int countParameters(String in, boolean checkDoubleSize) {
		String s = StringUtilities.getAfter(in, '(');
		if (s == null) {
			s = in;
		}
		
		if (s.trim().length() <= 1) {
			return 0;
		}
		
		int paramCount = 0;
		
		if (checkDoubleSize) {
			String[] params = StringUtilities.tokenize(s);
			int len = params.length;
			
			for (int i = 0; i < len; i++) {
				paramCount++;
				if (params[i].equals("long") || params[i].equals("double")) {
					paramCount++;
				}
			}
		} else {
			paramCount = 1;
			int len = s.length();
			
			for (int i = 0; i < len; i++) {
				if (s.charAt(i) == ',') {
					paramCount++;
				}
			}
		}
		
		return paramCount;
	}
	
	/**
	 *	@see	countParameters
	 */
	public static int countJVMParameters(String in) {
		if ((in == null) || (in.length() == 0)) {
			return 0;
		}
		
		int paramCount = 0;
		int len = in.length();
		
		for (int i = 0; i < len; i++) {
			switch (in.charAt(i)) {
				case 'B':
				case 'C':
				case 'D':
				case 'F':
				case 'I':
				case 'J':
				case 'S':
				case 'Z':
					paramCount++;
					break;
				
				case 'L':
					while (in.charAt(i) != ';') i++;
					paramCount++;
					break;
			}
		}
		
		return paramCount;
	}
	
	/**
	 *	Note: his method assumes that the names has been already sorted
	 *	by ascending order ("java.lang.Object" before "java.lang.Exception").
	 */
	public static String[] extractPackagesFromNames(String[] names) {
		int len = names.length;
		String[] names2 = new String[len];
		
		boolean canPlace;
		String currPackageName;
		String currClassName;
		
		int pos = 0;
		for (int i = 0; i < len; i++) {
			canPlace = true;
			currPackageName = getPackage(names[i]);
			currClassName = getClassName(names[i]);
			
			for (int j = 0; j < i; j++) {
				if (currPackageName.equals(getPackage(names[j]))) {
					canPlace = false;
					break;
				}
			}
			if (canPlace) {
				// from '0' (not 'i+1')
				for (int j = 0; j < len; j++) {
					if (i != j) {
						if (currClassName.equals(getClassName(names[j]))) {
							canPlace = false;
							break;
						}
					}
				}
			}
			
			if (canPlace) {
				names2[pos] = currPackageName;
				pos++;
			}
		}
		
		String[] packages = new String[pos];
		System.arraycopy(names2, 0, packages, 0, pos);
		
		return packages;
	}
	
	/**
	 *	This method may be used for removing the package from the fully qualified
	 *	class name.
	 *
	 *	@param	fqName		the fully qualified class name (i.e. java.lang.Object)
	 *						to be unpackaged ('null' here will cause
	 *						<code>java.lang.NullPointerException</code>)
	 *	@param	packages	the list of packages (i.e java.util, java.util.zip, ...)
	 *
	 *	Note: the unpackaging only with 'java.lang' doesn't need the 'packages'
	 *		  parameter (leave it 'null').
	 *
	 *	@see	#getClassName
	 */
	public static String unpackage(String fqName, String[] packages) {
		String out = new String(fqName);
		
		if ((packages != null) && (out.indexOf(".") != -1)) {
			int len = packages.length;
			for (int i = len-1; i >= 0; i--) {
				if (out.startsWith(packages[i])) {
					out = out.substring(packages[i].length() + 1);
					
					if (out.indexOf(".") != -1) {
						// restore 'out'
						out = new String(fqName);
					} else {
						break;
					}
				}
			}
		}
		
		if (out.startsWith("java.lang.")) {
			out = out.substring(10);
		}
		
		return out;
	}
	
}