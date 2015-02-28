// ===========================================================================
//	MethodDecompiler.java (part of douglas.mencken.bm.decompiler package)
// ===========================================================================

package douglas.mencken.bm.decompiler;

import java.util.EmptyStackException;
import java.lang.reflect.Modifier;

import douglas.mencken.util.ByteTransformer;
import douglas.mencken.util.SpecialStack;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.exceptions.InvalidClassFormatError;
import douglas.mencken.bm.storage.*;
//////////////////import douglas.mencken.bm.engine.BytecodeLocksmith;
import douglas.mencken.bm.engine.Unpackager;
import douglas.mencken.bm.engine.StackCalculator;

/**
 *	<code>MethodDecompiler</code>
 *	
 *	@version  0.63d4
 */

public class MethodDecompiler extends Object implements Decompiler {
	
	private JavaMethod method;
/////////////	private BytecodeBlock[] bytecodeBlocks;
	private boolean[] declaredVars;
	
	private int spaceCount;
	private StringBuffer buf;
	
	// -----------------------------------------------------------------------------------
	
	/**
	 *	Initializes a new <code>MethodDecompiler</code> object.
	 *
	 *	@param		method			the method to decompile.
	 *	@param		spaceCount		the number of spaces to be one <i>tab</i>.
	 */
	public MethodDecompiler(JavaMethod method, int spaceCount) {
		if (method.getOwnerClass() == null) {
			throw new IllegalArgumentException("method's owner is 'null'");
		}
		
		this.method = method;
		this.declaredVars = new boolean[method.getLocalVariables().length];
		this.spaceCount = spaceCount;
		this.buf = new StringBuffer();
		
// System.out.println("----- DECOMPILING: \"" + method.getNameWithParameters() + "\" -----");
		
		try {
			this.bytecodeBlocks = MethodDecompiler.prepareBytecodes(method);
			this.decompile();
		} catch (BadBytecodesException bbe) {
			UsefulModalDialogs.doErrorDialog(
				"BadBytecodesException (caught in MethodDecompiler for method '" +
				method.getNameWithParameters() + "'): " + bbe.getMessage()
			);
			this.bytecodeBlocks = null;
			return;
		}
	}
	
	protected void doDeclaration() {
		StringUtilities.addSpaces(buf, spaceCount);
		
		if (method.isSynthetic()) {
			buf.append("// ");
		}
		
		String methodName = method.getMethodName();
		if (methodName.equals("<clinit>")) {
			buf.append("static");
			return;
		}
		
		int access_flags = method.getAccess();
		if (method.getOwner().isInterface()) {
			access_flags = access_flags^Modifier.ABSTRACT;
		}
		String accessString = Modifier.toString(access_flags);
		if (accessString != "") {
			buf.append(accessString);
			buf.append(' ');
		}
		
		Unpackager unpackager = Unpackager.getCurrentUnpackager();
		if (!methodName.equals("<init>")) {
			buf.append(unpackager.unpackage(method.getReturnType())).append(' ');
			buf.append(methodName);
		} else {
			buf.append(unpackager.unpackage(method.getOwner().getClassName()));
		}
		
		LocalVariableItem[] locals = method.getLocalVariables();
		buf.append("(");
		buf.append(LocalVariableItem.getParameterList(locals, true));
		buf.append(")");
		
		// unpackaged 'throws ...' string
		String[] exceptions = method.getExceptions();
		int excCount = exceptions.length;
		if (excCount > 0) {
			for (int i = 0; i < excCount; i++) {
				exceptions[i] = unpackager.unpackage(exceptions[i]);
			}
			buf.append(JavaMethod.makeThrowsString(exceptions));
		}
	}
	
