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
package com.barbre.fiddle.browser.sun.beanbuilder.property;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingEditorSupport;



/**
 * Column model for the PropertyTable
 *
 * @version 1.6 02/27/02
 * @author  Mark Davidson
 */
public class PropertyColumnModel extends DefaultTableColumnModel  {
    private final static String COL_LABEL_PROP = "Property";
    private final static String COL_LABEL_DESC = "Description";
    private final static String COL_LABEL_VALUE = "Value";

    private static final int minColWidth = 30;

    public PropertyColumnModel()  {
        // Configure the columns and add them to the model
        TableColumn column;

        // Property
        column = new TableColumn(0);
        column.setHeaderValue(COL_LABEL_PROP);
        column.setPreferredWidth(minColWidth);
        column.setCellRenderer(new PropertyNameRenderer());
        addColumn(column);

        // Value
        column = new TableColumn(1);
        column.setHeaderValue(COL_LABEL_VALUE);
        column.setPreferredWidth(minColWidth);
        column.setCellEditor(new PropertyValueEditor());
        column.setCellRenderer(new PropertyValueRenderer());
        addColumn(column);
    }

    /**
     * Renders the name of the property. Sets the short description of the
     * property as the tooltip text.
     */
    class PropertyNameRenderer extends DefaultTableCellRenderer  {

        /**
         * Get UI for current editor, including custom editor button
         * if applicable.
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
						       boolean isSelected, boolean hasFocus, int row, int column) {

            PropertyTableModel model = (PropertyTableModel)table.getModel();
            PropertyDescriptor desc = model.getPropertyDescriptor(row);

            setToolTipText(desc.getShortDescription());
            setBackground(UIManager.getColor("control"));
            
            return super.getTableCellRendererComponent(table, value,
						       isSelected, hasFocus, row, column);
        }
    }


    /**
     * Renderer for a value with a property editor or installs the default cell rendererer.
     */
    class PropertyValueRenderer implements TableCellRenderer  {

    	private DefaultTableCellRenderer renderer;
        private PropertyEditor editor;

        private Hashtable editors;
        private Class type;

        private Border selectedBorder;
        private Border emptyBorder;

        public PropertyValueRenderer()  {
	    renderer = new DefaultTableCellRenderer();
            editors = new Hashtable();
        }

        /**
         * Get UI for current editor, including custom editor button
         * if applicable.
         * XXX - yuck! yuck! yuck!!!!
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
						       boolean isSelected, boolean hasFocus, int row, int column) {

            PropertyTableModel model = (PropertyTableModel)table.getModel();
            type = model.getPropertyType(row);
            if (type != null)  {
                editor = (PropertyEditor)editors.get(type);
                if (editor == null)  {
                    editor = model.getPropertyEditor(row);

                    if (editor != null)  {
                        editors.put(type, editor);
                    }
                }
            } else {
                editor = null;
            }

            if (editor != null)  {
                // Special case for the enumerated properties. Must reinitialize
                // to reset the combo box values.
                if (editor instanceof SwingEditorSupport)  {
                    ((SwingEditorSupport)editor).init(model.getPropertyDescriptor(row));
                }

                editor.setValue(value);

                Component comp = editor.getCustomEditor();
                if (comp != null)  {
                    comp.setEnabled(isSelected);

                    if (comp instanceof JComponent)  {
                        if (isSelected)  {
                            if (selectedBorder == null)
                                selectedBorder = BorderFactory.createLineBorder(table.getSelectionBackground(), 1);

                            ((JComponent)comp).setBorder(selectedBorder);
                        } else {
                            if (emptyBorder == null)
                                emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

                            ((JComponent)comp).setBorder(emptyBorder);
                        }
                    }
                    return comp;
                }
            }
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        /**
         * Retrieves the property editor for this value.
         */
        public PropertyEditor getPropertyEditor()  {
            return editor;
        }
    }

}
