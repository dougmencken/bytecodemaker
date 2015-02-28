// ===========================================================================
//	CodeLocksmith.java (part of douglas.mencken.bm.engine package)
// ===========================================================================

package douglas.mencken.bm.engine;

import java.util.StringTokenizer;
import douglas.mencken.util.*;

import douglas.mencken.tools.LogMonitor;
import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;

/**
 *	It does the "black" work with method's code.
 *
 *	@version	0.62d4
 *	@since		Bytecode Maker 0.5.8
 */

public class CodeLocksmith extends Object {
	
	private CodeLocksmith() { super(); }
	
	/**
	 *	Simplifies method's instruction (for the man).
	 *
	 *	<pre>
	 *	Before:                  After:
	 *	---------------------------------------
	 *	0 nop                    (null)
	 *	2...8 iconst_n           2 iconst [n]
	 *	16 sipush, 17 bipush,
	 *	18 ldc1,
	 *	19 ldc2, 20 ldc2w        16 push [what]
	 *	xload_n                  xload [n]
	 *	xstore_n                 xstore [n]
	 *	new + invokenonvirtual   new [class_name] (args)
	 *	</pre>
	 *
	 *	New format for
	 *	<code>getstatic, putstatic, getfield, putfield,
	 *	checkcast, instanceof,
	 *	invokevirtual, invokenonvirtual, invokestatic, invokeinterface</code>:
	 *	1. [class_name]
	 *	2. [(return) type] [class] [name] (args)
	 */
	/***** RE-WORK FOR JVMInstructionSet
	public static JVMInstruction[] extractInstructions(JavaMethod method) {
		if ((method == null) || method.isNative() || method.isAbstract()) {
			return null;
		}
		
		int codeLength = method.getCodeLength();
		JVMInstruction[] ret = new JVMInstruction[codeLength];
		JVMInstruction[] originalInstructions = method.getInstructions();
		
		boolean isInstructionsValid = false;
		for (int i = 0; i < codeLength; i++) {
			if (originalInstructions[i] != null) {
				isInstructionsValid = true;
				break;
			}
		}
		
		if (!isInstructionsValid) {
			throw new InternalError(
				"only 'null' Instructions in '" + method.getNameWithParameters() + "'"
			);
		}
		
		System.arraycopy(originalInstructions, 0, ret, 0, codeLength);
		
		int lastPC = method.calculateLastPC();
		if (ret[lastPC] != null) {
			if (ret[lastPC].getOpcode() == -79) { // -79 is 177
				// no 'return' at end of void methods
				ret[lastPC] = null;
			}
		}
		
		String arg2;
		
		// (int)(1/2) = 0, (int)(2/2) = 1
		int max_stack = method.getMaxStack();
		if (max_stack == 1) max_stack = 2;
		MiniStack stack_187new = new MiniStack((int)(max_stack/2));
		
		for (int i = 0; i < codeLength; i++) {
			if (ret[i] != null) {
				int pc = ret[i].getPCValue();
				int opcode = ByteTransformer.toUnsignedByte(ret[i].getOpcode());
				
				switch (opcode) {
					// nop
					case 0:
						ret[i] = null;
						break;
					
					// iconst_n
					case 2: case 3: case 4: case 5: case 6: case 7: case 8:
						ret[i] = new JavaBytecode(pc, (byte)2, Integer.toString(opcode - 3));
						ret[i].setBytecode("iconst");
						break;
					
					// 16 bipush, 17 sipush -> 16 push
					case 16: case 17:
						ret[i] = new JavaBytecode(pc, (byte)16, ret[i].getArg());
						ret[i].setBytecode("push");
						break;
					// 18 ldc1, 19 ldc2, 20 ldc2w -> 16 push
					case 18: case 19: case 20:
						ret[i] = new JavaBytecode(pc, (byte)16, ret[i].getArg2());
						ret[i].setBytecode("push");
						break;
					
					// iload_n
					case 26: case 27: case 28: case 29:
						ret[i] = new JavaBytecode(pc, (byte)21, Integer.toString(opcode - 26));
						break;
					// lload_n
					case 30: case 31: case 32: case 33:
						ret[i] = new JavaBytecode(pc, (byte)22, Integer.toString(opcode - 30));
						break;
					// fload_n
					case 34: case 35: case 36: case 37:
						ret[i] = new JavaBytecode(pc, (byte)23, Integer.toString(opcode - 34));
						break;
					// dload_n
					case 38: case 39: case 40: case 41:
						ret[i] = new JavaBytecode(pc, (byte)24, Integer.toString(opcode - 38));
						break;
					// aload_n
					case 42: case 43: case 44: case 45:
						ret[i] = new JavaBytecode(pc, (byte)25, Integer.toString(opcode - 42));
						break;
					
					// istore_n
					case 59: case 60: case 61: case 62:
						ret[i] = new JavaBytecode(pc, (byte)54, Integer.toString(opcode - 59));
						break;
					// lstore_n
					case 63: case 64: case 65: case 66:
						ret[i] = new JavaBytecode(pc, (byte)55, Integer.toString(opcode - 63));
						break;
					// fstore_n
					case 67: case 68: case 69: case 70:
						ret[i] = new JavaBytecode(pc, (byte)56, Integer.toString(opcode - 67));
						break;
					// dstore_n
					case 71: case 72: case 73: case 74:
						ret[i] = new JavaBytecode(pc, (byte)57, Integer.toString(opcode - 71));
						break;
					// astore_n
					case 75: case 76: case 77: case 78:
						ret[i] = new JavaBytecode(pc, (byte)58, Integer.toString(opcode - 75));
						break;
					
					// new
					case 187:
						try {
							arg2 = StringUtilities.removeFirstLastSymbols(ret[i].getArg2());
							stack_187new.push(arg2);
							ret[i] = new JavaBytecode(pc, (byte)187, arg2);
						} catch (Exception exc) {
							if (BMPreferencesManager.getShowLog()) {
								LogMonitor.addToCurrentLog(
									exc.getClass().getName() + ":\n\"" + exc.getMessage() + 
									"\",\n    processing '" + originalInstructions[i] + "'\n"
								);
							}
						}
						
						break;
					
					// newarray, anewarray
					//case 188:
					//	break;
					
					// anewarray
					//case 189:
					//	break;
					
					// getstatic, getfield
					// putstatic, putfield
					case 178: case 180:
					case 179: case 181:
					{
						try {
							arg2 = StringUtilities.removeFirstLastSymbols(ret[i].getArg2());
							
							String[] type_classField = StringUtilities.splitByChar(arg2, ' ');
							String[] class_field = StringUtilities.splitByChar(
								type_classField[1], '.', true
							);
							
							ret[i] = new JavaBytecode(
								pc, (byte)opcode,
								type_classField[0],
								class_field[0], class_field[1]
							);
						} catch (Exception exc) {
							if (BMPreferencesManager.getShowLog()) {
								LogMonitor.addToCurrentLog(
									exc.getClass().getName() + ":\n\"" + exc.getMessage() + 
									"\",\n    processing '" + originalInstructions[i] + "'\n"
								);
							}
						}
						
						break;
					}
					
					// checkcast
					// instanceof
					case 192:
					case 193:
						try {
							arg2 = StringUtilities.removeFirstLastSymbols(ret[i].getArg2());
							ret[i] = new JavaBytecode(pc, (byte)opcode, arg2);
						} catch (Exception exc) {
							if (BMPreferencesManager.getShowLog()) {
								LogMonitor.addToCurrentLog(
									exc.getClass().getName() + ":\n\"" + exc.getMessage() + 
									"\",\n    processing '" + originalInstructions[i] + "'\n"
								);
							}
						}
						
						break;
					
					// invokevirtual
					// invokenonvirtual
					// invokestatic
					// invokeinterface
					case 182:
					case 183:
					case 184:
					case 185:
					{
						boolean invokevirtual = (opcode == 182);
						boolean invokenonvirtual = (opcode == 183);
						boolean invokestatic = (opcode == 184);
						boolean invokeinterface = (opcode == 185);
						boolean staticMethod = false;
						
						try {
							arg2 = StringUtilities.removeFirstLastSymbols(ret[i].getArg2());
							if (arg2.indexOf("static") != -1) {
								staticMethod = true;
								arg2 = StringUtilities.cut(arg2, "static ");
							}
							
							String[] returnType_classMethodParams =
								StringUtilities.splitByChar(arg2, ' ');
							String[] classMethod_params =
								StringUtilities.splitByChar(returnType_classMethodParams[1], '(');
							String[] class_method =
								StringUtilities.splitByChar(classMethod_params[0], '.', true);
							
							String returnType = returnType_classMethodParams[0];
							String className = class_method[0];
							String methodName = class_method[1];
							String paramsString = classMethod_params[1];
							
							int paramCount = ClassUtilities.countParameters(paramsString, false);
							
							if (invokeinterface) {
								int invokeinterfaceParamCount = Integer.parseInt(ret[i].getArg3());
								int allParamCount = ClassUtilities.countParameters(
									paramsString, true
								);
								if (!staticMethod) {
									allParamCount++;
								}
								
								if (allParamCount != invokeinterfaceParamCount) {
									if (BMPreferencesManager.getShowLog()) {
										LogMonitor.addToCurrentLog(
											"'invokeinterface': wrong parameter count" +
											"\n    " + ret[i] + "\n    " +
											"(method " + method.getNameWithParameters() + ")"
										);
									}
								}
							}
							
							String[] params = new String[paramCount];
							StringTokenizer tokenizer = new StringTokenizer(
								paramsString, " ,()\t\n\r\f", false
							);
							
							int pos = 0;
							while (tokenizer.hasMoreTokens()) {
								params[pos] = tokenizer.nextToken();
								pos++;
							}
							
							ret[i] = new JavaBytecode(
								pc, (byte)opcode,
								returnType, className, methodName
							);
							
							// check if it is an 'invokenonvirtual' for 'new'
							if (invokenonvirtual && !stack_187new.isEmpty()) {
								if (methodName.equals("<init>")) {
									String arg = (String)stack_187new.pop();
									// '187 new' bytecode
									ret[i] = new JavaBytecode(pc, (byte)187, arg);
								}
							}
							
							ret[i].setAdditionalArgs(params);
						} catch (Exception exc) {
							// avoid StackOverflowError
							if (invokenonvirtual && !stack_187new.isEmpty()) {
								stack_187new.pop();
							}
							
							if (BMPreferencesManager.getShowLog()) {
								LogMonitor.addToCurrentLog(
									exc.getClass().getName() + ":\n\"" + exc.getMessage() + 
									"\",\n    processing '" + originalInstructions[i] + "'\n"
								);
							}
						}
						
						break;
					}
				}
			}
		}
		
		return ret;
	} *****/
	
