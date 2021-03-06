package com.barbre.fiddle.elements;

/**
 * Generated by BeanBuilder for SIDL Fiddle
 */
public interface IUi2DAnimation extends IClass {

	boolean getCycle();

	void setCycle(boolean value);

	boolean getGrid();

	void setGrid(boolean value);

	boolean getVertical();

	void setVertical(boolean value);

	int getCellWidth();

	void setCellWidth(int value);

	int getCellHeight();

	void setCellHeight(int value);

	IFrame[] getFrames();

	void setFrames(IFrame[] value);

	IFrame getFrames(int index);

	void setFrames(int index, IFrame value);

}