// ===========================================================================
//	JVMInstructionSetEditor.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import douglas.mencken.beans.*;
import douglas.mencken.util.ByteTransformer;

/**
 *	<code>JVMInstructionSetEditor</code>
 *	
 *	@version	0.6d0
 *	@since		Bytecode Maker 0.6.0
 */

public class JVMInstructionSetEditor {
	
	// ...
	
}



// ===========================================================================
//	JVMInstructionSetEditor.java (part of douglas.mencken.bm.frames package)
//		public class JVMInstructionSetEditor
//		class CodeEditorMenuBar
// ===========================================================================

/*
public class JVMInstructionSetEditor extends Frame
implements ItemListener, WindowListener {
	
	MethodItem method;
	private StackFrame stackWindow = null;
	private LocalVariablesFrame localsWindow = null;
	private TextViewSelectableLine textView;
	
	private boolean branchesHighlighted = false;
	private boolean invokesHighlighted = false;
	
	private static WindowTracker windowTracker = new WindowTracker();
	
	// ------------------------------------------------------------------------------
	
	public JVMInstructionSetEditor(MethodItem method) {
		super("");
		
		if (method != null) {
			super.setTitle(method.getDeclaration(false));
			this.method = method;
		} else {
			super.setTitle("null");
			this.method = null;
		}
		
		addWindowListener(this);
		setResizable(false);
		
		super.setLayout(new BorderLayout());
		this.updateContents();
	}
	
	public void updateContents() {
		super.removeAll();
		
		this.textView = new TextViewSelectableLine();
		this.textView.addItemListener(this);
		Dimension screen = getToolkit().getScreenSize();
		
		Label label = null;
		
		if (this.method == null) {
			textView.appendln("* null *");
			label = new Label("* null *", Label.CENTER);
			
			label.setFont(new Font("Geneva", Font.BOLD, 10));
			label.setForeground(Color.red);
		} else {
			if (!this.method.isAbstractOrNative()) {
				int[] pcValues = method.obtainPCValues();
				
				super.add("West", new NumbersView(textView, pcValues));
				textView.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
				
				BytecodeItem[] bytecodes = method.getBytecodes();
				int codelen = bytecodes.length;
				
				StringBuffer buf = new StringBuffer();
				for (int j = 0; j < codelen; j++) {
					if (bytecodes[j] != null) {
						buf.append(DecompilerUtilities.toString(bytecodes[j])).append('\n');
					}
				}
				textView.append(buf.toString());
				
				super.add("Center", textView);
				super.setSize((int)(screen.width/2), (int)(screen.height/2));
			} else {
				textView.appendln("No code");
				label = new Label("No code", Label.CENTER);
				
				label.setFont(new Font("Geneva", Font.BOLD, 10));
				label.setForeground(Color.darkGray);
			}
		}
		
		if (label != null) {
			super.add("Center", label);
			super.setSize((int)(screen.width/2), 50);
		}
		
		new CodeEditorMenuBar(this);
		super.invalidate();
		super.validate();
	}
	
	public String getContents() {
		return textView.getContents();
	}
	
	public MethodItem getMethod() {
		return this.method;
	}
	
	void highlightBranches() {
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		NumbersView view = this.textView.getNumbersView();
		byte[] opcodes = this.method.getOpcodes();
		
		int len = opcodes.length;
		int i = 0;
		while (i < len) {
			if (BytecodeItem.isBranchBytecode(opcodes[i], true)) {
				if (branchesHighlighted) {
					textView.dehighlightLine(view.getIndexByValue(i));
				} else {
					textView.highlightLine(view.getIndexByValue(i));
				}
			}
			
			i = this.method.nextOpcodeIndex(i);
		}
		
		// toggle
		branchesHighlighted = !branchesHighlighted;
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	void highlightInvokes() {
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		NumbersView view = this.textView.getNumbersView();
		byte[] opcodes = this.method.getOpcodes();
		
		int len = opcodes.length;
		int i = 0;
//		while (i < len) {
//			byte opcode = opcodes[i];
//			if ((opcode == -74 /* 182 */ //) || (opcode == -73 /* 183 */) ||
//					(opcode == -72 /* 184 */) || (opcode == -71 /* 185 */)) {
//				if (invokesHighlighted) {
//					textView.dehighlightLine(view.getIndexByValue(i));
//				} else {
//					textView.highlightLine(view.getIndexByValue(i));
//				}
//			}
//			
//			i = this.method.nextOpcodeIndex(i);
//		}
		
		// toggle
