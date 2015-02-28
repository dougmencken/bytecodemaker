// ===========================================================================
// DecompilerUtilities.java (part of douglas.mencken.bm.decompiler package)
// ===========================================================================

package douglas.mencken.bm.decompiler;

import douglas.mencken.util.*;
import douglas.mencken.tools.UsefulModalDialogs;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.engine.Unpackager;

/**
 *	<code>DecompilerUtilities</code>
 *
 *	@version 1.10f1
 */

public class DecompilerUtilities extends Object {
	
	/** This class cannot be instantiated. */
	private DecompilerUtilities() {}
	
	// -------------------------------------------------------------------------------------
	
	/**
	 *	<code>toString(JavaField)</code>
	 *
	 *	Note:	Unpackager must be initialized.
	 */
	public static String toString(JavaField field) {
		if (field == null) {
			return "* null *";
		}
		
		StringBuffer buf = new StringBuffer();
		if (field.isSynthetic()) {
			buf.append("// ");
		}
		
		if (field.getAccessString().length() != 0) {
			buf.append(field.getAccessString());
			buf.append(' ');
		}
		
		String type = field.getType();
		buf.append(Unpackager.getCurrentUnpackager().unpackage(type));
		buf.append(' ');
		buf.append(field.getFieldName());
		
		if (field.getConstantValue().length() != 0) {
			buf.append(" = ");
			buf.append(field.getConstantValue());
			if (type.equals("float")) {
				buf.append('f');
			} else if (type.equals("long")) {
				buf.append('L');
			} else if (type.equals("double")) {
				buf.append('d');
			}
		}
		
		return buf.toString();
	}
	
	/**
	 *	<code>toString(BytecodeItem)</code>
	 */
/******************************
	public static String toString(BytecodeItem bytecode) {
		if (bytecode == null) {
			return "* null *";
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append(bytecode.getBytecode());
		
		int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
		String arg = bytecode.getArg();
		String arg2 = bytecode.getArg2();
		String arg3 = bytecode.getArg3();
		
		// from 153 to 168, from 198 to 201
		if (((opcode >= 153) && (opcode <= 168)) || ((opcode >= 198) && (opcode <= 201))) {
			int branchDisplayMode = BMPreferencesManager.getBranchDisplayMode();
			if (branchDisplayMode == 0) {
				arg2 = null;
			} else if (branchDisplayMode == 1) {
				arg = null;
			}
		}
		
		if (arg != null) { buf.append(' ').append(arg); }
		if (arg2 != null) { buf.append(' ').append(arg2); }
		if (arg3 != null) { buf.append(' ').append(arg3); }
		
		String[] additionalArgs = bytecode.getAdditionalArgs();
		if ((additionalArgs != null) && (additionalArgs.length != 0)) {
			int len = additionalArgs.length;
			buf.append(' ');
			
			for (int i = 0; i < len; i++) {
				if (additionalArgs[i] != null) {
					buf.append(' ');
					buf.append(additionalArgs[i]);
				}
			}
		}
		
		return buf.toString();
	}
*********************************/
	
/*********************************
	public static BytecodeBlock[] splitSubroutines(BytecodeBlock bigBlock) {
	////////////// throws BadBytecodesException {
		BytecodeItem[] bytecodes = bigBlock.getBytecodes();
		BytecodeBlock[] result = new BytecodeBlock[] { bigBlock };
		int blen = bytecodes.length;
		
		// (startPCs != null) -> bigBlock has subroutines
		int[] startPCs = null;
		int[] endPCs = null;
		
		BytecodeItem bytecode;
		for (int i = 0; i < blen; i++) {
			bytecode = bytecodes[i];
			if (bytecode != null) {
				int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
				if ((opcode == 168) || (opcode == 201)) {
					int startPC = Integer.parseInt(
						StringUtilities.removeFirstLastSymbols(bytecode.getArg2())
					);
					
					// startPC must be unique
					boolean isUnique = true;
					if (startPCs != null) {
						int len = startPCs.length;
						for (int j = 0; j < len; j++) {
							if (startPC == startPCs[j]) {
								isUnique = false;
							}
						}
					}
					
					if (isUnique) {
						startPCs = ArrayUtilities.addElement(startPCs, startPC);
					}
					
					// update 'jsr' ('jsr_w' -> 'jsr') bytecode
					if (startPCs != null) {
						int len = startPCs.length;
						for (int j = 0; j < len; j++) {
							if (startPC == startPCs[j]) {
								bytecodes[i] = new BytecodeItem(
									bytecode.getPCValue(), (byte)opcode,
									"sub" + (j+1)
								);
							}
						}
					}
				}
			}
		}
		
		if (startPCs != null) {
			for (int i = 0; i < blen; i++) {
				bytecode = bytecodes[i];
				if (bytecode != null) {
					int opcode = ByteTransformer.toUnsignedByte(bytecode.getOpcode());
					if ((opcode == 169) || (opcode == 209)) {
						int endPC = bytecode.getPCValue();
						endPCs = ArrayUtilities.addElement(endPCs, endPC);
					}
				}
			}
			
			int subroutineCount = startPCs.length;
			if ((endPCs == null) || (endPCs.length != subroutineCount)) {
				throw new BadBytecodesException("invalid subroutine");
			}
			
			for (int i = 0; i < subroutineCount; i++) {
				int startPC = startPCs[i];
				int endPC = endPCs[i];
				
				BytecodeItem[] newBytecodes = new BytecodeItem[endPC - startPC + 1];
				for (int j = startPC; j <= endPC; j++) {
					newBytecodes[j - startPC] = bytecodes[j];
					bytecodes[j] = null;
				}
				
				BytecodeBlock newBlock = new BytecodeBlock(newBytecodes, true);
				int blockCount = result.length;
				
				BytecodeBlock[] newBlocks = new BytecodeBlock[blockCount+1];
				System.arraycopy(result, 0, newBlocks, 0, blockCount);
				newBlocks[blockCount] = newBlock;
				
				result = newBlocks;
			}
		}
		
		return result;
	}
*****************************************/
	
