// ===========================================================================
//	JVMInstructionSetBeanInfo.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.beans.*;
import java.awt.*;
import java.lang.reflect.*;

/**
 *	<code>JVMInstructionSetBeanInfo</code>
 *
 *	@version	0.8f0
 *	@since		Bytecode Maker 0.6.0
 */

public class JVMInstructionSetBeanInfo extends SimpleBeanInfo {
	
	public BeanDescriptor getBeanDescriptor() {
		return new BeanDescriptor(
			JVMInstructionSet.class,
			JVMInstructionSetEditor.class
		);
	}
	
	public MethodDescriptor[] getMethodDescriptors() {
		return new MethodDescriptor[0];
	}
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("JVMInstructionSet_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("JVMInstructionSet_Mono16.gif");
			
			default:
				return null;
		}
	}
	
}