//		invokesHighlighted = !invokesHighlighted;
//		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//	}
	
/*	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public void windowClosing(WindowEvent e) {
		if (stackWindow != null) stackWindow.dispose();
		if (localsWindow != null) localsWindow.dispose();
		windowTracker.closeWindow(this);
	}
	
	void setStackWindowVisible(boolean newVisible) {
		if ((stackWindow == null) && newVisible) {
			stackWindow = new StackFrame(this);
		}
		
		if (stackWindow != null) {
			stackWindow.setVisible(newVisible);
		}
		
		this.itemStateChanged(null);
	}
	
	void setLocalVariablesWindowVisible(boolean newVisible) {
		if ((localsWindow == null) && newVisible) {
			localsWindow = new LocalVariablesFrame(this);
		}
		
		if (localsWindow != null) {
			localsWindow.setVisible(newVisible);
		}
		
		this.itemStateChanged(null);
	}
	
	boolean isStackWindowVisible() {
		return (stackWindow != null) ? stackWindow.isVisible() : false;
	}
	
	boolean isLocalVariablesWindowVisible() {
		return (localsWindow != null) ? localsWindow.isVisible() : false;
	}*/
	
//	public int getSelectedPC() {
//		Object[] selected = textView.getSelectedObjects();
//		
//		if (selected != null) {
//			int pc = Integer.parseInt((String)selected[1]);
//			return pc;
//		} else {
//			return -1;
//		}
//	}
	
//	public void updateMenus() {
//		CodeEditorMenuBar mbar = (CodeEditorMenuBar)super.getMenuBar();
//		mbar.updateMenu();
//	}
	
//	public void itemStateChanged(ItemEvent evt) {
//		int pc = this.getSelectedPC();
//		if (pc < 0 /* initially */) {
//			return;
//		}
//		
//		try {
//			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//			
//			if (stackWindow != null) {
//				stackWindow.updateValues(pc);
//			}
//			if (localsWindow != null) {
//				localsWindow.updateValues(pc);
//			}
//			
//			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//		} catch (OutOfMemoryError noRAM) {
//			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//			UsefulModalDialogs.tellAboutNoMemoryAvailable(true);
//			
//			if (stackWindow != null) {
//				this.stackWindow.dispose();
//				this.stackWindow = null;
//			}
//			
//			if (localsWindow != null) {
//				this.localsWindow.dispose();
//				this.localsWindow = null;
//			}
//			
//			this.updateMenus();
//		}
//	}
//	
//	public static void showEditor(MethodItem method, int index) {
//		JVMInstructionSetEditor JVMInstructionSetEditor = new JVMInstructionSetEditor(method);
//		JVMInstructionSetEditor.setLocation(30, 30);
//		windowTracker.showWindow(String.valueOf(index), JVMInstructionSetEditor);
//	}
//	
//	public static void hideAllEditors() {
//		windowTracker.closeAll();
//	}
//	
//}

