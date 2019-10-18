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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ListIterator;

import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;

import com.barbre.fiddle.browser.sun.beanbuilder.util.BeanInfoFactory;
import com.barbre.fiddle.browser.sun.beanbuilder.util.DescriptorComparator;
import com.barbre44.util.Debug;

/**
 * Table model used to obtain property names and values. This model encapsulates an array 
 * of PropertyDescriptors.
 *
 * @version 1.16 02/27/02
 * @author  Mark Davidson
 */
public class PropertyTableModel extends AbstractTableModel {

	private PropertyDescriptor[] descriptors;
	private BeanDescriptor beanDescriptor;
	private BeanInfo info;
	private Object bean;

	// Cached property editors.
	private static Hashtable propEditors;

	// Shared instance of a comparator
	private static DescriptorComparator comparator = new DescriptorComparator();

	private static final int NUM_COLUMNS = 2;

	public static final int COL_NAME = 0;
	public static final int COL_VALUE = 1;

	// Filter options
	public static final int VIEW_ALL = 0;
	public static final int VIEW_STANDARD = 1;
	public static final int VIEW_EXPERT = 2;
	public static final int VIEW_READ_ONLY = 3;
	public static final int VIEW_BOUND = 4;
	public static final int VIEW_CONSTRAINED = 5;
	public static final int VIEW_HIDDEN = 6;
	public static final int VIEW_PREFERRED = 7;

	private int currentFilter = VIEW_STANDARD;

	// Sort options
	public static final int SORT_DEF = 0;
	public static final int SORT_NAME = 1;
	public static final int SORT_TYPE = 2;

	private int sortOrder = SORT_NAME;

	public PropertyTableModel() {

		if (propEditors == null) {
			propEditors = new Hashtable();
			registerPropertyEditors();
		}
	}

	public PropertyTableModel(Object bean) {
		this();
		setObject(bean);
	}

	/**
	 * Sets the current filter of the Properties.
	 *
	 * @param filter one of VIEW_ constants
	 */
	public void setFilter(int filter) {
		this.currentFilter = filter;
		filterTable(currentFilter);
	}

	/** 
	 * Returns the current filter type
	 */
	public int getFilter() {
		return currentFilter;
	}

	/**
	 * Sets the current sort order on the data
	 * @param sort one of the SORT_ constants
	 */
	public void setSortOrder(int sort) {
		this.sortOrder = sort;
		sortTable(sortOrder);
	}

	public int getSortOrder() {
		return sortOrder;
	}

	/** 
	 * Set the table model to represents the properties of the object.
	 */
	public void setObject(Object bean) {
		this.bean = bean;

		info = BeanInfoFactory.getBeanInfo(bean.getClass());

		if (info != null) {
			beanDescriptor = info.getBeanDescriptor();
			filterTable(getFilter());
		}
	}

	/** 
	 * Return the current object that is represented by this model.
	 */
	public Object getObject() {
		return bean;
	}

	/**
	 * Get row count (total number of properties shown)
	 */
	public int getRowCount() {
		if (descriptors == null) {
			return 0;
		}
		return descriptors.length;
	}

	/**
	 * Get column count (2: name, value)
	 */
	public int getColumnCount() {
		return NUM_COLUMNS;
	}

	/**
	 * Check if given cell is editable
	 * @param row table row
	 * @param col table column
	 */
	public boolean isCellEditable(int row, int col) {
		if (col == COL_VALUE && descriptors != null) {
			return (descriptors[row].getWriteMethod() == null) ? false : true;
		} else {
			return false;
		}
	}

