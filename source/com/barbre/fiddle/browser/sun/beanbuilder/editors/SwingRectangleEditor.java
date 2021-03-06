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

import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * An editor for editing Rectangle.
 *
 * @version  %I% %G%
 * @author  Mark Davidson
 */
public class SwingRectangleEditor extends SwingEditorSupport {
    
    private JTextField xTF;
    private JTextField yTF;
    private JTextField widthTF;
    private JTextField heightTF;

    public SwingRectangleEditor() {
        xTF = new JTextField();
        xTF.setDocument(new NumberDocument());
        yTF = new JTextField();
        yTF.setDocument(new NumberDocument());
        widthTF = new JTextField();
        widthTF.setDocument(new NumberDocument());
        heightTF = new JTextField();
        heightTF.setDocument(new NumberDocument());
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("X: "));
        panel.add(xTF);
        panel.add(new JLabel("Y: "));
        panel.add(yTF);
        panel.add(new JLabel("Width: "));
        panel.add(widthTF);
        panel.add(new JLabel("Height: "));
        panel.add(heightTF);
    }
    
    public void setValue(Object value)  {
        super.setValue(value);
        
	if (value != null) {
	    Rectangle rect = (Rectangle)value;
	    xTF.setText(Integer.toString(rect.x));
	    yTF.setText(Integer.toString(rect.y));
	    widthTF.setText(Integer.toString(rect.width));
	    heightTF.setText(Integer.toString(rect.height));
	} else {
	    // null rect
	    xTF.setText("");
	    yTF.setText("");
	    widthTF.setText("");
	    heightTF.setText("");
	}
    }
    
    public Object getValue()  {
	try {
	    int x = Integer.parseInt(xTF.getText());
	    int y = Integer.parseInt(yTF.getText());
	    int width = Integer.parseInt(widthTF.getText());
	    int height = Integer.parseInt(heightTF.getText());
	    
	    return new Rectangle(x, y, width, height);
	} catch (NumberFormatException ex) {
	    // Fall out but return null
	}
	return null;
    }

}
