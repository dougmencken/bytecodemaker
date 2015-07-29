/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.storage;

import java.awt.Toolkit;
import java.io.*;

import douglas.mencken.util.StringUtilities;
import douglas.mencken.exceptions.InvalidClassFormatError;

/**
 *	<code>JavaConstantPoolElement</code>
 *	(local to package)
 *
 *	@version 2.06f3
 */

class JavaConstantPoolElement extends Object implements Externalizable {
	
	protected transient int number;
	protected int tag;
	
	/**
	 *	For constants of basic types (tag <= 6).
	 */
	protected String contents;
	
	/**
	 *	For constants of reference types (tag >= 7).
	 */
	protected int ref1;
	
	/**
	 *	For constants of reference types (tag >= 9, two references).
	 */
	protected int ref2;
	
	/**
	 *	Full (but <code>protected</code>) constructor form.
	 */
	protected JavaConstantPoolElement(int tag, String contents, int ref1, int ref2) {
		this.number = 0;
		this.tag = tag;
		
		this.setContents(contents);
		this.ref1 = ref1;
		this.ref2 = ref2;
	}
	
	/**
	 *	Initializes a new <code>JavaConstantPoolElement</code> object
	 *	with the specified contents.
	 */
	JavaConstantPoolElement(int tag, String contents) {
		this(tag, contents, /* ref1 */ 0, /* ref2 */ 0);
	}
	
	/**
	 *	Initializes a new <code>JavaConstantPoolElement</code> object
	 *	with one reference.
	 */
	JavaConstantPoolElement(int tag, int ref) {
		this(tag, /* contents */ "", ref, /* ref2 */ 0);
	}
	
	/**
	 *	Initializes a new <code>JavaConstantPoolElement</code> object
	 *	with two references.
	 */
	JavaConstantPoolElement(int tag, int ref1, int ref2) {
		this(tag, /* contents */ "", ref1, ref2);
	}
	
	/**
	 *	Default (null) constructor form.
	 */
	JavaConstantPoolElement() {
		this(
			/* tag */ JavaConstantPool.CONSTANT_UTF8, /* contents */ "",
			/* ref1 */ 0, /* ref2 */ 0
		);
	}
	
	/**
	 *	Writes constant to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		int tag = this.getTag();
		out.write(tag);
		
		switch (tag) {
			case JavaConstantPool.CONSTANT_UTF8:
				try {
					out.writeUTF(this.getContents());
				} catch (UTFDataFormatException e) {}
				break;
			case JavaConstantPool.CONSTANT_Integer:
				try {
					out.writeInt(Integer.parseInt(this.getContents()));
				} catch (NumberFormatException e) {}
				break;
			case JavaConstantPool.CONSTANT_Float:
				try {
					out.writeFloat((Float.valueOf(this.getContents())).floatValue());
				} catch (NumberFormatException e) {}
				break;
			case JavaConstantPool.CONSTANT_Long:
				try {
					out.writeLong((Long.valueOf(this.getContents())).longValue());
				} catch (NumberFormatException e) {}
				break;
			case JavaConstantPool.CONSTANT_Double:
				try {
					out.writeDouble((Double.valueOf(this.getContents())).doubleValue());
				} catch (NumberFormatException e) {}
				break;
			
			case JavaConstantPool.CONSTANT_Classref:
			case JavaConstantPool.CONSTANT_Stringref:
				out.writeShort(this.getReference());
				break;
			
			case JavaConstantPool.CONSTANT_Fieldref:
			case JavaConstantPool.CONSTANT_Methodref:
			case JavaConstantPool.CONSTANT_InterfaceMethodref:
			case JavaConstantPool.CONSTANT_NameAndType:
				out.writeShort(this.getReference());
				out.writeShort(this.getReference2());
				break;
			
			default:
				throw new InternalError("Unknown tag: " + tag);
		}
	}
	
	/**
	 *	Reads constant from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.tag = in.readUnsignedByte();
		
		switch (this.tag) {
			case /* UTF8 */ 1:
			{
				int length = in.readUnsignedShort();
				byte[] chars = new byte[length];
				in.read(chars);
				
				this.setContents(StringUtilities.toJavaString(new String(chars)));
				break;
			}
			