	/**
	 * Get text value for cell of table
	 * @param row table row
	 * @param col table column
	 */
	public Object getValueAt(int row, int col) {

		Object value = null;

		if (col == COL_NAME) {
			value = descriptors[row].getDisplayName();
		} else {
			// COL_VALUE is handled        
			Method getter = descriptors[row].getReadMethod();

			if (getter != null) {
				Class[] paramTypes = getter.getParameterTypes();
				Object[] args = new Object[paramTypes.length];

				try {
					for (int i = 0; i < paramTypes.length; i++) {
						Debug.println(this, "\tShouldn't happen! getValueAt getter = " + getter + " parameter = " + paramTypes[i]);
						args[i] = paramTypes[i].newInstance();
					}
				} catch (Exception ex) {
					// XXX - handle better
					ex.printStackTrace();
				}

				try {
					value = getter.invoke(bean, args);
				} catch (IllegalArgumentException ex) {
					// XXX - handle better
					ex.printStackTrace();
				} catch (IllegalAccessException ex2) {
					// XXX - handle better
					ex2.printStackTrace();
				} catch (InvocationTargetException ex3) {
					// XXX - handle better
					ex3.printStackTrace();
				} catch (NoSuchMethodError ex4) {
					// XXX - handle better
					System.out.println("NoSuchMethodError for method invocation");
					System.out.println("Bean: " + bean.toString());
					System.out.println("Getter: " + getter.getName());
					System.out.println("Getter args: ");
					for (int i = 0; i < args.length; i++) {
						System.out.println("\t" + "type: " + paramTypes[i] + " value: " + args[i]);
					}
					ex4.printStackTrace();
				}
			}

		}
		return value;
	}

	/** 
	 * Set the value of the Values column.
	 */
	public void setValueAt(Object value, int row, int column) {

		if (column != COL_VALUE || descriptors == null || row > descriptors.length) {
			return;
		}

		Method setter = descriptors[row].getWriteMethod();
		if (setter != null) {
			try {
				setter.invoke(bean, new Object[] { value });
			} catch (IllegalArgumentException ex) {
				// XXX - handle better
				System.out.println("Setter: " + setter + "\nArgument: " + value.getClass().toString());
				System.out.println("Row: " + row + " Column: " + column);
				ex.printStackTrace();
				System.out.println("\n");
			} catch (IllegalAccessException ex2) {
				// XXX - handle better
				System.out.println("Setter: " + setter + "\nArgument: " + value.getClass().toString());
				System.out.println("Row: " + row + " Column: " + column);
				ex2.printStackTrace();
				System.out.println("\n");
			} catch (InvocationTargetException ex3) {
				// XXX - handle better
				System.out.println("Setter: " + setter + "\nArgument: " + value.getClass().toString());
				System.out.println("Row: " + row + " Column: " + column);
				ex3.printStackTrace();
				System.out.println("\n");
			}
		}
	}

	/** 
	 * Returns the Java type info for the property at the given row.
	 */
	public Class getPropertyType(int row) {
		return descriptors[row].getPropertyType();
	}

	/** 
	 * Returns the PropertyDescriptor for the row.
	 */
	public PropertyDescriptor getPropertyDescriptor(int row) {
		return descriptors[row];
	}

	/** 
	 * Returns a new instance of the property editor for a given class. If an
	 * editor is not specified in the property descriptor then it is looked up
	 * in the PropertyEditorManager.
	 */
	public PropertyEditor getPropertyEditor(int row) {
		Class cls = descriptors[row].getPropertyEditorClass();

		PropertyEditor editor = null;

		if (cls != null) {
			try {
				editor = (PropertyEditor) cls.newInstance();
			} catch (Exception ex) {
				// XXX - debug
				System.out.println("PropertyTableModel: Instantiation exception creating PropertyEditor");
			}
		} else {
			// Look for a registered editor for this type.
			Class type = getPropertyType(row);
			if (type != null) {
				editor = (PropertyEditor) propEditors.get(type);

				if (editor == null) {
					// Load a shared instance of the property editor.
					editor = PropertyEditorManager.findEditor(type);
					if (editor != null)
						propEditors.put(type, editor);
				}

				if (editor == null) {
					// Use the editor for Object.class
					editor = (PropertyEditor) propEditors.get(Object.class);
					if (editor == null) {
						editor = PropertyEditorManager.findEditor(Object.class);
						if (editor != null)
							propEditors.put(Object.class, editor);
					}

				}
			}
		}
		return editor;
	}

