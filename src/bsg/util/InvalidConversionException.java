/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 */

// TBD: Which package would be the best place for this class?
//      It is a general data-related exception...        so perhaps 'isl.data'?
//      However I don't have a 'data' package yet and I don't want
//      create a new package just for an exception class...
//      Arguably the 'util' package should be used for objects with
//      no natural "home" until there's enough (2 or 3 at least)
//      that seem to "go together"...                    so perhaps 'isl.util'?
//      On the other hand this exception will mostly like occur
//      during loading input or saving output and hence is
//      somewhat related to IO...                        so perhaps 'isl.io'?
//      I'll put it in 'isl.util' for now, but may change my mind later...

package bsg.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see java.lang.RuntimeException
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class InvalidConversionException extends UnsupportedOperationException
{
  // TBD: Generate serialization ID using serialver tool
  // TBD: Add doc
  // NOTE: I would not have made an exception class serializable
  //       but it must inherit that from RuntimeException because
  //       the Java compiler complains about needing serialVersionUID.
  //       I suppose exceptions need to be serializable for RMI
  //       to work.
  private static final long serialVersionUID = 1L;    

  // TBD: Add doc
  private static final Logger log
             = LoggerFactory.getLogger( InvalidConversionException.class);


  /**
   * The object (input) which could not be converted to the {@link #type}
   * specified.
   *
   */
  protected Object source;


  /**
   * The type of object (output) that the conversion could not produce
   * from the {@link #source} provided.
   *
   */
  protected Class<?> type;


  /**
   * TBD: Add doc
   *
   */
  protected static String defaultMsgFmt
                       = "Unable to convert object ''{0}'' to type ''{1}''";


  /**
   * TBD: Add doc
   * Constructs a <code>InvalidConversionException</code> with
   * <code>message</code>, <code>src</code>, <code>type</code>,
   * and <code>cause</code>.
   *
   * @param msg     the detail message
   * @param src     the conversion input object
   * @param type    the conversion output type
   * @param cause   the cause (which is saved for later retrieval by the
   *                {@link Throwable.getCause()} method). (A null value is
   *                permitted, and indicates that the cause is nonexistent
   *                or unknown.)
   */
  public InvalidConversionException ( String msg, Object src, Class<?> type,
                                      Throwable cause )
  {
    super( msg, cause );
    this.source = src;
    this.type   = type;
  }

  /**
   * TBD: Add doc
   * Constructs a InvalidConversionException with the specified information.
   * A detail message is a String that describes this particular exception.
   *
   * @param msg     the detail message
   * @param src     the conversion input object
   * @param type    the conversion output type
   */
  public InvalidConversionException ( String msg, Object src, Class<?> type ) {
    super( msg );
    this.source = src;
    this.type   = type;
  }

  /**
   * TBD: Add doc
   *
   * @param src     the conversion input object
   * @param type    the conversion output type
   * @param cause   the cause (which is saved for later retrieval by the
   *                {@link Throwable.getCause()} method). (A null value is
   *                permitted, and indicates that the cause is nonexistent
   *                or unknown.)
   */
  InvalidConversionException( Object src, Class<?> type, Throwable cause ) {
    this( createMessage(defaultMsgFmt,src,type,cause), src, type, cause );
  }

  /**
   * TBD: Add doc
   * Constructs a InvalidConversionException with the specified information.
   * A detail message is a String that describes this particular exception.
   *
   * @param src     the conversion input type
   * @param type    the conversion output type
   * @param cause   additional information about why the conversion failed
   */
  public InvalidConversionException ( Object src, Class<?> type, String cause ) {
    this( createMessage(defaultMsgFmt,src,type,cause), src, type );
  }

  /**
   * TBD: Add doc
   *
   */
  InvalidConversionException( Object src, Class<?> type ) {
    this( src, type, (String)null );
  }


  /**
   * Gets the source parameter passed by constructor.
   *
   * @return the object (input) that the conversion was unable to change
   */
  public Object getSource () {
    return source;
  }

  /**
   * Gets the type parameter passed by constructor.
   *
   * @return the type of object (output) that the conversion was unable
   *         to create
   */
  public Class<?> getType () {
    return type;
  }


  // === [ Convenience Methods ] ==========================================

  /**
   * TBD: Add doc
   *
   */
  protected static String createMessage ( String fmt, Object src,
                                          Class<?> type, Object cause )
  {
    String msg = MessageFormat.format( fmt, src, type );

    if ( cause != null ) {
      msg += " -- " + cause.toString();
    }

    return msg;
  }


}  // end of InvalidConversionException class


// NOTE: Constructor Chaining in Exception classes
//   It would be very convenient to chain all the constructors together.
//   In particular, constructors that do _not_ accept the Throwable
//   "cause" could simply call those that do by passing in null as
//   the "cause".  Thereby all the constructors would feed into one
//   that takes all possible arguments and it is from there that
//   'super( msg, cause )' is called.
//
//   However, the Java documentation of Throwable tells us that once
//   that "cause" has been set, even to null, it can not be changed.
//   This is a good thing, of course.  But it means that if our
//   constructors which do _not_ take a cause decide to pass null
//   along to the other constructors as the assumed cause, then
//   we have prevented the user from using 'initCause'.  In other
//   words, if we want to allow the users of this exception class
//   to be able to set the "cause" via the 'initCause' method, then
//   we can not pass "null" as the "cause" inside the constructor
//   chain.  We must have two constructor chains: one for when the
//   "cause" is provided and one for when it is not.
//
//   With that being said, looking at the implementation of the
//   Throwable class, it appears there is a way to get around this
//   limitation.  If we _could_ pass 'this' to the superclass
//   constructor that would fool it setting it to the "unset"
//   value and hence the user can still call 'initCause'.  Alas,
//   you can _not_ call 'super' and pass in 'this' since the
//   the instance does not exist yet.  Another work-around would
//   be to override the 'initCause' method and implement some
//   type of flag constant, e.g.
//      protected static final Throwable NO_CAUSE = new Throwable();
//   Our override of 'initCause' would then allow users to reset
//   the "cause" provided that its value was NO_CAUSE.
//


