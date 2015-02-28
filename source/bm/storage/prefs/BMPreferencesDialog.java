// ===========================================================================
//	BMPreferencesDialog.java (part of douglas.mencken.bm.storage.prefs package)
//		public class BMPreferencesDialog
//		interface BMPreferencesDialogPart
//		class SavePreferencesPanel
//		class OpenPreferencesPanel
//		class DecompilerPreferencesPanel
//		class GeneralPreferencesPanel
//	
// ===========================================================================

package douglas.mencken.bm.storage.prefs;

import java.awt.*;
import java.awt.event.*;

import douglas.mencken.beans.*;
import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.util.InvisibleFrame;

/**
 *	<code>BMPreferencesDialog</code>
 *	(local to package)
 *
 *	@version 1.13v
 */

class BMPreferencesDialog extends Dialog
implements ActionListener, ItemListener  {
	
	private LWTabbedPane tabbedPane;
	private GeneralPreferencesPanel generalPrefs;
	private DecompilerPreferencesPanel decompilerPrefs;
	private OpenPreferencesPanel openPrefs;
	private SavePreferencesPanel savePrefs;
	
	private BMPreferences backupPreferences;
	
	/**
	 *	The default constructor.
	 */
	public BMPreferencesDialog() {
		super(new InvisibleFrame(), "Preferences", true);
		super.setBackground(Color.white);
		
		this.backupPreferences = (BMPreferences)(BMPreferencesManager.getPreferences().clone());
		
		this.generalPrefs = new GeneralPreferencesPanel();
		this.decompilerPrefs = new DecompilerPreferencesPanel();
		this.openPrefs = new OpenPreferencesPanel();
		this.savePrefs = new SavePreferencesPanel();
		
		this.tabbedPane = new LWTabbedPane(this.createCommonComponent());
		this.tabbedPane.addTab("General", generalPrefs);
		this.tabbedPane.addTab("Decompiler", decompilerPrefs);
		this.tabbedPane.addTab("Open", openPrefs);
		this.tabbedPane.addTab("Save", savePrefs);
		
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.addItemListener(this);
		super.add(this.tabbedPane);
		this.updatePanels();
		
		int dimension_w = BMPreferencesDialogPart.PREFERENCES_DIALOG_PART_DIMENSION.width;
		int dimension_h = BMPreferencesDialogPart.PREFERENCES_DIALOG_PART_DIMENSION.height;
		super.setSize(dimension_w + 4, dimension_h + 75);
		super.setResizable(false);
		super.setLocation(WindowUtilities.getCenterLocation(this));
	}
	
	private Component createCommonComponent() {
		Button defaultsButton = new Button("Use Defaults");
		defaultsButton.setSize(WindowUtilities.WIDE_BUTTON_DIMENSION);
		defaultsButton.setActionCommand("DEFAULTS");
		defaultsButton.addActionListener(this);
		
		Panel buttonPanel = WindowUtilities.createOKCancelButtonPanel(
			this,
			new Dimension(
				BMPreferencesDialogPart.PREFERENCES_DIALOG_PART_DIMENSION.width,
				defaultsButton.getSize().height*2 + 5
			),
			defaultsButton
		);
		
		return buttonPanel;
	}
	
	public void itemStateChanged(ItemEvent evt) {
		this.tabbedPane.invalidate();
		this.tabbedPane.doLayout();
		this.tabbedPane.validate();
	}
	
	public void updatePanels() {
		this.generalPrefs.updatePanel();
		this.decompilerPrefs.updatePanel();
		this.openPrefs.updatePanel();
		this.savePrefs.updatePanel();
	}
	
	public void setVisible(boolean newVisible) {
		if (newVisible) {
			/* HIDE ALL EDITORS - OR UPDATE??? */ // JVMInstructionSetEditor.hideAllEditors();
			this.updatePanels();
		}
		
		super.setVisible(newVisible);
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("OK")) {
			/* UPDATE FRAMES */ // BMEnvironment.updateFrames();
			super.setVisible(false);
		} else if (command.equals("CANCEL")) {
			BMPreferencesManager.setPreferences(this.backupPreferences);
			this.updatePanels();
			super.dispose();
		} else if (command.equals("DEFAULTS")) {
			BMPreferencesManager.useDefaultPreferences();
			this.updatePanels();
		}
	}
	
}


// ------------------------------------------------------------------------------------------

interface BMPreferencesDialogPart extends ItemListener {
	
	public static final Dimension PREFERENCES_DIALOG_PART_DIMENSION = new Dimension(500, 155);
	
