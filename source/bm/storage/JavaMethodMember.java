// ===========================================================================
//	JavaMethodMember.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

/**
 *	<code>JavaMethodMember</code>
 *
 *	@version	0.7d0
 *	@since		Bytecode Maker 0.6.0
 */

public interface JavaMethodMember {
	
	public JavaMethod getOwnerMethod();
	public void setOwnerMethod(JavaMethod owner);
	
}
