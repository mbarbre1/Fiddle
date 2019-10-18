package com.barbre.fiddle.elements;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface INode {
	/**
	 * File object is defined in
	 */
	UIFile getFile();

	/**
	 * File object is defined in
	 */
	void setFile(UIFile f);

	/**
	 * Parent object (or null)
	 */
	INode getParent();

	/**
	 * Parent object (or null)
	 */
	void setParent(INode c);

	/**
	 * add a child element
	 */
	void addChild(INode c);

	/**
	 * remove a child element
	 */
	void removeChild(INode c);

	/**
	 * get all children
	 */
	INode[] getChildren();

	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
	void firePropertyChange(PropertyChangeEvent evt);
	void firePropertyChange(String propertyName, boolean oldValue, boolean newValue);
	void firePropertyChange(String propertyName, int oldValue, int newValue);
	void firePropertyChange(String propertyName, Object oldValue, Object newValue);

}