// ===========================================================================
//	JavaClassMember.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

/**
 *	<code>JavaClassMember</code>
 *
 *	@version	0.7d0
 *	@since		Bytecode Maker 0.6.0
 */

public interface JavaClassMember {
	
	public JavaClass getOwnerClass();
	public void setOwnerClass(JavaClass owner);
	
}
