package com.barbre.fiddle.io.utility;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barbre44.util.Debug;

/**
 * Maintain a map of known bean info.  This should reduce
 * memory overhead from calls to GenericBeanInfo.
 *  _BeanInfo. java
 */
public final class BeanInfoManager {
	private static final Map info = new HashMap();
	private static final List hiddenProperties = new ArrayList();

	static {
		hiddenProperties.add("file");
		hiddenProperties.add("set");		
		hiddenProperties.add("class");		
		hiddenProperties.add("parent");		
	}

	/**
	 * Method getBeanInfo.
	 * @param clazz
	 * @return BeanInfo
	 */
	public static final BeanInfo getBeanInfo(Class clazz) {
		BeanInfo b = (BeanInfo) info.get(clazz);
		if (b == null) {
			try {
				b = Introspector.getBeanInfo(clazz);
				hideSelected(b);
			} catch (IntrospectionException e) {
				Debug.println(null, e.getMessage());
			}
			info.put(clazz, b);
		}
		return b;
	}
	
	/**
	 * Method hideSelected.
	 * @param info
	 */
	private static void hideSelected(BeanInfo info) {
		PropertyDescriptor[] pd = info.getPropertyDescriptors();
		for (int i=0; i<pd.length; i++) {
			if (hiddenProperties.contains(pd[i].getName().toLowerCase()))
				pd[i].setHidden(true);
		}
	}
}
