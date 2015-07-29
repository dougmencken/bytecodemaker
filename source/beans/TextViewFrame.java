/*
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package douglas.mencken.beans;

import java.awt.*;
import java.awt.event.*;
import douglas.mencken.util.WindowCloser;

/**
 *	<code>TextViewFrame</code>
 *
 *	@version 1.06.2f
 */

public class TextViewFrame extends Frame {
	
	private TextView textView;
	
	public TextViewFrame(String title, TextView view, boolean useNumbersView) {
		super(title);
		this.textView = view;
		
		Dimension screen = getToolkit().getScreenSize();
		setBounds(3, 5, (int)(screen.width / 2), screen.height - 25);
		setResizable(false);
		
		setLayout(new BorderLayout());
		add("Center", textView);
		if (useNumbersView) {
			add("West", new NumbersView(textView));
		}
		
		new WindowCloser(this);
	}
	
	public TextViewFrame(String title, TextView view) {
		this(title, view, false);
	}
	
	public TextViewFrame(TextView view, boolean useNumbersView) {
		this("", view, useNumbersView);
	}
	
	public TextViewFrame(TextView view) {
		this("", view, false);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		if (width < 120) width = 120;
		if (height < 90) height = 90;
		super.setBounds(x, y, width, height);
	}
	
	public TextView getTextView() {
		return textView;
	}
	
}