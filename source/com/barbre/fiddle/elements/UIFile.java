
package com.barbre.fiddle.elements;

public interface UIFile extends INode {
	String getName();
	UIFileSet getSet();
	void setName(String name);
	void setSet(UIFileSet set);
}
