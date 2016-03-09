/*
 * Copyright 2003-2016 - Regents of the University of California, San
 * Francisco.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 */

// TBD: Should I put the test classes in packages?  Should I use
//      the same package name as the test target class so that
//      the test class can access package-private information?
package bsg.test;

import org.junit.*;
import static bsg.test.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsg.util.Complex;

//import java.lang.reflect.*;


/**
 * Test class for the {@link Complex}.
 *
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see Complex
 * @see org.apache.commons.math.complex.Complex
 * @see org.apache.commons.math.complex.ComplexTest
 * @see ComplexMath
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
// TBD: Should test classes subclass the test target class so that
//      they can access protected variables?
public class ComplexTest
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( ComplexTest.class);


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


  // === [ Test 'abs' Function ] ==========================================

  /**
   * TBD: Add doc
   *
   */
  public static final int ABS_ULPS = 1;

  /**
   * TBD: Add doc
   *
   * @note For complex numbers, the absolute value (aka magnitude
   *       or modulus) is defined as the radius (or distance from)
   *       the origin of the complex plane.  In other words, if
   *       z = a + bi, then abs(z) = sqrt( a^2 + b^2 );
   *
   */
  public void testAbs ( String name, Complex z ) {
    String test   = null;
    double abs    = 0.0;
    double hypot  = 0.0;

    try {
      test = String.format( "z.abs() for z = %#15s", z );

      abs = z.abs();
      hypot = Math.hypot( z.real(), z.imag() ); // sqrt( a^2 + b^2 )

      assertEquals( test, abs, hypot, ABS_ULPS );

    } catch ( Throwable e ) {
      log.error( e.getMessage() );
    }

  }

  /**
   *
   */
  //@Test
  public void testAbs () {
    Complex z  =  null;

    // TBD: Reflexive test, ie testCompare(z1,z1)
    // TBD: NaN tests
    // TBD: Infinite value tests
    // TBD: +/- zero tests
    // TBD: null tests

    for ( int r = 0 ; r < 5 ; r++ ) {
      for ( int i = 0 ; i < 5 ; i++ ) {
        testAbs( "", new Complex(+r,+i) );
        testAbs( "", new Complex(+r,-i) );
        testAbs( "", new Complex(-r,+i) );
        testAbs( "", new Complex(-r,-i) );
      }
    }

    // TBD: random real and random imag values

  }

  // TBD: The Apache unit testing looks to be extensive so I probably
  //      don't really need to worry about doing too much here but
  //      I wanted to have a sanity check anyway ...



  // === [ Test Comparison Operations ] ===================================

  /**
   * TBD: Add doc
   *
   */
  public void testCompare ( String name, Complex z1, Complex z2 ) {
    String test = null;
    double d1 = 0.0;
    double d2 = 0.0;

    try {
      test = String.format( "Comparing z1 %#15s to z2 %#15s", z1, z2 );

      d1 = Math.hypot( z1.real(), z1.imag() ); // magnitude of z1
      d2 = Math.hypot( z2.real(), z2.imag() ); // magnitude of z2
      // log.debug( "Test " + name + ": "
      //          + String.format( "Magnitude z1 %#15s: %g, z1.abs(): %g;"
      //                         + " Magnitude z2 %#15s: %g, z2.abs(): %g",
      //                          z1, d1, z1.abs(), z2, d2, z2.abs() ) );

      assertEquals( test, Complex.compare(z1,z2), Double.compare(d1,d2) );

    } catch ( Throwable e ) {
      log.error( e.getMessage() );
    }

  }

  /**
   *
   */
  @Test
  public void testCompare () {
    Complex z1  =  null;
    Complex z2  =  null;

    // TBD: Reflexive test, ie testCompare(z1,z1)
    // TBD: NaN tests
    // TBD: Infinite value tests
    // TBD: +/- zero tests
    // TBD: null tests

    for ( int r = 0 ; r < 5 ; r++ ) {
      for ( int i = 0 ; i < 5 ; i++ ) {
        testCompare( "", new Complex(r,i), new Complex(+r,+i) );
        testCompare( "", new Complex(r,i), new Complex(+r,-i) );
        testCompare( "", new Complex(r,i), new Complex(-r,+i) );
        testCompare( "", new Complex(r,i), new Complex(-r,-i) );

        //if ( true ) continue;

        for ( int k = 1 ; k < 3 ; k++ ) {
          testCompare( "", new Complex(r,i), new Complex(r+k,i+0) );
          testCompare( "", new Complex(r,i), new Complex(r+k,i+k) );
          testCompare( "", new Complex(r,i), new Complex(r+0,i+k) );

          testCompare( "", new Complex(r,i), new Complex(r-k,i+k) );
          testCompare( "", new Complex(r,i), new Complex(r-k,i+0) );
          testCompare( "", new Complex(r,i), new Complex(r-k,i-k) );

          testCompare( "", new Complex(r,i), new Complex(r+0,i-k) );
          testCompare( "", new Complex(r,i), new Complex(r+k,i-k) );
        }

      }
    }

    // TBD: random real and random imag values

  }

  // Tests to do
  // -----------
  //    *
  //       *
  //       *
  //

}  // end of ComplexTest class


// To compile tests:
// ----------------
//    $ ( cd ../../../src/isl/util ; javac -Xlint Complex.java )
//    $ javac -Xlint ComplexTest.java
//

// To run tests:
// -------------
//    $ java org.junit.runner.JUnitCore isl.util.ComplexTest
//
