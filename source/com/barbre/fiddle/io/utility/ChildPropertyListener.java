package com.barbre.fiddle.io.utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.barbre.fiddle.elements.INode;

/**
 * ChildPropertyListener.java
 */
public class ChildPropertyListener implements PropertyChangeListener {
	private INode theNode;

	public ChildPropertyListener(INode aNode) {
		super();
		theNode = aNode;
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		theNode.firePropertyChange(evt);
	}

}
