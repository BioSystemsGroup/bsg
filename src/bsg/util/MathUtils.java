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

import static java.lang.Math.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Math
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class MathUtils
{

  // TBD: Add doc
  private static final Logger log
                   = LoggerFactory.getLogger( MathUtils.class );
  // Future: private static final Logger log = LoggingUtils.getLogger();


  /**
   * TBD: Add doc
   *
   */
  public static final int MAX_ULPS = 4 * 1024 * 1024;  // 2^20

  // TBD: There should probably be separate 'double' and 'float' versions
  //      of MAX_ULPS because the distance from MAX_VALUE to NAN might be
  //      different for each type.  The purpose of MAX_ULPS is (mainly)
  //      to make sure NAN can not be considered 'close to' a valid
  //      float or double.

  // TBD: Move this into a Math utility class along with equals(double,double,int)
  // TBD: This value was suggested by Dawson in his implementation of
  //      AlmostEqual2sComplement, but it seems very large.  Perhaps we should
  //      specify something smaller.
  //      See: http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm

  /**
   * TBD: Add doc
   *
   */
  public static final long NEGATIVE_ZERO = Double.doubleToLongBits( -0.0 );
  // = 0x8000000000000000


  // === [ ULPs Convenience Methods ] =====================================

  /**
   * TBD: Add doc
   *
   */
  public static void checkULPs ( int ulps ) {
    if ( ulps < 0 || ulps >= MathUtils.MAX_ULPS ) {
      String err = "Ulps (units in last place) relative error measurement"
                 + " must be > 0 and < " + MathUtils.MAX_ULPS + ";"
                 + " value '" + ulps + "' is out of range";
      throw new IllegalArgumentException( err );
    }
  }


  // === [ Equality Comparison Utility Methods ] ==========================

  /**
   * TBD: Move a Math utils class.
   *
   */
  public static long ulps ( double d1, double d2 ) {
    long bits1 = Double.doubleToLongBits( d1 );
    long bits2 = Double.doubleToLongBits( d2 );
    long ulps  = 0L;

    if ( bits1 < 0 ) bits1 = NEGATIVE_ZERO - bits1;
    if ( bits2 < 0 ) bits2 = NEGATIVE_ZERO - bits2;

    ulps = Math.abs( bits1 - bits2 );

    // log.debug( String.format( "Number of ulps (units in last place)"
    //          + " between d1 (%e) and d2 (%e) is abs( %d bits - %d bits ) = %d",
    //            d1, d2, bits1, bits2, ulps ) );

    return ulps;
  }

  /**
   * TBD: Move a Math utils class.
   *
   */
  public static long ulps ( Number n1, Number n2 ) {
    return ulps( n1.doubleValue(), n2.doubleValue() );
  }

  // TBD: Add float, Double, Float and Number versions of the 'ulps' method


  /**
   * TBD: Add doc
   * TBD: Moved from CSVDataModel class...
   *      Review implementation, logging, comments and etc
   *
   */
  public static boolean equals ( double tgt, double val, double tolerance ) {
    if ( (tgt - tolerance) <= val && val <= (tgt + tolerance) ) {
      // log.debug( String.format("Value %15e is within the tolerance (+/- %15e)"
      //                         + " of target value %15e -- returning true",
      //                          val,tolerance,tgt) );
      return true;
    }
    // log.debug( String.format("Value %15e is NOT within the tolerance (+/- %15e)"
    //                         + " of target value %15e -- returning false",
    //                          val,tolerance,tgt) );
    return false;
  }

  // TBD: After writing this simple tolerance equality test method, I did
  //      a little research and came across some good links:
  //         http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm
  //         http://www.ibm.com/developerworks/java/library/j-math2/index.html
  //
  //      After reading those articles, it seems clear that our 'equals' method
  //      should be rewrite to use a relative tolerance.  Whether the relative
  //      tolerance is measured as a percentage of the target value, or as the
  //      number of ULPs (Units of Least Percision or Units in the Last Place)
  //      plus/minus around the target value, is something I want to discuss
  //      with Glen.  It seems that using ULPs is the best choice to me, but
  //      there might be good reasons to go another way.

  // TBD: Move the equals to a utility library.  Perhaps a
  //      math utility class along with the gamma method/class
  //
  // TBD: Create a Enum to indicate is the tolerance interval bounds
  //      are open or closed, ie <= or just <.  Add BoundsType argument
  //      to method.  Perhaps a left and right BoundsType?
  //
  // TBD: Look into implementing the equals method using
  //         Math.abs( tgt - val ) <= tol
  //      I wonder which operations are faster in the most typical
  //      use cases.  Is one implementation inheritantly better?
  //
  // TBD: Create float, long, int versions of equals for
  //      completeness.  Perhaps byte, char versions too?
  //
  // TBD: An alternative design would be:
  //         isWithinInterval( double min, double max, value );
  //      The interval method can actually be implemented for any type
  //      (primitive or Object) that is Comparable since no calculation
  //      it necessary.
  //      Note: could also be called isWithinRange.
  //
  // TBD: Create an interface for objects that support calculations
  //      e.g. addition, subtraction and etc.  Perhaps call it the
  //      Calculable interface.  Calculable implementers would have
  //      the option of only support a subset of the operations,
  //      perhaps.
  //
  //      With the Calculable interface defined, then methods such as
  //      equals can be implemented to handle (a) any
  //      object implementing Calculable _or_ (b) any object that
  //      can be converted to Calculable (e.g. Number subclasses).
  //      Conversion to Calculable would be handled via a factory
  //      registration pattern, i.e. static Class --> Factory mapping
  //      maintained by a utility class initiated at class loading.
  //


  // TBD:
  // public static Number max ( Number a, Number b ) {
  // }

  // TBD:
  // public static Number min ( Number a, Number b ) {
  // }


  /**
   * TBD: Add doc
   * Returns a > b
   *
   */
  // public static boolean isGreaterThan ( T a, T b ) {
  @SuppressWarnings ( "unchecked" )
  public static boolean isGreaterThan ( Object a, Object b ) {
    String msg = null;

    //log.debug( "Checking is a ({}) > b ({})", a, b );

    // TBD:
    //    * Check is a and/or b is null
    //    * Add check that a.getClass() == b.getClass()
    //    * IF a and b are different classes, then determine
    //      if which is the "higher rank" class and convert
    //      the other to that, e.g. if a is an int and b is
    //      a double, then convert a to a double and then
    //      compare

    if ( a == b ) {
      log.debug( "a == b ({}) ==> return false", a );
      return false;
    }

    // TBD: We should be checking is a 'isAssignableFrom' b
    //      and vice versa
    if ( a.getClass() != b.getClass() ) {
      msg = "Arguments are not instances of the same class"
          + " -- a.getClass(): '" + a.getClass() + "',"
          +    " b.getClass(): '" + b.getClass() + "'";
      throw new UnsupportedOperationException( msg );
    }

    if ( a instanceof Comparable ) {
      // TBD: Check that b is also Comparable and if not...
      return ( ((Comparable)a).compareTo( b ) > 0 );
    }

    // Complex now implements Comparable
    //if ( a instanceof Complex ) {
    //  // TBD: Check that b is also Complex and if not...
    //  return true; // TBD
    //}

    //if ( a instanceof Number ) {
    //  // TBD: Check that b is also Number and if not...
    //  return ( Math.max( (Number)a, (Number)b ) == a ); // TBD
    //}

    msg = "Argument must be an instance of Comparable, Number or Complex"
        + " -- Argument type: '" + a.getClass().getName() + "'";
    throw new IllegalArgumentException( msg );
  }

  /**
   * TBD: Add doc
   * Returns a > b
   *
   */
  // public static boolean isLessThan ( T a, T b ) {
  public static boolean isLessThan ( Object a, Object b ) {
    //log.debug( "Checking is a ({}) < b ({})", a, b );
    return isGreaterThan( b, a );
  }


  public static String getCompareSymbol ( int result ) {
    if ( result == 0 ) return "==";
    if ( result <  0 ) return "<";
    return ">";
  }

  public static String getCompareString ( int result ) {
    if ( result == 0 ) return "equal to";
    if ( result <  0 ) return "less than";
    return "greater than";
  }


  // === [ Main & Testing Methods ] =======================================

  /**
   * TBD: Move a Math utils class.
   *
   */
  public static void printHexForSmallDoubles () {
    String table = String.format( "\n%25s  %25s  %25s  %25s\n",
                   "Float value", "Hexadecimal", "Hexadecimal", "Decimal" );

    for ( int i = 0 ; i < 7 ; i++ ) {
      double d = 0.0;
      long   n = 0L;

      if ( i <  2 )  d = +0.0 + (2-i) * Double.MIN_VALUE;
      if ( i == 2 )  d = +0.0;
      if ( i == 3 )  d = -0.0;
      if ( i >  3 )  d = -0.0 + (3-i) * Double.MIN_VALUE;

      n = Double.doubleToLongBits( d );
      //if ( n < 0 ) n = NEGATIVE_ZERO - n;
      table += String.format( "%+25e  %25s  %#25x  %+25d\n",
                              d, Double.toHexString(d), n, n );
    }

    log.debug( table );
    //System.out.println( table );
    //System.exit( 0 );
  }

  public static double area(float circumference) {
    return StrictMath.pow( (double)circumference, 2.0 ) / ( 4.0 * StrictMath.PI );
  }

  /**
   * TBD: Add doc
   *
   */
  public static void main ( String[] args ) {
    double MAX_VALUE = Double.MAX_VALUE;
    double MIN_VALUE = Double.MIN_VALUE;
    double NaN = Double.NaN;

    log.debug( "MIN_VALUE = " + MIN_VALUE );
    log.debug( "1e1       = " + 1e1       );
    log.debug( "1e10      = " + 1e10      );
    log.debug( "1e100     = " + 1e100     );
    log.debug( "1e200     = " + 1e200     );
    log.debug( "1e300     = " + 1e300     );
    log.debug( "MAX_VALUE = " + MAX_VALUE );

    log.debug( "ulps( 0.0,        NaN ) = " + ulps( 0.0,        NaN ) );
    log.debug( "ulps( MIN_VALUE,  NaN ) = " + ulps( MIN_VALUE,  NaN ) );
    log.debug( "ulps( 1e1,        NaN ) = " + ulps( 1e0,        NaN ) );
    log.debug( "ulps( 1e10,       NaN ) = " + ulps( 1e10,       NaN ) );
    log.debug( "ulps( 1e100,      NaN ) = " + ulps( 1e100,      NaN ) );
    log.debug( "ulps( 1e200,      NaN ) = " + ulps( 1e200,      NaN ) );
    log.debug( "ulps( 1e300,      NaN ) = " + ulps( 1e300,      NaN ) );
    log.debug( "ulps( MAX_VALUE,  NaN ) = " + ulps( MAX_VALUE,  NaN ) );
   // ulps( 0.0,        NaN ) = 9,221,120,237,041,090,560
   // ulps( MIN_VALUE,  NaN ) = 9,221,120,237,041,090,559
   // ulps( 1e1,        NaN ) = 4,613,937,818,241,073,152
   // ulps( 1e10,       NaN ) = 4,464,579,750,165,217,280
   // ulps( 1e100,      NaN ) = 3,118,098,783,991,970,947
   // ulps( 1e200,      NaN ) = 1,622,167,671,873,772,966
   // ulps( 1e300,      NaN ) =   126,131,315,912,182,372
   // ulps( MAX_VALUE,  NaN ) =     2,251,799,813,685,249

    printHexForSmallDoubles();

    log.debug("Math.ulp(0.0f) = "+Math.ulp(0.0f));
    log.debug("Float.MIN_VALUE = "+Float.MIN_VALUE);

  }

}  // end of MathUtils class



// TO DO 6/11
// -----
//    * 
//    * 
//    * 
// 



