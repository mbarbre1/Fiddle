package com.barbre.fiddle.io.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.io.loaders.UILoader;


/**
 * Manage creation of basic types.
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Jan 24, 2003  Created class.
 *
 * </ul><p>
 */
public final class BasicControlManager {
	private static BasicControlManager instance = null;
	private Map controls = new HashMap();

	/**
	 * @see java.lang.Object#Object()
	 */
	private BasicControlManager() {
		super();
		loadControls();
	}

	/**
	 * Method getInstance.
	 * @return BasicControlManager
	 */
	public static BasicControlManager getInstance() {
		if (instance == null) {
			instance = new BasicControlManager();
		}
		return instance;
	}

	/**
	 * Method getTypes.
	 * @return List
	 */
	public List getTypes() {
		return new ArrayList(controls.keySet());
	}

	/**
	 * Method createType.
	 * @param type
	 * @param parent
	 * @return INode
	 */
	public INode createType(String type, INode parent) {
		Element el = (Element) controls.get(type);
		Object o = UILoader.createEQClass(parent, el, el.getNamespace());
			
		return (INode) o;
	}

	/**
	 * Method loadControls.
	 */
	private void loadControls() {
		try {
			String basicFile = PropertiesLoader.getProperty(FiddleConstants.BASIC_FILENAME, "Basic_Controls.xml");			
			Document doc = UILoader.builder.build(new File(basicFile));
			List elements = doc.getRootElement().getChildren();
			if (elements.size() == 0)
				return;
			
			Iterator i = elements.iterator();
			while (i.hasNext()) {
				Element element = (Element) i.next();
				String type = element.getName();
				controls.put(type, element);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
			
		}
	}
}
