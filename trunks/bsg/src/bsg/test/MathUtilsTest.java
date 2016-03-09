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
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.lang.reflect.*;

import static bsg.util.MathUtils.*;


/**
 * Test class for the {@link MathUtils}.
 *
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see MathUtils
 * @see Math
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
// TBD: Should test classes subclass the test target class so that
//      they can access protected variables?
public class MathUtilsTest
{

  // TBD: Add doc
  private static final Logger log = LoggerFactory.getLogger( MathUtilsTest.class);


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


  /**
   *
   */
  @Test
  public void testUlps () {
    double d1  =  0.0;
    double d2  =  0.0;
    long ulps  = 0L;

    for ( long i = 0 ; i < 4 ; i++ ) {
      d1 = +0.0;
      d2 = d1 + i * Double.MIN_VALUE;
      ulps = i;
      assertEquals( String.format("ulps(%+f,%+f) = %d",d1,d2,ulps),
                  ulps, ulps(d1,d2) );
      d2 = d1 - i * Double.MIN_VALUE;
      assertEquals( String.format("ulps(%+f,%+f) = %d",d1,d2,ulps),
                    ulps, ulps(d1,d2) );

      d1 = -0.0;
      d2 = d1 + i * Double.MIN_VALUE;
      ulps = i;
      assertEquals( String.format("ulps(%+f,%+f) = %d",d1,d2,ulps),
                  ulps, ulps(d1,d2) );
      d2 = d1 - i * Double.MIN_VALUE;
      assertEquals( String.format("ulps(%+f,%+f) = %d",d1,d2,ulps),
                    ulps, ulps(d1,d2) );
    }

  }

  // Tests to do
  // -----------
  //    *
  //       *
  //       *
  //

}  // end of MathUtilsTest class


// To compile tests:
// ----------------
//    $ ( cd ../../../src/isl/util ; javac -Xlint MathUtils.java )
//    $ javac -Xlint MathUtilsTest.java
//

// To run tests:
// -------------
//    $ java org.junit.runner.JUnitCore isl.util.MathUtilsTest
//