	/***** RE-WORK for JVMInstructionSet ****
	public static JVMInstruction[] removeInstruction(final JVMInstruction[] instructions, int pc) {
//System.out.println("\nremoving: " + instructions[pc]);
//System.out.println("\n---------- INSTRUCTIONS BEFORE");
//if(bytecodes!=null) {
//for(int i=0;i<instructions.length;i++) {
//if(instructions[i]!=null){
//System.out.println(instructions[i]);
//}}}
		if (instructions == null) return null;
		JavaBytecode removeMe = bytecodes[pc];
		
		int count = bytecodes.length;
		int pcDecrement = pc - BytecodeBlock.calculatePreviousPC(bytecodes, pc);
		JavaBytecode[] ret = new JavaBytecode[count - pcDecrement];
		
		String bytecodeArg2 = removeMe.getArg2();
		boolean branchBytecode = JavaBytecode.isBranchBytecode(removeMe.getOpcode(), false);
		int branchDestination = Integer.parseInt(
			StringUtilities.removeFirstLastSymbols(bytecodeArg2)
		);
		
		int pos = 0;
		for (int i = 0; i < count; i++) {
			if (bytecodes[i] != null) {
				if (i != pc) {
					JavaBytecode current = (JavaBytecode)bytecodes[i].clone();
					int opcode = ByteTransformer.toUnsignedByte(current.getOpcode());
					
					// '169 ret' and '209 ret_w' are the special
					// branch opcodes
					// (<arg> is an index into the method's frame
					// to a 'jvm_ret_addr' local variable that contains
					// the _pc_ of the caller).
					
					switch (opcode) {
						// tableswitch
						case 170:
							// ...
							break;
						// lookupswitch
						case 171:
							// ...
							break;
						
						// ifxx
						case 153: case 154: case 155: case 156: case 157: case 158:
							// convert from the prepared form
							if (current.getArg2().indexOf("(") != 0) {
								String[] additionalArgs = { current.getArg() };
								current = new JavaBytecode(
									i, (byte)opcode,
									current.getArg2(),
									"(" +
										Integer.toString(Integer.parseInt(current.getArg2()) + i)
									+ ")",
									"*"
								);
								current.setAdditionalArgs(additionalArgs);
							}
							break;
						
						// if_icmpxx, if_acmpeq, if_acmpne
						case 159: case 160: case 161: case 162: case 163: case 164:
						case 165: case 166:
							// convert from the prepared form
							if (current.getArg3().length() > 0) {
								String[] additionalArgs = { current.getArg(), current.getArg2() };
								current = new JavaBytecode(
									i, (byte)opcode,
									current.getArg3(),
									"(" +
										Integer.toString(Integer.parseInt(current.getArg3()) + i) +
									")",
									"*"
								);
								current.setAdditionalArgs(additionalArgs);
							}
							break;
					}
					
					if (JavaBytecode.isBranchBytecode(current.getOpcode(), false)) {
						String modifiedArg2 = current.getArg2();
						int current_dst = Integer.parseInt(
							StringUtilities.removeFirstLastSymbols(modifiedArg2)
						);
						
						if (current_dst >= pc) {
							if ((current_dst == pc) && branchBytecode) {
								if (branchDestination >= pc) {
									modifiedArg2 =
										"(" +
											Integer.toString(branchDestination - pcDecrement) +
										")";
								} else {
									modifiedArg2 = bytecodeArg2;
								}
							} else {
								modifiedArg2 =
									"(" + Integer.toString(current_dst - pcDecrement) + ")";
							}
						} else {
							modifiedArg2 = "(" + Integer.toString(current_dst) + ")";
						}
						
						int arg2_pc = Integer.parseInt(
							StringUtilities.removeFirstLastSymbols(modifiedArg2)
						);
						
						current.setArg2(modifiedArg2);
						
						if (i >= pc) {
							current.setArg(Integer.toString(arg2_pc - i + pcDecrement));
						} else {
							current.setArg(Integer.toString(arg2_pc - i));
						}
					}
					
					if (i > pc) {
						current.setPCValue(current.getPCValue() - pcDecrement);
					}
					
					if (!current.getArg3().equals("*")) {
						ret[pos] = current;
					} else {
						// convert back to the prepared form
						String[] additionalArgs = current.getAdditionalArgs();
						int args_count = additionalArgs.length;
						
						ret[pos] = new JavaBytecode(pos, (byte)opcode, additionalArgs[0]);
						if (args_count > 1) {
							ret[pos].setArg2(additionalArgs[1]);
							ret[pos].setArg3(current.getArg());
						} else {
							ret[pos].setArg2(current.getArg());
						}
					}
				}
			} else {
				ret[pos] = null;
			}
			
			if (i == pc) {
				pos -= (pcDecrement - 1);
			} else {
				pos++;
			}
		}
		
//System.out.println("\n-------- BYTECODES AFTER");
//for(int i=0;i<ret.length;i++) {
//if(ret[i]!=null){
//System.out.println(ret[i]);
//}}
		return ret;
	} *****/
	
	
	/**
	 *	<pre>
	 *	x1: goto x2
	 *	x2: goto x8    ->    x1: xreturn
	 *	...        
	 *	x8: xreturn
	 *	</pre>
	 */
	/***** JavaBytecode IS NO LONGER ****
	public static JavaBytecode[] tryResolve_goto_return(JavaBytecode[] bytecodes, String methodSignature)
	throws BadBytecodesException {
		if (bytecodes == null) {
			throw new IllegalArgumentException("can't work with 'null' bytecodes");
		}
		if (methodSignature == null) {
			throw new IllegalArgumentException("can't work with 'null' method signature");
		}
		
		// assume that the method ends with 'xreturn'
		int lastpc = bytecodes.length - 1;
		
		for (int i = 0; i < lastpc; i++) {
			JavaBytecode bytecode = bytecodes[i];
			if (bytecode != null) {
				int pc = bytecode.getPCValue();
				
				// goto
				if (bytecode.getOpcode() == -89) { // -89 is 167
					int dst = Integer.parseInt(
						StringUtilities.removeFirstLastSymbols(bytecode.getArg2())
					);
					
					if (dst == lastpc) {
						if (methodSignature.endsWith(")V")) {
							// '167 goto (lastpc)' -> '177 return' for void methods
							bytecodes[i] = new JavaBytecode(pc, (byte)177);
						}
					}
				}
			}
		}
		
		boolean changesMade = true;
		while (changesMade) {
			changesMade = false;
			for (int i = 0; i < lastpc; i++) {
				JavaBytecode bytecode = bytecodes[i];
				if (bytecode != null) {
					int pc = bytecode.getPCValue();
					
					// goto
					if (bytecode.getOpcode() == -89) {  // -89 is 167
						JavaBytecode dstBytecode = bytecodes[Integer.parseInt(
							StringUtilities.removeFirstLastSymbols(bytecode.getArg2())
						)];
						if (dstBytecode == null) {
							throw new BadBytecodesException(
								bytecode.toString() + " points to an invalid pc"
							);
						}
						
						int dstOpcode = ByteTransformer.toUnsignedByte(dstBytecode.getOpcode());
						if (dstOpcode == 177) {
							// '167 goto (return)' -> '177 return'
							bytecodes[i] = new JavaBytecode(pc, (byte)177);
							changesMade = true;
						} else if ((dstOpcode >= 172) && (dstOpcode <= 176)) {
							// '167 goto (xreturn)' -> '[opcode] xreturn'
							bytecodes[i] = new JavaBytecode(pc, (byte)dstOpcode);
							changesMade = true;
						}
					}
				}
			}
		}
		
		return bytecodes;
	} *****/
	