	protected void decompile() { //////////////////////throws BadBytecodesException {
		if (method.isDeprecated()) {
			StringUtilities.addSpaces(buf, spaceCount);
			buf.append("/**\n");
			StringUtilities.addSpaces(buf, spaceCount+1);
			buf.append("* @deprecated\n");
			StringUtilities.addSpaces(buf, spaceCount+1);
			buf.append("*/\n");
		}
		
		this.doDeclaration();
		
		if (method.isAbstractOrNative()) {
			buf.append(";\n");
			return;
		}
		
		int codeLength = method.getCodeLength();
		if (codeLength <= 1) {
			// Empty method
			buf.append(" {}\n");
			return;
		}
		
		buf.append(" {\n");
		
/******************************************
		TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
		boolean hasBranches = method.hasBranchBytecodes(true);
		BlockDecompiler decompiler;
		int lastPC = method.calculateLastPC();
		
		if ((catches.length == 0) && (!hasBranches)) {
			// Simple flat method
			decompiler = new BlockDecompiler(
				method, bytecodeBlocks[0],
				0, method.calculateLastPC(),
				2, spaceCount,
				this.declaredVars
			);
			buf.append(decompiler.getString());
		} else { // (catches.length != 0) || hasBranches
			int firstFlatEnd = getFirstFlatBlockEnd(bytecodeBlocks[0].getBytecodes());
			int lastFlatStart = getLastFlatBlockStart(bytecodeBlocks[0].getBytecodes());
			
			if (firstFlatEnd > 0) {
				decompiler = new BlockDecompiler(
					method, bytecodeBlocks[0], 0, firstFlatEnd,
					2, spaceCount,
					this.declaredVars
				);
				
				String result = decompiler.getString();
				if (result.length() > 0) {
					buf.append(result);
					buf.append('\n');
				}
			}
			
			StackCalculator s = method.getStackCalculator();
	//////	//////	BytecodeItem[] mainBytecodes = bytecodeBlocks[0].getBytecodes();
			boolean inProgress = false;
			
			for (int pc = (firstFlatEnd + 1); pc < lastFlatStart; pc++) {
				BytecodeItem bytecode = mainBytecodes[pc];
				if (bytecode != null) {
					byte opcode = bytecode.getOpcode();
					switch (opcode) {
						// tableswitch
						// lookupswitch
						case -86: // 170
						case -85: // 171
						{
							int at = BytecodeBlock.calculatePreviousPC(mainBytecodes, pc);
							SpecialStack stack = s.getStackAt(at);
							String stackValue = (String)stack.pop();
							
							StringUtilities.addSpaces(buf, spaceCount*2);
							buf.append("switch (").append(stackValue).append(") {\n");
							
							// the offset for 'default' block
							int defaultOffset = Integer.parseInt(bytecode.getArg());
							int[][] allValues = JavaSourceBlock.extractValuesFromSwitch(bytecode);
							int[] matchValues = allValues[0];
							int[] offsetValues = allValues[1];
							int valueCount = offsetValues.length;
							
							int breakPC = JavaSourceBlock.findBlockEnd(
								JavaSourceBlock.BLOCK_TYPE_switch,
								mainBytecodes,
								pc
							);
							
							for (int ii = 0; ii < valueCount; ii++) {
								if (offsetValues[ii] != defaultOffset) {
									StringUtilities.addSpaces(buf, spaceCount*3);
									buf.append("case ").append(matchValues[ii]);
									buf.append(":\n");
									
									int endPC = defaultOffset + pc;
									
									if ((ii + 1) < valueCount) {
										endPC = offsetValues[ii + 1] + pc;
									}
									endPC = BytecodeBlock.calculatePreviousPC(
										mainBytecodes, endPC
									);
									
									boolean append_break = false;
									BytecodeItem endBytecode = mainBytecodes[endPC];
									if (BytecodeItem.isBranchBytecode(endBytecode.getOpcode(), true)) {
										endPC = BytecodeBlock.calculatePreviousPC(
											mainBytecodes, endPC
										);
										if (endBytecode.getOpcode() == -89) { // goto
											append_break = true;
										}
									}
									
									decompiler = new BlockDecompiler(
										method, bytecodeBlocks[0],
										offsetValues[ii] + pc, endPC,
										4, spaceCount,
										this.declaredVars
									);
									
									String result = decompiler.getString();
									if (result.length() > 0) buf.append(result);
									
									if (append_break) {
										StringUtilities.addSpaces(buf, spaceCount*4);
										buf.append("break;\n");
									}
								}
							}
							
							StringUtilities.addSpaces(buf, spaceCount*3);
							buf.append("default:\n");
							
							decompiler = new BlockDecompiler(
								method, bytecodeBlocks[0],
								defaultOffset + pc,
								BytecodeBlock.calculatePreviousPC(mainBytecodes, breakPC),
								4, spaceCount,
								this.declaredVars
							);
							String result = decompiler.getString();
							if (result.length() > 0) buf.append(result);
							
							StringUtilities.addSpaces(buf, spaceCount*2);
							buf.append("}\n");
							
							pc = breakPC;
							
							break;
						}
						
						// ...
						// case xxx:
						// ...
						
						default:
							inProgress = true;
							break;
					}
				}
			}
			
			if (inProgress) {
				StringUtilities.addSpaces(buf, spaceCount*2);
				buf.append("// (in progress)\n");
			}
			
			if (lastFlatStart < lastPC) {
				decompiler = new BlockDecompiler(
					method, bytecodeBlocks[0], lastFlatStart, lastPC,
					2, spaceCount,
					this.declaredVars
				);
				
				String result = decompiler.getString();
				if (result.length() > 0) {
					buf.append('\n');
					buf.append(result);
				}
			}
		}
*******************************************/
		
		StringUtilities.addSpaces(buf, spaceCount);
		if (method.isSynthetic()) {
			buf.append("// ");
		}
		
		buf.append("}\n");
	}
	
