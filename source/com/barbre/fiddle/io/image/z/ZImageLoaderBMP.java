package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package

import java.io.*;
import java.awt.*;
import java.awt.image.*;

/*
 ZImageLoaderBMP 1.0   97/3/5
 Loader for Windows Bitmap (BMP) images.
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

public class ZImageLoaderBMP extends ZImageLoader implements ZProgressStatus
{
  private int []bmiColors;
  private int
    zl,
    biPlanes,
    biBitCount,
    colNum,
    redColor15 = (31 << 10),
    greenColor15 = (31 << 5),
    blueColor15 = 31,
    redColor24 = (255 << 16),
    greenColor24 = (255 << 8),
    blueColor24 = 255,
    orientation = 0,
    pixels = 0,
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
    lastFramePos = 0,
    alpha = 0,
    fileLength = 0,
    RLEtype,
    RLElength,
    RLEpause = 0,
    padding = 0,
    scanLineSize = 0,
    actLine = 0,
    lastPercent = 0,
    actPercent = 0;

  private String bfType;

  private long
    bfSize,
    bfOffBits,
    biSize,
    biWidth,
    biHeight,
    biCompression,
    biSizeImage,
    biClrUsed,
    biClrImportant;

  private byte[] cache = null;

  private boolean
    loaded = false,
    RLEencoded = false;


  // return information about the loader
  public String getInfo()
  {
    return("JavaZine's ZImageLoaderBMP");
  }


  // load the BMP header
  protected void readHeader()
    throws IOException,
           ZUnsupportedFormatException,
           ZWrongFileFormatException
  {
    // this message will only be printed if debug mode is set
    print(getInfo() + " running");

    /* Note:
       the "imageFile" variable is an instance of ZFileInput, it has been
       created by the ZImageLoader superclass and can now be used */

    // open the file

    // big/low endian switch because the BMP files come from the Wintel world
    imageFile.setIntelSwitch(true);

    // reading the bitmap's header
    bfType = imageFile.readString(2); // read ASCII "BM"
    if (bfType.equals("BM") != true)
      throw new ZUnsupportedFormatException(" Not a valid BMP file");

    bfSize = imageFile.read32Bit(); // read file size
    imageFile.skipBytes(4); // skip bfReserved1 and bfReserved2
    // read Byte offset after header where image data begins
    bfOffBits = imageFile.read32Bit();

    print("bfType: " + bfType);
    print("bfSize: " + bfSize);
    print("bfOffBits: " + bfOffBits);

    biSize = imageFile.read32Bit();
    biWidth = imageFile.read32Bit();
    biHeight = imageFile.read32Bit();
    biPlanes = imageFile.read16Bit();
    biBitCount = imageFile.read16Bit();

    if (biBitCount < 8)
      throw new ZUnsupportedFormatException("Bitmaps with less than 256 colors aren't supported yet");

    if (biBitCount < 24)
      colNum = 256;

    biCompression = imageFile.read32Bit();
    biSizeImage = imageFile.read32Bit();
    imageFile.skipBytes(8); // skip biXPelsPerMeter and biYPelsPerMeter
    biClrUsed = imageFile.read32Bit();
    biClrImportant = imageFile.read32Bit();

    // show header info if debug mode is set
    print("biSize: " + biSize);
    print("biWidth: " + biWidth);
    print("biHeight: " + biHeight);
    print("biPlanes: " + biPlanes);
    print("biBitCount: " + biBitCount);
    print("biCompression: " + biCompression);
    print("biSizeImage: " + biSizeImage);
    print("biClrUsed: " + biClrUsed);
    print("biClrImportant: " + biClrImportant);

    // allocate and initialize the frame's buffer
    initFrameBuffer((int) biWidth, (int) biHeight);

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


  /* implements the ZProgressStatus interface to get info about the actual loading status
     from the ZFileInput class (method readBytes()) */
  public void imageLoaded(int percent, Image picture, String text)
  {
    newStatus(percent, "loading image ");
  }


  // load the bitmap's data
  protected void loadPicture()
    throws IOException,
           ZUnsupportedFormatException,
           ZWrongFileFormatException
  {
    int cacheSize = 0,
        stuff = 0;

    if (biCompression != 0)
      RLEencoded = true; // it's a compressed image

    // look for palette
    if (biBitCount < 24)
    {
      if (biClrUsed > 0)
        colNum = (int) biClrUsed;

      bmiColors = new int[colNum];
      for(zl = 0; zl < colNum; zl++)
      {
        blue = (imageFile.readByte() & 255);
        green = (imageFile.readByte() & 255);
        red = (imageFile.readByte() & 255);
        bmiColors[zl] = (red * 65536) + (green * 256) + blue;
        imageFile.skipBytes(1);
      }
    }

    scanLineSize = (int) ((biWidth * biBitCount + 31) / 32) * 4;

    if (biSizeImage != 0)
      cacheSize = (int) biSizeImage; // read raw image data into cache array
    else
      cacheSize = (int) (scanLineSize * biHeight);

    // reading image data
    pixels = (int)(biWidth * biHeight); // total number of pixels
    cache = imageFile.readBytes(cacheSize, this);

    print("bytes in the frame's buffer: " + frameBuffer.length);
    print("bytes in cache: " + cache.length);
    print("pixels: " + pixels);

    cachePtr = 0;
    stepFrameCol = 1; // default left to right, bottom to top
    stepFrameRow = - (2 * xRes);
    framePtr = (yRes - 1) * xRes;
    pixelCt = 0; // counter for pixels

    RLEpause = 0;
    RLElength = 1;

    if (biBitCount == 8)
      decodeCMAP();

    if (biBitCount == 24)
      decodeTC();

    // close the file
    imageFile.close();

    imageLoaded = true; // image loading has finished
    cache = null;     // free the cache array
    bmiColors = null; // free the palette array
    System.gc(); // invoke garbage collector
  }


  // decode colormapped images
  private void decodeCMAP()
    throws IOException
  {
    int cacheStep = 1,
        xOff,
        yOff,
        pad2 = 0,
        bytes = (int) biWidth,
        fours = bytes / 4,
        tempCol = 0,
        padding = bytes - (fours * 4);

    if (padding > 0)
      padding = ((fours + 1) * 4) - bytes;

    if (RLEencoded)
    {
      lastFramePos = framePtr;
      stepFrameRow = 0 - xRes;

      print("8-bit BMP image with palette, RLE encoded");

      while(pixelCt < pixels)
      {
        pad2 = 0;
        RLElength = (cache[cachePtr++] & 255);

        //print("RLE Byte: " + RLElength);

        if (RLElength > 0)
        {
          pixel = (cache[cachePtr++] & 255);
          tempCol = bmiColors[pixel];

          //print("pixels of same color: " + RLElength);

          for(zl = 0; zl < RLElength; zl++)
          {
            frameBuffer[framePtr++] = tempCol;
            pixelCt++;
          }
        }
        else
        {
          RLEtype = (cache[cachePtr++] & 255);

          //print("no 2. RLEtype: " + RLEtype);

          if (RLEtype == 0) // end of line
            newLine();
          else
            if (RLEtype == 1) // end of bitmap
              return;
            else
              if (RLEtype == 2) // delta
              {
                xOff = (cache[cachePtr++] & 255);
                yOff = (cache[cachePtr++] & 255);

                print("delta xOff: " + xOff + " yoff: " + yOff);

                if (yOff > 0)
                  for(zl = 0; zl < yOff; zl++)
                  {
                    framePtr -= xRes;
                    pixelCt += xRes;
                  }

                  if (xOff > 0)
                  {
                    framePtr += xOff;
                    col += xOff;
                    pixelCt += xOff;
                  }
              }
              else
                if (RLEtype > 2)
                {
                  RLElength = RLEtype;
                  pad2 = RLElength / 2;
                  pad2 *= 2;
                  pad2 = RLElength - pad2;

                  // print("literal bytes: " + RLElength + "  even pad: " + pad2);

                  for(zl = 0; zl < RLElength; zl++)
                  {
                    pixel = (cache[cachePtr++] & 255);
                    frameBuffer[framePtr++] = bmiColors[pixel];
                    pixelCt++;
                  }

                  cachePtr += pad2;
                }
                else
                  print("RLEtype strange: " + RLEtype);
        }
      }
    }
    else
    {
      print("8-bit BMP image with palette, not encoded");

      while (pixelCt < pixels)
      {
        pixel = (cache[cachePtr++] & 255);
        frameBuffer[framePtr++] = bmiColors[pixel];
        col++;
        if (col == xRes)
        {
          newStatus("decompressing image ");
          framePtr += stepFrameRow;
          col = 0;
          cachePtr += padding;
        }

        pixelCt++;
      }

      print("decoding ended");
    }
  }


  private final void newLine()
  {
    newStatus("decompressing image ");

    lastFramePos += stepFrameRow;
    framePtr = lastFramePos;
    col = 0;
    cachePtr += padding;
  }


  // decode 24-Bit TrueColor image
  private void decodeTC()
    throws IOException
  {
    print("load True Color BMP image");

    int bytes = (int) (biWidth * 3),
        fours = bytes >> 2;

    padding = bytes - (fours << 2);

    if (padding > 0)
      padding = ((fours + 1) << 2) - bytes;

    while (pixelCt < pixels)
    {
      red = cache[cachePtr + 2];
      green = cache[cachePtr + 1];
      blue = cache[cachePtr];
      cachePtr += 3;
      frameBuffer[framePtr] = (alpha << 24) + (red << 16) + (green << 8) + blue;
      framePtr += stepFrameCol;
      col++;

      if (col == xRes)
      {
        cachePtr += padding;
        framePtr += stepFrameRow;
        col = 0;
        newStatus("decompressing image ");
      }

      pixelCt++;
    }
  }

}
