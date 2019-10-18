/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.barbre.fiddle.browser.sun.beanbuilder.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 * Single instance status bar. Supports a "busy bar" sort of 
 * like Netscape's
 * <p>
 * This class is also a MouseListener which listens to MOUSE_ENTERED
 * and MOUSE_EXITED events from Action derived components so that
 * the value of the Action.LONG_DESCRIPTION key is sent as a message
 * to the status bar. 
 * <p>
 * To enable this behavior, add the StatusBar
 * instance as a MouseListener to the component that was created from 
 * an Action.
 *
 * @version 1.5 02/27/02
 * @author  Mark Davidson
 */
public class StatusBar extends JPanel implements ActionListener,
						 MouseListener {    
    private static final int PROGRESS_MAX = 100;
    private static final int PROGRESS_MIN = 0;
    
    private JLabel label;
    private Dimension preferredSize;

    private JProgressBar progressBar;
    private Timer timer;

    // The direction that the progress bar is moving.
    private boolean forward;

    // Shared instance of the status bar
    private static StatusBar statusBar;

    private StatusBar()  {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEtchedBorder());
    
        // Create the progress bar.
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 
                                PROGRESS_MIN, PROGRESS_MAX);
        progressBar.setPreferredSize(new Dimension(60, progressBar.getPreferredSize().height + 2));
        progressBar.setVisible(false);
                           
	// Set a large blank label to set the preferred size.
        label = new JLabel("                                                                                        ");
        preferredSize = new Dimension(getWidth(label.getText()), 2 * getFontHeight());

        this.add(progressBar);
        this.add(label);
    }

    /**
     * Returns the shared StatusBar
     */
    public static StatusBar getInstance() {
	if (statusBar == null) {
	    statusBar = new StatusBar();
	}
	return statusBar;
    }

    /**
     * Sets the shared instance of the StatusBar.
     */
    public static void setInstance(StatusBar sb) {
	statusBar = sb;
    }
    
    /*
     * Returns the string width
     * @param s the string
     * @return the string width
     */
    protected int getWidth(String s) {
        FontMetrics fm = this.getFontMetrics(this.getFont());
        if (fm == null) {
            return 0;
        }
        return fm.stringWidth(s);
    }

    /*
     * Returns the height of a line of text
     * @return the height of a line of text
     */
    protected int getFontHeight() {
        FontMetrics fm = this.getFontMetrics(this.getFont());
        if (fm == null) {
            return 0;
        }
        return fm.getHeight();
    }

    /**
     * Returns the perferred size
     * @return the preferred size
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    /**
     * Sets non-transient status bar message
     * @param message the message to display on the status bar
     */
    public void setMessage(String message) {

        label.setText(message);
        label.repaint();
    }
    
    /** 
     * Starts the busy bar.
     */
    public void startBusyBar()  {
        forward = true;
        if (timer == null)  {
            setMessage("");
            progressBar.setVisible(true);
            timer = new Timer(15, this);
            timer.start();
        }
    }
    
    /** 
     * Stops the busy bar. No penalty for calling it.
     */
    public void stopBusyBar()  {
        if (timer != null)  {
            timer.stop();
            timer = null;
        }
        setMessage("");
        progressBar.setVisible(false);
        progressBar.setValue(PROGRESS_MIN);
    }
    
    //
    // ActionListener method.
    //
    
    
    
    public void actionPerformed(ActionEvent evt)  {
        int value = progressBar.getValue();
        
        if (forward)  {
            // The progress bar is incresing
            if (value < PROGRESS_MAX)  {
                progressBar.setValue(value + 1);
            } else {
                forward = false;
                progressBar.setValue(value - 1);
            }
        } else {
            // The progress bar is decreasing
            if (value > PROGRESS_MIN)  {
                progressBar.setValue(value - 1);
            } else {
                forward = true;
                progressBar.setValue(value + 1);
            }
        }
    }

    //
    // MouseListener methods 
    //

    public void mouseClicked(MouseEvent evt) {}
    public void mousePressed(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt) {}

    public void mouseExited(MouseEvent evt) {
	setMessage("");
    }

    /**
     * Takes the LONG_DESCRIPTION of the Action based components
     * and sends them to the Status bar
     */
    public void mouseEntered(MouseEvent evt) {
	if (evt.getSource() instanceof AbstractButton)  {
	    AbstractButton button = (AbstractButton)evt.getSource();
	    Action action = button.getAction();
	    if (action != null)  {
		String message = (String)action.getValue(Action.LONG_DESCRIPTION);
		setMessage(message);
	    }
	}
    }

} // end class StatusBar


