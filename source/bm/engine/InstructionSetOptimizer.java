/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