	public static String makeUnpackagedDeclaration(JavaClass theClass) {
		if (theClass == null) {
			throw new IllegalArgumentException();
		}
		
		String[] packages = theClass.obtainPackages();
		String[] interfaces = theClass.getInterfaces();
		int interfaceCount = interfaces.length;
		if (!BMPreferencesManager.getUseFullyQualifiedNames()) {
			for (int i = 0; i < interfaceCount; i++) {
				interfaces[i] = ClassUtilities.unpackage(interfaces[i], packages);
			}
		}
		
		String name = theClass.getClassName();
		String superName = theClass.getSuperclassName();
		if (!BMPreferencesManager.getUseFullyQualifiedNames()) {
			name = ClassUtilities.unpackage(theClass.getClassName(), packages);
			superName = ClassUtilities.unpackage(theClass.getSuperclassName(), packages);
		}
		
		String declaration = JavaClass.makeDeclaration(
			theClass.getAccess(), name, superName, interfaces
		);
		declaration = StringUtilities.cut(declaration, "synchronized ");
		
		return declaration;
	}
	
	public static String makeListOfImports(JavaClass theClass) {
		StringBuffer buf = new StringBuffer();
		
		if (!BMPreferencesManager.getUseFullyQualifiedNames()) {
			boolean java_sun_packdisp = BMPreferencesManager.getUsePackageImport_java_sun();
			boolean moreThan3_packdisp =
						BMPreferencesManager.getUsePackageImportIfMoreThan3Imports();
			boolean use_packdisp = BMPreferencesManager.getUsePackageImport();
			
			if (!use_packdisp) {
				java_sun_packdisp = false;
				moreThan3_packdisp = false;
			}
			
			String[] classImports = theClass.obtainClassImports();
			String[] packageImports = ClassUtilities.extractPackagesFromNames(classImports);
			
			int cilen = classImports.length;
			int pilen = packageImports.length;
			String import_i;
			
			if (use_packdisp && !java_sun_packdisp && !moreThan3_packdisp) {
				// use only package import
				for (int i = 0; i < pilen; i++) {
					buf.append("import ");
					buf.append(packageImports[i]);
					buf.append(".*;\n");
				}
			} else {
				// *** TO DO: moreThan3_packdisp ***
				
				if (java_sun_packdisp) {
					int packageImportsUsed = 0;
					for (int i = 0; i < pilen; i++) {
						import_i = packageImports[i];
						if (import_i.startsWith("java") || import_i.startsWith("sun")) {
							packageImportsUsed++;
							
							buf.append("import ");
							buf.append(import_i);
							buf.append(".*;\n");
						}
					}
					
					if (packageImportsUsed > 0) {
						buf.append('\n');
					}
				}
				
				for (int i = 0; i < cilen; i++) {
					import_i = classImports[i];
					
					if (java_sun_packdisp) {
						if (import_i.startsWith("java") || import_i.startsWith("sun")) {
							continue;
						}
					}
					
					buf.append("import ");
					buf.append(import_i);
					buf.append(";\n");
				}
			}
		}
		
		return buf.toString();
	}
	
	/**
	 *	Dump class (don't prepare bytecodes by default).
	 */
	public static String dumpClass(JavaClass theClass) {
		return dumpClass(theClass, false);
	}
	
