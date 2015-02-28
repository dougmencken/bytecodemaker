// ===========================================================================
//	JavaClassBeanInfo.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.beans.*;
import java.awt.*;
import java.lang.reflect.*;

/**
 *	@version	0.7a5
 *	@since		Bytecode Maker 0.5.8
 */

public class JavaClassBeanInfo extends SimpleBeanInfo {
	
	public BeanDescriptor getBeanDescriptor() {
		return new BeanDescriptor(JavaClass.class, JavaClassCustomizer.class);
	}
	
	public MethodDescriptor[] getMethodDescriptors() {
		return new MethodDescriptor[0];
	}
	
	public PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] descriptors = new PropertyDescriptor[0];
		return descriptors;
	}
	
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[0];
	}
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("JavaClass_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("JavaClass_Mono16.gif");
			
			default:
				return null;
		}
	}
	
}
