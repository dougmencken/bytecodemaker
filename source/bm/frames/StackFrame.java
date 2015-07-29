/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.util.SpecialStack;
import douglas.mencken.beans.CornerLabel;
import douglas.mencken.beans.CornerLabelFrame;
import douglas.mencken.bm.storage.JavaMethod;
/// LOCAL-TO-PACKAGE import douglas.mencken.bm.storage.JVMInstruction;
import douglas.mencken.bm.storage.JVMInstructionSetEditor;
import douglas.mencken.bm.engine.StackCalculator;

/**
 *	<code>StackFrame</code>
 *
 *	@version 1.14f1
 */

public class StackFrame extends CornerLabelFrame {
	
	private JVMInstructionSetEditor codeEditor = null;
	private int maxstack = 0;
	
	private static final int STACK_OK = 0;
	private static final int STACK_OVERFLOW_ERROR = 1;
	private static final int STACK_IS_NULL = 2;
	private static final int STACK_PC_OUT_OF_BOUNDS_ERROR = 3;
	private static final int STACK_DESTROYED = 4;
	
	/**
	 *	The constructor.
	 */
	public StackFrame(JVMInstructionSetEditor codeEditor) {
		super( /**** "Stack", codeEditor.getMethod().getMaxStack() ****/ );
		this.maxstack = super.getLabelCount();
		
		this.codeEditor = codeEditor;
	/////	setTitle(codeEditor.getMethod().getNameWithParameters() + ": Stack");
		
		super.pack();
		
	/////	int xpos = codeEditor.getLocation().x + codeEditor.getSize().width + 10;
	/////	int ypos = codeEditor.getLocation().y;
	/////	super.setBounds(xpos, ypos, 300, getSize().height);
	/////	super.setResizable(false);
		
		new StackFrameMenuBar(this);
		addWindowListener(this);
	}
	
	public void setVisible(boolean newVisible) {
		super.setVisible(newVisible);
//#####		codeEditor.toFront();
	}
	
	public void updateValues(int pc) {
		int state = STACK_OK;
		
//////////		try {
//////////			byte opcode = codeEditor.getMethod().getCode()[pc];
			
//////////			// ireturn, lreturn, freturn, dreturn, areturn, return
//////////			if ((opcode >= -84 /* 172 */) && (opcode <= -79 /* 177 */)) {
//////////				state = STACK_DESTROYED;
//////////			}
//////////		} catch (ArrayIndexOutOfBoundsException exc) {
//////////			state = STACK_PC_OUT_OF_BOUNDS_ERROR;
//////////		}
		
//&&&&&		StackCalculator sc = codeEditor.getMethod().getStackCalculator();
//&&&&&		if (sc.getStackAt(pc) == null) {
//&&&&&			state = STACK_IS_NULL;
//&&&&&		}// else {
//&&&&&			//if ( (this.maxstack == 0) || (sc.getStackAt(pc).isEmpty()) ) {
//&&&&&				//state = STACK_IS_NULL;
//&&&&&			//}
//&&&&&		//}
		
		int curr_pos = 0;
		
/******		if (state == STACK_OK) {
			SpecialStack stack = sc.getStackAt(pc);
			
			Object[] values = stack.getStackValues();
			int size = values.length;
			
			try {
				for (int i = 0; i < size; i++) {
					String value = (String)values[i];
					if (!value.startsWith(" ")) {
						super.changeLabel(curr_pos, value, String.valueOf(curr_pos));
						curr_pos++;
					}
				}
			} catch (ArrayIndexOutOfBoundsException breakLoop) {
				if (curr_pos > this.maxstack) {
					state = STACK_OVERFLOW_ERROR;
				}
			}
		} *********/
		
/******		switch (state) {
			case STACK_DESTROYED:
				for (int i = 0; i < this.maxstack; i++) {
					super.changeLabel(i, "(destroyed)", "");
				}
				curr_pos = this.maxstack;
				break;
			
			case STACK_OVERFLOW_ERROR:
				super.setBorderColor(Color.red);
				super.setBorderSize(2);
				break;
			
			case STACK_OK:
				super.setBorderColor(Color.black);
				super.setBorderSize(1);
				break;
			
			case STACK_IS_NULL:
			case STACK_PC_OUT_OF_BOUNDS_ERROR:
				super.setBorderColor(Color.white);
				break;
		} *************************/
		
		for (int i = curr_pos; i < this.maxstack; i++) {
			super.changeLabel(i, "", "");
		}
	}
	
	public void windowClosing(WindowEvent e) {
		super.setVisible(false);
/////////////		codeEditor.updateMenus();
	}
	
}


class StackFrameMenuBar extends MenuBar implements ActionListener {
	
	private StackFrame owner;
	
	StackFrameMenuBar(StackFrame frame) {
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
