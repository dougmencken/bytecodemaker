// ===========================================================================
//	JavaMethodIndexedMember.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

/**
 *	<code>JavaMethodIndexedMember</code>
 *
 *	@version	0.7d0
 *	@since		Bytecode Maker 0.6.0
 */

public interface JavaMethodIndexedMember extends JavaMethodMember {
	
	public int getIndex();
	public void setIndex(int index);
	public void setOwnerMethod(JavaMethod owner, int index);
	
}
