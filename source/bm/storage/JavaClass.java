// ===========================================================================
//	JavaClass.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.lang.reflect.Modifier;
import java.io.*;

import douglas.mencken.util.ClassUtilities;
import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.bm.storage.constants.BMErrorStrings;

/**
 *	<code>JavaClass</code>
 *	
 *	@version 2.0d12
 */

public class JavaClass extends Object implements Externalizable {
	
	private float javaClassFormatVersion = 45.3f;
	
	/**
	 *	An access modifiers for this class.
	 */
	private int accessModifiers;
	
	/**
	 *	Number in constant pool of Classref constant
	 *	containing name of the class.
	 */
	private int classRef;
	
	/**
	 *	Number in constant pool of Classref constant
	 *	containing name of the superclass.
	 */
	private int superclassRef;
	
	/**
	 *	An array of numbers in constant pool of Classref constants
	 *	containing interface names.
	 */
	private int[] interfaceRefs;
	
	/**
	 *	The constant pool.
	 */
	private JavaConstantPool constantPool = null;
	
	/**
	 *	An array containing all JavaField objects for this class.
	 */
	private JavaField[] fields;
	
	/**
	 *	An array containing all JavaMethod objects for this class.
	 */
	private JavaMethod[] methods;
	
	/**
	 *	<i>("SourceFile" attribute)</i><br>
	 *	Number in constant pool of UTF8 constant containing
	 *	name of source file.
	 */
	private int sourceNameRef;
	
	/**
	 *	<i>("AbsoluteSourcePath" attribute)</i><br>
	 *	Number in constant pool of UTF8 constant containing
	 *	absolute source path.
	 */
	private int absoulteSourcePathRef;
	
	/**
	 *	The default (null) constructor.
	 */
	public JavaClass() {
		super();
		this.init(0, 0, 0);
		
		try {
			JavaClass newClass = JavaClassMaker.makeJavaClass(
				"public synchronized class UntitledClass extends java.lang.Object"
			);
			this.accessModifiers = newClass.accessModifiers;
			this.classRef = newClass.classRef;
			this.superclassRef = newClass.superclassRef;
			this.constantPool = newClass.constantPool;
		} catch (Exception exc) { /* impossible */ throw new InternalError(); }
	}
	
	/**
	 *	Creates new java class.
	 */
	public JavaClass(int modifiers, int classRef, int superclassRef) {
		super();
		this.init(modifiers, classRef, superclassRef);
	}
	
	/**
	 *	Initializes new java class.
	 */
	protected void init(int modifiers, int classRef, int superclassRef) {
		this.interfaceRefs = new int[0];
		this.fields = new JavaField[0];
		this.methods = new JavaMethod[0];
		
		this.accessModifiers = modifiers;
		this.classRef = classRef;
		this.superclassRef = superclassRef;
	}
	
	/**
	 *	Writes java class to the ObjectOutput.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// ...
	}
	
	/**
	 *	Reads java class from the ObjectInput.
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// ...
	}
	
	/**
	 *	@see	#getClassName
	 */
	public int getClassReference() {
		return this.classRef;
	}
	
	/**
	 *	@see	#getSuperclassName
	 */
	public int getSuperclassReference() {
		return this.superclassRef;
	}
	
	/**
	 *	@see	#getInterfaces
	 */
	public int[] getInterfaceReferences() {
		return this.interfaceRefs;
	}
	
	/**
	 *	(local to package)
	 */
	//void setClassReference(int newRef) {
	//	this.classRef = newRef;
	//}
	
	/**
	 *	(local to package)
	 */
	//void setSuperclassReference(int newRef) {
	//	this.superclassRef = newRef;
	//}
	
	/**
	 *	@return		a fully qualified class name.
	 */
	public String getClassName() {
		if (this.constantPool != null) {
			String[] resolved = this.constantPool.resolveConstant(this.classRef);
			return ClassUtilities.toCommas(resolved[0]);
		} else {
			StringBuffer buf = new StringBuffer("* unknown (@");
			buf.append(this.classRef).append(") *");
			return buf.toString();
		}
	}
	
	public void setClassName(String newName) {
		if (this.constantPool != null) {
			// ...
		}
	}
	
	/**
	 *	@return		a fully qualified superclass name.
	 */
	public String getSuperclassName() {
		if (this.constantPool != null) {
			String[] resolved = this.constantPool.resolveConstant(this.superclassRef);
			return ClassUtilities.toCommas(resolved[0]);
		} else {
			StringBuffer buf = new StringBuffer("* unknown (@");
			buf.append(this.superclassRef).append(") *");
			return buf.toString();
		}
	}
	
	// To search for a new constant instead of changing current,
	// use
	//
	// if (this.constantPool != null) {
	//		return this.constantPool.findClassrefConstantByContents(
	//			ClassUtilities.fromCommas(newSuper)
	//		).getNumber();
	// }
	public void setSuperclassName(String newSuper) {
		if (this.constantPool != null) {
			//JavaConstantPoolElement superName = this.constantPool.getConstantBySuperref(
			//	this.superclassRef
			//);
			//superName.setContents(newSuper);
		}
	}
	
	/**
	 *	@return		a fully qualified names of all interfaces implemented
	 *				in this class.
	 */
	public String[] getInterfaces() {
		int count = this.interfaceRefs.length;
		String[] interfaces = new String[count];
		
		if (this.constantPool != null) {
			for (int i = 0; i < count; i++) {
				interfaces[i] = ClassUtilities.toCommas(
					this.constantPool.resolveConstant(this.interfaceRefs[i])[0]
				);
			}
		} else {
			for (int i = 0; i < count; i++) {
				StringBuffer buf = new StringBuffer("* unknown (@");
				buf.append(this.interfaceRefs[i]).append(") *");
				
				interfaces[i] = buf.toString();
			}
		}
		
		return interfaces;
	}
	
