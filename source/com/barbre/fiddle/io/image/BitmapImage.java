package com.barbre.fiddle.io.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class BitmapImage {
	/**
	loadbitmap() method converted from Windows C code.
	Reads only uncompressed 24- and 8-bit images.  Tested with
	images saved using Microsoft Paint in Windows 95.  If the image
	is not a 24- or 8-bit image, the program refuses to even try.
	I guess one could include 4-bit images by masking the byte
	by first 1100 and then 0011.  I am not really 
	interested in such images.  If a compressed image is attempted,
	the routine will probably fail by generating an IOException.
	Look for variable ncompression to be different from 0 to indicate
	compression is present.
	
	Arguments:
	sdir and sfile are the result of the FileDialog()
	getDirectory() and getFile() methods.
	
	Returns:
	Image Object, be sure to check for (Image)null !!!!
	
	*/
	public Image loadbitmap(String file) {
		Image image;
		try {
			FileInputStream fs = new FileInputStream(file);
			int bflen = 14; // 14 byte BITMAPFILEHEADER
			byte bf[] = new byte[bflen];
			fs.read(bf, 0, bflen);
			int bilen = 40; // 40-byte BITMAPINFOHEADER
			byte bi[] = new byte[bilen];
			fs.read(bi, 0, bilen);

			// Interperet data.
			int nsize = (((int) bf[5] & 0xff) << 24) | (((int) bf[4] & 0xff) << 16) | (((int) bf[3] & 0xff) << 8) | (int) bf[2] & 0xff;
			int nbisize = (((int) bi[3] & 0xff) << 24) | (((int) bi[2] & 0xff) << 16) | (((int) bi[1] & 0xff) << 8) | (int) bi[0] & 0xff;
			int nwidth = (((int) bi[7] & 0xff) << 24) | (((int) bi[6] & 0xff) << 16) | (((int) bi[5] & 0xff) << 8) | (int) bi[4] & 0xff;
			int nheight = (((int) bi[11] & 0xff) << 24) | (((int) bi[10] & 0xff) << 16) | (((int) bi[9] & 0xff) << 8) | (int) bi[8] & 0xff;
			int nplanes = (((int) bi[13] & 0xff) << 8) | (int) bi[12] & 0xff;
			int nbitcount = (((int) bi[15] & 0xff) << 8) | (int) bi[14] & 0xff;

			// Look for non-zero values to indicate compression
			int ncompression = (((int) bi[19]) << 24) | (((int) bi[18]) << 16) | (((int) bi[17]) << 8) | (int) bi[16];
			int nsizeimage = (((int) bi[23] & 0xff) << 24) | (((int) bi[22] & 0xff) << 16) | (((int) bi[21] & 0xff) << 8) | (int) bi[20] & 0xff;
			int nxpm = (((int) bi[27] & 0xff) << 24) | (((int) bi[26] & 0xff) << 16) | (((int) bi[25] & 0xff) << 8) | (int) bi[24] & 0xff;
			int nypm = (((int) bi[31] & 0xff) << 24) | (((int) bi[30] & 0xff) << 16) | (((int) bi[29] & 0xff) << 8) | (int) bi[28] & 0xff;
			int nclrused = (((int) bi[35] & 0xff) << 24) | (((int) bi[34] & 0xff) << 16) | (((int) bi[33] & 0xff) << 8) | (int) bi[32] & 0xff;
			int nclrimp = (((int) bi[39] & 0xff) << 24) | (((int) bi[38] & 0xff) << 16) | (((int) bi[37] & 0xff) << 8) | (int) bi[36] & 0xff;
			if (nbitcount == 24) {
				// No Palatte data for 24-bit format but scan lines are
				// padded out to even 4-byte boundaries.
				int npad = (nsizeimage / nheight) - nwidth * 3;
				int ndata[] = new int[nheight * nwidth];
				byte brgb[] = new byte[(nwidth + npad) * 3 * nheight];
				fs.read(brgb, 0, (nwidth + npad) * 3 * nheight);
				int nindex = 0;
				for (int j = 0; j < nheight; j++) {
					for (int i = 0; i < nwidth; i++) {
						ndata[nwidth * (nheight - j - 1) + i] = (255 & 0xff) << 24 | (((int) brgb[nindex + 2] & 0xff) << 16) | (((int) brgb[nindex + 1] & 0xff) << 8) | (int) brgb[nindex] & 0xff;
						// System.out.println("Encoded Color at ("						+i + "," + j + ")is:" + nrgb + " (R,G,B)= (" + ((int) (brgb[2]) & 0xff) + "," + ((int) brgb[1] & 0xff) + "," + ((int) brgb[0] & 0xff) + ")");
						nindex += 3;
					}
					nindex += npad;
				}

				image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(nwidth, nheight, ndata, 0, nwidth));
			} else if (nbitcount == 8) {
				// Have to determine the number of colors, the clrsused
				// parameter is dominant if it is greater than zero.  If
				// zero, calculate colors based on bitsperpixel.
				int nNumColors = 0;
				if (nclrused > 0) {
					nNumColors = nclrused;
				} else {
					nNumColors = (1 & 0xff) << nbitcount;
				}
				System.out.println("The number of Colors is" + nNumColors);

				// Some bitmaps do not have the sizeimage field calculated
				// Ferret out these cases and fix 'em.
				if (nsizeimage == 0) {
					nsizeimage = ((((nwidth * nbitcount) + 31) & ~31) >> 3);
					nsizeimage *= nheight;
					System.out.println("nsizeimage (backup) is" + nsizeimage);
				}

				// Read the palatte colors.
				int npalette[] = new int[nNumColors];
				byte bpalette[] = new byte[nNumColors * 4];
				fs.read(bpalette, 0, nNumColors * 4);
				int nindex8 = 0;
				for (int n = 0; n < nNumColors; n++) {
					npalette[n] = (255 & 0xff) << 24 | (((int) bpalette[nindex8 + 2] & 0xff) << 16) | (((int) bpalette[nindex8 + 1] & 0xff) << 8) | (int) bpalette[nindex8] & 0xff;
					// System.out.println ("Palette Color "+n					+" is:" + npalette[n] + " (res,R,G,B)= (" + ((int) (bpalette[nindex8 + 3]) & 0xff) + "," + ((int) (bpalette[nindex8 + 2]) & 0xff) + "," + ((int) bpalette[nindex8 + 1] & 0xff) + "," + ((int) bpalette[nindex8] & 0xff) + ")");
					nindex8 += 4;
				}

				// Read the image data (actually indices into the palette)
				// Scan lines are still padded out to even 4-byte
				// boundaries.
				int npad8 = (nsizeimage / nheight) - nwidth;

				int ndata8[] = new int[nwidth * nheight];
				byte bdata[] = new byte[(nwidth + npad8) * nheight];
				fs.read(bdata, 0, (nwidth + npad8) * nheight);
				nindex8 = 0;
				for (int j8 = 0; j8 < nheight; j8++) {
					for (int i8 = 0; i8 < nwidth; i8++) {
						ndata8[nwidth * (nheight - j8 - 1) + i8] = npalette[((int) bdata[nindex8] & 0xff)];
						nindex8++;
					}
					nindex8 += npad8;
				}

				image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(nwidth, nheight, ndata8, 0, nwidth));
			} else {
				System.out.println("Not a 24-bit or 8-bit Windows Bitmap, aborting...");
				image = (Image) null;
			}

			fs.close();
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Image) null;
	}

	public static void main(String[] arguments) {
		BitmapImage bmi = new BitmapImage();
		Image i = bmi.loadbitmap("c:\\eclipse\\mikes_ws\\fiddle\\lib\\EQ000007.bmp");
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(i)));
		System.exit(0);
	}

}