	public void setPreferences();
	public void updatePanel();
	
}


// ------------------------------------------------------------------------------------------

class SavePreferencesPanel extends Panel implements BMPreferencesDialogPart {
	
	private LWCheckbox[] classTypes;
	
	SavePreferencesPanel() {
		super();
		super.setLayout(null);
		
		this.initComponents();
		this.addComponents();
		
		super.setSize(PREFERENCES_DIALOG_PART_DIMENSION);
	}
	
	private void initComponents() {
		LWCheckboxGroup group = new LWCheckboxGroup();
		
		this.classTypes = new LWCheckbox[2];
		classTypes[0] = new LWCheckbox(
			BMPreferencesManager.DEFAULT_CLASS_TYPE + " (default)", group, false
		);
		classTypes[1] = new LWCheckbox(
			BMPreferencesManager.JDK_CLASS_TYPE_display + " (JDK)", group, false
		);
		classTypes[0].setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		classTypes[1].setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		classTypes[0].addItemListener(this);
		classTypes[1].addItemListener(this);
	}
	
	private void addComponents() {
		GroupBox box2 = new GroupBox(null);
		box2.setLocation(20, 10);
		box2.setCaptionText("Class Type");
		
		classTypes[0].setLocation(7, 25);
		classTypes[1].setLocation(7, 40);
		
		box2.add(classTypes[0]);
		box2.add(classTypes[1]);
		super.add(box2);
	}
	
	public void updatePanel() {
		String classType = BMPreferencesManager.getClassType();
		if (classType.equals(BMPreferencesManager.DEFAULT_CLASS_TYPE)) {
			classTypes[0].setState(true);
		} else {
			classTypes[1].setState(true);
		}
	}
	
	public void setPreferences() {
		if (classTypes[0].getState()) {
			BMPreferencesManager.setClassType(BMPreferencesManager.DEFAULT_CLASS_TYPE);
		} else {
			BMPreferencesManager.setClassType(BMPreferencesManager.JDK_CLASS_TYPE);
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.setPreferences();
	}
	
}


// ------------------------------------------------------------------------------------------

class OpenPreferencesPanel extends Panel implements BMPreferencesDialogPart {
	
	OpenPreferencesPanel() {
		super();
		super.setLayout(null);
		
		this.initComponents();
		this.addComponents();
		
		super.setSize(PREFERENCES_DIALOG_PART_DIMENSION);
	}
	
	private void initComponents() {
	}
	
	private void addComponents() {
	}
	
	public void updatePanel() {
	}
	
	public void setPreferences() {
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.setPreferences();
	}
	
}


// ------------------------------------------------------------------------------------------

class DecompilerPreferencesPanel extends Panel implements BMPreferencesDialogPart {
	
	private LWCheckbox displayPackages;
	private LWCheckbox display_java_sun_Packages;
	private LWCheckbox displayMoreThan3;
	
	DecompilerPreferencesPanel() {
		super();
		super.setLayout(null);
		
		this.initComponents();
		this.addComponents();
		
		super.setSize(PREFERENCES_DIALOG_PART_DIMENSION);
	}
	
	private void initComponents() {
		this.displayPackages = new LWCheckbox("Display packages instead of classes in import list");
		displayPackages.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		displayPackages.addItemListener(this);
		
		this.display_java_sun_Packages = new LWCheckbox("Only \'java\' and \'sun\'");
		display_java_sun_Packages.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		display_java_sun_Packages.addItemListener(this);
		
		this.displayMoreThan3 = new LWCheckbox("Package if more than 3 class imports");
		displayMoreThan3.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		displayMoreThan3.addItemListener(this);
	}
	
	private void addComponents() {
		displayPackages.setLocation(20, 25);
		super.add(displayPackages);
		
		display_java_sun_Packages.setLocation(35, 40);
		super.add(display_java_sun_Packages);
		displayMoreThan3.setLocation(35, 55);
		super.add(displayMoreThan3);
	}
	
	public void updatePanel() {
		boolean displayPackages_state = BMPreferencesManager.getUsePackageImport();
		
		displayPackages.setState(displayPackages_state);
		display_java_sun_Packages.setState(BMPreferencesManager.getUsePackageImport_java_sun());
		displayMoreThan3.setState(BMPreferencesManager.getUsePackageImportIfMoreThan3Imports());
		
		display_java_sun_Packages.setEnabled(displayPackages_state);
		displayMoreThan3.setEnabled(displayPackages_state);
	}
	
