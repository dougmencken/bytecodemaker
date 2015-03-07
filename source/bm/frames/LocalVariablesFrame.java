// ===========================================================================
//	LocalVariablesFrame.java (part of douglas.mencken.bm.frames package)
//		public class LocalVariablesFrame
//		class LocalVariablesFrameMenuBar
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.beans.CornerLabel;
import douglas.mencken.beans.CornerLabelFrame;
import douglas.mencken.bm.storage.LocalVariableWithValue;
import douglas.mencken.bm.engine.LocalVarsCalculator;

/**
 *	<code>LocalVariablesFrame</code>
 *
 *	@version 1.04f2
 */

public class LocalVariablesFrame extends CornerLabelFrame {
	
	//private JVMInstructionSetEditor codeEditor = null; ///// JVMInstructionSetEditor -> JVMInstructionSetEditor
	
	public LocalVariablesFrame() { ///////////JVMInstructionSetEditor codeEditor) {
///////		super("Local Variables", codeEditor.getMethod().getMaxLocals());
///////		this.codeEditor = codeEditor;
///////		setTitle(codeEditor.getMethod().getNameWithParameters() + ": Local Variables");
		
		super.pack();
		
//###		int xpos = codeEditor.getLocation().x + codeEditor.getSize().width + 10;
//###		int ypos = codeEditor.getLocation().y;
//###		super.setBounds(xpos, ypos, 400, getSize().height);
//###		super.setResizable(false);
		
		new LocalVariablesFrameMenuBar(this);
		addWindowListener(this);
	}
	
	public void setVisible(boolean newVisible) {
		super.setVisible(newVisible);
///////		codeEditor.toFront();
	}
	
	public void updateValues(int pc) {
		if (pc < 0) {
			return;
		}
		
///////		LocalVarsCalculator lvc = codeEditor.getMethod().getLocalVarsCalculator();
///////		LocalVariableWithValue[] localVarValues = lvc.getLocalVariablesAt(pc);
///////		boolean methodIsStatic = codeEditor.getMethod().isStatic();
		
/*****		if (localVarValues != null) {
			int args_count = codeEditor.getMethod().getArrayOfParameters().length;
			
			int max_locals = codeEditor.getMethod().getMaxLocals();
			int count = localVarValues.length;
			LocalVariableWithValue current;
			
			for (int i = 0; i < max_locals; i++) {
				if (i < count) {
					current = localVarValues[i];
					if (current != null) {
						int slot = current.getSlotNumber();
						Object val = current.getValue();
						
						String text =
								current.getVariableType() + ' ' +
								current.getVariableName();
						
						if ((!methodIsStatic) && (slot == 0)) {
							text = ('(' + text + ')');
						} else if (val != null) {
							text = (text + " = " + val);
						}
						
						if (slot <= args_count) {
							text = ('[' + text + ']');
						}
						
						super.changeLabel(i, text, String.valueOf(slot));
					} else {
						super.changeLabel(i, "* null *", "?");
					}
				} else {
					super.changeLabel(i, "", "");
				}
			}
		} else {
			super.setBorderColor(Color.white);
		}
*******************************/
	}
	
	/**
	 *	Handles the <code>windowClosing</code> event.
	 */
	public void windowClosing(WindowEvent e) {
		super.setVisible(false);
////////////		codeEditor.updateMenus();
	}
	
}


class LocalVariablesFrameMenuBar extends MenuBar implements ActionListener {
	
	private LocalVariablesFrame owner;
	
	LocalVariablesFrameMenuBar(LocalVariablesFrame frame) {
		Menu file = new Menu("File");
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		file.add(close);
		
		super.add(file);
		frame.setMenuBar(this);
		this.owner = frame;
	}
	
	public void actionPerformed(ActionEvent evt) {
		String c = evt.getActionCommand();
		if (c.equals("CLOSE")) {
			owner.windowClosing(null);
		}
	}
	
}
