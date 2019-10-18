package com.barbre.fiddle.io.utility;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.ITypeManager;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre44.util.Debug;

/**
 * When a property changes, this manager will test to see if it has a
 * related property for it's "object" value.  If so, this manager will
 * apply the change to the "object" property.
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Dec 13, 2002  Created class.
 *
 * </ul><p>
 */
public class IndirectReferenceManager implements PropertyChangeListener {
	private static IndirectReferenceManager instance = null;
	private PropertyDescriptor[] properties;
	private String refProperty;
	private Object source;
	private String property;
	private Object value;
	private PropertyDescriptor pd;

	/**
	 * Constructor for _IndirectReferenceManager.
	 */
	private IndirectReferenceManager() {
		super();
	}

	public static IndirectReferenceManager getInstance() {
		if (instance == null) {
			instance = new IndirectReferenceManager();
		}
		return instance;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			source = evt.getSource();
			property = evt.getPropertyName();
			value = evt.getNewValue();

			// Guard against property changes to the object reference.  A change
			// to this must be handled elsewhere.
			if (property.indexOf("Object") > 0)
				return;

			refProperty = property + "Object";

			properties = BeanInfoManager.getBeanInfo(source.getClass()).getPropertyDescriptors();
			for (int i = 0; i < properties.length; i++) {
				pd = properties[i];
				if (pd.getName().equalsIgnoreCase(refProperty)) {
					if (pd instanceof IndexedPropertyDescriptor) {
						indexedChange();
					} else {
						nonIndexedChange();
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void indexedChange() throws IllegalAccessException, InvocationTargetException {
		IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
		int length = Array.getLength(value);
		Method method = pd.getWriteMethod();
		ITypeManager manager = getTypeManager(source);

		Object theArray = Array.newInstance(ipd.getIndexedPropertyType(), length);

		for (int i = 0; i < length; i++) {
			String aValue = (String) Array.get(value, i);
			Debug.println(this, "value=" + value + ";  aValue=" + aValue);
			Object args = (manager.get(aValue));
			checkArg(source, pd, aValue, args);
			Array.set(theArray, i, args);
		}

		method.invoke(source, new Object[] { theArray });
	}

	private void nonIndexedChange() throws IllegalAccessException, InvocationTargetException {
		Method method = pd.getWriteMethod();
		Object args = (getTypeManager(source).get("" + value));
		Debug.println(this, "value=" + value + ";  args=" + args);		
		checkArg(source, pd, value, args);
		method.invoke(source, new Object[] { args });
	}

	private void checkArg(Object source, PropertyDescriptor pd, Object value, Object args) {
		if (args == null) {
			Debug.println(this, "Type manager returned null for " + source + "::" + pd.getName() + " value = " + value);
		}
	}

	private ITypeManager getTypeManager(Object source) {
		UIFile file = ((INode) source).getFile();
		UIFileSet set = file.getSet();
		ITypeManager manager = set.getTypeManager();
		return manager;
	}

}
