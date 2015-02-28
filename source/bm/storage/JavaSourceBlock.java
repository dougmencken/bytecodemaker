// ===========================================================================
//	JavaSourceBlock.java (part of douglas.mencken.bm.storage package)
// ===========================================================================

package douglas.mencken.bm.storage;

import douglas.mencken.util.ArrayUtilities;
import douglas.mencken.util.StringUtilities;

/**
 *	<code>JavaSourceBlock</code>
 *	
 *	@version	0.78d1
 *	@since		Bytecode Maker 0.5.9
 */

public class JavaSourceBlock extends Object implements JavaMethodMember {
	
	public static final int BLOCK_TYPE_SIMPLE = 0;
	public static final int BLOCK_TYPE_LOOP = 1;
	public static final int BLOCK_TYPE_if_else = 2;
	public static final int BLOCK_TYPE_switch = 3;
	public static final int BLOCK_TYPE_try_catch = 4;
	public static final int BLOCK_TYPE_synchronized = 5;
	public static final int BLOCK_TYPE_jsr = 6;
	
	/**
	 *	Blocks enclosed in this block.
	 */
	protected JavaSourceBlock[] enclosedBlocks = new JavaSourceBlock[0];
	
	/**
	 *	The level of enclosure:
	 *	
	 *	1 - entire method
	 *	2 - block in method
	 *	3 - enclosed in 2
	 *	...
	 */
	protected int level;
	
	/**
	 *	The type of this block.
	 */
	protected int type;
	
	protected JavaMethod ownerMethod;
	protected int startPC;
	protected int endPC;
	
	/**
	 *	The main constructor.
	 */
	public JavaSourceBlock(JavaMethod owner, int level, int type) {
		super();
		this.ownerMethod = owner;
		this.level = level;
		this.type = type;
		
		this.startPC = -1;
		this.endPC = -1;
	}
	
	public JavaSourceBlock(JavaMethod owner) {
		this(owner, 1, JavaSourceBlock.BLOCK_TYPE_SIMPLE);
	}
	
	public JavaMethod getOwnerMethod() {
		return this.ownerMethod;
	}
	
	public void setOwnerMethod(JavaMethod owner) {
		this.ownerMethod = owner;
	}
	
	public static int findBlockEnd(JavaSourceBlock block) {
		return findBlockEnd(
			block.getBlockType(),
			block.ownerMethod.getCode(),
			block.getStartPCValue()
		);
	}
	
/*
->> MOVE IT SOMEWHERE!!!!!!!!!
CAN'T BE HERE AS NORMALLY
*/
	/**
	 *	@return		[][0] - match values,
	 *				[][1] - offset values.
	 *	
	 *	(static method)
	 */
//	public static int[][] extractValuesFromSwitch(JVMInstruction instruction) {
//		byte opcode = instruction.getOpcode();
//	 	if ((opcode != -86 /* 170 */) && (opcode != -85 /* 171 */)) {
//	 		throw new IllegalArgumentException("'switch' instruction required");
//	 	}
//	 	
//	 	int[] allValues = ArrayUtilities.stringArrayToIntArray(instruction.getAdditionalArguments());
//		int[] matchValues = null;
//		int[] offsetValues = null;
//		int valueCount = 0;
//		
//		if (opcode == -86 /* 170 tableswitch */) {
//			offsetValues = allValues;
//			valueCount = offsetValues.length;
//			
//			int minValue = Integer.parseInt(bytecode.getArg2());
//			int maxValue = Integer.parseInt(bytecode.getArg3());
//			
//			if (valueCount != (maxValue - minValue + 1)) {
//				throw new InternalError("bad 'tableswitch'");
//			}
//			
//			matchValues = new int[valueCount];
//			for (int index = 0; index < valueCount; index++) {
//				matchValues[index] = minValue + index;
//			}
//		} else if (opcode == -85 /* 171 lookupswitch */) {
//			valueCount = Integer.parseInt(bytecode.getArg2());
//			int valueCount_x2 = valueCount*2;
//			
//			if (allValues.length != valueCount_x2) {
//				throw new InternalError("bad 'lookupswitch'");
//			}
//			
//			matchValues = new int[valueCount];
//			offsetValues = new int[valueCount];
//			for (int index = 0; index < valueCount_x2; index += 2) {
//				matchValues[index/2] = allValues[index];
//				offsetValues[index/2] = allValues[index + 1];
//			}
//		}
//		
//		// sort offsets
//		ArrayUtilities.sortWith(offsetValues, matchValues);
//		
//		return new int[][] { matchValues, offsetValues };
//	}
	
