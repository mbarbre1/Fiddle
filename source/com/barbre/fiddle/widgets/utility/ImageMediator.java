package com.barbre.fiddle.widgets.utility;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import com.barbre.fiddle.elements.ITextureInfo;
import com.barbre.fiddle.elements.IUi2DAnimation;
import com.barbre.fiddle.io.loaders.ImageLoader;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre44.util.Debug;

/**
 * Mediator between front end and the Image loaders
 * ImageMediator.java
 */
public final class ImageMediator {
	private static BufferedImage aBufferedImage;
	private static Point aPoint;
	private static Dimension aSize;
	private static Image anImage;

	/**
	 * Method getImage.
	 * @param name
	 * @return Image
	 */
	public static Image getImage(String name) {
		Image image = ImageLoader.getImage(name);
		Debug.println(null, image);
		return image;
	}


	/**
	 * Method getImage.
	 * @param name
	 * @return Image
	 */
	public static Image getImage(ITextureInfo name) {
		String fname = getFilename(name)		;
		Image image = ImageLoader.getImage(fname);
		Debug.println(null, image);
		return image;
	}

	/**
	 * Method getImage.
	 * @param anim
	 * @return Image
	 */
	public static Image getImage(IUi2DAnimation anim) {
		if (anim == null)
			return null;
		String fname = getFilename(anim);
		aBufferedImage = ImageLoader.getBufferedImage(fname);
		aPoint = anim.getFrames(0).getLocation().getPoint();
		aSize = anim.getFrames(0).getSize().getSize();
		anImage = ImageLoader.getSubImage(fname + anim.getitem(), aBufferedImage, aPoint.x, aPoint.y, aSize.width, aSize.height);
		Debug.println(null, anImage);
		return anImage;
	}

	/**
	 * Method getFilename.
	 * @param anim
	 * @return String
	 */
	private static String getFilename(IUi2DAnimation anim) {
		String fname = anim.getFile().getSet().getDirectory() + File.separator + anim.getFrames(0).getTexture();	
		if (new File(fname).exists() == false)
			fname =UILoader.DEFAULT_FILE_SET.getDirectory() + File.separator + anim.getFrames(0).getTexture();
		return fname;
	}
	

	/**
	 * Method getFilename.
	 * @param anim
	 * @return String
	 */
	private static String getFilename(ITextureInfo info) {
		String fname = info.getFile().getSet().getDirectory() + File.separator + info.getitem();
		if (new File(fname).exists() == false)
			fname = UILoader.DEFAULT_FILE_SET.getDirectory() + File.separator + info.getitem();
		return fname;
	}
	
}
