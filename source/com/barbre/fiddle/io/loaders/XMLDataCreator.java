package com.barbre.fiddle.io.loaders;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.io.DefaultUIFile;
import com.barbre.fiddle.io.utility.BeanInfoManager;
import com.barbre44.util.Debug;

public class XMLDataCreator {
	private static Map SIDL = new HashMap();

	/**
	 * Method createXMLData.
	 * @param node
	 * @param file
	 * @param doc
	 * @param parent
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void createXMLData(INode node, UIFile file, Document doc, Element parent) throws InvocationTargetException, IllegalAccessException {
		loadSIDL();
		Element me = createElement(node, parent);
		addItemName(node, me);
		Map keyedProperties = getPropertyDescripterMap(node);

		Iterator i = getSIDLTemplate(getNodeType(node)).iterator();
		while (i.hasNext()) {
			Element element = (Element) i.next();
			String key = element.getAttributeValue("name").toUpperCase();
			PropertyDescriptor aPD = (PropertyDescriptor) keyedProperties.get(key);
			String type = element.getAttributeValue("name");
			Debug.println(this, "createXMLData: node=" + node + ", key = " + key + ", PD = " + (aPD == null ? "null" : aPD.getName()) + ", type = " + type);
			if (aPD instanceof IndexedPropertyDescriptor) {
				Object[] data = (Object[]) read(node, aPD);
				if (data != null) {
					for (int j = 0; j < data.length; j++) {
						if (data[j] != null)
							addData(file, doc, me, element, data[j], type);
					}
				}
			} else {
				Object data = read(node, aPD);
				if (data != null)
					addData(file, doc, me, element, data, type);
			}
		}
	}

	/**
	 * Method getSIDLTemplate.
	 * @param name
	 * @return List
	 */
	private List getSIDLTemplate(String name) {
		List list = new ArrayList();
		Element template = (Element) SIDL.get(name);
		if (template != null) {
			addSuperTypeElements(list, template);
			list.addAll(template.getChildren("element", template.getNamespace()));
		} else {
			Debug.println(this, "getSIDLTemplate: template == null.  name=" + name);
		}
		return list;
	}

	/**
	 * Method addSuperTypeElements.
	 * @param list
	 * @param template
	 */
	private void addSuperTypeElements(List list, Element template) {
		Element superType = template.getChild("superType", template.getNamespace());
		if (superType != null) {
			String parent = superType.getAttributeValue("type");
			if (parent != null) {
				Debug.println(this, "getSIDLTemplate: superType=" + parent);
				list.addAll(getSIDLTemplate(parent));
			}
		}
	}

	/**
	 * Method read.
	 * @param node
	 * @param aPD
	 * @return Object
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object read(INode node, PropertyDescriptor aPD) throws IllegalAccessException, InvocationTargetException {
		try {
			Object data = aPD.getReadMethod().invoke(node, new Object[] {
			});
			return data;
		} catch (NullPointerException e) {
			Debug.println(this, aPD.getName() + ": e=" + e);
			return null;
		}
	}

	/**
	 * Method addData.
	 * @param file
	 * @param doc
	 * @param me
	 * @param element
	 * @param data
	 * @param type
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void addData(UIFile file, Document doc, Element me, Element element, Object data, String type) throws InvocationTargetException, IllegalAccessException {
		Element el = new Element(type);
		me.addContent(el);
		if (isPrimitive(element.getAttributeValue("type"))) {
			el.setText("" + data);
		} else {
			createXMLData((INode) data, file, doc, el);
		}
	}

	/**
	 * Method getPropertyDescripterMap.
	 * @param node
	 * @return Map
	 */
	private Map getPropertyDescripterMap(INode node) {
		PropertyDescriptor[] pd = BeanInfoManager.getBeanInfo(node.getClass()).getPropertyDescriptors();
		Map keyedProperties = new HashMap();
		for (int i = 0; i < pd.length; i++) {
			keyedProperties.put(pd[i].getName().toUpperCase(), pd[i]);
		}
		return keyedProperties;
	}

	/**
	 * Method addItemName.
	 * @param node
	 * @param me
	 */
	private void addItemName(INode node, Element me) {
		if (node instanceof IClass) {
			IClass c = (IClass) node;
			if (c.getitem() != null)
				me.setAttribute("item", c.getitem());
		}
	}

	/**
	 * Method createElement.
	 * @param node
	 * @param parent
	 * @return Element
	 */
	private Element createElement(INode node, Element parent) {
		if (isPrimitive(getNodeType(node)) || (node.getParent() instanceof DefaultUIFile)) {
			Element me = new Element(getNodeType(node));
			parent.addContent(me);
			return me;
		}
		return parent;
	}

	/**
	 * Method loadSIDL.
	 */
	private void loadSIDL() {
		if (SIDL.size() == 0) {
			UILoader.loadSIDL(SIDL);
		}
	}

	/**
	 * Method getNodeType.
	 * @param n
	 * @return String
	 */
	private String getNodeType(INode n) {
		String className = n.getClass().getName();
		return getNodeType(className);
	}

	/**
	 * Method getNodeType.
	 * @param className
	 * @return String
	 */
	private String getNodeType(String className) {
		int index = className.indexOf(".EQ") + 3;
		return className.substring(index);
	}

	/**
	 * Method isPrimitive.
	 * @param type
	 * @return boolean
	 */
	private boolean isPrimitive(String type) {
		type = type.toUpperCase();
		return (type.equals("STRING")) || (type.equals("INT")) || (type.equals("BOOLEAN") || (type.indexOf(":ITEM") > 0));
	}
}
