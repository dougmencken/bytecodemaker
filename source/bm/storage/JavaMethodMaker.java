// ===========================================================================
//	JavaMethodMaker.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

/**
 *	<code>JavaMethodMaker</code>
 *
 *	@version	0.7d0
  *	@since		Bytecode Maker 0.6.0
 */

public class JavaMethodMaker extends Object {
	
	/**
	 *	This class cannot be instantiated.
	 */
	private JavaMethodMaker() { super(); }
	
	/**
	 *	(static method)
	 */
	public static JavaMethod makeJavaMethod(JavaClass owner, int index, int accessMods,
											String methodName, String jvmSignature) {
		if (owner != null) {
			JavaConstantPool pool = owner.getConstantPool();
			if (pool == null) {
				throw new InternalError("can't make method without constant pool");
			}
			
			int nameNumber = pool.addBasicTypeConstant(/* UTF8 */ 1, methodName);
			int typeNumber = pool.addBasicTypeConstant(/* UTF8 */ 1, jvmSignature);
			pool.addRefTypeConstant(/* NameAndType */ 12, nameNumber, typeNumber);
			
			return new JavaMethod(owner, index, accessMods, nameNumber, typeNumber);
		} else {
			JavaMethod ret = new JavaMethod();
			ret.init(accessMods, 0, 0);
			
			return ret;
		}
	}
	
	/**
	 *	Returns a string like " throws Exception1, Exception2".
	 *	
	 *	(static method)
	 */
	public static String makeThrowsString(String[] exceptions) {
		if (exceptions == null) throw new IllegalArgumentException();
		
		int elen = exceptions.length;
		if (elen != 0) {
			StringBuffer buf = new StringBuffer(" throws ");
			
			for (int i = 0; i < elen; i++) {
				buf.append(exceptions[i]);
				if ((i+1) != elen) buf.append(", ");
			}
			
			return buf.toString();
		} else {
			return "";
		}
	}
	
}
