/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.menu;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.tools.UsefulMessageDialogs;

import douglas.mencken.bm.storage.JavaClass;
// LOCAL-TO-PACKAGE import douglas.mencken.bm.storage.JavaConstantPoolElement;
import douglas.mencken.bm.frames.*;
// REMOVED import douglas.mencken.bm.action.BytecodeOptimizer;
// REMOVED import douglas.mencken.bm.action.ConstantPoolOptimizer;
import douglas.mencken.bm.BMEnvironment;

/**
 *	<code>OptionsMenu</code>
 *
 *	@version 1.37f1
 */

public final class OptionsMenu extends Menu implements BMMenu {
	
	private MenuItem editClass;
	private MenuItem optimize;
	private MenuItem verify;
	
	public OptionsMenu() {
		super("Options");
		
		editClass = new MenuItem("Edit Class...", new MenuShortcut(KeyEvent.VK_E));
		editClass.setActionCommand("EDIT");
		editClass.addActionListener(this);
		super.add(editClass);
		
		super.addSeparator();
		
		optimize = new MenuItem("Optimize...", new MenuShortcut(KeyEvent.VK_T));
		optimize.setActionCommand("OPTIMIZE");
		optimize.addActionListener(this);
		super.add(optimize);
		
		verify = new MenuItem("Verify...", new MenuShortcut(KeyEvent.VK_Y));
		verify.setActionCommand("VERIFY");
		verify.addActionListener(this);
		super.add(verify);
		
		this.updateMenu();
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("EDIT")) {
			this.editClass();
		} else if (command.equals("OPTIMIZE")) {
		 	this.optimize();
		} else if (command.equals("VERIFY")) {
			this.verify();
		}
	}
	
	private void editClass() {
		ClassFrame.getCurrentFrame().editClass();
	}
	
	private void optimize() {
		OptimizationSetupDialog osd = new OptimizationSetupDialog();
		osd.setVisible(true);
		
		JavaClass currentClass = BMEnvironment.getCurrentClass();
		
		int result = osd.getResult();
		if ((result & OptimizationSetupDialog.OPTIMIZE_CONSTANT_POOL) != 0) {
			/* REMOVED ConstantPoolOptimizer cp_optimizer = new ConstantPoolOptimizer();
			Object[] optimizedConstants = cp_optimizer.optimizeAll(
				currentClass.getPoolManager().getConstants()
			);
			
			if (cp_optimizer.isOptimized()) {
				currentClass.setConstants(optimizedConstants);
				UsefulMessageDialogs.doInfoDialog("Constant Pool: Optimization complete");
				
				// update frames
				//ClassFrame.getCurrentFrame().update();
				//JavaConstantPoolCustomizer.getCurrentFrame().update();
			} else {
				UsefulMessageDialogs.doInfoDialog("Constant Pool: No optimizations made");
			}*/
			UsefulMessageDialogs.doInfoDialog("Constant Pool: No optimizations made - IN PROGRESS");
		}
		if ((result & OptimizationSetupDialog.OPTIMIZE_BYTECODES) != 0) {
			/* REMOVED BytecodeOptimizer bc_optimizer = new BytecodeOptimizer();
			bc_optimizer.optimizeAll(currentClass.getMethods());
			if (bc_optimizer.isOptimized()) {
				UsefulMessageDialogs.doInfoDialog("Bytecodes: Optimization complete");
			} else {
				UsefulMessageDialogs.doInfoDialog("Bytecodes: No optimizations made");
			}*/
			UsefulMessageDialogs.doInfoDialog("Bytecodes: No optimizations made - IN PROGRESS");
		}
	}
	
	private void verify() {
		// ... IMPLEMENT in Bytecode Maker 0.6.0
	}
	
	public void updateMenu() {
		if (BMEnvironment.getCurrentClass() == null) {
			editClass.setEnabled(false);
			optimize.setEnabled(false);
			verify.setEnabled(false);
			
			super.setEnabled(false);
		} else {
			editClass.setEnabled(true);
			optimize.setEnabled(true);
			verify.setEnabled(true);
			
			super.setEnabled(true);
		}
	}
	
}
