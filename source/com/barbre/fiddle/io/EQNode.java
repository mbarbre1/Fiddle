package com.barbre.fiddle.io;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.UIFile;

/**
 * Base home for all nodes
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Dec 13, 2002  Created class.
 *
 * </ul><p>
 */
public class EQNode implements INode {
	private PropertyChangeSupport pcs;
	private UIFile file;
	private INode parent;
	private List children = new ArrayList();

	/**
	 * Constructor for EQNode.
	 */
	public EQNode() {
		super();
		pcs = new PropertyChangeSupport(this);
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#getFile()
	 */
	public UIFile getFile() {
		return file;
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#setFile(UIFile)
	 */
	public void setFile(UIFile f) {
		file = f;
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#getParent()
	 */
	public INode getParent() {
		return parent;
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#setParent(INode)
	 */
	public void setParent(INode c) {
		if (parent != null) {
			parent.removeChild(this);
		}
		parent = c;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#addChild(INode)
	 */
	public void addChild(INode c) {
		if (!children.contains(c)) {
			children.add(c);
		}
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#removeChild(INode)
	 */
	public void removeChild(INode c) {
		children.remove(c);
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#getChildren()
	 */
	public INode[] getChildren() {
		return (INode[]) children.toArray(new INode[0]);
	}

	/**
	 * Fire an existing PropertyChangeEvent to any registered listeners.
	 * No event is fired if the given event's old and new values are
	 * equal and non-null.
	 * @param evt  The PropertyChangeEvent object.
	 */
	public void firePropertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(evt);
	}

	/**
	 * Report a boolean bound property update to any registered listeners.
	 * No event is fired if old and new are equal and non-null.
	 * <p>
	 * This is merely a convenience wrapper around the more general
	 * firePropertyChange method that takes Object values.
	 *
	 * @param propertyName  The programmatic name of the property
	 *		that was changed.
	 * @param oldValue  The old value of the property.
	 * @param newValue  The new value of the property.
	 */
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Report an int bound property update to any registered listeners.
	 * No event is fired if old and new are equal and non-null.
	 * <p>
	 * This is merely a convenience wrapper around the more general
	 * firePropertyChange method that takes Object values.
	 *
	 * @param propertyName  The programmatic name of the property
	 *		that was changed.
	 * @param oldValue  The old value of the property.
	 * @param newValue  The new value of the property.
	 */
	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Report a bound property update to any registered listeners.
	 * No event is fired if old and new are equal and non-null.
	 *
	 * @param propertyName  The programmatic name of the property
	 *		that was changed.
	 * @param oldValue  The old value of the property.
	 * @param newValue  The new value of the property.
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Add a PropertyChangeListener to the listener list.
	 * The listener is registered for all properties.
	 *
	 * @param listener  The PropertyChangeListener to be added
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a PropertyChangeListener from the listener list.
	 * This removes a PropertyChangeListener that was registered
	 * for all properties.
	 *
	 * @param listener  The PropertyChangeListener to be removed
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
