package com.barbre.fiddle.io.image.z;
import java.awt.*;
import java.net.*;
import java.applet.*;
import com.barbre.fiddle.io.image.*;  // JavaZine's zfileio package

/*
 ZImageViewApplet 1.0   97/2/27
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

public class ZImageViewApplet extends Applet implements ZProgressStatus
{
  Font font = new Font("TimesRoman", Font.BOLD, 18);
  MediaTracker theTracker = null;
  ZImageLoader theLoader = new ZImageLoader();
  Image myPic = null;
  URL myURL = null;
  Graphics direct = null;
  boolean first = false;

  String myName = "<noimage>",
         loadText = "",
         s;

  int loaded = 0,
      lastPercent = 100,
      total;


  public void init()
  {
    s = getParameter("Image");
    if (s != null)
      myName = s;

    theLoader.setClient(this);
    /* set the ZImageLoader's debug flag to true
       so its messages are printed to the console */
    theLoader.debug(true);

    repaint();
    direct = getGraphics();
  }

  public void paint(Graphics g)
  {
    g.setFont(font);

    if (!first)
    {
      direct = getGraphics();
      first = true;

      try
      {
        myURL = new URL(getDocumentBase(), myName);
        myPic = theLoader.load(myURL, (Component)this);

        try
        {
          Thread.sleep(50);
        }
        catch (InterruptedException e)
        {
        }
      }
      catch (MalformedURLException e)
      {
        print(e.getMessage());
      }
      catch (ZNoSuchLoaderException e)
      {
        print(e.getMessage());
      }
    }

    if (myPic != null && loaded == 100)
      g.drawImage(myPic, 0, 0, this);
    else
    {
      if (lastPercent > loaded)
      {
        g.setColor(Color.white);
        g.fillRect(0, 0, size().width, size().height);
        g.setColor(Color.black);
        g.drawRect(10, 10, size().width - 20, 30);
        g.setColor(Color.black);
        g.setFont(font);
        total = size().width - 24;
      }

      lastPercent = loaded;
      g.setColor(Color.red);
      g.fillRect(12, 12, (total * loaded) / 100, 26);
      g.setColor(Color.red);
      g.drawString(myName + " " + loadText, 10, 70);

      // for debugging purposes
      // print(loadText + loaded + "%");

      if (loaded == 100)
        repaint();
    }
  }

  // implement ZProgressStatus interface for callbacks
  public void imageLoaded(int percent, Image picture, String text)
  {
    loaded = percent;
    loadText = text;
    repaint();
    Thread.yield();
  }

  public void print(String text)
  {
    System.out.println(text);
    getAppletContext().showStatus(text);
  }

  public void update(Graphics g)
  {
    paint(g);
  }

}