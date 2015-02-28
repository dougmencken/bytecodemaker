// ===========================================================================
//	OptimizationSetupDialog.java (part of douglas.mencken.bm.frames package)
// ===========================================================================

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.LWCheckbox;
import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>OptimizationSetupDialog</code>
 *
 *	@version 0.50a2
 */

public class OptimizationSetupDialog extends Dialog implements ActionListener {
	
	private LWCheckbox optimizeConstantPool;
	private LWCheckbox optimizeBytecodes;
	private int result = 0;
	
	public static final int OPTIMIZE_CONSTANT_POOL = 0x00000100;
	public static final int OPTIMIZE_BYTECODES = 0x00000200;
	
	public OptimizationSetupDialog() {
		super(new InvisibleFrame(), "Optimization Setup", true);
		super.setBackground(Color.white);
		
		super.setSize(300, 100);
		this.initComponents();
		this.addComponents();
		
		super.setResizable(false);
		super.setLocation(WindowUtilities.getCenterLocation(this));
	}
	
	private void initComponents() {
		this.optimizeConstantPool = new LWCheckbox("Optimize Constant Pool");
		optimizeConstantPool.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		this.optimizeBytecodes = new LWCheckbox("Optimize Bytecodes");
		optimizeBytecodes.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
	}
	
	private void addComponents() {
		Panel optimizersPanel = new Panel(new GridLayout(2, 1));
		optimizersPanel.add(optimizeConstantPool);
		optimizersPanel.add(optimizeBytecodes);
		
		int bdm_width = optimizeConstantPool.getPreferredSize().width;
		int bdm_height = optimizeConstantPool.getPreferredSize().height;
		optimizersPanel.setSize(bdm_width + 5, (bdm_height - 3)*2);
		optimizersPanel.setLocation(10, 10);
		
		Panel centerPanel = new Panel(null);
		centerPanel.add(optimizersPanel);
		
		Button optimizeAllButton = new Button("Optimize All");
		optimizeAllButton.setSize(WindowUtilities.WIDE_BUTTON_DIMENSION);
		optimizeAllButton.setActionCommand("ALL");
		optimizeAllButton.addActionListener(this);
		
		Panel buttonPanel = WindowUtilities.createOKCancelButtonsPanel(
			this,
			new Dimension(
				super.getSize().width,
				WindowUtilities.BUTTON_DIMENSION.height*2 + 5
			),
			optimizeAllButton
		);
		
		super.add("Center", centerPanel);
		super.add("South", buttonPanel);
	}
	
	public int getResult() {
		return this.result;
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("OK")) {
			//ConstantPoolFrame.getCurrentFrame().setVisible(false);
			//ViewMenu.updateMenu();
			this.result = 0;
			
			if (optimizeConstantPool.getState()) {
				this.result = this.result^OPTIMIZE_CONSTANT_POOL;
			}
			if (optimizeBytecodes.getState()) {
				this.result = this.result^OPTIMIZE_BYTECODES;
			}
			super.dispose();
		} else if (command.equals("CANCEL")) {
			this.result = 0;
			super.dispose();
		} else if (command.equals("ALL")) {
			optimizeConstantPool.setState(true);
			optimizeBytecodes.setState(true);
		}
	}
	
}