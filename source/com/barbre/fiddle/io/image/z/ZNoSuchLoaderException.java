package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package


// an exception indicating that there is no loader for the file
public class ZNoSuchLoaderException extends Exception
{
  /**
   * Constructs a ZNoSuchLoaderException with no detail message.
   * A detail message is a String that describes this particular exception.
   */
  public ZNoSuchLoaderException()
  {
	  super();
  }

  /**
   * Constructs a ZNoSuchLoaderException with the specified detail message.
   * A detail message is a String that describes this particular exception.
   * @param s the detail message
   */
  public ZNoSuchLoaderException(String s)
  {
	  super("ZNoSuchLoaderException: " + s);
  }

}