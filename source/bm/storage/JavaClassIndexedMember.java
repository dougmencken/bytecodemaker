// ===========================================================================
//	JavaClassIndexedMember.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

/**
 *	<code>JavaClassIndexedMember</code>
 *
 *	@version	0.7d0
 *	@since		Bytecode Maker 0.6.0
 */

public interface JavaClassIndexedMember extends JavaClassMember {
	
	public int getIndex();
	public void setIndex(int index);
	public void setOwnerClass(JavaClass owner, int index);
	
}
