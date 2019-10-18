package com.barbre.fiddle.dialogs;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.barbre.fiddle.FiddleConstants;

public class AboutBox {
		private static final String copyright = //
		"SIDL Fiddle version " + FiddleConstants.VERSION + ", Copyright (C) 2002 Mike Barbre. " + //
		"\n\nTHIS PROGRAM, IS \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, " + //
		"ANY PROBLEM YOU ENCOUNTER AS A RESULT OF THIS PROGRAM IS YOUR OWN PROBLEM AND NOT MINE.\n\n" + //
		"This is free software.  You may change or modify it as long as I am credited as the original " + //
		"copyright holder.  If you enhance this product, I would like to have the updated code but this " + //
		"is not a requirement.";

	private static final String[] credits = { //
		"Everquest(tm) is copyrighted by Sony Entertainment.", //
		"Targa image reader Copyright (c) 1997, Marcel Schoen and Andre Pinheiro", //
		"Bitmap reader modified from Javaworld tip #43 By Jeff West, with John D. Mitchell", //
		"Property editors and Property Panel based on Bean Builder. Copyright 2002 Sun Microsystems, Inc. All rights reserved." };

	private static final String[] tabs = { "About", "Bean Builder" };

	private static final String SUN_DISCLAIMER =
		"THIS VERSION USES \"BEAN BUILDER\" BY SUN MICROSYSTEMS.\nBELOW IS THE REQUIRED COPYRIGHT NOTICE\n\n\nBean Builder: A Component Assembler to Demonstrate new UI Technologies. 1.0 beta January 2002\n\n Copyright 2002 Sun Microsystems, Inc. All rights reserved. \n  \n Redistribution and use in source and binary forms, with or \n without modification, are permitted provided that the following \n conditions are met: \n  \n - Redistributions of source code must retain the above copyright \n   notice, this list of conditions and the following disclaimer. \n  \n - Redistribution in binary form must reproduce the above \n   copyright notice, this list of conditions and the following \n   disclaimer in the documentation and/or other materials \n   provided with the distribution. \n  \n Neither the name of Sun Microsystems, Inc. or the names of \n contributors may be used to endorse or promote products derived \n from this software without specific prior written permission. \n  \n This software is provided \"AS IS,\" without a warranty of any \n kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND \n WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, \n FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY \n EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY \n DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR \n RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR \n ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE \n FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, \n SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER \n CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF \n THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS \n BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. \n  \n You acknowledge that this software is not designed, licensed or \n intended for use in the design, construction, operation or \n maintenance of any nuclear facility.	";

	private static JPanel thePanel = null;

	/**
	 * @see Object#Object()
	 */
	public AboutBox() {
		super();
	}

	/**
	 * Method showDialog.
	 */
	public void showDialog() {
		JTabbedPane tabber = new JTabbedPane();
		tabber.addTab(tabs[0], getPanel());
		tabber.addTab(tabs[1], getSunDisclaimer());
		JOptionPane.showMessageDialog(null, tabber, "About SIDL Fiddle", JOptionPane.PLAIN_MESSAGE);
	}

	private JScrollPane getSunDisclaimer() {
		JScrollPane scroller = new JScrollPane();
		scroller.setPreferredSize(getPanel().getSize());
		JTextArea text = new JTextArea(SUN_DISCLAIMER);
		text.setEditable(false);
		scroller.setViewportView(text);
		return scroller;
	}

	/**
	 * Method getPanel.
	 * @return JPanel
	 */
	private JPanel getPanel() {
		if (thePanel == null) {
			thePanel = new JPanel();
			thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.Y_AXIS));
			initMainPanel();
		}
		return thePanel;
	}

	/**
	 * Method initPanel.
	 */
	private void initMainPanel() {
		String jVersion = System.getProperty("java.runtime.version");
		String os = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		long memory = Runtime.getRuntime().freeMemory() / 1024;

		JLabel header = createLabel("SIDL Fiddle " + FiddleConstants.VERSION);
		header.setFont(new Font("Helvetica", Font.BOLD, 14));
		thePanel.add(header);

		thePanel.add(createLabel("Java version: " + jVersion + "     OS: " + os + " " + osVersion + "     Free memory: " + memory + "K"));

		thePanel.add(createLabel(" "));
		JTextArea t = new JTextArea(copyright, 12, 40);
		t.setEditable(false);
		t.setWrapStyleWord(true);
		t.setLineWrap(true);
		t.setAlignmentX(0f);
		t.setOpaque(false);
		t.setBorder(BorderFactory.createTitledBorder("As legal as it gets"));
		t.setMinimumSize(t.getPreferredSize());
		thePanel.add(t);

		thePanel.add(createLabel(" "));
		JList list = new JList(credits);
		list.setBackground(thePanel.getBackground());
		list.setBorder(BorderFactory.createTitledBorder("Credit"));
		list.setAlignmentX(0f);
		thePanel.add(list);

	}

	/**
	 * Method createLabel.
	 * @param text
	 * @return JLabel
	 */
	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.black);
		label.setAlignmentX(0f);
		return label;
	}

	/**
	 * Method main.
	 * @param arguments
	 */
	public static void main(String[] arguments) {
		new AboutBox().showDialog();
		System.exit(0);
	}
}