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

package com.barbre.fiddle.browser.sun.beanbuilder.editors;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A property editor for editing strings.
 *
 * @version 1.3 02/27/02
 * @author  Mark Davidson
 */
public class SwingStringEditor extends SwingEditorSupport {
    
    private JTextField textfield;

    public SwingStringEditor() {
        textfield = new JTextField();
        textfield.addKeyListener(new KeyAdapter()  {
            // XXX - JTextfield should send an actionPerformed event.
            // this was broken for 1.3 beta but fixed in 1.3. This
            // is the workaround.
            public void keyPressed(KeyEvent evt)  {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)  {
                    setValue(textfield.getText());
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(textfield);
    }

    public void setValue(Object value)  {
        super.setValue(value);
        if (value != null)  {
            textfield.setText(value.toString());
        } else {
            textfield.setText("");
        }
    }
}