package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package

import java.awt.Image;
import java.io.IOException;

/*
 ZImageLoaderTGA 1.0   97/3/5
 Loader for Truevision Targa (TGA) images.
 Copyright (c) 1997, Marcel Schoen and Andre Pinheiro

 Code by Marcel Schoen (Marcel.Schoen@village.ch)
 Documentation/Arrangements by Andre Pinheiro (l41325@alfa.ist.utl.pt)

 All rights reserved.

 Published by JavaZine - Your Java webzine
 Links to JavaZine's websites
   - http://camoes.rnl.ist.utl.pt/~adlp/JavaZine/Links/JavaZine.html

 Permission to use, copy, modify, and distribute this software
 and its documentation for NON-COMMERCIAL or COMMERCIAL purposes
 and without fee is hereby granted provided that the copyright
 and "published by" messages above appear in all copies.
 We will not be held responsible for any unwanted effects due to
 the usage of this software or any derivative.
 No warrantees for usability for any specific application are
 given or implied.
*/

public class ZImageLoaderTGA extends ZImageLoader implements ZProgressStatus {
	/* variables instatiated by the superclass:
	   ZFileInput imageFile;
	   int[] frameBuffer;
	*/
	// TGA-specific variables
	private int zl,
		redColor15 = (31 << 10),
		greenColor15 = (31 << 5),
		blueColor15 = 31,
		redColor24 = (255 << 16),
		greenColor24 = (255 << 8),
		blueColor24 = 255,
		orientation = 0,
		imgType = 0,
		IDlength = 0,
		cMapFirst = 0,
		xOrigin,
		yOrigin,
		description,
		depth = 0,
		pixels = 0,
		cMapType = 0,
		cMapLength = 0,
		cMapEntrySize = 0,
		framePtr = 0,
		cachePtr = 0,
		red = 0,
		green = 0,
		blue = 0,
		col = 0,
		cacheStep = 0,
		stepFrameCol = 0,
		stepFrameRow = 0,
		pixelCt = 0,
		pixel = 0,
		alpha = 0,
		fileLength = 0,
		RLEtype,
		RLElength,
		RLEcounter,
		RLEpause = 0,
		w,
		h,
		value;
	private boolean loaded = false, RLEencoded = false;
	private byte[] cache = null;
	private int[] palette = null;

	// return information about the loader
	public String getInfo() {
		return ("JavaZine's ZImageLoaderTGA");
	}

	/* implements the ZProgressStatus interface to get info about the actual loading status
	   from the ZFileInput class (method readBytes()) */
	public void imageLoaded(int percent, Image picture, String text) {
		newStatus(percent, "loading image ");
	}

	// load the TGA header in order to prepare the frame's buffer
	protected void readHeader() throws IOException, ZUnsupportedFormatException, ZWrongFileFormatException {
		// this message will only be printed if debug mode is set
		print(getInfo() + " running");

		/* Note:
		   the "imageFile" variable is an instance of ZFileInput, it has been
		   created by the ZImageLoader superclass and can now be used */

		// open the file

		// big/low endian switch because the TGA files come from the Wintel world
		imageFile.setIntelSwitch(true);

		// checking signature
		IDlength = imageFile.readByte(); // read one single byte
		cMapType = imageFile.readByte();
		imgType = imageFile.readByte();
		cMapFirst = imageFile.read16Bit(); // read 16 bits
		cMapLength = imageFile.read16Bit();
		cMapEntrySize = imageFile.readByte();
		xOrigin = imageFile.read16Bit();
		yOrigin = imageFile.read16Bit();
		w = imageFile.read16Bit(); // read width of image in pixels
		h = imageFile.read16Bit(); // read height of image in pixels

		initFrameBuffer(w, h); // allocate and initialize frameBuffer

		/* Note:
		
		   After the initFrameBuffer() call the array of integers frameBuffer[]
		   has been allocated and can now be used to throw the image data into it.
		
		   Remember that a Java int has 32 bits.
		
		   The color model is as follows
		   (from left to right, i.e. from high bit (31) to low bit (0))
		   AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB
		   A = Alpha Channel (ignore it, it's not used in any way)
		   R = Red
		   G = Green
		   B = Blue
		
		   The variables "xRes" and "yRes" contain the width and heigth of the image. */
	}

