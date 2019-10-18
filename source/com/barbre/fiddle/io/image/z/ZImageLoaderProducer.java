package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package

import java.awt.*;
import java.awt.image.*;
import java.util.*;

/*
 ZImageLoaderProducer 1.0   97/3/6
 An ImageProducer that converts arrays into Images.
 Based on a chapter from the book "Exploring Java" published by O'Reilly And Associates.
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

class ZImageLoaderProducer implements ImageProducer
{
  private int viewXRes,
              viewYRes;
  private Vector consumers = new Vector();
  private Enumeration enum;
  private ImageConsumer consum;
  private DirectColorModel directColors = null;


  // creates an Image object which receives the pixels
  public Image createImage(Component client)
  {
    Image img = client.createImage(this);
    Graphics g = client.getGraphics();
    g.drawImage(img, 0, 0, client);

    return img;
  }


  public void print(String text)
  {
    System.out.println(text);
  }


  // produces the pixels
  public void init(int[] buffer)
  {
    for(enum = consumers.elements(); enum.hasMoreElements();)
    {
      consum = (ImageConsumer)enum.nextElement();
      consum.setPixels(0, 0, viewXRes, viewYRes, directColors, buffer, 0, viewXRes);
      consum.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
  }


  // produces the pixels
  public void update(int[] buffer)
  {
    for(enum = consumers.elements(); enum.hasMoreElements();)
    {
      consum = (ImageConsumer)enum.nextElement();
      consum.setPixels(0, 0, viewXRes, viewYRes, directColors, buffer, 0, viewXRes);
      consum.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }
  }


  public void setDimensions(int w, int h, DirectColorModel cm)
  {
    viewXRes = w;
    viewYRes = h;
    directColors = cm;
  }


  // ImageProducer's implementation
  public synchronized void addConsumer(ImageConsumer c)
  {
    if (isConsumer(c))
      return;

    consumers.addElement(c);
    c.setHints(ImageConsumer.TOPDOWNLEFTRIGHT | ImageConsumer.SINGLEPASS);
    c.setDimensions(viewXRes, viewYRes);
    c.setProperties(new Hashtable());
    c.setColorModel(directColors);
  }

  public synchronized boolean isConsumer(ImageConsumer c)
  {
    return (consumers.contains(c));
  }

  public synchronized void removeConsumer(ImageConsumer c)
  {
    consumers.removeElement(c);
  }

  public void startProduction(ImageConsumer c)
  {
    addConsumer(c);
  }

  public void requestTopDownLeftRightResend(ImageConsumer c)
  {
    // nothing here
  }
}
