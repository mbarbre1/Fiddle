package com.barbre.fiddle.io;

import java.util.HashMap;
import java.util.Map;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.ITypeManager;

/**
 * Manages the predefined items (":item") types.
 */
public final class ItemTypeManager implements ITypeManager {

	private Map map = new HashMap();

	/**
	 * Method add.
	 * @param category
	 * @param item
	 */
	public void add(IClass item) {
		map.put(item.getitem(), item);
	}

	/**
	 * Method remove.
	 * @param category
	 * @param item
	 */
	public void remove(IClass item) {
		map.remove(item.getitem());
	}

	/**
	 * Method get.
	 * @param category
	 * @param key
	 * @return IClass
	 */
	public IClass get(String key) {
		return (IClass) map.get(key);
	}

	/**
	 * Method addDefault.
	 * @param dft
	 */
	public void addDefault(ITypeManager dft) {
		map.putAll(dft.getMap());
	}

	/**
	 * Method getMap.
	 * @return Map
	 */
	public Map getMap() {
		return map;
	}
}