package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package


// an exception indicating that the file format is wrong
public class ZWrongFileFormatException extends Exception
{
  /**
   * Constructs a ZWrongFileFormatException with no detail message.
   * A detail message is a String that describes this particular exception.
   */
   public ZWrongFileFormatException()
   {
     super();
   }

   /**
     * Constructs a ZWrongFileFormatException with the specified detail message.
     * A detail message is a String that describes this particular exception.
     * @param s the detail message
     */
   public ZWrongFileFormatException(String s)
   {
     super("ZWrongFileFormatException: " + s);
   }

}