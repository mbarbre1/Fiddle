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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ChooserComboPopup extends JPopupMenu {
  SwingColorEditor ce;

  public ChooserComboPopup(SwingColorEditor c){
    super();
    this.ce = c;
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    SwatchChooserPanel s = new SwatchChooserPanel(c,this);
    s.buildChooser();
    p.add(s,BorderLayout.NORTH); 
    JButton b = new JButton("Other ...");
    b.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	Color color = null;
	color = JColorChooser.showDialog(getParent(), "Color Chooser", color);
	getCE().setValue(color);
	// XXX: update the recentSwatch
	setVisible(false);
      }
    });
    p.add(b, BorderLayout.SOUTH); // , BorderLayout.SOUTH);
    add(p);
    addMouseListener(new PopupListener());
  }

  public SwingColorEditor getCE(){
    return this.ce;
  }

  class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e){
    }

    public void mouseReleased(MouseEvent e){
      setVisible(false);
    }
  }

}
