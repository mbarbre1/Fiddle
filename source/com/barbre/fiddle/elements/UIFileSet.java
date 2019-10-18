
package com.barbre.fiddle.elements;

import java.util.List;

/**
 * A set of ui files that define a "skin"
 */
public interface UIFileSet {
	ITypeManager getTypeManager();
	void setTypeManager(ITypeManager m);
	List getFiles();
	void addFile(UIFile f);
	void removeFile(UIFile f);	
	void setName(String name);
	String getName();
	String getDirectory();	
	void setDirectory(String dir);
}
