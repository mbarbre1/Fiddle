package com.barbre.fiddle.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.barbre.fiddle.MessageManager;
import com.barbre.fiddle.elements.ITypeManager;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre44.util.Debug;

public class DefaultUIFileSet implements UIFileSet{
	private String name = "";
	private List files = null;
	private ITypeManager manager = null;
	private String dir;
	private boolean filesParsed = false;

	/**
	 * @see UIFileSet#getTypeManager()
	 */
	public ITypeManager getTypeManager() {
		if (manager == null) {
			manager = new ItemTypeManager();
		}
		return manager;
	}

	/**
	 * @see UIFileSet#setTypeManager(ITypeManager)
	 */
	public void setTypeManager(ITypeManager m) {
		manager = m;
	}

	/**
	 * @see UIFileSet#getFiles()
	 */
	public List getFiles() {
		if (files == null) {
			files = new ArrayList();
		}
		return files;
	}

	/**
	 * @see UIFileSet#addFile(UIFile)
	 */
	public void addFile(UIFile f) {
		if (getFiles().contains(f) == false) {
			getFiles().add(f);
		}
	}

	/**
	 * @see UIFileSet#removeFile(UIFile)
	 */
	public void removeFile(UIFile f) {
		getFiles().remove(f);
	}

	/**
	 * @see UIFileSet#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see UIFileSet#setName(String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see UIFile#getDirectory()
	 */
	public String getDirectory() {
		return dir;
	}

	/**
	 * @see UIFile#setDirectory(String)
	 */
	public void setDirectory(String dir) {
		this.dir = dir;
		Debug.println(this, "Directory set to " + dir + " for " + getName());
	}
	
	public void parseFiles() {
		if (! filesParsed) {
			Debug.println(this, "parseFiles");
			try {				
				Iterator i = getFiles().iterator();
				while (i.hasNext()) {
					UIFile element = (UIFile) i.next();
					MessageManager.addMessage(this, "parsing " + getDirectory() + "/" + element.getName());							
					UILoader.getInstance().parseFile(this, element);					
				}
				MessageManager.addMessage(this, "Parsing finished.");
			} catch (IOException e) {
				e.printStackTrace();
				MessageManager.addMessage(this, "I/O exception while parsing XML File " + getName() + ".  Exception text: " + e.getMessage());
			}
			filesParsed = true;
		}
	}
}
