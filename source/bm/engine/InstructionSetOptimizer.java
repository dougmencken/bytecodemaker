// ===========================================================================
//	InstructionSetOptimizer.java (part of douglas.mencken.bm.engine package)
// ===========================================================================

package douglas.mencken.bm.engine;

import douglas.mencken.util.Optimizer;
import douglas.mencken.tools.UsefulMessageDialogs;
import douglas.mencken.tools.LogMonitor;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.storage.JVMInstructionSet;

/**
 *	<code>InstructionSetOptimizer</code>
 *
 *	@version 0.21d1
 */

public class InstructionSetOptimizer extends Object implements Optimizer {
	
	protected boolean isOptimized;
	
	public InstructionSetOptimizer() {
		super();
		this.isOptimized = false;
	}
	
	/**
	 *	Optimizes a <code>JVMInstructionSet</code>.
	 *	@param	'in' must be of type JVMInstructionSet.
	 */
	public Object optimize(Object in) {
		if ((in == null) || !(in instanceof JVMInstructionSet)) {
			UsefulMessageDialogs.doErrorDialog("Invalid object to optimize");
		}
		
		return in;
	}
	
	public boolean isOptimized() {
		return this.isOptimized;
	}
	
}