	/** 
	 * Returns a flag indicating if the encapsulated object has a customizer.
	 */
	public boolean hasCustomizer() {
		if (beanDescriptor != null) {
			Class cls = beanDescriptor.getCustomizerClass();
			return (cls != null);
		}

		return false;
	}

	/** 
	 * Gets the customizer for the current object.
	 * @return New instance of the customizer or null if there isn't a customizer.
	 */
	public Component getCustomizer() {
		Component customizer = null;

		if (beanDescriptor != null) {
			Class cls = beanDescriptor.getCustomizerClass();

			if (cls != null) {
				try {
					customizer = (Component) cls.newInstance();
				} catch (Exception ex) {
					// XXX - debug
					System.out.println("PropertyTableModel: Instantiation exception creating Customizer");
				}
			}
		}

		return customizer;
	}

	/** 
	 * Sorts the table according to the sort type.
	 * 
	 */
	public void sortTable(int sort) {
		if (sort == SORT_DEF || descriptors == null)
			return;

		if (sort == SORT_NAME) {
			Arrays.sort(descriptors, comparator);
		} else {
			Arrays.sort(descriptors, comparator);
		}
		fireTableDataChanged();
	}

	/** 
	 * Filters the table to display only properties with specific attributes.
	 * Will sort the table after the data has been filtered.
	 *
	 * @param view The properties to display.
	 */
	public void filterTable(int view) {
		if (info == null)
			return;

		descriptors = info.getPropertyDescriptors();

		// Use collections to filter out unwanted properties
		ArrayList list = new ArrayList();
		list.addAll(Arrays.asList(descriptors));

		ListIterator iterator = list.listIterator();
		PropertyDescriptor desc;
		while (iterator.hasNext()) {
			desc = (PropertyDescriptor) iterator.next();

			switch (view) {
				case VIEW_ALL :
					if (desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_STANDARD :
					if (desc.getWriteMethod() == null || desc.isExpert() || desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_EXPERT :
					if (desc.getWriteMethod() == null || !desc.isExpert() || desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_READ_ONLY :
					if (desc.getWriteMethod() != null || desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_HIDDEN :
					if (!desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_BOUND :
					if (!desc.isBound() || desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_CONSTRAINED :
					if (!desc.isConstrained() || desc.isHidden()) {
						iterator.remove();
					}
					break;
				case VIEW_PREFERRED :
					if (!desc.isPreferred() || desc.isHidden()) {
						iterator.remove();
					}
					break;
			}
		}
		descriptors = (PropertyDescriptor[]) list.toArray(new PropertyDescriptor[list.size()]);
		sortTable(getSortOrder());
	}

	/** 
	 * Method which registers property editors for types.
	 */
	private static void registerPropertyEditors() {
		PropertyEditorManager.registerEditor(Color.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingColorEditor.class);
		PropertyEditorManager.registerEditor(Font.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingFontEditor.class);
		PropertyEditorManager.registerEditor(Border.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingBorderEditor.class);
		PropertyEditorManager.registerEditor(Boolean.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingBooleanEditor.class);
		PropertyEditorManager.registerEditor(boolean.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingBooleanEditor.class);
		PropertyEditorManager.registerEditor(Integer.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingIntegerEditor.class);
		PropertyEditorManager.registerEditor(int.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingIntegerEditor.class);
		PropertyEditorManager.registerEditor(Float.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingNumberEditor.class);
		PropertyEditorManager.registerEditor(float.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingNumberEditor.class);
		PropertyEditorManager.registerEditor(java.awt.Dimension.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingDimensionEditor.class);
		PropertyEditorManager.registerEditor(java.awt.Point.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingPointEditor.class);
		PropertyEditorManager.registerEditor(java.awt.Rectangle.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingRectangleEditor.class);
		PropertyEditorManager.registerEditor(java.awt.Insets.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingInsetsEditor.class);
		PropertyEditorManager.registerEditor(String.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingStringEditor.class);
		PropertyEditorManager.registerEditor(Object.class, com.barbre.fiddle.browser.sun.beanbuilder.editors.SwingObjectEditor.class);
	}
}
