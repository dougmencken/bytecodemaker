// ===========================================================================
//	ClassDecompiler.java (part of douglas.mencken.bm.decompiler package)
// ===========================================================================

package douglas.mencken.bm.decompiler;

import java.awt.*;

import douglas.mencken.beans.LWProgressFrame;
import douglas.mencken.util.StringUtilities;
import douglas.mencken.util.event.CancelEvent;
import douglas.mencken.util.event.CancelListener;
import douglas.mencken.tools.LogMonitor;

import douglas.mencken.bm.storage.*;
import douglas.mencken.bm.storage.prefs.BMPreferencesManager;
import douglas.mencken.bm.engine.Unpackager;
import douglas.mencken.bm.BMEnvironment;

/**
 *	<code>ClassDecompiler</code>
 *
 *	@version 1.01f0
 */

public class ClassDecompiler extends Object
implements Decompiler, CancelListener {
	
	private JavaClass theClass;
	private int spaceCount;
	private boolean cancelFlag = false;
	private String decompilerString = "";
	
	public void operationCanceled(CancelEvent e) {
		this.cancelFlag = true;
	}
	
	public ClassDecompiler(JavaClass theClass) {
		this(theClass, 4);
	}
	
	public ClassDecompiler(JavaClass theClass, int spaceCount) {
		this.theClass = theClass;
		// for further unpackage ability
		new Unpackager(theClass);
		
		LWProgressFrame pframe = null;
		if (BMPreferencesManager.getShowProgressBar()) {
			pframe = LWProgressFrame.getCurrentFrame();
			pframe.setLabelText("Decompiling " + theClass.getClassName());
			pframe.setProgressBarForeground(new Color(0xAA0000));
			pframe.setProgressBarBackground(Color.lightGray);
			pframe.setValue(0);
			pframe.addCancelListener(this);
			pframe.setVisible(true);
		}
		
		this.spaceCount = spaceCount;
		StringBuffer buf = new StringBuffer();
		
	///////	String packageName = theClass.getPackageName();
	///////	if (packageName.length() != 0) {
	///////		buf.append("package ");
	///////		buf.append(packageName);
	///////		buf.append(";\n\n");
	///////	}
		
		// imports & class declaration
		String listOfImports = DecompilerUtilities.makeListOfImports(theClass);
		if (listOfImports.length() != 0) {
			buf.append(listOfImports);
			buf.append("\n");
		}
		buf.append(DecompilerUtilities.makeUnpackagedDeclaration(theClass));
		buf.append(" {\n");
		
		if (pframe != null) pframe.setValue(5);
		
		// fields & methods
		if (cancelFlag) return;
		this.processFields(buf);
		if (cancelFlag) return;
		this.processMethods(buf);
		if (cancelFlag) return;
		
		if (pframe != null) pframe.setValue(100);
		buf.append("}");
		this.decompilerString = buf.toString();
		
		if (pframe != null) pframe.setVisible(false);
		if (BMPreferencesManager.getShowLog()) {
			LogMonitor.showCurrent();
		}
	}
	
	protected void processFields(StringBuffer buf) {
		LWProgressFrame pframe = null;
		if (BMPreferencesManager.getShowProgressBar()) {
			pframe = LWProgressFrame.getCurrentFrame();
		}
		
		int barStart = 5;
		int barEnd = barStart + 7;
		
//////		JavaField[] fields = theClass.getFields();
//////		int count = fields.length;
		int count = 0;
		
		if (count > 0) {
			buf.append("\n");
			
			// static fields
			int staticFieldCount = 0;
			for (int i = 0; i < count; i++) {
//////////				JavaField currField = fields[i];
//////////				if (currField.isStatic()) {
//////////					staticFieldCount++;
//////////					
//////////					StringUtilities.addSpaces(buf, spaceCount);
//////////					buf.append(DecompilerUtilities.toString(fields[i]));
//////////					buf.append(";\n");
					
//////////					if (pframe != null) {
//////////						pframe.setValue(barStart, barEnd, count, i);
//////////					}
//////////					if (cancelFlag) return;
//////////				}
			}
			
			StringBuffer tempBuf = new StringBuffer();
			barStart = 12;
			barEnd = barStart + 7;
			
			// class fields
			int classFieldCount = 0;
/*****			for (int i = 0; i < count; i++) {
				JavaField currField = fields[i];
				if (!currField.isStatic()) {
					classFieldCount++;
					
					StringUtilities.addSpaces(tempBuf, spaceCount);
	///////////////				tempBuf.append(DecompilerUtilities.toString(currField));
					tempBuf.append(";\n");
					
					if (pframe != null) {
						pframe.setValue(barStart, barEnd, count, i);
					}
					if (cancelFlag) return;
				}
			} *****/
			
			if ((staticFieldCount > 0) && (classFieldCount > 0)) {
				buf.append('\n');
			}
			
			buf.append(tempBuf);
		}
		
		if (pframe != null) pframe.setValue(20);
	}
	
	protected void processMethods(StringBuffer buf) {
		LWProgressFrame pframe = null;
		if (BMPreferencesManager.getShowProgressBar()) {
			pframe = LWProgressFrame.getCurrentFrame();
		}
		
		int barStart = 20;
		int barEnd = 99;
		
///////		JavaMethod[] methods = theClass.getMethods();
		JavaMethod[] methods = null;
		
		if ((methods != null) && (methods.length != 0)) {
			buf.append("\n");
			
			int count = methods.length;
			for (int i = 0; i < count; i++) {
				JavaMethod curr_method = methods[i];
				
				try {
					MethodDecompiler md = new MethodDecompiler(curr_method, spaceCount);
					buf.append(md.getString());
				} catch (Throwable t) {
					if (BMPreferencesManager.getShowLog()) {
						LogMonitor.addToCurrentLog(
							"Cannot decompile method \'" + curr_method.getMethodName() + "\'"
						);
					}
					t.printStackTrace();
				}
				
				if ((i+1) != count) {
					if ((curr_method.getCodeLength() > 1) ||
							(methods[i+1].getCodeLength() > 1)) {
						buf.append("\n");
					}
				} else {
					buf.append("\n");
				}
				
				if (pframe != null) {
					pframe.setValue(barStart, barEnd, count, i);
				}
				if (cancelFlag) return;
			}
		} else {
			buf.append("\n");
		}
	}
	
	/**
	 *	@return		decompilation result
	 */
	public String getString() {
		return this.decompilerString;
	}
	
}