	// load the TGA file
	protected void loadPicture() throws IOException, ZUnsupportedFormatException, ZWrongFileFormatException {
		depth = imageFile.readByte(); // read image depth in bits per pixel
		description = imageFile.readByte(); // read description bits
		// calculate orientation (top-down, left-right, etc.)
		orientation = (description & 0xF0) >> 4;

		// show header info if debug mode is set
		print("ID field length: " + IDlength);
		print("Color map type: " + cMapType);
		print("Image type: " + imgType);
		print("First color map entry: " + cMapFirst);
		print("Color map length: " + cMapLength);
		print("Color map entry size: " + cMapEntrySize);
		print("X Origin: " + xOrigin);
		print("Y Origin: " + yOrigin);
		print("width: " + w);
		print("height: " + h);
		print("depth: " + depth);
		print("orientation: " + orientation);

		// prepare variables for the RLE decoding process
		RLEpause = 0;
		RLEcounter = 0;
		RLElength = 1;

		if (imgType == 9 | imgType == 10 | imgType == 11) {
			print("RLE encoded picture");
			RLEencoded = true; // it's a compressed image
		}

		imageFile.skipBytes(IDlength); // skip ID field

		if (cMapType == 1) // there's a color map
			{
			if (cMapEntrySize == 16)
				throw new ZUnsupportedFormatException("16-bit Truevision Targa files with palette aren't supported yet");

			print("loading color map");

			palette = new int[cMapLength * (cMapEntrySize / 8)];
			value = 0;

			for (zl = 0; zl < cMapLength; zl++) {
				if (cMapEntrySize == 24) {
					palette[value++] = imageFile.readByte();
					palette[value++] = imageFile.readByte();
					palette[value++] = imageFile.readByte();
				}
			}
		}

		// reading image data
		pixels = w * h; // total number of pixels

		if (!RLEencoded) // picture is not RLE-compressed?
			{
			// read raw image data into cache array
			cache = imageFile.readBytes(pixels * (depth / 8), this);
			print("data bytes in cache: " + cache.length);
		}

		// convert the image data from the byte array into the frame's buffer
		print("converting " + frameBuffer.length + " pixels");

		stepFrameCol = 1; // default - left to right, bottom to top
		stepFrameRow = - (2 * xRes);
		framePtr = (yRes - 1) * xRes;
		pixelCt = 0; // counter for pixels

		if (orientation == 0x10) // right to left, bottom to top
			{
			stepFrameCol = -1;
			stepFrameRow = 0;
			framePtr = pixels - 1;
		} else if (orientation == 0x20) // left to right, top to bottom
			{
			stepFrameCol = 1;
			stepFrameRow = 0;
			framePtr = 0;
		} else if (orientation == 0x30) // right to left, top to bottom
			{
			stepFrameCol = -1;
			stepFrameRow = 2 * xRes;
			framePtr = xRes - 1;
		}

		if (imgType == 1 | imgType == 9)
			decodeCMAP(); // decode a colormapped image
		else if (imgType == 2 | imgType == 10)
			decodeTC(); // decode a true color image
		else if (imgType == 3 | imgType == 11)
			decodeGray(); // decode a grayscale image

		print("image decoded, closing file");

		// close the file
		imageFile.close();

		imageLoaded = true; // image loading has finished
		cache = null; // free the cache array
		palette = null; // free the palette array
		System.gc(); // invoke garbagge collector
	}

	// decode a gray scale image
	private void decodeGray() throws IOException {
		print("loading a grayscale image");

		while (pixelCt < pixels) {
			if (RLEencoded) {
				if (RLEpause == 0)
					decodeRLE();

				pixel = imageFile.readByte();
			} else
				pixel = (cache[cachePtr++] & 255);

			value = (alpha << 24) + (pixel << 16) + (pixel << 8) + pixel;

			for (RLEcounter = 0; RLEcounter < RLElength; RLEcounter++) {
				frameBuffer[framePtr] = value;
				framePtr += stepFrameCol;
				col++;

				if (col == xRes) {
					newStatus("decompressing image ");
					framePtr += stepFrameRow;
					col = 0;
				}

				pixelCt++;
			}

			if (RLEpause > 0)
				RLEpause--;
		}
	}

