package com.barbre.fiddle.decorators;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.IFrameTemplate;
import com.barbre.fiddle.elements.IWindowDrawTemplate;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class BorderDecorator extends Decorator {
	private IFrameTemplate template;

	private Image topLeft;
	private Image top;
	private Image topRight;
	private Image left;
	private Image right;
	private Image bottomLeft;
	private Image bottom;
	private Image bottomRight;
	private Image rightTop;
	private Image rightBottom;
	private Image leftTop;
	private Image leftBottom;
	private Image middle;

	/**
	 * Method BorderDecorator.
	 * @param parent
	 * @param template
	 */
	public BorderDecorator(JComponent parent, IClass eqParent, IWindowDrawTemplate template) {
		super(parent, eqParent);
		this.template = template.getBorder();
		refresh();
	}

	/**
	 * Method BorderDecorator.
	 * @param parent
	 * @param template
	 */
	public BorderDecorator(JComponent parent, IClass eqParent, IFrameTemplate template) {
		super(parent, eqParent);
		this.template = template;
		refresh();
	}

	/**
	 * @see Decorator#paint(Graphics)
	 */
	public void paint(Graphics g) {
		if (template == null) {
			return;
		}

		Rectangle parentRect = getParent().getBounds();
		Rectangle drawRect = new Rectangle();

		
		if (topLeft != null) {
			drawRect.width = topLeft.getWidth(getParent());
			drawRect.height = topLeft.getHeight(getParent());
			draw(topLeft, g, drawRect);
		}

		if ((topRight != null) && (top != null)) {
			drawRect.x = drawRect.width + 1;
			drawRect.width = parentRect.width - drawRect.x - topRight.getWidth(getParent());
			drawRect.height = top.getHeight(getParent());
			draw(top, g, drawRect);
		}

		if (topRight != null) {
			drawRect.x += drawRect.width + 1;
			drawRect.width = topRight.getWidth(getParent());
			drawRect.height = topRight.getHeight(getParent());
			draw(topRight, g, drawRect);
		}
		
		if ((topLeft != null) && (leftTop != null)) {
			drawRect.x = 0;
			drawRect.y = topLeft.getHeight(getParent()) + 1;
			drawRect.width = leftTop.getWidth(getParent());
			drawRect.height = leftTop.getHeight(getParent());
			draw(leftTop, g, drawRect);
		}

		if ((leftTop != null) && (left != null) && (leftBottom != null) && (bottomLeft != null)) {
			drawRect.y += leftTop.getHeight(getParent());
			drawRect.width = left.getWidth(getParent());
			drawRect.height = parentRect.height - drawRect.y - leftBottom.getHeight(getParent()) - bottomLeft.getHeight(getParent());
			draw(left, g, drawRect);
		}			

		if (leftBottom != null) {
			drawRect.y += drawRect.height;
			drawRect.width = leftBottom.getWidth(getParent());
			drawRect.height = leftBottom.getHeight(getParent());
			draw(leftBottom, g, drawRect);
		}


		if (bottomLeft != null) {
			drawRect.y += drawRect.height;
			drawRect.width = bottomLeft.getWidth(getParent());
			drawRect.height = bottomLeft.getHeight(getParent());
			draw(bottomLeft, g, drawRect);
		}
	
		if ((bottomLeft != null) && (bottom != null) && (bottomRight != null)) {
			drawRect.x += bottomLeft.getWidth(getParent());
			drawRect.width = parentRect.width - bottomRight.getWidth(getParent());
			drawRect.height = bottom.getHeight(getParent());
			draw(bottom, g, drawRect);
		}

		if (bottomRight != null) {
			drawRect.x = drawRect.width;
			drawRect.width = bottomRight.getWidth(getParent());
			drawRect.height = bottomRight.getHeight(getParent());
			draw(bottomRight, g, drawRect);
		}

		if ((bottomRight != null) && (rightBottom != null)) {
			drawRect.x += bottomRight.getWidth(getParent()) - rightBottom.getWidth(getParent());
			drawRect.y -= (rightBottom.getHeight(getParent()));
			drawRect.width = rightBottom.getWidth(getParent());
			drawRect.height = rightBottom.getHeight(getParent());
			draw(rightBottom, g, drawRect);
		}
		
		if ((topRight != null) && (right != null) && (rightBottom != null) && (rightTop != null)) {
			drawRect.y = topRight.getHeight(getParent()) + 1 + rightTop.getHeight(getParent());
			drawRect.width = right.getWidth(getParent());
			drawRect.height = (parentRect.height - rightBottom.getHeight(getParent()) - rightTop.getHeight(getParent()) - topRight.getHeight(getParent()));
			draw(right, g, drawRect);
		}

		if ((topRight != null) && (rightTop != null)) {
			drawRect.y = topRight.getHeight(getParent()) + 1;
			drawRect.width = rightTop.getWidth(getParent());
			drawRect.height = rightTop.getHeight(getParent());
			draw(rightTop, g, drawRect);
		}
		
	}

	/**
	 * @see Decorator#refresh()
	 */
	public void refresh() {
		if (template != null) {
			topLeft = ImageMediator.getImage(template.getTopLeftObject());
			top = ImageMediator.getImage(template.getTopObject());
			topRight = ImageMediator.getImage(template.getTopRightObject());
			left = ImageMediator.getImage(template.getLeftObject());
			right = ImageMediator.getImage(template.getRightObject());
			bottomLeft = ImageMediator.getImage(template.getBottomLeftObject());
			bottom = ImageMediator.getImage(template.getBottomObject());
			bottomRight = ImageMediator.getImage(template.getBottomRightObject());
			rightTop = ImageMediator.getImage(template.getRightTopObject());
			rightBottom = ImageMediator.getImage(template.getRightBottomObject());
			leftTop = ImageMediator.getImage(template.getLeftTopObject());
			leftBottom = ImageMediator.getImage(template.getLeftBottomObject());
		}
	}
}