// ===========================================================================
//	AccessChanger.java
// ===========================================================================

package douglas.mencken.bm.plugins;

/**
 *	<code>AccessChanger</code>
 *	A standard plug-in for Bytecode Maker.
 *
 *	In SpecialMenu was:
 *		private MenuItem changeName;
 *		
 *		private MenuItem makePublic;
 *		private MenuItem makeProtected;
 *		private MenuItem makePrivate;
 *		private MenuItem makeDefault;
 *		
 *		private MenuItem makeSynchronized;
 *		private MenuItem makeXXX;
 *
 *	>>> Move HERE!!!
 *
 *	@version	0.5d0
 *	@since		Bytecode Maker 0.6.0
 */

public class AccessChanger extends DefaultPlugIn {
	
	public AccessChanger() { super(); }
	
	public String getPlugInName() {
		return "Access Changer";
	}
	
	/**
	 *	Plug-Ins' main method.
	 */
	public void plugin() {
		// ...
	}
	
}
