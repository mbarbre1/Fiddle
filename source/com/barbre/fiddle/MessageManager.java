package com.barbre.fiddle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.barbre44.util.Debug;

/**
 * All messages to the user can be routed through here to commonly
 * display them.
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Dec 14, 2002  Created class.
 *
 * </ul><p>
 */
public final class MessageManager {
	private static MessageManager instance = null;
	private PropertyChangeSupport pcs;

	/**
	 * Constructor for MessageManager.
	 */
	private MessageManager() {
		super();
		pcs = new PropertyChangeSupport(this);
	}

	public static MessageManager getInstance() {
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	public static void addMessage(Object source, String text) {
		getInstance().message(source, text);
	}
	
	public void message(Object source, String text) {	
		Debug.println(source, text)	;
		if (source == null)
			source = this;
		pcs.firePropertyChange(new PropertyChangeEvent(source, FiddleConstants.USER_MESSAGE, "", text));
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

