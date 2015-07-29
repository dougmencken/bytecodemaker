/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.bm.frames;

import java.awt.*;
import java.awt.event.*;

import com.apple.mrj.MRJApplicationUtils;
import com.apple.mrj.MRJAboutHandler;

import douglas.mencken.beans.RoundButton;
import douglas.mencken.beans.LWLabel;
import douglas.mencken.util.InvisibleFrame;
import douglas.mencken.util.WindowUtilities;
import douglas.mencken.util.FontUtilities;
import douglas.mencken.icons.IconCanvas;
import douglas.mencken.bm.BMEnvironment;

/**
 *	<code>BMAbout</code>
 *
 *	@version 1.1
 */

public class BMAbout extends Dialog
implements ActionListener, MRJAboutHandler {
	
	private BMLogo logo;
	
	public BMAbout() {
		super(new InvisibleFrame(), "About Bytecode Maker", true);
		MRJApplicationUtils.registerAboutHandler(this);
		
		this.logo = new BMLogo();
		IconCanvas logoCanvas = new IconCanvas(logo);
		
		RoundButton button = new RoundButton("OK");
		button.addActionListener(this);
		
		LWLabel versionLabel = new LWLabel(BMAbout.getVersionString(), LWLabel.LEFT);
		
		LWLabel copyrightLabel = new LWLabel(
			"(c) 1997-2003, 2015 Douglas Mencken",
			FontUtilities.createFont(FontUtilities.GENEVA_9),
			LWLabel.LEFT
		);
		
		/* LWLabel allRightsLabel = new LWLabel(
			"All rights reserved",
			FontUtilities.createFont(FontUtilities.GENEVA_9),
			LWLabel.LEFT
		); */
		LWLabel gplLabel = new LWLabel(
			"This is free software, licensed under GNU GPL v3+",
			FontUtilities.createFont(FontUtilities.GENEVA_9),
			LWLabel.LEFT
		);
		gplLabel.setSize(gplLabel.getPreferredSize().width + 20, gplLabel.getPreferredSize().height + 20);
		
		Panel infoPanel = new Panel(new GridLayout(4, 1));
		infoPanel.add(versionLabel);
		infoPanel.add(new LWLabel());
		infoPanel.add(copyrightLabel);
		infoPanel.add(gplLabel);
		
		GridBagLayout gridbag = new GridBagLayout();
		super.setLayout(gridbag);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.anchor = GridBagConstraints.NORTH;
			gridbag.setConstraints(logoCanvas, c);
		}
		super.add(logoCanvas);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.anchor = GridBagConstraints.SOUTHWEST;
			gridbag.setConstraints(infoPanel, c);
		}
		super.add(infoPanel);
		
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.anchor = GridBagConstraints.SOUTHEAST;
			gridbag.setConstraints(button, c);
		}
		super.add(button);
		
		super.setBackground(Color.white);
		super.setSize(320, 80 + button.getPreferredSize().height);
		
		Point pos = WindowUtilities.getCenterLocation(this);
		super.setLocation(pos.x, pos.y - 80);
		super.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.setVisible(false);
	}
	
	public static String getVersionString() {
		return BMEnvironment.makeVersionString();
	}
	
	public void handleAbout() {
		super.setVisible(true);
	}
	
}
