// ===========================================================================
//	DefaultPlugIn.java
// ===========================================================================

package douglas.mencken.bm.plugins;

/**
 *	<code>DefaultPlugIn</code>
 *	
 *	@version	1.01f
 *	@since		Bytecode Maker 0.5.9
 */

public abstract class DefaultPlugIn extends Object {
	
	public abstract void plugin();
	
	public abstract String getPlugInName();
	
	public final String getGroupName() {
		return "default";
	}
	
	protected DefaultPlugIn() { super(); }
	
}
