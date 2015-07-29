/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

/** 
 *	Defines the interface for optimizers.
 *	
 *	@version 1.14f
 */

public interface Optimizer {
	
	/**
	 *	Optimizes a given object.
	 *
	 *	@param	in	object before optimization.
	 *	@return		object after optimization.
	 */
	public Object optimize(Object in);
	
	/**
	 *	@return		<code>true</code> after any optimizations made,
	 *				<code>false</code> otherwise.
	 */
	public boolean isOptimized();
	
}