	// decode a colormapped image
	private void decodeCMAP() throws IOException {
		if (depth == 16) {
		} else if (depth == 8) {
			print("decoding an 8-bit TGA image with palette");

			while (pixelCt < pixels) {
				if (RLEencoded) {
					if (RLEpause == 0)
						decodeRLE();

					pixel = imageFile.readByte() * 3;
				} else {
					pixel = (cache[cachePtr] & 255) * 3;
					cachePtr++;
				}

				red = palette[pixel + 2];
				green = palette[pixel + 1];
				blue = palette[pixel];
				value = (red << 16) + (green << 8) + blue;

				for (RLEcounter = 0; RLEcounter < RLElength; RLEcounter++) {
					frameBuffer[framePtr] = value;
					framePtr += stepFrameCol;
					col++;

					if (col == xRes) {
						newStatus("decompressing image ");
						framePtr += stepFrameRow;
						col = 0;
					}

					pixelCt++;
				}

				if (RLEpause > 0)
					RLEpause--;
			}
		}
	}

	// decode 16-bit, 24-bit and 32-bit TrueColor images
	private final void decodeTC() throws IOException {
		print("loading a True Color image");

		if (depth == 16) {
			while (pixelCt < pixels) {
				if (RLEencoded) {
					if (RLEpause == 0)
						decodeRLE();

					pixel = imageFile.readByte();
					pixel += imageFile.readByte() * 256;
				} else {
					pixel = (cache[cachePtr + 1] & 255);
					pixel = (pixel << 8) + (cache[cachePtr] & 255);
					cachePtr += 2;
				}

				red = (pixel & redColor15) >> 10;
				green = (pixel & greenColor15) >> 5;
				blue = pixel & blueColor15;
				red = (red << 8) >> 5;
				green = (green << 8) >> 5;
				blue = (blue << 8) >> 5;
				value = (alpha << 24) + (red << 16) + (green << 8) + blue;

				for (RLEcounter = 0; RLEcounter < RLElength; RLEcounter++) {
					frameBuffer[framePtr] = value;
					framePtr += stepFrameCol;
					col++;

					if (col == xRes) {
						newStatus("decompressing image ");
						framePtr += stepFrameRow;
						col = 0;
					}

					pixelCt++;
				}

				if (RLEpause > 0)
					RLEpause--;
			}
		} else if (depth == 24) {
			while (pixelCt < pixels) {
				if (RLEencoded) {
					if (RLEpause == 0)
						decodeRLE();

					blue = imageFile.readByte();
					green = imageFile.readByte();
					red = imageFile.readByte();
				} else {
					red = cache[cachePtr + 2];
					green = cache[cachePtr + 1];
					blue = cache[cachePtr];
					cachePtr += 3;
				}

				value = (alpha << 24) + (red << 16) + (green << 8) + blue;

				for (RLEcounter = 0; RLEcounter < RLElength; RLEcounter++) {
					frameBuffer[framePtr] = value;
					framePtr += stepFrameCol;
					col++;

					if (col == xRes) {
						newStatus("decompressing image ");
						framePtr += stepFrameRow;
						col = 0;
					}

					pixelCt++;
				}

				if (RLEpause > 0)
					RLEpause--;
			}
		} else if (depth == 32) {
			while (pixelCt < pixels) {
				if (RLEencoded) {
					if (RLEpause == 0)
						decodeRLE();

					blue = imageFile.readByte();
					green = imageFile.readByte();
					red = imageFile.readByte();
					alpha = imageFile.readByte();
				} else {
					red = cache[cachePtr + 2];
					green = cache[cachePtr + 1];
					blue = cache[cachePtr];
					alpha = cache[cachePtr + 3];
					cachePtr += 4;
				}

				value = (alpha << 24) + (red << 16) + (green << 8) + blue;
				for (RLEcounter = 0; RLEcounter < RLElength; RLEcounter++) {
					frameBuffer[framePtr] = value;
					framePtr += stepFrameCol;
					col++;
					if (col == xRes) {
						newStatus("decompressing image ");
						framePtr += stepFrameRow;
						col = 0;
					}
					pixelCt++;
				}

				if (RLEpause > 0)
					RLEpause--;
			}
		}
	}

	// decode a RLE compression byte
	private final void decodeRLE() throws IOException {
		int v = imageFile.readByte();
		RLEtype = (v & 0x80);
		RLElength = (v & 0x7F) + 1;

		if (RLEtype == 0) {
			RLEpause = RLElength;
			RLElength = 1;
		}
	}

}