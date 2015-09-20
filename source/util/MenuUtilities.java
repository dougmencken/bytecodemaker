/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.util;

import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

/**
 *	<code>MenuUtilities</code>
 *
 *	@version 1.2
 */

public final class MenuUtilities extends Object {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private MenuUtilities() { super(); }
	
	/**
	 * Format of description:
	 *     label ("-anything" for separator, ">title" for sub-menu)
	 *       , shortcut key
	 *           , action command
	 *               , "f" disables the item | "x" for CheckboxMenuItem
	 *     ....
	 *
	 * Example:
	 *     MenuUtilities.fillMenuByDesc(new String[] {
	 *         "New...",   "N",  "NEW",   "",
	 *         "Open...",  "O",  "OPEN",  "",
	 *         "Close",    "W",  "CLOSE", "f",
	 *         "Check Me", null, "CHECK", "x"
	 *     }, this, this);
	 */
	public static final void fillMenuByDesc(String[] desc, Menu theMenu,
						ActionListener eatAction, ItemListener eatItem) {
		if ((desc == null) || (theMenu == null)) return;
		
		int entryCount = desc.length / 4;
		for (int ii = 0; ii < entryCount; ii++) {
			String descLabel = desc[ii*4];
			String descShortcut = desc[ii*4 + 1];
			String descActionCmd = desc[ii*4 + 2];
			String descAbility = desc[ii*4 + 3];
			
			if ((descLabel == null) || (descLabel.length() == 0)) {
				descLabel = "Untitled Item";
				descShortcut = null;
				descActionCmd = null;
				descAbility = "f";
			} else if ((descLabel.length() == 1) && !(descLabel.equals("-"))) {
				/* avoid items with single-char label */
				descLabel = "Item " + descLabel;
			}
			
			if (descAbility == null) descAbility = "";
			char abilityChar = (descAbility.length() == 0) ? 0 : descAbility.charAt(0);
			
			if (descLabel.charAt(0) == '>') {
				/* it is Menu (sub-menu) */
				Menu item = new Menu(descLabel.substring(1));
				if (abilityChar == 'f') {
					item.setEnabled(false);
				}
				theMenu.add(item);
			} else {
				/* it is MenuItem */
				if (descLabel.charAt(0) == '-') {
					/* separator */
					theMenu.addSeparator();
				} else {
					MenuShortcut shortcut = null;
					if ((descShortcut != null) && (descShortcut.length() != 0)) {
						shortcut = new MenuShortcut(MenuUtilities.charToKeyEventCode(descShortcut.charAt(0)));
					}
					
					MenuItem item = null;
					if (abilityChar == 'x') {
						/* checkbox item */
						item = new CheckboxMenuItem(descLabel);
						if (shortcut != null) item.setShortcut(shortcut);
						// note: CheckboxMenuItem doesn't generate action events
						if (eatItem != null) ((CheckboxMenuItem)item).addItemListener(eatItem);
					} else {
						/* plain item */
						item = new MenuItem(descLabel, shortcut);
						if (abilityChar == 'f') {
							item.setEnabled(false);
						}
						if ((descActionCmd != null) && (descActionCmd.length() != 0)) {
							item.setActionCommand(descActionCmd);
							if (eatAction != null) {
								item.addActionListener(eatAction);
							}
						}
					}
					if (item != null) theMenu.add(item);
				}
			}
		}
	}
	
	public static final void fillMenuByDesc(String[] desc, Menu theMenu, ActionListener eatAction) {
		fillMenuByDesc(desc, theMenu, eatAction, null);
	}
	
	public static final MenuItem findItemByLabel(Menu theMenu, String theLabel) {
		if ((theMenu == null) || (theLabel == null) || (theLabel.length() < 2)) {
			return null;
		}
		
		int itemCount = theMenu.getItemCount();
		for (int k = 0; k < itemCount; k++) {
			MenuItem current = theMenu.getItem(k);
			String label = current.getLabel();
			if (label.equals(theLabel) || label.equals(theLabel.substring(1))) {
				return current;
			}
		}
		
		return null;
	}
	
	/* KeyEvent.getExtendedKeyCodeForChar is not available before Java 7 */
	public static final int charToKeyEventCode(char key) {
		switch (key) {
			case ' ': return KeyEvent.VK_SPACE;
			case ',': return KeyEvent.VK_COMMA;
			case '-': return KeyEvent.VK_MINUS;
			case ';': return KeyEvent.VK_SEMICOLON;
			case '=': return KeyEvent.VK_EQUALS;
			case '`': return KeyEvent.VK_BACK_QUOTE;
			
			case '0': return KeyEvent.VK_0;
			case '1': return KeyEvent.VK_1;
			case '2': return KeyEvent.VK_2;
			case '3': return KeyEvent.VK_3;
			case '4': return KeyEvent.VK_4;
			case '5': return KeyEvent.VK_5;
			case '6': return KeyEvent.VK_6;
			case '7': return KeyEvent.VK_7;
			case '8': return KeyEvent.VK_8;
			case '9': return KeyEvent.VK_9;
			
			case 'A': case 'a': return KeyEvent.VK_A;
			case 'B': case 'b': return KeyEvent.VK_B;
			case 'C': case 'c': return KeyEvent.VK_C;
			case 'D': case 'd': return KeyEvent.VK_D;
			case 'E': case 'e': return KeyEvent.VK_E;
			case 'F': case 'f': return KeyEvent.VK_F;
			case 'G': case 'g': return KeyEvent.VK_G;
			case 'H': case 'h': return KeyEvent.VK_H;
			case 'I': case 'i': return KeyEvent.VK_I;
			case 'J': case 'j': return KeyEvent.VK_J;
			case 'K': case 'k': return KeyEvent.VK_K;
			case 'L': case 'l': return KeyEvent.VK_L;
			case 'M': case 'm': return KeyEvent.VK_M;
			case 'N': case 'n': return KeyEvent.VK_N;
			case 'O': case 'o': return KeyEvent.VK_O;
			case 'P': case 'p': return KeyEvent.VK_P;
			case 'Q': case 'q': return KeyEvent.VK_Q;
			case 'R': case 'r': return KeyEvent.VK_R;
			case 'S': case 's': return KeyEvent.VK_S;
			case 'T': case 't': return KeyEvent.VK_T;
			case 'U': case 'u': return KeyEvent.VK_U;
			case 'V': case 'v': return KeyEvent.VK_V;
			case 'W': case 'w': return KeyEvent.VK_W;
			case 'X': case 'x': return KeyEvent.VK_X;
			case 'Y': case 'y': return KeyEvent.VK_Y;
			case 'Z': case 'z': return KeyEvent.VK_Z;
			
			default: return 0;
		}
	}
	
}
