// ===========================================================================
//	JavaMethodBeanInfo.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.beans.*;
import java.awt.*;
import java.lang.reflect.*;

/**
 *	@version	0.72a2
 *	@since		Bytecode Maker 0.5.8
 */

public class JavaMethodBeanInfo extends SimpleBeanInfo {
	
	public BeanDescriptor getBeanDescriptor() {
		return new BeanDescriptor(JavaMethod.class, JavaMethodCustomizer.class);
	}
	
	public MethodDescriptor[] getMethodDescriptors() {
		return new MethodDescriptor[0];
	}
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("JavaMethod_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("JavaMethod_Mono16.gif");
			
			default:
				return null;
		}
	}
	
}