/*
class CodeEditorMenuBar extends MenuBar implements ActionListener {
	
	private JVMInstructionSetEditor owner;
	private CheckboxMenuItem stackWindow;
	private CheckboxMenuItem localsWindow;
	private Frame infoWindow;
	
	private Menu file;
	private Menu options;
	private Menu view;
	
	CodeEditorMenuBar(JVMInstructionSetEditor frame) {
		boolean noCode = (frame.method == null) || frame.method.isAbstractOrNative();
		
		this.file = new Menu("File");
		MenuItem save = new MenuItem("Save...", new MenuShortcut(KeyEvent.VK_S));
		save.setActionCommand("SAVE");
		save.addActionListener(this);
		file.add(save);
		MenuItem close = new MenuItem("Close", new MenuShortcut(KeyEvent.VK_W));
		close.setActionCommand("CLOSE");
		close.addActionListener(this);
		file.add(close);
		
		this.options = new Menu("Options");
		if (noCode) options.setEnabled(false);
		MenuItem move = new MenuItem("Move...", new MenuShortcut(KeyEvent.VK_M));
		if (noCode) move.setEnabled(false);
		move.setActionCommand("MOVE");
		move.addActionListener(this);
		options.add(move);
		
		MenuItem edit = new MenuItem("Edit...", new MenuShortcut(KeyEvent.VK_E));
		if (noCode) edit.setEnabled(false);
		edit.setActionCommand("EDIT");
		edit.addActionListener(this);
		options.add(edit);
		
		options.addSeparator();
		
		MenuItem insert = new MenuItem("Insert...");
		if (noCode) insert.setEnabled(false);
		insert.setActionCommand("INSERT");
		insert.addActionListener(this);
		options.add(insert);
		
		MenuItem remove = new MenuItem("Remove");
		if (noCode) remove.setEnabled(false);
		remove.setActionCommand("REMOVE");
		remove.addActionListener(this);
		options.add(remove);
		
		options.addSeparator();
		
		CheckboxMenuItem branchesHL = new CheckboxMenuItem("Highlight Branches");
		if (noCode || !frame.method.hasBranchBytecodes(true)) {
			branchesHL.setEnabled(false);
		}
		branchesHL.setActionCommand("HL_BRANCHES");
		branchesHL.addActionListener(this);
		options.add(branchesHL);
		
		CheckboxMenuItem invokesHL = new CheckboxMenuItem("Highlight Invokes");
		if (noCode || !frame.method.hasInvokeBytecodes()) {
			invokesHL.setEnabled(false);
		}
		invokesHL.setActionCommand("HL_INVOKES");
		invokesHL.addActionListener(this);
		options.add(invokesHL);
		
		this.view = new Menu("View");
		if (noCode) view.setEnabled(false);
		
		this.stackWindow = new CheckboxMenuItem("Stack");
		if (noCode) stackWindow.setEnabled(false);
		stackWindow.setShortcut(new MenuShortcut(KeyEvent.VK_4));
		stackWindow.setActionCommand("STACK_W");
		stackWindow.addActionListener(this);
		view.add(stackWindow);
		
		this.localsWindow = new CheckboxMenuItem("Local Variables");
		if (noCode) localsWindow.setEnabled(false);
		localsWindow.setShortcut(new MenuShortcut(KeyEvent.VK_5));
		localsWindow.setActionCommand("LOCALS_W");
		localsWindow.addActionListener(this);
		view.add(localsWindow);
		
		view.addSeparator();
		
		MenuItem infoWindow = new MenuItem("Information");
		if (noCode) infoWindow.setEnabled(false);
		infoWindow.setShortcut(new MenuShortcut(KeyEvent.VK_I));
		infoWindow.setActionCommand("INFO_W");
		infoWindow.addActionListener(this);
		view.add(infoWindow);
		
		super.add(this.file);
		super.add(this.options);
		super.add(this.view);
		
		frame.setMenuBar(this);
		this.owner = frame;
		
		this.updateMenu();
	}
	
	public void updateMenu() {
		if (owner.method != null) {
			this.options.setEnabled(true);
			this.view.setEnabled(true);
			
			if (owner.method.getMaxStack() == 0) {
				owner.setStackWindowVisible(false);
				stackWindow.setEnabled(false);
				stackWindow.setState(false);
			} else {
				stackWindow.setEnabled(true);
				stackWindow.setState(owner.isStackWindowVisible());
			}
			
			if (owner.method.getMaxLocals() == 0) {
				owner.setLocalVariablesWindowVisible(false);
				localsWindow.setEnabled(false);
				localsWindow.setState(false);
			} else {
				localsWindow.setEnabled(true);
				localsWindow.setState(owner.isLocalVariablesWindowVisible());
			}
			
			if ((!owner.isVisible()) && (this.infoWindow != null)) {
				this.infoWindow.dispose();
			}
		} else {
			this.options.setEnabled(false);
			this.view.setEnabled(false);
		}
	}*/
	
	/*
	public void actionPerformed(ActionEvent evt) {
		String c = evt.getActionCommand();
		if (c.equals("CLOSE")) {
			owner.windowClosing(null);
		} else if (c.equals("SAVE")) {
			String name = "dump";
			if (owner.method != null) {
				name = owner.method.getMethodName() + ".dump";
			}
			String path = SaveDialog.showSaveFileDialog(name);
			
			if (path != null) {
				AutoTypeCreatorFixer.getCurrentFixer().add(new File(path), "TEXT", "ByTe");
				FileUtilities.writeBytesToFile(owner.getContents().getBytes(), path);
			}
		} else if (c.equals("MOVE")) {
			// ...
		} else if (c.equals("EDIT")) {
			// ... new xxxEditor(xxx);
		} else if (c.equals("INSERT")) {
			// ...
		} else if (c.equals("REMOVE")) {
			// ...
		} else if (c.equals("HL_BRANCHES")) {
			owner.highlightBranches();
		} else if (c.equals("HL_INVOKES")) {
			owner.highlightInvokes();
		} else if (c.equals("STACK_W")) {
			owner.setStackWindowVisible(stackWindow.getState());
		} else if (c.equals("LOCALS_W")) {
			owner.setLocalVariablesWindowVisible(localsWindow.getState());
		} else if (c.equals("INFO_W")) {
			if (this.infoWindow != null) {
				this.infoWindow.dispose();
			}
			
			int xpos = owner.getLocation().x;
			int ypos = owner.getLocation().y + 60;
			this.infoWindow = makeInformationFrame(owner.getMethod(), new Point(xpos, ypos));
			this.infoWindow.setVisible(true);
		}
	}
	*/
	
	/**
	 *	Information Frame displays for the method:
	 *	max stack & max locals,
	 *	flags (synthetic, deprecated),
	 *	local variables,
	 *	catches
	 */
	/*static Frame makeInformationFrame(MethodItem method, Point location) {
		if (method == null) return null;
		
		Frame infoFrame = new Frame(method.getNameWithParameters() + ": Information");
		new WindowCloser(infoFrame);
		infoFrame.setLocation(location);
		
		TextView outView = new TextView();
		outView.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		StringBuffer buf = new StringBuffer();
		
		buf.append(method.getJVMSignature()).append("\n\n");
		
		int maxstack = method.getMaxStack();
		int maxlocals = method.getMaxLocals();
		buf.append("max stack = ").append(maxstack).append('\n');
		buf.append("max locals = ").append(maxlocals);
		buf.append(" (parameter count = ").append(method.getParameterCount()).append(")\n\n");
		
		boolean newLine = false;
		if (method.isSynthetic()) {
			buf.append("Synthetic\n");
			newLine = true;
		}
		if (method.isDeprecated()) {
			buf.append("Deprecated\n");
			newLine = true;
		}
		
		if (newLine) {
			buf.append('\n');
			newLine = false;
		}*/
		
