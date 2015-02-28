// ===========================================================================
//	BMMenuBar.java (part of douglas.mencken.bm.menu package)
// ===========================================================================

package douglas.mencken.bm.menu;

import java.awt.*;
import java.io.File;
import com.apple.mrj.MRJOpenDocumentHandler;

/**
 *	<code>BMMenuBar</code>
 *
 *	@version 1.45f1
 */

public class BMMenuBar extends MenuBar implements MRJOpenDocumentHandler {
	
	public BMMenuBar() {
		super.add(new FileMenu());
		super.add(new EditMenu());
		super.add(new OptionsMenu());
		super.add(new WindowMenu());
		super.add(new ToolsMenu());
		
		BMMenu fileMenu = (BMMenu)super.getMenu(0);
		fileMenu.updateMenu();
	}
	
	public void handleOpenFile(File file) {
		FileMenu fileMenu = (FileMenu)super.getMenu(0);
		fileMenu.handleOpenFile(file);
	}
	
	public WindowMenu getWindowMenu() {
		int count = super.countMenus();
		for (int i = 0; i < count; i++) {
			Menu menu = super.getMenu(i);
			if (menu instanceof WindowMenu) {
				return (WindowMenu)menu;
			}
		}
		
		// not found
		return null;
	}
	
	public void updateMenuBar() {
		int count = super.getMenuCount();
		
		for (int i = 0; i < count; i++) {
			Menu menu = super.getMenu(i);
			((BMMenu)menu).updateMenu();
		}
	}
	
	
	/**
	 *	Makes the popup menu that it is similar to <code>BMMenuBar</code>.
	 *	(static method)
	 */
	public static PopupMenu makePopupMenu() {
		PopupMenu popup = new PopupMenu();
		
		popup.add(new FileMenu());
		popup.add(new EditMenu());
		popup.add(new OptionsMenu());
		popup.add(new WindowMenu());
		popup.add(new ToolsMenu());
		
		return popup;
	}
	
	/**
	 *	Enables or disables all items in the MenuContainer.
	 *	(static method)
	 *
	 *	@see	BMMenu#enableMenus
	 *	@see	BMMenu#disableMenus
	 */
	static void setMenusEnabled(MenuContainer mc, boolean enable) {
		boolean isMenuBar = mc instanceof MenuBar;
		if (!isMenuBar && !(mc instanceof Menu)) {
			throw new IllegalArgumentException();
		}
		
		if (isMenuBar) {
			MenuBar mbar = (MenuBar)mc;
			int count = mbar.getMenuCount();
			
			for (int i = 0; i < count; i++) {
				Menu menu = mbar.getMenu(i);
				if (menu instanceof BMMenu) {
					((BMMenu)menu).updateMenu();
				} else {
					menu.setEnabled(enable);
				}
			}
		} else {
			Menu menu = (Menu)mc;
			int count = menu.getItemCount();
			
			for (int i = 0; i < count; i++) {
				MenuItem mi = menu.getItem(i);
				if (mi instanceof BMMenu) {
					((BMMenu)mi).updateMenu();
				} else {
					mi.setEnabled(enable);
				}
			}
		}
	}
	
	/**
	 *	Enables all items in the MenuContainer by calling 'updateMenu()'
	 *	defined in interface <code>BMMenu</code> or by calling
	 *	'setEnabled(true)' defined in java.awt.Component.
	 *	(static method)
	 *
	 *	@see	BMMenu#enableMenus
	 */
	/*public static void enableMenus(MenuContainer mc) {
		setMenusEnabled(mc, true);
	}*/
	
	/**
	 *	Disables all items in the MenuContainer by calling 'updateMenu()'
	 *	defined in interface <code>BMMenu</code> or by calling
	 *	'setEnabled(false)' defined in java.awt.Component.
	 *	(static method)
	 *	
	 *	@see	BMMenu#disableMenus
	 */
	/*public static void disableMenus(MenuContainer mc) {
		setMenusEnabled(mc, false);
	}*/
	
}