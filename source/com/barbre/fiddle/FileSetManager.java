
package com.barbre.fiddle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barbre.fiddle.elements.UIFileSet;

/**
 * This manager keeps track of all the UI sets (folders) loaded.
 */
public class FileSetManager {
	private Map map = new HashMap();	
	private PropertyChangeSupport pcs;

	/**
	 * Constructor for FileSetManager.
	 */
	public FileSetManager() {
		super();
		pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Method add.
	 * @param set
	 */
	public void add(UIFileSet set) {
		if (map.containsKey(set.getName())) {
			remove(set.getName());
		}
		map.put(set.getName(), set);
		pcs.firePropertyChange(FiddleConstants.PROPERTY_FILESET_ADDED, null, set);
	}
	
	/**
	 * Method get.
	 * @param name
	 * @return UIFileSet
	 */
	public UIFileSet get(String name) {
		return (UIFileSet) map.get(name);
	}
	
	/**
	 * Method remove.
	 * @param name
	 */
	public void remove(String name) {
		Object o = map.remove(name);
		if (o != null) 
			pcs.firePropertyChange(FiddleConstants.PROPERTY_FILESET_REMOVED, null, o);		
	}
	
	/**
	 * Method getNames.
	 * @return List
	 */
	public List getNames() {
		List list = new ArrayList(map.keySet());
		Collections.sort(list);
		return list;
	}

	/**
	 * Method addPropertyChangeListener.
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	/**
	 * Method removePropertyChangeListener.
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	

}
