package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package


// an exception indicating that the file format is not supported
public class ZUnsupportedFormatException extends Exception
{
  /**
   * Constructs a ZUnsupportedFormatException with no detail message.
   * A detail message is a String that describes this particular exception.
   */
  public ZUnsupportedFormatException()
  {
    super();
  }

  /**
   * Constructs a ZUnsupportedFileFormatException with the specified detail message.
   * A detail message is a String that describes this particular exception.
   * @param s the detail message
   */
   public ZUnsupportedFormatException(String s)
   {
     super("ZUnsupportedFormatException: " + s);
   }

}