	/**
	 *	<pre>
	 *	x1: goto x2
	 *	x2: goto x3    ->    x1: goto x4
	 *	x3: goto x4
	 *	</pre>
	 *	(some compilers likes chains)
	 *	
	 *	@param	bytecodes		the JavaBytecode array to be checked
	 *							for goto chains.
	 *	@param	actuallyRemove	sets the mode of bytecode removing.
	 */
	/***** NEEDS RE-WORKING
	public static JavaBytecode[] tryResolve_goto_Chains(JavaBytecode[] bytecodes,
														boolean actuallyRemove)
	throws BadBytecodesException {
		if (bytecodes == null) {
			throw new IllegalArgumentException("can't work with 'null' bytecodes");
		}
		
		boolean changesMade = true;
		int codelength;
		JavaBytecode bytecode;
		int pc;
		
loop:	while (changesMade) {
			changesMade = false;
			codelength = bytecodes.length;
			
			for (int i = 0; i < codelength; i++) {
				bytecode = bytecodes[i];
				if (bytecode != null) {
					pc = bytecode.getPCValue();
					
					// goto
					if (bytecode.getOpcode() == -89) { // -89 is 167
						int dstpc = Integer.parseInt(
							StringUtilities.removeFirstLastSymbols(bytecode.getArg2())
						);
						
						JavaBytecode dstbytecode = bytecodes[dstpc];
						if (dstbytecode == null) {
							throw new BadBytecodesException(
								bytecode.toString() + " points to an invalid pc"
							);
						} else if (dstbytecode.getOpcode() == -89) { // -89 is 167
							// chain found, modify the main (first) 'goto'
							// and remove the intermediate (second) 'goto'
							int dstdstpc = Integer.parseInt(
								StringUtilities.removeFirstLastSymbols(dstbytecode.getArg2())
							);
							
							// x1: goto x2
							// x2: goto x1
							if (dstdstpc == i) {
								// create a special version of 'goto' (no args)
								bytecodes[i] = new JavaBytecode(pc, (byte)167);
							} else {
								bytecodes[i] = new JavaBytecode(
									pc, (byte)167,
									Integer.toString(dstdstpc - pc),
									dstbytecode.getArg2()
								);
							}
							
							if (actuallyRemove) {
								bytecodes = BytecodeLocksmith.removeBytecode(bytecodes, dstpc);
							} else {
								bytecodes[dstpc] = null;
							}
							
							changesMade = true;
							continue loop;
						}
					}
				}
			}
		}
		
		// work done
		return bytecodes;
	} *****/
	
