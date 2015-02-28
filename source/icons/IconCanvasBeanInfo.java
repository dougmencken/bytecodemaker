// ===========================================================================
//	IconCanvasBeanInfo.java (part of douglas.mencken.icons package)
// ===========================================================================

package douglas.mencken.icons;

import java.beans.*;
import java.awt.*;
import java.lang.reflect.*;

/**
 *	<code>IconCanvasBeanInfo</code>
 *
 *	@version	1.04
 */

public class IconCanvasBeanInfo extends SimpleBeanInfo {
	
	public Image getIcon(int iconKind) {
		switch (iconKind) {
			case ICON_COLOR_16x16:
				return loadImage("IconCanvas_Color16.gif");
			case ICON_MONO_16x16:
				return loadImage("IconCanvas_Mono16.gif");
				
			default:
				return null;
		}
	}
	
	public MethodDescriptor[] getMethodDescriptors() {
		MethodDescriptor[] md = new MethodDescriptor[1];
		Class iconCanvas;
		
		try {
			iconCanvas = Class.forName("douglas.mencken.icons.IconCanvas");
			
			Method repaint = iconCanvas.getMethod("repaint", null);
			md[0] = new MethodDescriptor(repaint);
		} catch (ClassNotFoundException e) {
			// Back to the low-level reflection
			return null;
		} catch (NoSuchMethodException e) {
			// Back to the low-level reflection
			return null;
		}
		
		return md;
	}
	
	public PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] pd = new PropertyDescriptor[1];
		
		try {
			pd[0] = new PropertyDescriptor("icon", IconCanvas.class);
			pd[0].setPropertyEditorClass(IconListEditor.class);
		} catch (IntrospectionException e) {}
		
		return pd;
	}
	
	public EventSetDescriptor[] getEventSetDescriptors() {
		EventSetDescriptor[] ed = new EventSetDescriptor[2];
		
		try {
			String[] mouseListenerMethods = {
				"mouseClicked",
				"mousePressed", "mouseReleased",
				"mouseEntered", "mouseExited"
			};
			ed[0] = new EventSetDescriptor(
				IconCanvas.class,
				"mouse",
				java.awt.event.MouseListener.class,
				mouseListenerMethods,
				"addMouseListener",
				"removeMouseListener"
			);
			
			String[] focusListenerMethods = {
				"focusGained", "focusLost"
			};
			ed[1] = new EventSetDescriptor(
				IconCanvas.class,
				"focus",
				java.awt.event.FocusListener.class,
				focusListenerMethods,
				"addFocusListener",
				"removeFocusListener"
			);
			
            ed[0].setDisplayName("mouse");
            ed[1].setDisplayName("focus");
        } catch (IntrospectionException e) {}
        
        return ed;
    }
    
}