// ===========================================================================
// JavaMethod.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.lang.reflect.Modifier;
import java.io.*;

import douglas.mencken.util.ClassUtilities;
import douglas.mencken.icons.NoteIcon;
import douglas.mencken.tools.DialogFactory;
import douglas.mencken.tools.UsefulMessageDialogs;

/**
 *	<code>JavaMethod</code>
 *
 *	@version 2.0d0
 */

public class JavaMethod extends Object
implements JavaClassIndexedMember, Externalizable {
	
	/**
	 *	An access modifiers for this method.
	 */
	private int accessModifiers;
	
	/**
	 *	Number in constant pool of UTF8 constant
	 *	containing name of the method.
	 */
	private int methodNameRef;
	
	/**
	 *	Number in constant pool of UTF8 constant
	 *	containing method's signature.
	 */
	private int jvmSignatureRef;
	
	/**
	 *	Method's code.
	 */
	private byte[] code;
	
	protected JavaClass ownerClass;
	protected int methodIndex;
	
	/**
	 *	The default (null) constructor.
	 */
	public JavaMethod() {
		super();
		this.ownerClass = null;
		this.methodIndex = -1;
		this.init(0, 0, 0);
	}
	
	/**
	 *	Creates new java method.
	 */
	public JavaMethod(JavaClass owner, int index,
						int accessModifiers, int nameRef, int jvmSignatureRef) {
		super();
		this.ownerClass = owner;
		this.methodIndex = index;
		this.init(accessModifiers, nameRef, jvmSignatureRef);
	}
	
	/**
	 *	Initializes the new java method.
	 */
	public void init(int accessModifiers, int nameRef, int jvmSignatureRef) {
		/*this.tryCatchBlocks = new TryCatchFinallyBlock[0];
		this.lineNumbers = new LineNumber[0];
		this.locals = new LocalVariable[0];
		this.exceptionRefs = new int[0];*/
		
		this.accessModifiers = accessModifiers;
		
		this.methodNameRef = nameRef;
		this.jvmSignatureRef = jvmSignatureRef;
		
		if (this.isAbstract() || this.isNative() || (this.ownerClass == null)) {
			this.setCode(null);
		} else {
			this.setCode(ClassUtilities.createEmptyMethod(this.getReturnType()));
		}
	}
	
	/**
	 *	Writes java method to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// ...
	}
	
	/**
	 *	Reads java method from the ObjectInput.
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
		return this.methodIndex;
	}
	
	public void setIndex(int index) {
		// ...
	}
	
	public void setOwnerClass(JavaClass owner, int index) {
		// ...
		this.ownerClass = owner;
		this.methodIndex = index;
	}
	
	public int getAccessModifiers() {
		return this.accessModifiers;
	}
	
	public void setAccessModifiers(int access) {
		this.setAccessModifiers(access, false);
	}
	
	/**
	 *	@param	showDialogs	if true, the dialogs like "Remove method's code? [Yes] [No]"
	 *						is to be displayed.
	 */
	public void setAccessModifiers(int access, boolean showDialogs) {
		if (!this.isAbstract() && Modifier.isAbstract(access)) {
			boolean success = this.setAbstract(true, showDialogs);
			if (!success) access = access ^ Modifier.ABSTRACT;
		} else if (this.isAbstract() && !Modifier.isAbstract(access)) {
			boolean success = this.setAbstract(false, showDialogs);
			if (!success) access = access | Modifier.ABSTRACT;
		}
		
		if (!this.isNative() && Modifier.isNative(access)) {
			boolean success = this.setNative(true, showDialogs);
			if (!success) access = access ^ Modifier.NATIVE;
		} else if (this.isNative() && !Modifier.isNative(access)) {
			boolean success = this.setNative(false, showDialogs);
			if (!success) access = access | Modifier.NATIVE;
		}
		
		if (!this.isStatic() && Modifier.isStatic(access)) {
			boolean success = this.setStatic(true, showDialogs);
			if (!success) access = access ^ Modifier.STATIC;
		} else if (this.isStatic() && !Modifier.isStatic(access)) {
			boolean success = this.setStatic(false, showDialogs);
			if (!success) access = access | Modifier.STATIC;
		}
		
		// update only if access changed
		if (this.accessModifiers != access) {
			this.accessModifiers = access;
			
			// ??? WHAT IS UPDATE ???
			// this.update();
		}
	}
	
	public boolean isStatic() {
		return Modifier.isStatic(this.accessModifiers);
	}
	
	public boolean isNative() {
		return Modifier.isNative(this.accessModifiers);
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(this.accessModifiers);
	}
	
	protected void setAbstract(boolean newAbstract) {
		this.setAbstract(newAbstract, false);
	}
	
	/**
	 *	@return		true on success.
	 */
	public boolean setAbstract(boolean newAbstract, boolean showDialog) {
		if (this.isAbstract() != newAbstract) {
			if (newAbstract) {
				if (!this.isNative()) {
					if (showDialog) {
						int yes_no = DialogFactory.doTwoButtonsDialog(
							new NoteIcon(),
							"Remove method's code?",
							" OK ", " Cancel "
						);
						
						if (yes_no != /* yes */ 0) {
							return false;
						}
					}
					
					// this.remove ??? Bytecodes ??? ();
				}
				
				this.accessModifiers = this.accessModifiers | Modifier.ABSTRACT;
			} else {
				this.accessModifiers = this.accessModifiers ^ Modifier.ABSTRACT;
				// this.setCode(ClassUtilities.createEmptyMethod(this.getReturnType()));
			}
			
			// this.update();
			return true;
		}
		
		return false;
	}
	
	public void setNative(boolean newNative) {
		this.setNative(newNative, false);
	}
	
	/**
	 *	@return		true on success.
	 */
	public boolean setNative(boolean newNative, boolean showDialog) {
		if (this.isNative() != newNative) {
			if (newNative) {
				if (!this.isAbstract()) {
					if (showDialog) {
						int yes_no = DialogFactory.doTwoButtonsDialog(
							new NoteIcon(),
							"Remove method's code?",
							" OK ", " Cancel "
						);
						
						if (yes_no != /* yes */ 0) {
							return false;
						}
					}
					
					// this.remove ??? Bytecodes ??? ();
				}
				
				this.accessModifiers = this.accessModifiers | Modifier.NATIVE;
			} else {
				this.accessModifiers = this.accessModifiers ^ Modifier.NATIVE;
				// this.setCode(ClassUtilities.createEmptyMethod(this.getReturnType()));
			}
			
			// this.update();
			return true;
		}
		
		return false;
	}
	
	public void setStatic(boolean newStatic) {
		this.setStatic(newStatic, false);
	}
	
	/**
	 *	@return		true on success.
	 */
	public boolean setStatic(boolean newStatic, boolean showDialog) {
		if (this.isStatic() != newStatic) {
			if (newStatic) {
				if (!this.isAbstract() && !this.isNative()) {
					if (showDialog) {
						int yes_no = DialogFactory.doTwoButtonsDialog(
							new NoteIcon(),
							"Make method static?",
							" Yes ", " No "
						);
						
						if (yes_no != /* yes */ 0) {
							return false;
						}
					}
					
					// BytecodeLocksmith.convertToStatic(this);
				}
				
				this.accessModifiers = this.accessModifiers | Modifier.STATIC;
			} else {
				if (!this.isAbstract() && !this.isNative()) {
					if (showDialog) {
						int yes_no = DialogFactory.doTwoButtonsDialog(
							new NoteIcon(),
							"Make method non-static?",
							" Yes ", " No "
						);
						
						if (yes_no != /* yes */ 0) {
							return false;
						}
					}
					
					// BytecodeLocksmith.convertToNonStatic(this);
				}
				
				this.accessModifiers = this.accessModifiers ^ Modifier.STATIC;
			}
			
			// this.update();
			return true;
		}
		
		return false;
	}
	
	/**
	 *	@see	#getMethodName
	 */
	public int getMethodNameReference() {
		return this.methodNameRef;
	}
	
	/**
	 *	@see	#getJVMSignature
	 */
	public int getJVMSignatureReference() {
		return this.jvmSignatureRef;
	}
	
	/**
	 *	@return		method's name.
	 */
	public String getMethodName() {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		
		if (pool != null) {
			return pool.getConstantContents(this.methodNameRef);
		} else {
			StringBuffer buf = new StringBuffer("* unknown (@");
			buf.append(this.methodNameRef).append(") *");
			return buf.toString();
		}
	}
	
	public void setMethodName(String newName) {
		JavaConstantPool pool = this.ownerClass.getConstantPool();
		if (pool != null) {
			// JavaConstantPoolElement name = pool.getConstantByRef(this.nameRef);
			// name.setContents(newName);
		}
	}
	
	/**
	 *	@return		length of method's code (in bytes).
	 */
	public int getCodeLength() {
		return this.code.length;
	}
	
	/**
	 *	@return		method's code.
	 */
	public byte[] getCode() {
		return this.code;
	}
	
	/**
	 *	Sets the new code for this method.
	 */
	public void setCode(byte[] newCode) {
		this.code = new byte[0];
		
		if (newCode != null) {
			if ((newCode.length != 0) && this.isAbstract()) {
				UsefulMessageDialogs.doWarningDialog(
					"Can't bound code with abstract method"
				);
			} else if ((newCode.length != 0) && this.isNative()) {
				UsefulMessageDialogs.doWarningDialog(
					"Can't bound code with native method"
				);
			} else {
				this.code = newCode;
			}
		}
	}
	
	public String getReturnType() {
		// ...................... //
		return "java.lang.Object";
	}
	
	public void setReturnType(String newReturnType) {
		// String signature = '(' + ClassUtilities.parseParameterList(this.getArrayOfParameters())
		// 					+ ')' + ClassUtilities.parseType(newReturnType);
		// this.setJVMSignature(signature);
	}
	
}
