/*
 * IPRL - Tests for Extension of JUnit Assert class
 *
 * Copyright 2003-2011 - Regents of the University of California, San
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
import static bsg.test.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsg.util.MathUtils;
import bsg.util.Complex;
import bsg.util.ComplexMath;

//import java.awt.geom.Ellipse2D;


/**
 * Tests for the sxtension of the JUnit {@link Assert} class...
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
public class AssertTest
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( AssertTest.class);


  // === [ Set-up & Tear-down Methods ] ===================================

  /**
   *
   */
  @Before
  public void setUp () {
    // Per test setup
  }

  /**
   *
   */
  @After
  public void tearDown () {
    // Per test tearDown
  }


  // === [ Tests for assertEquals (doubles) ] =============================


  /**
   * TBD: Add doc
   *
   */
  //@Test
  public void testAssertEqualsDoubleWithULPs ()
  {
    String msg = null;
    double expected  = 0.0;
    double actual    = 0.0;
    int ulps = 0;

    // assertEquals( msg, expected, actual, ulps );

    // TBD: Add tests here

  }

  // TBD: Add expected failure tests for the AssertEqualsDoubleWithULPs

  // TBD: Add tests for float as well as double


  // === [ Tests for assertEquals (Complex) ] =============================

  /**
   * TBD: Add doc
   *
   */
  @Test
  public void testAssertEqualsComplexWithULPs ()
  {
    String  msg = null;
    Complex expected  = null;
    Complex actual    = null;
    double  errReal   = 0.1;
    double  errImag   = 0.1;
    double  real      = 0.0;
    double  imag      = 0.0;
    double [] multipliers = { 0.0, 0.25, 0.5, 1.0, 1.5 };

    real = imag = 1.0;
    errReal = errImag = 0.1;
    expected = new Complex( real, imag );
    for ( double multReal : multipliers ) {
      for ( double multImag : multipliers ) {
        actual = new Complex( real + multReal * errReal,
                              imag + multImag * errImag );
        try {
          assertEquals( msg, expected, actual, errReal, errImag );
        } catch ( Throwable e ) {
          log.info( e.getMessage() );
        }
      }
    }

    // TBD: Add tests here

    // TBD: Test To Do
    //    * permutations of expected and/or actual = null/non-null
    //          ==> ???
    //    * permutations of expected and/or actual with real and/or imaginary
    //         components as Infinity and/or NaN  ==> ???
    //    * random complex number (add generator method to Complex Math)
    //         for expected and then brute force actual values as all
    //         numbers in rectangle defined by errReal, errImag
    //           ==> all values "equal" except the corners 
    //       * random values near origin
    //       * random values along real axis
    //       * random values along imaginary axis
    //       * random values at magnitude r and angle p, where p is
    //           a series of n values between 0 - 2*pi
    //       * 
    //    * actual = expected (same instance) ==> equal 
    //    * actual = new Complex( expected ) (clone) ==> equal 
    //    * 

  }

}  // end of AssertTest class


// To compile tests:
// -----------------
//    $ javac -Xlint *.java
//

// To run tests:
// -------------
//    $ java org.junit.runner.JUnitCore isl.util.junit.AssertTest
//


// TODO: 6/20
// ----
//    * 
//    * 
//    * 
//
