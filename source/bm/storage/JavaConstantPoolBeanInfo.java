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
 *	<code>JavaConstantPoolBeanInfo</code>
 *
 *	@version	0.8f1
 *	@since		Bytecode Maker 0.6.0
 */

public class JavaConstantPoolBeanInfo extends SimpleBeanInfo {
	
	public BeanDescriptor getBeanDescriptor() {
		return new BeanDescriptor(
			JavaConstantPool.class,
			JavaConstantPoolCustomizer.class
		);
	}
	
	public MethodDescriptor[] getMethodDescriptors() {
		return new MethodDescriptor[0];
	}
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("JavaConstantPool_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("JavaConstantPool_Mono16.gif");
			
			default:
				return null;
		}
	}
	
}