//		LocalVariableItem[] locals = method.getLocalVariables();
//		if (locals.length != 0) {
//			int llen = locals.length;
			
//			for (int j = 0; j < llen; j++) {
//				if (locals[j] != null) {
//					buf.append(locals[j]);
//					buf.append('\n');
//				}
//			}
			
//			buf.append('\n');
//		}
		
//		TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
//		if (catches.length != 0) {
//			int clen = catches.length;
			
//			for (int j = 0; j < clen; j++) {
				// start_pc, end_pc, handler_pc
//				int[] pcValues = catches[j].getPCValues();
//				String catchType = catches[j].getCatchType();
				
//				buf.append("try {\n");
//				buf.append("    /* ");
//				buf.append(pcValues[0] /* start_pc */);
//				buf.append(" to ");
//				buf.append(pcValues[1] /* end_pc */);
//				buf.append(" */\n");
				
//				if (catchType.equals("finally")) {
//					buf.append("} finally (\n");
//				} else {
//					buf.append("} catch (").append(catchType).append(") {\n");
//				}
				
//				buf.append("    goto ");
//				buf.append(pcValues[2] /* handler_pc */);
//				buf.append(";\n");
//				buf.append("}");
				
//				buf.append('\n');
//			}
//		}
		
//		outView.appendln(buf.toString());
//		infoFrame.setLayout(new BorderLayout());
//		infoFrame.add("Center", outView);
		