	public void setPreferences() {
		BMPreferencesManager.setUsePackageImport(displayPackages.getState());
		BMPreferencesManager.setUsePackageImport_java_sun(display_java_sun_Packages.getState());
		BMPreferencesManager.setUsePackageImportIfMoreThan3Imports(displayMoreThan3.getState());
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.setPreferences();
		this.updatePanel();
	}
	
}


// ------------------------------------------------------------------------------------------

class GeneralPreferencesPanel extends Panel implements BMPreferencesDialogPart {
	
	private LWCheckbox[] branchDisplayModes;
	private LWCheckbox useFQNames;
	private LWCheckbox showMemoryMonitorAtStartup;
	private LWCheckbox showProgressBar;
	private LWCheckbox showLog;
	
	GeneralPreferencesPanel() {
		super();
		super.setLayout(null);
		
		this.initComponents();
		this.addComponents();
		
		super.setSize(PREFERENCES_DIALOG_PART_DIMENSION);
	}
	
	private void initComponents() {
		this.branchDisplayModes = new LWCheckbox[3];
		LWCheckboxGroup group = new LWCheckboxGroup();
		branchDisplayModes[0] = new LWCheckbox("PC Increment Only", group, false);
		branchDisplayModes[1] = new LWCheckbox("Actual PC Only", group, false);
		branchDisplayModes[2] = new LWCheckbox("Both", group, false);
		branchDisplayModes[0].setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		branchDisplayModes[1].setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		branchDisplayModes[2].setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		branchDisplayModes[0].addItemListener(this);
		branchDisplayModes[1].addItemListener(this);
		branchDisplayModes[2].addItemListener(this);
		
		this.useFQNames = new LWCheckbox("Use Fully Qualified Names");
		useFQNames.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		useFQNames.addItemListener(this);
		
		this.showMemoryMonitorAtStartup = new LWCheckbox("Show Memory Monitor On Startup");
		showMemoryMonitorAtStartup.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		showMemoryMonitorAtStartup.addItemListener(this);
		
		this.showProgressBar = new LWCheckbox("Show Progress Bar");
		showProgressBar.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		showProgressBar.addItemListener(this);
		
		this.showLog = new LWCheckbox("Show Log");
		showLog.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		showLog.addItemListener(this);
	}
	
	private void addComponents() {
		GroupBox box1 = new GroupBox(null);
		box1.setLocation(20, 10);
		box1.setCaptionText("Branch Display Mode");
		
		branchDisplayModes[0].setLocation(7, 25);
		branchDisplayModes[1].setLocation(7, 40);
		branchDisplayModes[2].setLocation(7, 55);
		
		box1.add(branchDisplayModes[0]);
		box1.add(branchDisplayModes[1]);
		box1.add(branchDisplayModes[2]);
		super.add(box1);
		
		useFQNames.setLocation(20, 100);
		super.add(this.useFQNames);
		
		showMemoryMonitorAtStartup.setLocation(220, 25);
		super.add(this.showMemoryMonitorAtStartup);
		
		showProgressBar.setLocation(220, 40);
		super.add(this.showProgressBar);
		
		showLog.setLocation(220, 55);
		super.add(this.showLog);
	}
	
	public void updatePanel() {
		int branchDisplayMode = BMPreferencesManager.getBranchDisplayMode();
		branchDisplayModes[branchDisplayMode].setState(true);
		
		boolean fullNames = BMPreferencesManager.getUseFullyQualifiedNames();
		useFQNames.setState(fullNames);
		
		boolean showMemoryMonitor = BMPreferencesManager.getShowMemoryMonitorAtStartup();
		showMemoryMonitorAtStartup.setState(showMemoryMonitor);
		
		boolean showProgressBar = BMPreferencesManager.getShowProgressBar();
		this.showProgressBar.setState(showProgressBar);
		
		boolean showLog = BMPreferencesManager.getShowLog();
		this.showLog.setState(showLog);
	}
	
	public void setPreferences() {
		int branchDisplayModes_count = branchDisplayModes.length;
		for (int i = 0; i < branchDisplayModes_count; i++) {
			if (branchDisplayModes[i].getState()) {
				BMPreferencesManager.setBranchDisplayMode(i);
				break;
			}
		}
		
		BMPreferencesManager.setUseFullyQualifiedNames(useFQNames.getState());
		BMPreferencesManager.setShowMemoryMonitorAtStartup(showMemoryMonitorAtStartup.getState());
		BMPreferencesManager.setShowProgressBar(showProgressBar.getState());
		BMPreferencesManager.setShowLog(showLog.getState());
	}
	
	public void itemStateChanged(ItemEvent e) {
		this.setPreferences();
	}
	
}