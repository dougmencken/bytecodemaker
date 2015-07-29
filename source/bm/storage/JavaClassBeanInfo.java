/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

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
