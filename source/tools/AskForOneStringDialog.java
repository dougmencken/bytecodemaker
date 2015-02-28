// ===========================================================================
//	AskForOneStringDialog.java (part of douglas.mencken.tools package)
// ===========================================================================

package douglas.mencken.tools;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.beans.LWLabel;
import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.FontUtilities;

/**
 *	<code>AskForOneStringDialog</code>
 *
 *	@version 1.06f1
 */

public class AskForOneStringDialog extends Dialog implements ActionListener {
	
	protected TextField textField;
	protected String theResult;
	
	public AskForOneStringDialog(Frame parent, String message) {
		// modal dialog with no title
		super(parent, "", true);
		super.setBackground(Color.white);
		this.theResult = "";
		
		this.addComponents(message);
		
		super.setSize(270, 80);
		super.setResizable(false);
		
		Point preferredLocation = WindowUtilities.getCenterLocation(this);
		super.setLocation(preferredLocation.x, preferredLocation.y - 100);
	}
	
	public AskForOneStringDialog(Frame parent) {
		this(parent, "");
	}
	
	public AskForOneStringDialog(String message) {
		this(new InvisibleFrame(), message);
	}
	
	public AskForOneStringDialog() {
		this(new InvisibleFrame(), "");
	}
	
	private void addComponents(String message) {
		Panel buttonPanel = WindowUtilities.createOKCancelButtonPanel(
			this,
			new Dimension(270, 30),
			null
		);
		
		this.textField = new TextField("");
		this.textField.setFont(FontUtilities.createFont(FontUtilities.MONACO_9));
		
		LWLabel messageLabel = new LWLabel(
			message, FontUtilities.createFont(FontUtilities.MONACO_9), LWLabel.LEFT
		);
		
		GridBagLayout gridbag = new GridBagLayout();
		super.setLayout(gridbag);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.anchor = GridBagConstraints.NORTHWEST;
			gridbag.setConstraints(messageLabel, c);
		}
		add(messageLabel);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.gridx = 0;
			c.gridy = GridBagConstraints.REMAINDER;
			c.fill = GridBagConstraints.HORIZONTAL;
			gridbag.setConstraints(textField, c);
		}
		add(textField);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.SOUTH/*EAST*/;
			gridbag.setConstraints(buttonPanel, c);
		}
		add(buttonPanel);
	}
	
	public String getString() { return this.theResult; }
	
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		
		if (command.equals("CANCEL")) {
			this.theResult = "";
		} else if (command.equals("OK")) {
			this.theResult = textField.getText();
		}
		
		super.dispose();
	}
	
}