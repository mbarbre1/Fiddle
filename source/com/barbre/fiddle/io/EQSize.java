package com.barbre.fiddle.io;

import com.barbre.fiddle.elements.ISize;

import java.awt.Dimension;

/**
 * Generated by BeanBuilder for SIDL Fiddle
 */
public class EQSize extends EQNode implements ISize {
	private int CX;
	private int CY;

	public int getCX() {
		return CX;
	}

	public void setCX(int value) {
		int old = CX;
		CX = value;
		firePropertyChange("CX", old, CX);
	}

	public int getCY() {
		return CY;
	}

	public void setCY(int value) {
		int old = CY;
		CY = value;
		firePropertyChange("CY", old, CY);
	}

	public Dimension getSize() {
		return new Dimension(getCX(), getCY());
	}

	public void setSize(Dimension d) {
		setCX(d.width);
		setCY(d.height);
	}




}