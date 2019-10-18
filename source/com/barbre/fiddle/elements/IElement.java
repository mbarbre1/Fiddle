
package com.barbre.fiddle.elements;

import org.jdom.Element;

public interface IElement {
	String getName();
	void setName(String name);
	
	String getType();
	void setType(String type);
	
	
	boolean isItemReference(); 
	void setItemReference(boolean b);
	
	String getDefaultValue();
	void setDefaultValue(String s);
	
	String getMinOccurs();
	void setMinOccurs(String s);
	
	String getMaxOccurs();
	void setMaxOccurs(String s);	
	
	boolean isArray();
	boolean isPrimitive();

	int getIntValue();
	boolean getBooleanValue();

	Object getValue();
	void setValue(Object value);
	
	String getSuperType();
	void setSuperType(String s);
	
	void populate(INode parent, Element el);
	void addToParent(Element parent);
}
