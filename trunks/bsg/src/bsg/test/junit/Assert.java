/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 */

// TBD: Should this be part of the ISL utils package or perhaps something
//      completed separate, e.g. edu.ucsf.huntlab.util or com.tdi.util
//      or ???
package bsg.test.junit;

import org.junit.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsg.util.MathUtils;
import bsg.util.Complex;
import bsg.util.ComplexMath;

import java.awt.geom.Ellipse2D;
import java.util.Map;


/**
 * Extension of the JUnit {@link Assert} class...
 *
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Assert
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
// TBD: Should test classes subclass the test target class so that
//      they can access protected variables?
public class Assert extends org.junit.Assert
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( Assert.class);


  /**
   * TBD: Move an extension of org.junit.Assert.
   *
   */
  public static void assertEquals ( String msg, double expected,
                                                double actual, int ulps )
  {
    double tolerance = 0.0D;

    // HACK
    // TBD: I'm not sure if this is best way to do double comparison
    //      with tolerance.  There may be some edge cases, e.g. +0.0,
    //      -0.0, NaN, +Inf, -Inf and such, that are not handled
    //      correctly by simply using the Math.ulps(expected)

    MathUtils.checkULPs( ulps );

    tolerance =  ulps * Math.ulp( expected );
    // log.debug( String.format( "Checking if actual (%13e) = expected (%13e)"
    //                         + " +/- %d ulps (= %13e); [ulp distance: %d];"
    //                         + " message: '%s'",
    //                           actual, expected, ulps, tolerance,
    //                           MathUtils.ulps(expected,actual), msg ) );

    assertEquals( msg, expected, actual, tolerance );

    log.info( String.format( "Double value actual (%13e) is equal to expected (%13e)"
                           + " +/- %d ulps (= %13e); [ulp distance: %d];"
                           + " message: '%s'",
                             actual, expected, ulps, tolerance,
                             MathUtils.ulps(expected,actual), msg ) );
  }

  public static void assertEquals ( double expected, double actual, int ulps ) {
    assertEquals( null, expected, actual, ulps );
  }

  /**
   * TBD: Move an extension of org.junit.Assert.
   *
   */
  public static void assertEquals ( String msg, float expected,
                                                float actual, int ulps )
  {
    float tolerance = 0.0F;

    // HACK
    // TBD: I'm not sure if this is best way to do double comparison
    //      with tolerance.  There may be some edge cases, e.g. +0.0,
    //      -0.0, NaN, +Inf, -Inf and such, that are not handled
    //      correctly by simply using the Math.ulps(expected)

    MathUtils.checkULPs( ulps );

    tolerance =  ulps * Math.ulp( expected );
    // log.debug( String.format( "Checking if actual (%13e) = expected (%13e)"
    //                         + " +/- %d ulps (= %13e); [ulp distance: %d];"
    //                         + " message: '%s'",
    //                           actual, expected, ulps, tolerance,
    //                           MathUtils.ulps(expected,actual), msg ) );

    assertEquals( msg, expected, actual, tolerance );

    log.info( String.format( "Float value actual (%13e) is equal to expected (%13e)"
                           + " +/- %d ulps (= %13e); [ulp distance: %d];"
                           + " message: '%s'",
                             actual, expected, ulps, tolerance,
                             MathUtils.ulps(expected,actual), msg ) );
  }

  public static void assertEquals ( float expected, float actual, int ulps ) {
    assertEquals( null, expected, actual, ulps );
  }


  // TBD: Add 'assertEquals' methods that take a percentage as the relative
  //      tolerance measure instead of ulps.
  //      Note: I'm still thinking about this because the method signature
  //            can _not_ be 'assertEquals(double,double,double)' because
  //            that will override the org.junit.Assert#assertEquals.
  //            Or maybe that's okay?
  //


  // === [ Complex Number Comparison Tests ] ==============================

  // HACK
  public static String testEquals ( String msg,
                                    double expected, double actual,
                                    double tolerance,
                                    Map<String,String> errors )
  {
    try {
      assertEquals( msg, expected, actual, tolerance );
    } catch ( Throwable e ) {
      if ( errors != null ) {
        errors.put( msg, e.getMessage() );
      }
      return e.getMessage();
    }
    return null;
  }

  public static String toString ( Ellipse2D e ) {
    if ( e == null ) {
      return "" + e;
    }
    double w = e.getWidth();
    double h = e.getHeight();
    double x = e.getCenterX();
    double y = e.getCenterY();
    return "ellipse { center: (" + x + "," + y + "),"
                  + " width: " + w + ","
                  + " height: " + h + " }";
  }

  // TBD: Move these to a subclass: ComplexAssert

  /**
   * TBD: Move an extension of org.junit.Assert.
   *
   */
  public static void assertEquals ( String msg,
                                    Complex expected, Complex actual,
                                    double errReal, double errImag )
  {
    Ellipse2D  ellipse  = null;
    String     msgReal  = null;
    String     msgImag  = null;
    String     cpxFmt   = "%#25.10s";

    // log.debug( String.format( "Checking if actual (%10s) = expected (%10s)"
    //                         + " with tolerances: +/- %g on real axis and"
    //                         + " +/- %g on imaginary axis;"
    //                         + " message: '%s'",
    //                           actual, expected, errReal, errImag, msg ) );

    msgReal = testEquals( "real:", expected.real(), actual.real(), errReal, null );
    msgImag = testEquals( "imag:", expected.imag(), actual.imag(), errImag, null );

    if ( msgReal != null || msgImag != null ) {
      msg = String.format( "%s -- expected: " + cpxFmt + " but was: " + cpxFmt
          + " ( tolerance +/- %g real, +/- %g imag )"
          + " { components: %s%s%s }",
            (msg == null ? "Complex numbers not equal" : msg),
            expected, actual, errReal, errImag,
            (msgReal == null ? "" : msgReal),
            (msgReal != null && msgImag != null ? ", " : ""),
            (msgImag == null ? "" : msgImag)  );
      throw new AssertionError( msg );
    }

    // NOTE: Ellipse2D is created with (x,y) = upper left corner
    //       and the width and height.  Also note that the y axis
    //       is flipped (since this is the screen) so the y value
    //       of the upper left corner is expected.imag() - errImag/2.
    ellipse = new Ellipse2D.Double( expected.real() - errReal / 2.0,
                                    expected.imag() - errImag / 2.0,
                                    errReal,   errImag );
    if ( ! ellipse.contains(actual.real(),actual.imag()) ) {
      msg = String.format( "%s -- expected: " + cpxFmt + " but was: " + cpxFmt
                         + " ( tolerance %s )",
            (msg == null ? "Complex numbers not equal" : msg),
            expected, actual, toString(ellipse)  );
      throw new AssertionError( msg );
    }

    log.info( String.format( "Complex number actual (%10s) is equal to"
                           + " expected (%10s) with tolerances:"
                           + " +/- %g on real axis and"
                           + " +/- %g on imaginary axis;"
                           + " message: '%s'",
                             actual, expected, errReal, errImag, msg ) );

  }

  public static void assertEquals ( Complex expected, Complex actual,
                                    double errReal, double errImag )
  {
    assertEquals( null, expected, actual, errReal, errImag );
  }

  public static void assertEquals ( String msg,
                                    Complex expected, Complex actual,
                                    double err )
  {
    assertEquals( msg, expected, actual, err, err );
  }

  public static void assertEquals ( Complex expected, Complex actual,
                                    double err )
  {
    assertEquals( null, expected, actual, err, err );
  }


  /**
   * TBD: Move an extension of org.junit.Assert.
   *
   */
  public static void assertEquals ( String msg,
                                    Complex expected, Complex actual,
                                    int ulpsReal, int ulpsImag )
  {
    double errReal = 0.0D;
    double errImag = 0.0D;

    // HACK
    // TBD: I'm not sure if this is best way to do double comparison
    //      with tolerance.  There may be some edge cases, e.g. +0.0,
    //      -0.0, NaN, +Inf, -Inf and such, that are not handled
    //      correctly by simply using the Math.ulps(expected)

    MathUtils.checkULPs( ulpsReal );
    MathUtils.checkULPs( ulpsImag );

    errReal =  ulpsReal * Math.ulp( expected.real() );
    errImag =  ulpsImag * Math.ulp( expected.imag() );
    // log.debug( String.format( "Checking if actual (%15s) = expected (%15s)"
    //                         + " within +/- %d ulps (= %13e) on real axis"
    //                         + " and +/- %d ulps (=%13e) on imaginary axis;"
    //                         + " [ulp distance: %1.0s];"
    //                         + " message: '%s'",
    //                           actual, expected, ulpsReal, errReal,
    //                           ulpsImag, errImag,
    //                           ComplexMath.ulps(expected,actual), msg ) );

    assertEquals( msg, expected, actual, errReal, errImag );

    // log.info( String.format( "Complex number actual (%15s) is equal to"
    //                        + " expected (%15s)"
    //                        + " within +/- %d ulps (= %13e) on real axis"
    //                        + " and +/- %d ulps (=%13e) on imaginary axis;"
    //                        + " [ulp distance: %1.0s];"
    //                        + " message: '%s'",
    //                          actual, expected, ulpsReal, errReal,
    //                          ulpsImag, errImag,
    //                          ComplexMath.ulps(expected,actual), msg ) );
  }

  public static void assertEquals ( Complex expected, Complex actual,
                                    int ulpsReal, int ulpsImag )
  {
    assertEquals( null, expected, actual, ulpsReal, ulpsImag );
  }

  public static void assertEquals ( String msg,
                                    Complex expected, Complex actual, int ulps )
  {
    assertEquals( msg, expected, actual, ulps, ulps );
  }

  public static void assertEquals ( Complex expected, Complex actual, int ulps )
  {
    assertEquals( null, expected, actual, ulps );
  }


}  // end of Assert class


// To compile tests:
// -------------
//    $ javac -Xlint *.java
//

// TODO: 6/20
//    * Implement ComplexMath.ulps
//    * Implement ComplexMath.equals
//    *
//    * Install JUnit source and see if there's a way to prevent
//      the AssertionError from being thrown
//    * If not, then implement a Proxy class to create test<X>
//      type methods, ala testEquals above
//    * Create toString method for Ellipse class
//    * Create Ellipse factory class?
//    * Add factory method or constructor for creating Ellipse
//      with Complex point + width + height and/or 2 x Complex numbers
//    * Add methods that allow both tolerances to be specified as
//      single object, perhaps an instance of Complex where the
//      real part is the tolerance on the real axis and etc.
//    * Create Test class and methods for the Complex Assert methods
//    * 
//    * Compile the AbstractCDModelTest class
//    * Generate new .h file and move to refModel subdirectory
//    * Implement native methods
//    * Build library
//    * Test g(s) method
//    * 
//    * 
//

