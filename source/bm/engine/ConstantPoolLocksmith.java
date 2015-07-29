/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.engine;

import douglas.mencken.tools.LogMonitor;
import douglas.mencken.bm.storage.JavaClass;
import douglas.mencken.bm.storage.JavaConstantPool;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;

/**
 *	It does the "black" work with constant pool.
 *
 *	@version	0.72f4
 *	@since		Bytecode Maker 0.6.0
 */

public class ConstantPoolLocksmith extends Object {
	
	private ConstantPoolLocksmith() { super(); }
	
	/**
	 *	Changes tag for the constant with specified number to 'newTag'.
	 */
	/*public void changeConstantTag(int number, int newTag) {
		int oldTag = this.tag;
		this.tag = newTag;
		
		if ((oldTag <= 6) && (newTag > 6)) {
			// this.ref1 = ??? 1;
			// this.ref2 = ??? 2;
			
			if (this.owner != null) {
				JavaConstantPoolElement temp = this.owner.getPoolManager().findConstantByTag(
					newTag, this.number
				);
				
				if (temp != null) {
					this.ref1 = temp.ref1;
					this.ref2 = temp.ref2;
				}
			}
			
			this.setContents(null);
		} else if ((newTag <= 6) && (oldTag > 6)) {
			this.ref1 = 0;
			this.ref2 = 0;
		}
	}*/
	
	/**
	 *	(static method)
	 */
	public static String dumpConstants(JavaConstantPool pool) {
		StringBuffer buf = new StringBuffer();
		Object[] constants = pool.getConstants();
		int count = constants.length;
		
		for (int i = 1; i < count; i++) {
			if (constants[i] != null) {
				buf.append(constants[i].toString()).append('\n');
			}
		}
		
		return buf.toString();
	}
	