	/**
	 *	To convert 'non-static' method to 'static',
	 *	this method adds first parameter '<class_name> this'
	 *	to the declaration.
	 */
	public static void convertToStatic(JavaMethod method) {
		if (method.getOwnerClass() == null) {
			throw new IllegalArgumentException(
				"method \'" + method.getNameWithParameters() + "\' has no owner"
			);
		}
		
		if (!method.isStatic()) {
			String[] params = method.getArrayOfParameters();
			int paramCount = params.length;
			
			String[] newParams = new String[paramCount+1];
			System.arraycopy(params, 0, newParams, 1, paramCount);
			newParams[0] = method.getOwnerClass().getClassName();
			
			method.setArrayOfParameters(newParams);
		} else {
			throw new IllegalArgumentException(
				"method \'" + method.getNameWithParameters() + "\' is already static"
			);
		}
	}
	
	/**
	 *	To convert 'static' method to 'non-static',
	 *	this method increments all local var indexes by 1.
	 */
	public static void convertToNonStatic(JavaMethod method) {
		if (method.getOwnerClass() == null) {
			throw new IllegalArgumentException(
				"method \'" + method.getNameWithParameters() + "\' has no owner"
			);
		}
		
		if (method.isStatic()) {
			String[] params = method.getArrayOfParameters();
			int paramCount = params.length;
			if ((paramCount > 0) && (params[0].equals(method.getOwnerClass().getClassName()))) {
				String[] newParams = new String[paramCount-1];
				System.arraycopy(params, 1, newParams, 0, paramCount-1);
				method.setArrayOfParameters(newParams);
				
				return;
			}
			
			byte[] opcodes = method.getOpcodes();
			int codeLength = opcodes.length;
			boolean wide = false;
			
			int i = 0;
			while (i < codeLength) {
				switch (opcodes[i]) {
					// xload
					// xstore
					case 21: case 22: case 23: case 24: case 25:
					case 54: case 55: case 56: case 57: case 58:
						if (wide) {
							int slot = ByteTransformer.toUnsignedShort(opcodes, i+1);
							slot++;
							
							opcodes[i+1] = (byte)((slot >>> 8) & 0xFF);
							opcodes[i+2] = (byte)((slot >>> 0) & 0xFF);
							
							wide = false;
							i += 2;
						} else {
							if (opcodes[i+1] == -1 /* 255 */) {
								// 'wide' is required here
								throw new InternalError(
									"can't expand bytecode, 'addBytecode' is in progress"
								);
							} else {
								opcodes[i+1]++;
								i++;
							}
						}
						
						break;
					
					// xload_0, xload_1, xload_2
					// xstore_0, xstore_1, xstore_2
					case 26: case 27: case 28:
					case 30: case 31: case 32:
					case 34: case 35: case 36:
					case 38: case 39: case 40:
					case 42: case 43: case 44:
					
					case 59: case 60: case 61:
					case 63: case 64: case 65:
					case 67: case 68: case 69:
					case 71: case 72: case 73:
					case 75: case 76: case 77:
						opcodes[i]++;
						break;
					
					// xload_3
					// xstore_3
					case 29: case 33: case 37: case 41: case 45:
					case 62: case 66: case 70: case 74: case 78:
						throw new InternalError(
							"can't expand bytecode, 'addBytecode' is in progress"
						);
						
						// ...
						// break;
					
					// xaload
					// xastore
					case 46: case 47: case 48: case 49:
					case 50: case 51: case 52: case 53:
					
					case 79: case 80: case 81: case 82:
					case 83: case 84: case 85: case 86:
						throw new InternalError(
							"can't expand bytecode, 'addBytecode' is in progress"
						);
						
						// ...
						// break;
					
					// iinc
					case /* 132 */ -124: 
						if (wide) {
							int slot = ByteTransformer.toUnsignedShort(opcodes, i+1);
							slot++;
							
							opcodes[i+1] = (byte)((slot >>> 8) & 0xFF);
							opcodes[i+2] = (byte)((slot >>> 0) & 0xFF);
							
							wide = false;
							i += 4;
						} else {
							if (opcodes[i+1] == -1 /* 255 */) {
								// 'wide' is required here
								throw new InternalError(
									"can't expand bytecode, 'addBytecode' is in progress"
								);
							} else {
								opcodes[i+1]++;
							}
							
							i += 2;
						}
						
						break;
						
					// ret
					case /* 169 */ -87:
						if (opcodes[i+1] == -1 /* 255 */) {
							// convert to 'ret_w'
							opcodes[i] = /* 201 */ -47;
							// ...
							
							throw new InternalError(
								"can't expand bytecode, 'addBytecode' is in progress"
							);
						} else {
							opcodes[i+1]++;
							i++;
						}
						break;
						
					// ret_w
					case /* 201 */ -47:
						int slot = ByteTransformer.toUnsignedShort(opcodes, i+1);
						slot++;
						
						opcodes[i+1] = (byte)((slot >>> 8) & 0xFF);
						opcodes[i+2] = (byte)((slot >>> 0) & 0xFF);
						
						i += 2;
						break;
					
					// wide
					case /* 196 */ -60:
						wide = true;
						break;
					
					default:
						i = method.nextOpcodeIndex(i);
						break;
				}
				
				i++;
			}
			
			method.setOpcodes(opcodes);
		} else {
			throw new IllegalArgumentException(
				"method \'" + method.getNameWithParameters() + "\' is already non-static"
			);
		}
	}
	
}