	/**
	 *	Dump class.
	 */
	public static String dumpClass(JavaClass theClass, boolean prepareBytecodes) {
		if (theClass == null) return "";
		
		StringBuffer buf = new StringBuffer();
		buf.append(theClass.getDeclaration());
		buf.append(" {\n");
		
		JavaField[] fields = theClass.getFields();
		if ((fields != null) && (fields.length != 0)) {
			buf.append('\n');
			
			int len = fields.length;
			for (int i = 0; i < len; i++) {
				buf.append("\t");
				buf.append(fields[i].toString() + ";" + '\n');
			}
		}
		
		JavaMethod[] methods = theClass.getMethods();
		if ((methods != null) && (methods.length != 0)) {
			buf.append('\n');
			
			int len = methods.length;
			for (int i = 0; i < len; i++) {
				buf.append("\t");
				
				JavaMethod method = methods[i];
				buf.append(method);
				
				if (!method.isAbstractOrNative()) {
					buf.append(" {\n");
					
					LocalVariableItem[] locals = method.getLocalVariables();
					if (locals.length != 0) {
						int llen = locals.length;
						
						for (int j = 0; j < llen; j++) {
							if (locals[j] != null) {
								tabNewDumpLine(buf, method);
								
								buf.append('\t');
								buf.append(locals[j]);
								buf.append('\n');
							}
						}
						
						buf.append('\n');
					}
					
					TryCatchFinallyBlock[] catches = method.getTryCatchBlocks();
					if (catches.length != 0) {
						int clen = catches.length;
						
						for (int j = 0; j < clen; j++) {
							tabNewDumpLine(buf, method);
							
							// start_pc, end_pc, handler_pc
							int[] pcValues = catches[j].getPCValues();
							String catchType = catches[j].getCatchType();
							
							buf.append('\t');
							buf.append("try {\n");
							tabNewDumpLine(buf, method);
							buf.append("\t\t/* ");
							buf.append(pcValues[0] /* start_pc */);
							buf.append(" to ");
							buf.append(pcValues[1] /* end_pc */);
							buf.append(" */\n");
							
							tabNewDumpLine(buf, method);
							buf.append('\t');
							if (catchType.equals("finally")) {
								buf.append("} finally (\n");
							} else {
								buf.append("} catch (").append(catchType).append(") {\n");
							}
							
							tabNewDumpLine(buf, method);
							buf.append("\t\tgoto ");
							buf.append(pcValues[2] /* handler_pc */);
							buf.append(";\n");
							tabNewDumpLine(buf, method);
							buf.append("\t}");
							
							buf.append('\n');
						}
						
						buf.append('\n');
					}
					
					if (prepareBytecodes) {
						BytecodeBlock[] blocks = null;
						try {
							blocks = MethodDecompiler.prepareBytecodes(method);
						} catch (BadBytecodesException bbexc) {
							UsefulModalDialogs.doErrorDialog(
								"BadBytecodesException while preparing bytecodes (method '" +
								method.getMethodName() + "'): " + bbexc.getMessage()
							);
						}
						
						if (blocks != null) {
							int bcount = blocks.length;
							for (int n = 0; n < bcount; n++) {
								if (n != 0) {
									buf.append('\n');
									tabNewDumpLine(buf, method);
									buf.append("\tsub");
									buf.append(n);
									buf.append(": {");
									buf.append('\n');
								}
								
								BytecodeItem[] bytecodes = blocks[n].getBytecodes();
								int blen = bytecodes.length;
								for (int j = 0; j < blen; j++) {
									if (bytecodes[j] != null) {
										tabNewDumpLine(buf, method);
										
										buf.append('\t');
										if (n != 0) buf.append('\t');
										buf.append(bytecodes[j]);
										buf.append('\n');
									}
								}
								
								if (n != 0) {
									tabNewDumpLine(buf, method);
									buf.append("\t}\n");
								}
							}
						}
					} else {
						BytecodeItem[] bytecodes = method.getBytecodes();
						int blen = bytecodes.length;
						for (int j = 0; j < blen; j++) {
							if (bytecodes[j] != null) {
								tabNewDumpLine(buf, method);
								
								buf.append('\t');
								buf.append(bytecodes[j]);
								buf.append('\n');
							}
						}
					}
					
					tabNewDumpLine(buf, method);
					buf.append("}\n");
				} else {
					buf.append(";\n");
				}
				
				if ((i+1) != len) {
					buf.append('\n');
				}
			}
		}
		
		buf.append("\n}");
		return buf.toString();
	}
	
	private static void tabNewDumpLine(StringBuffer buffer, JavaMethod method) {
		buffer.append('\t');
		if (method.isSynthetic()) {
			buffer.append("// ");
		}
	}
	
}
