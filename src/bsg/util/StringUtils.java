/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package bsg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TBD: Add class description
 *
 * @future -
 *
 * @author Ken Cline
 * @see String
 * @see StringBuffer
 * @see StringBuilder
 * @since 1.0
 * @version $Revision: $ $Date: $
 *
 * $Id: $
 */
public class StringUtils {

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

  // === [ Basic String Operations ] ======================================
  public static boolean isEmpty(String str) {
    if (str == null) {
      return true;
    }
    return str.length() < 1;
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  // === [ Capitalization Utilities ] =====================================
  public static final StringBuffer capitalizeFirst(StringBuffer buf) {
    buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
    return buf;
  }

  public static final StringBuilder capitalizeFirst(StringBuilder buf) {
    buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
    return buf;
  }

  public static final String capitalizeFirst(String s) {
    return capitalizeFirst(new StringBuilder(s)).toString();
  }

  public static String convertStreamToString(java.io.InputStream is) throws NullPointerException {
    if (is == null) {
      throw new NullPointerException("InputStream cannot be null");
    }
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

}  // end of StringUtils class