	public String getString() {
		return buf.toString();
	}
	
	
	public static JVMInstructionSet[] prepareBytecodes(JavaMethod method) { ////// throws BadBytecodesException {
//////////		BytecodeItem[] bytecodes = BytecodeLocksmith.extractBytecodes(method);
/////////		if (bytecodes == null) return null;
		return null;
		
/***********************************************
		bytecodes = BytecodeLocksmith.tryResolve_goto_return(bytecodes, method.getJVMSignature());
		bytecodes = BytecodeLocksmith.tryResolve_goto_Chains(bytecodes, false);
		
		BytecodeItem bytecode;
		int codeLength = bytecodes.length - 1;
		StackCalculator s = method.getStackCalculator();
		
		for (int i = 0; i <= codeLength; i++) {
			bytecode = bytecodes[i];
			if (bytecode != null) {
				int pc = bytecode.getPCValue();
				int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
				
				switch (opcode) {
					// new
					case 187:
					{
						if (bytecode.getAdditionalArgs() == null) {
							bytecodes[i] = null;
							
							// remove 'dup' after
							int nextpc = BytecodeBlock.calculateNextPC(
								bytecodes, pc, codeLength
							);
							if (bytecodes[nextpc].getOpcode() == 89) { // dup
								bytecodes[nextpc] = null;
							}
						}
						break;
					}
					
					// newarray, anewarray
					case 188:
					case 189:
					{
						int pc_before = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
						SpecialStack stack = s.getStackAt(pc_before);
						String numberOfElements = (String)stack.pop();
						
						String arg2 = bytecode.getArg2();
						if (opcode == 189) {
							arg2 = StringUtilities.removeFirstLastSymbols(arg2);
						}
						
						bytecodes[i] = new BytecodeItem(
							pc, (byte)188, arg2, numberOfElements
						);
						
						break;
					}
					
					// ifxx
					// ifnull, ifnonull
					case 153: case 154: case 155: case 156: case 157: case 158:
					case 198: case 199:
					{
						int pc_before = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
						SpecialStack stack = s.getStackAt(pc_before);
						String value = (String)stack.pop();
						
						bytecodes[i] = new BytecodeItem(
							pc, (byte)opcode, value, bytecode.getArg()
						);
						// System.out.println("ifxx: " + bytecode);
						break;
					}
					
// 					// xcmpx
// 					case 148: case 149: case 150: case 151: case 152:
// 					{
// 						int pc_before = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
// 						SpecialStack stack = s.getStackAt(pc_before);
// 						String value2 = (String)stack.pop();
// 						String value1 = (String)stack.pop();
// 						
// 						bytecodes[i] = new BytecodeItem(pc, 148, value1, value2);
//						bytecodes[i].setBytecode("cmp");
//						
// 						// System.out.println("xcmpx: " + bytecode);
// 						break;
// 					}
					
					// xcmpx
					case 148: case 149: case 150: case 151: case 152:
					{
						bytecodes[i] = null;
						break;
					}
					
					// if_icmpxx, if_acmpeq, if_acmpne
					case 159: case 160: case 161: case 162: case 163: case 164:
					case 165: case 166:
					{
						int pc_before = BytecodeBlock.calculatePreviousPC(bytecodes, pc);
						SpecialStack stack = s.getStackAt(pc_before);
						String value2 = (String)stack.pop();
						String value1 = (String)stack.pop();
						
						bytecodes[i] = new BytecodeItem(
							pc, (byte)opcode, value1, value2, bytecode.getArg()
						);
						// System.out.println("if_xcmpxx: " + bytecode);
						
						break;
					}
				}
			}
		}
		
		BytecodeBlock[] blocks = DecompilerUtilities.splitSubroutines(
			new BytecodeBlock(bytecodes)
		);
		
		return blocks;
**********************************************/
	}
	
