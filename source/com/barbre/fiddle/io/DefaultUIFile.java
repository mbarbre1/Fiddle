package com.barbre.fiddle.io;

import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre44.util.Debug;

public class DefaultUIFile extends EQNode implements UIFile {
	private String name;
	private UIFileSet set = null;
	private boolean filesParsed = false;

	/**
	 * @see UIFile#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see UIFile#setName(String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see UIFile#getSet()
	 */
	public UIFileSet getSet() {
		return set;
	}

	/**
	 * @see UIFile#setSet(UIFileSet)
	 */
	public void setSet(UIFileSet set) {
		this.set = set;
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#getChildren()
	 */
	public INode[] getChildren() {
		Debug.println(this, "getChildren");
		((DefaultUIFileSet) getSet()).parseFiles();
		return super.getChildren();
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#getFile()
	 */
	public UIFile getFile() {
		return this;
	}

	/**
	 * @see com.barbre.fiddle.elements.INode#setFile(UIFile)
	 */
	public void setFile(UIFile f) {
		//		super.setFile(f);
	}

}
