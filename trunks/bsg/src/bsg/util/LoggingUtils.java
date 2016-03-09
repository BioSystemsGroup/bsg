/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 */

package bsg.util;

// TBD: Move to utilites class with getResourceFile methods

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import static ch.qos.logback.classic.Level.*;

import static java.lang.System.out;


/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Logger
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class LoggingUtils
{

  // TBD: Add doc
  private static final Logger log = getLogger( LoggingUtils.class );


  public static Logger getLogger ( Class<?> cls ) {
    // String msg = "Getting Logger for class '" + cls + "'...";
    // if ( log != null ) {
    //   log.debug( msg );
    // } else {
    //   out.println( msg );
    // }
    return LoggerFactory.getLogger( cls );
  }

  // public static Logger getLogger () {
  //   return getLogger( ClassUtils.getCallerClass(1) );
  // }

  public static Level getLevel ( Logger log ) {
    Level level = null;

    if ( log instanceof ch.qos.logback.classic.Logger ) {
      level = ((ch.qos.logback.classic.Logger)log).getLevel();
      if ( level == null ) {
        level = ((ch.qos.logback.classic.Logger)log).getEffectiveLevel();
        // out.println( "DEBUG: Level is null for Logger '" + log + "';"
        //              + " returning effective level '" + level + "'" );
        // TBD: Check if effective level is null too
      }
      return level;
    }

    // HACK HACK HACK
    // TBD: Possibly add something to convert other Logger types
    //      into Logback Logger, eg getName from other Logger and
    //      create a Logback Logger.  Or we could use reflection
    //      to check if other Logger type supports getLevel()

    String msg = "Operation 'getLevel' only support for instances of"
               + " 'ch.qos.logback.classic.Logger'"
               + " -- Logger implementation class is '" + log.getClass() + "'";
    throw new UnsupportedOperationException( msg );
  }

  public static String getLevelName ( Logger log ) {
    return getLevel( log ).toString();
  }

  public static int getLevelValue ( Logger log ) {
    return getLevel( log ).toInt();
  }

  public static void printAllLevels () {
    Level [] levels = { ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF };
    String listing  = null;

    listing = String.format( "%15s %10s  %-40s\n", "Value", "Name", "Object" );

    for ( Level lvl : levels ) {
      listing += String.format( "%15s %10s  %-40s\n",
                                lvl.toInt(), lvl.toString(),
                                lvl.getClass().getName() + '@' + lvl.hashCode() );
    }

    out.println( listing );

  }


}  // end of LoggingUtils class


