package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.barbre.fiddle.elements.IButton;
import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.ICombobox;
import com.barbre.fiddle.elements.IEditbox;
import com.barbre.fiddle.elements.IGauge;
import com.barbre.fiddle.elements.IInvSlot;
import com.barbre.fiddle.elements.ILabel;
import com.barbre.fiddle.elements.IListbox;
import com.barbre.fiddle.elements.ISTMLbox;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ISlider;
import com.barbre.fiddle.elements.ISpellGem;
import com.barbre.fiddle.elements.IStaticAnimation;
import com.barbre.fiddle.elements.IStaticText;
import com.barbre.fiddle.elements.ITabBox;

public final class WidgetFactory {
	private static Map associations = new HashMap();

	static {
		associations.put(IButton.class, Button.class);
		associations.put(IScreen.class, Screen.class);
		associations.put(ILabel.class, Label.class);		
		associations.put(IStaticText.class, StaticText.class);				
		associations.put(IInvSlot.class, InvSlot.class);
		associations.put(IGauge.class, Gauge.class);
		associations.put(ISpellGem.class, SpellGem.class);		
		associations.put(IStaticAnimation.class, StaticAnimation.class);
		associations.put(ISTMLbox.class, StmlBox.class);
		associations.put(ICombobox.class, Combobox.class);
		associations.put(IEditbox.class, Editbox.class);
		associations.put(IListbox.class, Listbox.class);
		associations.put(ITabBox.class, TabBox.class);
		associations.put(ISlider.class, Slider.class);				
	}

	/**
	 * Method createWidget.
	 * @param parent
	 * @param c
	 * @return Component
	 */
	public static Component createWidget(Component parent, IScreenPiece c) {

		try {
			Class clazz = findMatch(c);
			if (clazz != null) {
				Constructor constructor = clazz.getConstructor(new Class[] { Component.class, IScreenPiece.class });
				Component component = (Component) constructor.newInstance(new Object[] { parent, c });
				return component;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Class findMatch(IClass c) {
		Iterator i = associations.keySet().iterator();
		while (i.hasNext()) {
			Class element = (Class) i.next();
			if (element.isAssignableFrom(c.getClass()))
				return (Class) associations.get(element);
		}
		return null;
	}
	
	
}