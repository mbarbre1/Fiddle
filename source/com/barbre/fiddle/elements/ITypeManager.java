package com.barbre.fiddle.elements;

import java.util.Map;

public interface ITypeManager {
	void add(IClass item);
	void remove(IClass item);
	IClass get(String key);
	void addDefault(ITypeManager dft);
	Map getMap();
}