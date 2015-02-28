// ===========================================================================
//	Optimizer.java (part of douglas.mencken.util package)
// ===========================================================================

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