	/**
	 *	(static method)
	 */
	public static String dumpPoolReferences(JavaClass clazz) {
		JavaConstantPool pool = clazz.getConstantPool();
		StringBuffer buf = new StringBuffer("class {\n");
		
		int ref = clazz.getClassReference();
		if (ref > 0) {
			// class name
			buf.append("   @");
			buf.append(ref);
			buf.append(" [class name]: ");
			buf.append(clazz.getClassName());
			buf.append('\n');
		}
		
		ref = clazz.getSuperclassReference();
		if (ref > 0) {
			// superclass name
			buf.append("   @");
			buf.append(ref);
			buf.append(" [superclass name]: ");
			buf.append(clazz.getSuperclassName());
			buf.append('\n');
		}
		
		// interfaces
		int[] interfaceRefs = clazz.getInterfaceReferences();
		int count = interfaceRefs.length;
		
		if (count > 0) {
			buf.append("\n   interfaces {\n");
			
			for (int i = 0; i < count; i++) {
				buf.append("      @");
				buf.append(interfaceRefs[i]);
				buf.append(" [interface").append(i).append("]: ");
				buf.append(clazz.getInterface(i));
				buf.append('\n');
			}
			
			buf.append("   }\n");
		}
		
		// fields
		/*JavaField[] fields = clazz.getFields();
		if ((fields != null) && (fields.length != 0)) {
			buf.append("\n   fields {\n");
			int count = fields.length;
			
			for (int i = 0; i < count; i++) {
				ref = pool.findUTFConstantByContents(fields[i].getFieldName()).getNumber();
				buf.append("      @").append(ref);
				ref = pool.findUTFConstantByContents(fields[i].getJVMSignature()).getNumber();
				buf.append("@").append(ref);
				buf.append(" [field").append(i).append("]: ");
				buf.append(fields[i].getFieldName());
				buf.append(' ');
				buf.append(fields[i].getJVMSignature());
				
				String constantValue = fields[i].getConstantValue();
				if (constantValue.length() != 0) {
					ref = pool.findConstantByContents("ConstantValue").getNumber();
					buf.append(" (@").append(ref).append(": ConstantValue");
					ref = pool.findConstantByContents(constantValue).getNumber();
					buf.append(", @").append(ref).append(": ").append(constantValue).append(')');
				}
				if (fields[i].isSynthetic()) {
					ref = pool.findUTFConstantByContents("Synthetic").getNumber();
					buf.append(" (@").append(ref).append(": Synthetic)");
				}
				
				buf.append('\n');
			}
			
			buf.append("   }\n");
		}*/
		
		// methods
		/*JavaMethod[] methods = clazz.getMethods();
		if ((methods != null) && (methods.length != 0)) {
			buf.append("\n   methods {\n");
			int count = methods.length;
			JavaMethod method;
			
			for (int i = 0; i < count; i++) {
				method = methods[i];
				ref = pool.findUTFConstantByContents(method.getMethodName()).getNumber();
				buf.append("      @").append(ref);
				ref = pool.findUTFConstantByContents(method.getJVMSignature()).getNumber();
				buf.append("@").append(ref);
				buf.append(" [method").append(i).append("]: ");
				buf.append(method.getMethodName());
				buf.append(' ');
				buf.append(method.getJVMSignature());
				
				buf.append(" {");
				if (method.throwsExceptions()) {
					String[] exceptions = method.getExceptions();
					int exception_count = exceptions.length;
					
					for (int j = 0; j < exception_count; j++) {
						ref = pool.findClassrefConstantByContents(exceptions[j]).getNumber();
						buf.append("\n         @").append(ref);
						buf.append(" [exception").append(j).append("]: ");
						buf.append(exceptions[j]);
					}
				}
				
				// attributes (method level)
				if (method.isSynthetic()) {
					ref = pool.findUTFConstantByContents("Synthetic").getNumber();
					buf.append("\n         (@").append(ref).append(": Synthetic)");
				}
				if (method.isDeprecated()) {
					ref = pool.findUTFConstantByContents("Deprecated").getNumber();
					buf.append("\n         (@").append(ref).append(": Deprecated)");
				}
				
				if (method.isSynthetic() || method.isDeprecated()) {
					buf.append('\n');
				}
				
				JavaBytecode[] bytecodes = method.getBytecodes();
				int codelength = bytecodes.length;
				if (codelength != 0) {
					buf.append("\n         code {");
					
					for (int j = 0; j < codelength; j++) {
						if ((bytecodes[j] != null) &&
								JavaBytecode.isPoolUserBytecode(bytecodes[j].getOpcode())) {
							buf.append("\n            (").append(bytecodes[j].getPCValue());
							buf.append(") ").append(bytecodes[j].getBytecode());
							buf.append(" @").append(bytecodes[j].getArg().substring(1));
						}
					}
				}
				
				TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
				int catchCount = catches.length;
				if (catchCount != 0) {
					buf.append('\n');
					
					for (int j = 0; j < catchCount; j++) {
						if (!catches[j].getCatchType().equals("finally")) {
							buf.append("\n            @");
							buf.append(catches[j].getCatchTypeReference());
							buf.append(" [catch").append(j).append("]: catch_type = ");
							buf.append(catches[j].getCatchType());
						}
					}
				}
				
				LocalVariable[] locals = method.getLocalVariables();
				int localVarCount = locals.length;
				if (localVarCount != 0) {
					buf.append('\n');
					
					for (int j = 0; j < localVarCount; j++) {
						if (locals[j] != null) {
							buf.append("\n            ");
							
							ref = pool.findUTFConstantByContents(
								locals[j].getVariableName()
							).getNumber();
							buf.append('@').append(ref);
							
							ref = pool.findUTFConstantByContents(
								locals[j].getJVMVariableType()
							).getNumber();
							buf.append('@').append(ref);
							
							buf.append(" [localvar").append(j).append("]: ");
							buf.append(locals[j].toString(false));
						}
					}
				}
				
				if (codelength != 0) {
					buf.append("\n         }");
				}
				
				buf.append("\n      }");
				buf.append('\n');
				if ((i+1) < count) buf.append('\n');
			}
			
			buf.append("   }\n");
		}
		
		// attributes (class level)
		String sourceName = clazz.getSourceName();
		String sourcePath = clazz.getSourcePath();
		boolean hasClassLevelAttrs = false;
		if ((sourceName.length() != 0) || (sourcePath.length() != 0)) {
			hasClassLevelAttrs = true;
		}
		
		if (hasClassLevelAttrs) {
			if (sourceName.length() != 0) {
				buf.append("\n   (@");
				ref = pool.findUTFConstantByContents(sourceName).getNumber();
				buf.append(ref);
				buf.append(" [source name]: ");
				buf.append(sourceName);
				buf.append(')');
			}
			if (sourcePath.length() != 0) {
				buf.append("\n   (@");
				ref = pool.findUTFConstantByContents(sourcePath).getNumber();
				buf.append(ref);
				buf.append(" [source path]: ");
				buf.append(sourcePath);
				buf.append(')');
			}
			
			buf.append('\n');
		}*/
		
		buf.append("}\n");
		
		return buf.toString();
	}
	
}