			case /* Unicode */ 2:
				throw new InvalidClassFormatError("Unicode constant (2)");
			
			case /* Integer */ 3:
			{
				String intValue = Integer.toString(in.readInt());
				this.setContents(intValue);
				break;
			}
			
			case /* Float */ 4:
			{
				String floatValue = Float.toString(in.readFloat());
				this.setContents(floatValue);
				break;
			}
			
			case /* Long */ 5:
			{
				String longValue = Long.toString(in.readLong());
				this.setContents(longValue);
				break;
			}
			
			case /* Double */ 6:
			{
				String doubleValue = Double.toString(in.readDouble());
				this.setContents(doubleValue);
				break;
			}
			
			case /* Classref */ 7:
			case /* Stringref */ 8:
			{
				this.ref1 = in.readUnsignedShort();
				this.ref2 = 0;
				break;
			}
			
			case /* Fieldref */ 9:
			case /* Methodref */ 10:
			case /* InterfaceMethodref */ 11:
			case /* NameAndType */ 12:
			{
				this.ref1 = in.readUnsignedShort();
				this.ref2 = in.readUnsignedShort();
				break;
			}
			
			default:
				throw new InvalidClassFormatError("Unknown tag: " + this.tag);
		}
	}
	
	/**
	 *	(5) CONSTANT_Long, (6) CONSTANT_Double are stored
	 *	in <b>TWO</b> adjoining constant pool cells (64-bit).
	 */
	boolean isDoubleSize() {
		return ((this.tag == /* Long */ 5) || (this.tag == /* Double */ 6));
	}
	
	/**
	 *	Constant of basic types doesn't contain references to
	 *	another constants.
	 */
	boolean isBasicType() {
		return (this.tag <= 6);
	}
	
	/**
	 *	@return		the number of this constant in constant pool.
	 */
	int getNumber() {
		return this.number;
	}
	
	/**
	 *	Sets the number of this constant in constant pool.
	 */
	void setNumber(int newNumber) {
		this.number = newNumber;
	}
	
	/**
	 *	@return		the tag of this constant.
	 */
	int getTag() {
		return this.tag;
	}
	
	String getContents() {
		return this.contents;
	}
	
	void setContents(String newContents) {
		if ((newContents == null) || (newContents.length() == 0)) {
			this.contents = "";
		} else {
			if (this.isBasicType()) {
				this.contents = newContents;
			}
		}
	}
	
	int getReference() { return ref1; }
	void setReference(int ref1) { this.ref1 = ref1; }
	int getReference2() { return ref2; }
	void setReference2(int ref2) { this.ref2 = ref2; }
	
	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof JavaConstantPoolElement)) {
			JavaConstantPoolElement constant = (JavaConstantPoolElement)obj;
			if (constant.tag == this.tag) {
				if ((this.tag >= 1 /* UTF8 */) && (this.tag <= 6 /* Double */)) {
					if (constant.getContents().equals(this.getContents())) {
						return true;
					}
				} else {
					if ((constant.ref1 == this.ref1) && (constant.ref2 == this.ref2)) {
						return true;
					}
				}
				
			}
		}
		
		return false;
	}
	
	public String toString() {
		return this.toString(true);
	}
	
	/**
	 *	Note:	contents of basic type constants are always appended.
	 */
	public String toString(boolean appendContents) {
		StringBuffer s = new StringBuffer("#");
		
		if (this.number == 0) {
			s.append("[no number]");
		} else {
			if (!this.isDoubleSize()) {
				s.append(this.number);
			} else {
				s.append(this.number);
				s.append("#");
				s.append(this.number + 1);
			}
		}
		
		s.append(" (");
		if ((this.tag >= 1) && (this.tag <= 12)) {
			s.append(JavaConstantPool.CONSTANT_TAGS[this.tag]);
			if (this.ref1 != 0) {
				s.append(", @");
				s.append(this.ref1);
			}
			if (this.ref2 != 0) {
				s.append('@');
				s.append(this.ref2);
			}
			s.append(')');
		} else {
			s.append("* unknown tag *");
		}
		
		if (appendContents || (this.isBasicType())) {
			String contents = this.getContents();
			if ((contents.length() != 0) ) {
				s.append(": " + contents);
			}
		}
		
		return s.toString();
	}
	
}
