// ===========================================================================
//	JavaConstantPool.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.io.*;

import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.ClassUtilities;

import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.exceptions.InvalidClassFormatError;

import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.engine.ConstantPoolLocksmith;

/**
 *	<code>JavaConstantPool</code><br>
 *
 *	Notes:		1)	constant number 0 is reserved,
 *			2)	Fieldref, Methodref, InterfaceMethodref are used
 *				in bytecode stream only.
 *
 *	@version 2.04f1
 */

public class JavaConstantPool extends Object
implements JavaClassMember, Externalizable {
	
	public static final int CONSTANT_UTF8 = 1;
	public static final int CONSTANT_Integer = 3;
	public static final int CONSTANT_Float = 4;
	public static final int CONSTANT_Long = 5;
	public static final int CONSTANT_Double = 6;
	public static final int CONSTANT_Classref = 7;
	public static final int CONSTANT_Stringref = 8;
	public static final int CONSTANT_Fieldref = 9;
	public static final int CONSTANT_Methodref = 10;
	public static final int CONSTANT_InterfaceMethodref = 11;
	public static final int CONSTANT_NameAndType = 12;
	
	public static final String[] CONSTANT_TAGS = {
		null,
		"UTF8",
		"* Unicode *",
		"Integer",
		"Float",
		"Long",
		"Double",
		"Classref",
		"Stringref",
		"Fieldref",
		"Methodref",
		"InterfaceMethodref",
		"NameAndType"
	};
	
	protected JavaClass ownerClass = null;
	
	/**
	 *	Array of constants. Cannot be null.
	 */
	protected JavaConstantPoolElement[] constantPoolElements;
	
	/**
	 *	Numbers in constant pool of UTF8 constants containing
	 *	attribute names.
	 */
	private int syntheticUTF8ConstantNumber;
	private int deprecatedUTF8ConstantNumber;
	private int constantValueUTF8ConstantNumber;
	private int exceptionsUTF8ConstantNumber;
	private int codeUTF8ConstantNumber;
	private int localVariableTableUTF8ConstantNumber;
	private int lineNumberTableUTF8ConstantNumber;
	
	public int getSyntheticUTF8ConstantNumber() {
		return this.syntheticUTF8ConstantNumber;
	}
	
	public int getDeprecatedUTF8ConstantNumber() {
		return this.deprecatedUTF8ConstantNumber;
	}
	
	public int getConstantValueUTF8ConstantNumber() {
		return this.constantValueUTF8ConstantNumber;
	}
	
	public int getExceptionsUTF8ConstantNumber() {
		return this.exceptionsUTF8ConstantNumber;
	}
	
	public int getCodeUTF8ConstantNumber() {
		return this.codeUTF8ConstantNumber;
	}
	
	public int getLocalVariableTableUTF8ConstantNumber() {
		return this.localVariableTableUTF8ConstantNumber;
	}
	
	public int getLineNumberTableUTF8ConstantNumber() {
		return this.lineNumberTableUTF8ConstantNumber;
	}
	
	/**
	 *	Updates numbers in constant pool of UTF8 constants containing
	 *	attribute names.
	 */
	private void updateUTF8ConstantNumbers() {
		this.syntheticUTF8ConstantNumber = 0;
		this.deprecatedUTF8ConstantNumber = 0;
		this.constantValueUTF8ConstantNumber = 0;
		this.exceptionsUTF8ConstantNumber = 0;
		this.codeUTF8ConstantNumber = 0;
		this.localVariableTableUTF8ConstantNumber = 0;
		this.lineNumberTableUTF8ConstantNumber = 0;
		
		int count = this.constantPoolElements.length;
		if (count > 1) {
			for (int i = 1; i < count; i++) {
				JavaConstantPoolElement constant = this.constantPoolElements[i];
				if ((constant != null) && (constant.getTag() == /* UTF8 */ 1)) {
					if (constant.getContents().equals("Synthetic")) {
						this.syntheticUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("Deprecated")) {
						this.deprecatedUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("ConstantValue")) {
						this.constantValueUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("Exceptions")) {
						this.exceptionsUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("Code")) {
						this.codeUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("LocalVariableTable")) {
						this.localVariableTableUTF8ConstantNumber = i;
					} else if (constant.getContents().equals("LineNumberTable")) {
						this.lineNumberTableUTF8ConstantNumber = i;
					}
				}
			}
		}
	}
	
	/**
	 *	Constructs a new <code>JavaConstantPool</code> object
	 *	with a specified constant pool elements.
	 */
	protected JavaConstantPool(JavaConstantPoolElement[] constants, JavaClass owner) {
		if ((constants == null) || (constants.length == 0)) {
			this.constantPoolElements = new JavaConstantPoolElement[1];
		} else {
			this.constantPoolElements = constants;
		}
		
		this.updateUTF8ConstantNumbers();
		this.setOwnerClass(owner);
		
		this.constantPoolElements[0] = null;
	}
	
	/**
	 *	Constructs a new <code>JavaConstantPool</code> object
	 *	with a specified constant pool elements and without an owner.
	 */
	protected JavaConstantPool(JavaConstantPoolElement[] constants) {
		this(constants, null);
	}
	
	/**
	 *	Constructs a new <code>JavaConstantPool</code> object
	 *	with a specified number of constants.
	 */
	public JavaConstantPool(int constantCount, JavaClass owner) {
		this(new JavaConstantPoolElement[constantCount], owner);
	}
	
	/**
	 *	Constructs a new <code>JavaConstantPool</code> object
	 *	with a specified number of constants and without an owner.
	 */
	public JavaConstantPool(int constantCount) {
		this(new JavaConstantPoolElement[constantCount], null);
	}
	
	/**
	 *	The default constructor.
	 */
	public JavaConstantPool() {
		this(1, null);
	}
	
	/**
	 *	(public)
	 */
	public Object[] getConstants() {
		return this.constantPoolElements;
	}
	
	/**
	 *	(protected)
	 */
	protected void setConstants(JavaConstantPoolElement[] constants) {
		this.constantPoolElements =
					(constants != null) ? constants : new JavaConstantPoolElement[0];
		this.updateUTF8ConstantNumbers();
		this.renumberConstants();
		
		if (this.constantPoolElements.length > 0) {
			this.constantPoolElements[0] = null;
		}
	}
	
	/**
	 *	(public)
	 */
	public void setConstants(Object[] objects) {
		int count = objects.length;
		JavaConstantPoolElement[] constants = new JavaConstantPoolElement[count];
		
		for (int i = 0; i < count; i++) {
			constants[i] = (objects[i] == null) ?
								null : (JavaConstantPoolElement)objects[i];
		}
		
		this.setConstants(constants);
	}
	
	public int getConstantCount() {
		return this.constantPoolElements.length - 1;
	}
	
	/**
	 *	Writes all constants to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		int count = this.constantPoolElements.length;
		out.writeShort(count);
		
		for (int i = 1; i < count; i++) {
			if (this.constantPoolElements[i] != null) {
				this.constantPoolElements[i].writeExternal(out);
			}
		}
	}
	
    /**
	 *	Reads constants from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int count = in.readUnsignedShort();
		this.constantPoolElements = new JavaConstantPoolElement[count];
		
		for (int i = 1; i < count; i++) {
			this.constantPoolElements[i] = new JavaConstantPoolElement();
			this.constantPoolElements[i].readExternal(in);
			
			if (this.constantPoolElements[i].isDoubleSize()) {
				i++;
			}
		}
	}
	
	public JavaClass getOwnerClass() {
		return this.ownerClass;
	}
	
	/**
	 *	Sets the owner class for this constant pool.
	 *	Also renumbers the constants.
	 */
	public void setOwnerClass(JavaClass owner) {
		if ((owner != null) && (owner != this.ownerClass)) {
			this.ownerClass = owner;
		}
		
		this.renumberConstants();
	}
	
	/**
	 *	@return		source-like contents of the constant.
	 */
	public String getConstantContents(int number) {
		JavaConstantPoolElement constant = this.constantPoolElements[number];
		int tag = constant.getTag();
		
		switch (tag) {
			case CONSTANT_UTF8:
				return constant.getContents();
			case CONSTANT_Integer:
				return constant.getContents();
			case CONSTANT_Float:
				return constant.getContents() + 'f';
			case CONSTANT_Long:
				return constant.getContents() + 'L';
			case CONSTANT_Double:
				return constant.getContents() + 'd';
			
			case CONSTANT_Classref:
				return ClassUtilities.toCommas(this.resolveConstant(number)[0]);
			case CONSTANT_Stringref:
				return '\"' + this.resolveConstant(number)[0] + '\"';
			
			case CONSTANT_Fieldref:
			{
				String[] field = this.resolveConstant(number);
				return	/* type */ field[2] + ' ' +
						/* class name */ ClassUtilities.toCommas(field[0]) + '.' +
						/* name */ field[1];
			}
			case CONSTANT_Methodref:
			case CONSTANT_InterfaceMethodref:
			{
				String[] method = this.resolveConstant(number);
				String[] type_params = ClassUtilities.descriptionToTypeAndParameters(method[2]);
				
				return	/* type */ type_params[0] + ' ' +
						/* class name */ ClassUtilities.toCommas(method[0]) + '.' +
						/* name */ method[1] + '(' + /* params */ type_params[1] + ')';
			}
			
			case CONSTANT_NameAndType:
			{
				String[] name_dsc = this.resolveConstant(number);
				
				if (name_dsc[1].indexOf(')') != -1) /* method's dsc */ {
					String[] type_params = ClassUtilities.descriptionToTypeAndParameters(name_dsc[1]);
					return type_params[0] + ' ' + name_dsc[0] + '(' + type_params[1] + ')';
				} else /* field's dsc */ {
					return name_dsc[1] + ' ' + name_dsc[0];
				}
			}
			
			default:
				return "unknown constant, tag = " + tag;
		}
	}
	
	/**
	 *	Changes contents of the constant.
	 */
	public void setConstantContents(int number, String contents) {
		JavaConstantPoolElement constant = this.constantPoolElements[number];
		
		if (constant.isBasicType()) {
			constant.setContents(contents);
		} else {
			UsefulMessageDialogs.doErrorDialog(
				"Attempt to change contents for '" +
				JavaConstantPool.CONSTANT_TAGS[constant.getTag()] + "'"
			);
		}
	}
	
	public int addBasicTypeConstant(int tag, String contents) {
		if ((tag >= 1) && (tag <= 6)) {
			JavaConstantPoolElement newConstant = new JavaConstantPoolElement(tag, contents);
			return this.addConstant(newConstant);
		} else {
			throw new IllegalArgumentException("invalid tag");
		}
	}
	
	public int addRefTypeConstant(int tag, int ref) {
		if ((tag == 7) || (tag == 8)) {
			JavaConstantPoolElement newConstant = new JavaConstantPoolElement(tag, ref);
			try {
				this.checkReferences(newConstant);
			} catch (Exception exc) {
				throw new IllegalArgumentException(exc.getMessage());
			}
			
			return this.addConstant(newConstant);
		} else {
			throw new IllegalArgumentException("invalid tag");
		}
	}
	
	public int addRefTypeConstant(int tag, int ref1, int ref2) {
		if ((tag >= 9) && (tag <= 12)) {
			JavaConstantPoolElement newConstant = new JavaConstantPoolElement(tag, ref1, ref2);
			try {
				this.checkReferences(newConstant);
			} catch (Exception exc) {
				throw new IllegalArgumentException(exc.getMessage());
			}
			
			return this.addConstant(newConstant);
		} else {
			throw new IllegalArgumentException("invalid tag");
		}
	}
	
	/**
	 *	@return		the number of constant in new pool.
	 *
	 *	(protected)
	 */
	protected int addConstant(JavaConstantPoolElement newConstant) {
		int count = this.constantPoolElements.length;
		int equalConstantNumber = 0;
		
		for (int i = 1; i < count; i++) {
			if (this.constantPoolElements[i] != null) {
				if (this.constantPoolElements[i].equals(newConstant)) {
					equalConstantNumber = i;
					break;
				}
			}
		}
		
		if (equalConstantNumber == 0) {
			JavaConstantPoolElement[] newPool;
			
			if (newConstant.isDoubleSize()) {
				newPool = new JavaConstantPoolElement[count+2];
				System.arraycopy(this.constantPoolElements, 0, newPool, 0, count);
				
				newConstant.setNumber(count);
				newPool[count] = newConstant;
				newPool[count+1] = null;
			} else {
				newPool = new JavaConstantPoolElement[count+1];
				System.arraycopy(this.constantPoolElements, 0, newPool, 0, count);
				
				newConstant.setNumber(count);
				newPool[count] = newConstant;
			}
			
			this.constantPoolElements = newPool;
			this.updateUTF8ConstantNumbers();
			
			return count;
		} else {
			return equalConstantNumber;
		}
	}
	
	public void removeConstant(int number) {
		if (this.constantPoolElements.length > 0) {
			Object[] poolObj = ArrayUtilities.removeElement(this.constantPoolElements, number);
			if (this.constantPoolElements[number].isDoubleSize()) {
				poolObj = ArrayUtilities.removeElement(this.constantPoolElements, number+1);
			}
			
			int count = poolObj.length;
			this.constantPoolElements = new JavaConstantPoolElement[count];
			
			for (int i = 0; i < count; i++) {
				this.constantPoolElements[i] = (JavaConstantPoolElement)poolObj[i];
			}
			
			/*
?????? WHAT ABOUT UPDATING REFS, DELETING UNREFD ETC. ??????
			*/
			
			this.updateUTF8ConstantNumbers();
		} else {
			throw new NullPointerException(
				"ConstantPoolManager.removeConstant(int): constants = null"
			);
		}
	}
	
	/**
	 *	@return		true on success.
	 */
	public boolean removeDuplicates() {
		JavaConstantPoolElement[] constants = this.constantPoolElements;
		int count = constants.length;
		
		boolean optimized = false;
		for (int i = 0; i < count; i++) {
			for (int j = i+1; j < count; j++) {
				if ((constants[i] != null) && (constants[j] != null)) {
					if (constants[i].equals(constants[j])) {
						if (BMPreferencesManager.getShowLog()) {
							LogMonitor.addToCurrentLog(
								"[-] " + constants[j].toString(false) +
								"\n    [ = " + constants[i].toString(false) + "]\n"
							);
						}
						
						if (!optimized) { optimized = true; }
						
						this.removeConstant(j);
						count--;
					}
				}
			}
		}
		
		return optimized;
	}
	
	/**
	 *	For Classref, Stringref constants returns one string: resolved contents.
	 *	For NameAndType constants returns two strings: name, type.
	 *	For Fieldref, Methodref, InterfaceMethodref returns three strings: class name, name, type.
	 */
	public String[] resolveConstant(int ref) {
		try {
			this.checkReferences(ref);
		} catch (Exception exc) {
			throw new InvalidClassFormatError(exc.getMessage());
		}
		
		JavaConstantPoolElement constant = this.constantPoolElements[ref];
		int tag = constant.getTag();
		int ref1 = constant.getReference();
		int ref2 = constant.getReference2();
		
		switch (tag) {
			case CONSTANT_Classref:
			case CONSTANT_Stringref:
			{
				return new String[] { this.constantPoolElements[ref1].getContents() };
			}
			
			case CONSTANT_NameAndType:
			{
				return new String[] {
					this.constantPoolElements[ref1].getContents(),
					this.constantPoolElements[ref2].getContents()
				};
			}
			
			case CONSTANT_Fieldref:
			case CONSTANT_Methodref:
			case CONSTANT_InterfaceMethodref:
			{
				JavaConstantPoolElement const1 /* Classref */ = this.constantPoolElements[ref1];
				JavaConstantPoolElement const2 /* NameAndType */ = this.constantPoolElements[ref2];
				
				String className = this.constantPoolElements[const1.getReference()].getContents();
				String name = this.constantPoolElements[const2.getReference()].getContents();
				String type = this.constantPoolElements[const2.getReference2()].getContents();
				
				return new String[] { className, name, type };
			}
		}
		
		throw new InternalError();
	}
	
	protected void checkReferences(JavaConstantPoolElement constant) throws Exception {
		int tag = constant.getTag();
		String stringTag = JavaConstantPool.CONSTANT_TAGS[tag];
		
		if ((tag < 7) || (tag > 12)) {
			throw new IllegalArgumentException("invalid tag: " + tag);
		}
		
		int ref1 = constant.getReference();
		int ref2 = constant.getReference2();
		
		switch (tag) {
			case CONSTANT_Classref:
			case CONSTANT_Stringref:
			{
				if (!this.constantPoolElements[ref1].isBasicType()) {
					throw new Exception(stringTag + ": reference too deep");
				}
			}
			
			case CONSTANT_NameAndType:
			{
				JavaConstantPoolElement const1 = this.constantPoolElements[ref1];
				JavaConstantPoolElement const2 = this.constantPoolElements[ref2];
				
				if (!const1.isBasicType() || !const2.isBasicType()) {
					throw new Exception(stringTag + ": reference(s) too deep");
				}
			}
			
			case CONSTANT_Fieldref:
			case CONSTANT_Methodref:
			case CONSTANT_InterfaceMethodref:
			{
				JavaConstantPoolElement const1 /* Classref */ =
									this.constantPoolElements[ref1];
				JavaConstantPoolElement const2 /* NameAndType */ =
									this.constantPoolElements[ref2];
				
				if (const1.getTag() != CONSTANT_Classref) {
					throw new Exception(stringTag + ": bad reference (Classref required)");
				}
				if (const2.getTag() != CONSTANT_NameAndType) {
					throw new Exception(stringTag + ": bad reference (NameAndType required)");
				}
				
				this.checkReferences(const1.getNumber());
				this.checkReferences(const2.getNumber());
			}
		}
	}
	
	/**
	 *	Throws an exception in case of no success.
	 */
	public void checkReferences(int number) throws Exception {
		JavaConstantPoolElement constant = this.constantPoolElements[number];
		this.checkReferences(constant);
	}
	
	protected JavaConstantPoolElement findEqualConstant(final JavaConstantPoolElement constant) {
		if (constant != null) {
			int count = constantPoolElements.length;
			
			for (int i = 1; i < count; i++) {
				JavaConstantPoolElement current = constantPoolElements[i];
				if (current != null) {
					if (current.equals(constant)) {
						return current;
					}
				}
			}
		}
		
		return null;
	}
	
	public int findUTF8ConstantByContents(String contents) {
		return this.findConstantByContentsAndTag(contents, CONSTANT_UTF8);
	}
	
	public int findClassrefConstantByContents(String contents) {
		return this.findConstantByContentsAndTag(contents, CONSTANT_Classref);
	}
	
	public int findNameAndTypeConstantByContents(String contents) {
		return this.findConstantByContentsAndTag(contents, CONSTANT_NameAndType);
	}
	
	public int findConstantByContentsAndTag(String contents, int tag) {
		int count = this.constantPoolElements.length;
		
		for (int i = 1; i < count; i++) {
			JavaConstantPoolElement current = this.constantPoolElements[i];
			if (current != null) {
				if (current.getTag() == tag) {
					if (current.getContents().equals(contents)) {
						return current.getNumber();
					}
				}
			}
		}
		
		return 0;
	}
	
	public int findConstantByContents(String contents) {
		int count = this.constantPoolElements.length;
		
		for (int i = 1; i < count; i++) {
			JavaConstantPoolElement current = this.constantPoolElements[i];
			if (current != null) {
				if (current.getContents().equals(contents)) {
					return current.getNumber();
				}
			}
		}
		
		return 0;
	}
	
	public int findConstantByReferences(int ref1, int ref2) {
		int count = this.constantPoolElements.length;
		
		for (int i = 1; i < count; i++) {
			JavaConstantPoolElement current = this.constantPoolElements[i];
			if (current != null) {
				if ((current.getReference() == ref1) &&
						(current.getReference2() == ref2)) {
					return current.getNumber();
				}
			}
		}
		
		return 0;
	}
	
	/**
	 *	Searches for a first constant with tag 'tag'.
	 *	Returns 0 in case of no success.
	 */
	public int findFirstConstantWithTag(int tag) {
		return this.findFirstConstantWithTag(tag, 0);
	}
	
	/**
	 *	Searches for a first constant with tag 'tag', excluding number 'n'.
	 *	Returns 'null' in case of no success.
	 */
	public int findFirstConstantWithTag(int tag, int n) {
		int count = this.constantPoolElements.length;
		
		for (int i = 1; i < count; i++) {
			if (i != n) {
				JavaConstantPoolElement current = this.constantPoolElements[i];
				if (current != null) {
					if (current.getTag() == tag) {
						return current.getNumber();
					}
				}
			}
		}
		
		return 0;
	}
	
	/**
	 *	Renumbers the constants.
	 */
	private void renumberConstants() {
		int count = this.constantPoolElements.length;
		if (count > 1) {
			for (int i = 1; i < count; i++) {
				JavaConstantPoolElement constant = this.constantPoolElements[i];
				if (constant != null) {
					constant.setNumber(i);
					if (constant.isDoubleSize()) {
						i++;
					}
				}
			}
		}
	}
	
	public String[] collectConstantContentsByTag(int tag) {
		int count = this.constantPoolElements.length;
		if (count == 0) { return new String[0]; }
		
		// count such constants
		int retCount = 0;
		for (int i = 1; i < count; i++) {
			if (this.constantPoolElements[i] != null) {
				if (this.constantPoolElements[i].getTag() == tag) {
					retCount++;
				}
			}
		}
		
		String[] ret = new String[retCount];
		int pos = 0;
		
		for (int i = 1; i < count; i++) {
			if (this.constantPoolElements[i] != null) {
				if (this.constantPoolElements[i].getTag() == tag) {
					ret[pos++] = this.constantPoolElements[i].getContents();
				}
			}
		}
		
		return ret;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer(super.getClass().getName());
		buf.append(":\n").append("------------------------------------------------\n");
		buf.append(ConstantPoolLocksmith.dumpConstants(this));
		buf.append("------------------------------------------------\n");
		
		return buf.toString();
	}
	
}