//		infoFrame.setSize(380, 260);
//		infoFrame.setResizable(false);
//		
//		return infoFrame;
//	}
	
//}

//public class JavaBytecodeCustomizer extends Panel
//implements Customizer, ItemListener {
	
//	private PropertyChangeSupport propertyChangeSupport;
//	private JVMInstruction instruction;
//	private Choice mnemonicSelector;
	
	/*public static Choice makeMnemonicSelector() {
		Choice mnemonicSelector = new Choice();
		
		int mnemonic_count = JVMInstructionSet.MNEMONICS.length;
		for (int i = 0; i < mnemonic_count; i++) {
			if (!JVMInstructionSet.MNEMONICS[i].equals("* unknown *")) {
				mnemonicSelector.add(makeBytecodeString(JVMInstructionSet.MNEMONICS[i], i));
			} else {
				mnemonicSelector.add(makeBytecodeString("-", i));
			}
		}
		
		mnemonicSelector.setFont(new Font("Monaco", Font.PLAIN, 9));
		mnemonicSelector.setBounds(80, 20, 250, 20);
		
		return mnemonicSelector;
	}*/
	
	/**
	 *	The constructor.
	 */
	/*public JavaBytecodeCustomizer() {
		super(null);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.mnemonicSelector = makeMnemonicSelector();
		this.mnemonicSelector.addItemListener(this);
		super.add(this.mnemonicSelector);
		
		LWLabel opcodeLabel = new LWLabel("Opcode: ", LWLabel.LEFT);
		opcodeLabel.setLocation(5, 20);
		super.add(opcodeLabel);
		
		super.setSize(this.getPreferredSize());
	}
	
	private static String makeBytecodeString(String bytecode, int opcode) {
		String decOpcode = Integer.toString(opcode, 10);
		String hexOpcode = Integer.toString(opcode, 16).toUpperCase();
		
		if (decOpcode.length() == 2) {
			decOpcode += " ";
		} else if (decOpcode.length() == 1) {
			decOpcode += "  ";
		}
		
		if (hexOpcode.length() == 1) {
			hexOpcode += " ";
		}
		
		return '#' + decOpcode + " $" + hexOpcode + "  " + bytecode;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(400, 50);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		propertyChangeSupport.removePropertyChangeListener(pcl);
	}
	
	public void setObject(Object bean) {
		if (bean instanceof JavaBytecode) {
			this.bytecode = (JavaBytecode)bean;
			
			int opcode = ByteTransformer.toUnsignedByte(this.bytecode.getOpcode());
			this.mnemonicSelector.select(opcode);
			
			// ...
		} else {
			throw new IllegalArgumentException("bad object");
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		String oldBytecode = bytecode.toString();
		
		if (!this.mnemonicSelector.getSelectedItem().endsWith("-") &&
				!this.mnemonicSelector.getSelectedItem().endsWith("*")) {
			// bytecode.setPCValue(...);
			
			bytecode.setOpcode((byte)this.mnemonicSelector.getSelectedIndex());
			
			// bytecode.setBytecode(...);
			// bytecode.setArg(...);
			// bytecode.setArg2(...);
			// bytecode.setArg3(...);
			// bytecode.setAdditionalArgs(additionalArgs);
		} else {
			super.getToolkit().beep();
		}
		
		String newBytecode = bytecode.toString();
		
		propertyChangeSupport.firePropertyChange("JavaBytecode", oldBytecode, newBytecode);
	}*/
//	
//}