	/**
	 *	Flat bytecode block: no branches, no loops, no subrotines.
	 *	First flat block: from_pc = 0.
	 *	(static method)
	 *
	 *	@see	#getLastFlatBlockStart
	 */
//&&&&&&&&	public static int getFirstFlatBlockEnd(final BytecodeItem[] bytecodes) {
//&&&&&&&&		if (bytecodes == null) {
//&&&&&&&&			throw new IllegalArgumentException("bytecodes is 'null'");
//&&&&&&&&		}
//&&&&&&&&		
//&&&&&&&&		int lastpc = BytecodeBlock.calculatePreviousPC(bytecodes, bytecodes.length);
//&&&&&&&&		int[] ffbEnds = new int[0];
//&&&&&&&&		
//&&&&&&&&		for (int i = 0; i <= lastpc; i++) {
//&&&&&&&&			BytecodeItem current = bytecodes[i];
//&&&&&&&&			if (current != null) {
//&&&&&&&&				int pcvalue = current.getPCValue();
//&&&&&&&&				int opcode = ByteTransformer.toUnsignedByte(current.getOpcode());
//&&&&&&&&				
//&&&&&&&&				switch (opcode) {
//&&&&&&&&					/* ifxx */ case 153: case 154: case 155: case 156: case 157: case 158:
//&&&&&&&&					/* ifnull, ifnonull*/ case 198: case 199:
//&&&&&&&&					{
//&&&&&&&&						int offset = Integer.parseInt(current.getArg2());
//&&&&&&&&						int ffb_end = (offset < 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&						ffbEnds = ArrayUtilities.addElement(ffbEnds, ffb_end);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* if_icmpxx */ case 159: case 160: case 161: case 162: case 163: case 164:
//&&&&&&&&					/* if_acmpxx */ case 165: case 166:
//&&&&&&&&					{
//&&&&&&&&						int offset = Integer.parseInt(current.getArg3());
//&&&&&&&&						int ffb_end = (offset < 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&						ffbEnds = ArrayUtilities.addElement(ffbEnds, ffb_end);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* goto, jsr */ case 167: case 168:
//&&&&&&&&					/* goto2, jsr_w */ case 200: case 201:
//&&&&&&&&					{
//&&&&&&&&						try {
//&&&&&&&&							int offset = Integer.parseInt(current.getArg());
//&&&&&&&&							int ffb_end = (offset < 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&							ffbEnds = ArrayUtilities.addElement(ffbEnds, ffb_end);
//&&&&&&&&						} catch (NumberFormatException exc) {
//&&&&&&&&							ffbEnds = ArrayUtilities.addElement(ffbEnds, pcvalue);
//&&&&&&&&						}
//&&&&&&&&						
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* xxxswitch */ case 170: case 171:
//&&&&&&&&					{
//&&&&&&&&						// ... find _min_ of "-" offsets ...
//&&&&&&&&						ffbEnds = ArrayUtilities.addElement(ffbEnds, pcvalue);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&				}
//&&&&&&&&			}
//&&&&&&&&		}
//&&&&&&&&		
//&&&&&&&&		if (ffbEnds.length > 0) {
//&&&&&&&&			int min_end = ArrayUtilities.getMinElement(ffbEnds);
//&&&&&&&&			int firstFlatEnd = BytecodeBlock.calculatePreviousPC(bytecodes, min_end);
//&&&&&&&&			return (firstFlatEnd != -1) ? firstFlatEnd : min_end;
//&&&&&&&&		} else {
//&&&&&&&&			return lastpc;
//&&&&&&&&		}
//&&&&&&&&	}
	