	/**
	 *	@return		a fully qualified name of interface number 'n'
	 *				implemented in this class.
	 */
	public String getInterface(int n) {
		int count = this.interfaceRefs.length;
		if ((n < count) && (n >= 0)) {
			if (this.constantPool != null) {
				return ClassUtilities.toCommas(
					this.constantPool.resolveConstant(this.interfaceRefs[n])[0]
				);
			} else {
				StringBuffer buf = new StringBuffer("* unknown (@");
				buf.append(this.interfaceRefs[n]).append(") *");
				return buf.toString();
			}
		} else {
			throw new IndexOutOfBoundsException("out of range");
		}
	}
	
	/**
	 *	@return		the constant pool for this class.
	 */
	public JavaConstantPool getConstantPool() {
		return this.constantPool;
	}
	
	/**
	 *	Sets constant pool.
	 */
	public void setConstantPool(JavaConstantPool newPool) {
		this.constantPool = newPool;
	}
	
	//public void setConstants(Object[] newConstants) {
	//	if (newConstants == null) {
	//		newConstants = new JavaConstantPoolElement[0];
	//	}
	//	
	//	int count = newConstants.length;
	//	JavaConstantPoolElement[] constants = new JavaConstantPoolElement[count];
	//	for (int i = 0; i < count; i++) {
	//		constants[i] = (JavaConstantPoolElement)newConstants[i];
	//	}
	//	
	//	this.constantPool = new JavaConstantPool(constants, this);
	//}
	
	public int getAccessModifiers() {
		return this.accessModifiers;
	}
	
	public void setAccessModifiers(int newAccess) {
		if (this.accessModifiers != newAccess) {
			if (this.isInterface() != Modifier.isInterface(newAccess)) {
				throw new IllegalArgumentException(
					"can't change INTERFACE access modifier"
				);
			}
			
			this.accessModifiers = newAccess;
		}
	}
	
	/**
	 *	@return		true if this class is an interface.
	 */
	public boolean isInterface() {
		return Modifier.isInterface(this.accessModifiers);
	}
	
	/**
	 *	Changes 'interface' access modifier.
	 */
	public void setInterface(boolean newInterface) {
		if (this.isInterface() != newInterface) {
			this.accessModifiers ^= Modifier.INTERFACE;
			
			int fieldCount = this.fields.length;
			int methodCount = this.methods.length;
			
			if (newInterface) /* convert to interface */ {
				// interfaces are always abstract
				this.accessModifiers |= Modifier.ABSTRACT;
				
				// remove 'synchronized'
				if (Modifier.isSynchronized(this.accessModifiers)) {
					this.accessModifiers ^= Modifier.SYNCHRONIZED;
				}
				
				if (!this.getSuperclassName().equals("java.lang.Object")) {
					UsefulModalDialogs.tellAboutError(
						12, BMErrorStrings.getErrorString(12)
					);
					this.setSuperclassName("java.lang.Object");
				}
				
				if (fieldCount != 0) {
					// remove all non-'static final' fields
					for (int index = 0; index < fieldCount; index++) {
						if (!fields[index].isStatic() || !fields[index].isFinal()) {
//							this.removeField(index);
							
							// go back
							fieldCount--;
							index--;
						}
					}
				}
				
				if (methodCount != 0) {
					// make all methods abstract
					for (int index = 0; index < methodCount; index++) {
						this.methods[index].setAbstract(true);
					}
				}
			} else /* convert to class */ {
				// add 'synchronized'
				this.accessModifiers |= Modifier.SYNCHRONIZED;
				
				if (methodCount != 0) {
					this.accessModifiers |= Modifier.ABSTRACT;
				}
			}
		}
	}
	
	/**
	 *	Set interfaces for this class.
	 */
	public void setInterfaces(String[] interfaces) {
		if (interfaces == null) {
			this.interfaceRefs = new int[0];
		} else {
			// ...
		}
	}
	
	/**
	 *	Set all fields for this class.
	 */
	public void setFields(JavaField[] newFields) {
		if (newFields == null) {
			this.fields = new JavaField[0];
		} else {
			// ...
		}
	}
	
	/**
	 *	Set all methods for this class.
	 */
	public void setMethods(JavaMethod[] newMethods) {
		if (newMethods == null) {
			// ...
		} else {
			// ...
		}
	}
	
	public int getFieldCount() {
		return this.fields.length;
	}
	
	public int getMethodCount() {
		return this.methods.length;
	}
	
	public void addField(JavaField field) {
		// ...
	}
	
	public void addMethod(JavaMethod method) {
		// ...
	}
	
	public String[] obtainClassImports() {
		String[] classNames = this.constantPool.collectConstantContentsByTag(
			JavaConstantPool.CONSTANT_Classref
		);
		String myPackageName = ClassUtilities.getPackage(this.getClassName());
		
		// ...
		
		return new String[0];
	}
	
	/**
	 *	Gets the list of package names that is used by this class.
	 *	The package of this class are also included.
	 *
	 *	@return  string array of package names.
     */
	public String[] obtainPackages() {
		String[] ret = ClassUtilities.extractPackagesFromNames(this.obtainClassImports());
		String myPackageName = ClassUtilities.getPackage(this.getClassName());
		
		// ...
		
		return new String[0];
	}
	
}
