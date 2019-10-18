package com.barbre.fiddle.io.loaders;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.io.utility.BeanInfoManager;
import com.barbre44.util.Debug;

/**
 * Moves data from XML file to the object reference
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Dec 14, 2002  Created class.
 *
 * </ul><p>
 */
public final class XMLDataLoader {
	private String propertyName;
	private String value;
	private PropertyDescriptor[] pd;
	private IndexedPropertyDescriptor ipd;
	private Object obj;
	private Class type;

	/**
	 * Constructor for XMLDataLoader.
	 */
	public XMLDataLoader() {
		super();
	}

	public void load(Element el, Object object) {
		try {
			Debug.println(this, "load " + el.getName());
			pd = BeanInfoManager.getBeanInfo(object.getClass()).getPropertyDescriptors();
			for (int i = 0; i < pd.length; i++) {
				if (pd[i].getName().indexOf("Object") > 0) {
					continue;
				}

				if (pd[i].getName().equalsIgnoreCase("class")) {
					continue;
				}

				propertyName = pd[i].getName().substring(0, 1).toUpperCase() + pd[i].getName().substring(1);

				value = el.getChildText(propertyName, el.getNamespace());
				if (propertyName.equals("Item")) {
					value = el.getAttributeValue("item");
				}

				if (value == null) {
					continue;
				}
				//System.out.println("  " + propertyName + " = " + value );				

				if (pd[i] instanceof IndexedPropertyDescriptor) {
					doIndexedProperty(el, object, pd[i], propertyName);
				} else {
					Object theValue = getValueObject(pd[i], el, propertyName, object, value);
					if (theValue != null)
						pd[i].getWriteMethod().invoke(object, new Object[] { theValue });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getValueObject(PropertyDescriptor pd, Element el, String propertyName, Object object, String value) {
		obj = null;
		value = value.trim();
		type = null;
		if (pd instanceof IndexedPropertyDescriptor)
			type = ((IndexedPropertyDescriptor) pd).getIndexedPropertyType();
		else
			type = pd.getPropertyType();

		if (type == int.class) {
			try {
				obj = new Integer(value);
			} catch (NumberFormatException e) {
				// this is ok.  Value not present
			}
		}

		if (type == boolean.class) {
			obj = new Boolean(value);
		}

		if (type == String.class) {
			obj = value;
		}

		if (obj == null) {
			obj = UILoader.createEQClass((INode) object, el.getChild(propertyName), el.getNamespace());
		}

		Debug.println(this, "getValueObject type=" + type + "; obj=" + obj + "; value=" + value);

		return obj;
	}

	private void doIndexedProperty(Element el, Object object, PropertyDescriptor pd, String propertyName) {
		try {
			ipd = (IndexedPropertyDescriptor) pd;
			//					Object[] o = pd.getReadMethod().invoke(object, new Object[]{});
			List list = el.getChildren(propertyName, el.getNamespace());
			int index = 0;
			Object values = Array.newInstance(ipd.getIndexedPropertyType(), list.size());
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Element element = (Element) i.next();
				Array.set(values, index++, getValueObject(pd, el, propertyName, object, element.getText()));
				//Debug.println(this, "doIndexedProperty element=" + element.getName() + "; value = " + value.trim());
			}
			Debug.println(this, object.getClass().getName() + "::" + pd.getWriteMethod().getName());
			pd.getWriteMethod().invoke(object, new Object[] { values });
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
