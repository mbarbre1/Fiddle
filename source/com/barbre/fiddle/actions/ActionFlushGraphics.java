
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.io.loaders.ImageLoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

import com.barbre44.util.Debug;

public class ActionFlushGraphics extends AbstractAction {

	public ActionFlushGraphics() {
		super("Flush Graphics");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		try {
			ImageLoader.flush();
		} catch(Exception e) {
			Debug.println(this, e);
		}
	}

}
