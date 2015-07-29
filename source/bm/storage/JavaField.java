/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.lang.reflect.Modifier;
import java.io.*;

import douglas.mencken.util.ClassUtilities;

/**
 *	<code>JavaField</code>
 *
 *	@version 2.0d0
 */

public class JavaField extends Object
implements JavaClassIndexedMember, Externalizable {
	
	/**
	 *	An access modifiers for this field.
	 */
	private int accessModifiers;
	
	/**
	 *	Number in constant pool of UTF8 constant
	 *	containing name of the field.
	 */
	private int fieldNameRef;
	
	/**
	 *	Number in constant pool of UTF8 constant
	 *	containing field's signature.
	 */
	private int jvmSignatureRef;
	
	protected JavaClass ownerClass = null;
	protected int fieldIndex = -1;
	
	/**
	 *	Writes java field to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// ...
	}
	
	/**
	 *	Reads java field from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// ...
	}
	
	public JavaClass getOwnerClass() {
		return this.ownerClass;
	}
	
	public void setOwnerClass(JavaClass owner) {
		// ...
		this.ownerClass = owner;
	}
	
	public int getIndex() {
		return this.fieldIndex;
	}
	
	public void setIndex(int index) {
		// ...
	}
	
	public void setOwnerClass(JavaClass owner, int index) {
		// ...
		this.ownerClass = owner;
		this.fieldIndex = index;
	}
	
	// ...
	
	public int getAccessModifiers() {
		return this.accessModifiers;
	}
	
	public void setAccessModifiers(int newAccess) {
		this.accessModifiers = newAccess;
	}
	
	public boolean isStatic() {
		return Modifier.isStatic(this.accessModifiers);
	}
	
	public boolean isFinal() {
		return Modifier.isFinal(this.accessModifiers);
	}
	
	/**
	 *	@see	#getFieldName
	 */
	public int getFieldNameReference() {
		return this.fieldNameRef;
	}
	
	/**
	 *	@see	#getJVMSignature
	 */
	public int getJVMSignatureReference() {
		return this.jvmSignatureRef;
	}
	
	/**
	 *	@return		field's name.
	 */
	public String getFieldName() {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		
		if (pool != null) {
			return pool.getConstantContents(this.fieldNameRef);
		} else {
			StringBuffer buf = new StringBuffer("* unknown (@");
			buf.append(this.fieldNameRef).append(") *");
			return buf.toString();
		}
	}
	
	public void setFieldName(String newName) {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		if (pool != null) {
			// JavaConstantPoolElement name = pool.getConstantByRef(this.nameRef);
			// name.setContents(newName);
		}
	}
	
	/**
	 *	@return		field's signature.
	 *	@see		#getFieldType
	 */
	public String getJVMSignature() {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		
		if (pool != null) {
			return pool.getConstantContents(this.jvmSignatureRef);
		} else {
			StringBuffer buf = new StringBuffer("* unknown (@");
			buf.append(this.jvmSignatureRef).append(") *");
			return buf.toString();
		}
	}
	
	public void setJVMSignature(String newSignature) {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		if (pool != null) {
			// JavaConstantPoolElement jvmSignature = pool.getConstantByRef(this.jvmSignatureRef);
			// jvmSignature.setContents(newJVMType);
		}
	}
	
	/**
	 *	@return		field's type.
	 *	@see		#getJVMSignature
	 */
	public String getFieldType() {
		return ClassUtilities.describeJVMType(this.getJVMSignature());
	}
	
	public void setFieldType(String newType) {
		this.setJVMSignature(ClassUtilities.parseType(newType));
	}
	
	// ...
	
}