	/**
	 *	Last flat bytecode block: to_pc = last_pc.
	 *	(static method)
	 *
	 *	@see	#getFirstFlatBlockEnd
	 */
//&&&&&&&&	public static int getLastFlatBlockStart(final BytecodeItem[] bytecodes) {
//&&&&&&&&		if (bytecodes == null) {
//&&&&&&&&			throw new IllegalArgumentException("bytecodes is 'null'");
//&&&&&&&&		}
//&&&&&&&&		
//&&&&&&&&		int bytecodes_length = bytecodes.length;
//&&&&&&&&		int lastpc = BytecodeBlock.calculatePreviousPC(bytecodes, bytecodes_length);
//&&&&&&&&		int[] lfbStarts = new int[0];
//&&&&&&&&		
//&&&&&&&&		for (int i = 0; i <= lastpc; i++) {
//&&&&&&&&			BytecodeItem current = bytecodes[i];
//&&&&&&&&			if (current != null) {
//&&&&&&&&				int pcvalue = current.getPCValue();
//&&&&&&&&				int opcode = ByteTransformer.toUnsignedByte(current.getOpcode());
//&&&&&&&&				
//&&&&&&&&				switch (opcode) {
//&&&&&&&&					/* ifxx */ case 153: case 154: case 155: case 156: case 157: case 158:
//&&&&&&&&					/* ifnull, ifnonull*/ case 198: case 199:
//&&&&&&&&					{
//&&&&&&&&						int offset = Integer.parseInt(current.getArg2());
//&&&&&&&&						int lfb_start = (offset > 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&						lfbStarts = ArrayUtilities.addElement(lfbStarts, lfb_start);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* if_icmpxx */ case 159: case 160: case 161: case 162: case 163: case 164:
//&&&&&&&&					/* if_acmpxx */ case 165: case 166:
//&&&&&&&&					{
//&&&&&&&&						int offset = Integer.parseInt(current.getArg3());
//&&&&&&&&						int lfb_start = (offset > 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&						lfbStarts = ArrayUtilities.addElement(lfbStarts, lfb_start);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* goto, jsr */ case 167: case 168:
//&&&&&&&&					/* goto2, jsr_w */ case 200: case 201:
//&&&&&&&&					{
//&&&&&&&&						try {
//&&&&&&&&							int offset = Integer.parseInt(current.getArg());
//&&&&&&&&							int lfb_start = (offset > 0) ? (pcvalue + offset) : pcvalue;
//&&&&&&&&							lfbStarts = ArrayUtilities.addElement(lfbStarts, lfb_start);
//&&&&&&&&						} catch (NumberFormatException exc) {
//&&&&&&&&							lfbStarts = ArrayUtilities.addElement(lfbStarts, pcvalue);
//&&&&&&&&						}
//&&&&&&&&						
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&					
//&&&&&&&&					/* xxxswitch */ case 170: case 171:
//&&&&&&&&					{
//&&&&&&&&						// ... find _max_ of "+" offsets ...
//&&&&&&&&						lfbStarts = ArrayUtilities.addElement(lfbStarts, pcvalue);
//&&&&&&&&						break;
//&&&&&&&&					}
//&&&&&&&&				}
//&&&&&&&&			}
//&&&&&&&&		}
//&&&&&&&&		
//&&&&&&&&		if (lfbStarts.length > 0) {
//&&&&&&&&			int max_start = ArrayUtilities.getMaxElement(lfbStarts);
//&&&&&&&&			if (max_start > lastpc) {
//&&&&&&&&				return bytecodes_length;
//&&&&&&&&			}
//&&&&&&&&			
//&&&&&&&&			if (bytecodes[max_start] == null) {
//&&&&&&&&				max_start = BytecodeBlock.calculateNextPC(bytecodes, max_start, lastpc);
//&&&&&&&&			}
//&&&&&&&&			
//&&&&&&&&			if (BytecodeItem.isBranchBytecode(bytecodes[max_start].getOpcode(), true)) {
//&&&&&&&&				int lastFlatStart = BytecodeBlock.calculateNextPC(bytecodes, max_start, lastpc);
//&&&&&&&&				return (lastFlatStart == -1) ? bytecodes_length : lastFlatStart;
//&&&&&&&&			} else {
//&&&&&&&&				return max_start;
//&&&&&&&&			}
//&&&&&&&&		} else {
//&&&&&&&&			return 0;
//&&&&&&&&		}
//&&&&&&&&	}
	
}