	/**
	 *	Searches for the block end.
	 *	Block start, type and method's code are required as parameters.
	 *	
	 *	(static method)
	 */
	public static int findBlockEnd(int blockType, final byte[] code, int startPC) {
		if ((startPC < 0) || (startPC >= code.length)) {
			throw new IllegalArgumentException("out of bounds");
		}
		
//		JavaBytecode startBytecode = bytecodes[startPC];
//		
//		switch (blockType) {
//			 case BLOCK_TYPE_switch:
//			 {
//			 	int[] offsetValues = extractValuesFromSwitch(startBytecode)[1];
//			 	int defaultOffset = Integer.parseInt(startBytecode.getArg());
//			 	
//			 	int blockEnd = -1;
//			 	int valueCount = offsetValues.length;
//			 	
//			 	for (int ii = 0; ii < valueCount; ii++) {
//					int endOffset = BytecodeBlock.calculatePreviousPC(
//						bytecodes, offsetValues[ii] + startPC
//					);
//					
//					byte op = bytecodes[endOffset].getOpcode();
//					if (op == -89 /* 167 goto */) {
//						String arg2 = bytecodes[endOffset].getArg2();
//						blockEnd = Integer.parseInt(
//							StringUtilities.removeFirstLastSymbols(arg2)
//						);
//						break;
//					}
//				}
//				
//				return blockEnd;
//			 }
//			 
//			 case BLOCK_TYPE_SIMPLE:
//			 {
//			 	// just return last pc
//			 	int bytecodeCount = bytecodes.length;
//			 	int blockEnd = BytecodeBlock.calculatePreviousPC(bytecodes, bytecodeCount);
//			 	return blockEnd;
//			 }
//			 
//			 case BLOCK_TYPE_if_else:
//			 {
//			 	break;
//			 }
//			 
//			 case BLOCK_TYPE_LOOP:
//			 {
//			 	break;
//			 }
//			 
//			 case BLOCK_TYPE_synchronized:
//			 {
//			 	break;
//			 }
//			 
//			 case BLOCK_TYPE_jsr:
//			 {
//			 	break;
//			 }
//			 
//			 case BLOCK_TYPE_try_catch:
//			 {
//			 	return TryCatchFinallyBlock.findTryCatchBlockEnd(bytecodes, startPC);
//			 }
//			 
//			 default:
//			 	throw new IllegalArgumentException("invalid block type");
//		}
//		
		return -1;
	}
	
	public int getStartPCValue() {
		return this.startPC;
	}
	
	public void setStartPCValue(int startPC) {
		this.startPC = startPC;
	}
	
	public int getEndPCValue() {
		return this.endPC;
	}
	
	public void setEndPCValue(int endPC) {
		this.endPC = endPC;
	}
	
	public int getBlockType() {
		return this.type;
	}
	
	public void setBlockType(int newType) {
		if (this.type != newType) {
			if ((newType < BLOCK_TYPE_SIMPLE) || (newType > BLOCK_TYPE_jsr)) {
				throw new IllegalArgumentException("invalid block type");
			}
			
			this.type = newType;
		}
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int newLevel) {
		if (this.level != newLevel) {
			if (newLevel <= 0) {
				throw new IllegalArgumentException("'level' can't be <= 0");
			}
			
			this.level = newLevel;
		}
	}
	
}