// ===========================================================================
//	JavaConstantPoolOptimizer.java (part of douglas.mencken.bm.engine package)
// ===========================================================================

package douglas.mencken.bm.engine;

import douglas.mencken.util.Optimizer;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.storage.JavaConstantPool;

/**
 *	<code>JavaConstantPoolOptimizer</code>
 *
 *	@version 0.41d2
 */

public class JavaConstantPoolOptimizer extends Object implements Optimizer {
	
	protected boolean isOptimized;
	
	public JavaConstantPoolOptimizer() {
		super();
		this.isOptimized = false;
	}
	
	/**
	 *	Optimizes a <code>JavaConstantPool</code>.
	 *	@param	'in' must be of type JavaConstantPool.
	 */
	public Object optimize(Object in) {
		if ((in == null) || !(in instanceof JavaConstantPool)) {
			UsefulMessageDialogs.doErrorDialog("Invalid object to optimize");
		}
		
		JavaConstantPool pool = (JavaConstantPool)in;
		this.isOptimized = pool.removeDuplicates();
		
		// ... more optimizations: in progress
		
		if (BMPreferencesManager.getShowLog()) {
			LogMonitor.showCurrent();
		}
		
		return in;
	}
	
	public boolean isOptimized() {
		return this.isOptimized;
	}
	
}
