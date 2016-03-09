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

//import java.lang.Math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TBD: Add class description
 *
 * @future
 *    - 
 *
 * @author Ken Cline
 * @see isl.util.Complex
 * @see org.apache.commons.math.complex.Complex
 * @since 1.0
 * @version $Revision: $  $Date: $
 *
 * $Id: $
 */
public class ComplexMath extends MathUtils // extends Math?, MathUtils?
{
  // TBD: Generate serialization ID using serialver tool
  // TBD: Add doc
  private static final long serialVersionUID = 1L;    


  // TBD: Add doc
  private static final Logger log
                   = LoggerFactory.getLogger( ComplexMath.class );
  // Future: private static final Logger log = LoggingUtils.getLogger();


  /**
   * A complex number representing "3.1459... + 0.0i"
   * TBD: Add doc
   *
   */
  public static final Complex PI      = new Complex( Math.PI, 0.0 );
  public static final Complex TWO_PI  = PI.multiply( 2.0 );
  public static final Complex FOUR_PI = PI.multiply( 4.0 );

  // This is definition of PI used in the C++ code.  It defines PI out
  // to 17 decimal places, while the Math.PI goes out to 20 places.
  // public static final Complex PI      = new Complex( 3.14159265358979323, 0.0 );


  // === [ Complex Number Unary Operations ] ==============================

  public static double abs ( Complex z ) {
    return z.abs();
  }

  public static double argument ( Complex z ) {
    return z.getArgument();
  }

  public static Complex conjugate ( Complex z ) {
    return toComplex( z.conjugate() );
  }

  public static Complex negate ( Complex z ) {
    return toComplex( z.negate() );
  }


  // === [ Arithmetic Operations ] ========================================

  public static Complex add ( Complex a, Complex b ) {
    return a.add( b );
  }

  public static Complex sub ( Complex a, Complex b ) {
    return a.subtract( b );
  }

  public static Complex subtract ( Complex a, Complex b ) {
    return a.subtract( b );
  }

  public static Complex mul ( Complex a, Complex b ) {
    return a.multiply( b );
  }

  public static Complex multiply ( Complex a, Complex b ) {
    return a.multiply( b );
  }

  public static Complex div ( Complex a, Complex b ) {
    return a.divide( b );
  }

  public static Complex divide ( Complex a, Complex b ) {
    return a.divide( b );
  }


  // === [ Logarithmic & Power Operations ] ===============================

  public static Complex exp ( Complex z ) {
    return toComplex( z.exp() );
  }

  public static Complex log ( Complex z ) {
    return toComplex( z.log() );
  }

  // TBD: log10 ? log1p ?

  public static Complex pow ( Complex z, Complex e ) {
    return toComplex( z.pow( e ) );
  }

  public static Complex sqrt ( Complex z ) {
    return toComplex( z.sqrt() );
  }

  public static Complex sqrt1z ( Complex z ) {
    return toComplex( z.sqrt1z() );
  }


  // === [ Trigonometric Operations ] =====================================

  public static Complex cos ( Complex z ) {
    return toComplex( z.cos() );
  }

  public static Complex sin ( Complex z ) {
    return toComplex( z.sin() );
  }

  public static Complex tan ( Complex z ) {
    return toComplex( z.tan() );
  }


  // === [ Inverse Trigonometric Operations ] =============================

  public static Complex acos ( Complex z ) {
    return toComplex( z.acos() );
  }

  public static Complex asin ( Complex z ) {
    return toComplex( z.asin() );
  }

  public static Complex atan ( Complex z ) {
    return toComplex( z.atan() );
  }


  // === [ Hyperbolic Trigonometric Operations ] ==========================

  public static Complex cosh ( Complex z ) {
    return toComplex( z.cosh() );
  }

  public static Complex sinh ( Complex z ) {
    return toComplex( z.sinh() );
  }

  public static Complex tanh ( Complex z ) {
    return toComplex( z.tanh() );
  }

  // === [ Distance & Angle Operations ] ==================================
  private final static org.apache.commons.math3.ml.distance.EuclideanDistance dist_op = new org.apache.commons.math3.ml.distance.EuclideanDistance();
  public static double distance ( double ax, double ay,
                                  double bx, double by )
  {
    // TBD: Check for NaN or Infinity and return an appropriate value
    double [] a = { ax, ay };
    double [] b = { bx, by };
    return dist_op.compute( a, b );
  }

  public static double distance ( Complex a, double bx, double by ) {
    // TBD: Check for NaN or Infinity and return an appropriate value
    return distance( a.real(), a.imag(), bx, by );
  }

  public static double distance ( Complex a, Complex b ) {
    // TBD: Check for NaN or Infinity and return an appropriate value
    return distance( a.real(), a.imag(), b.real(), b.imag() );
  }

  // TBD:
  //    * Add distance methods that take one arg and assume the second
  //      point is the origin
  //    * Add methods that calculate the angle difference between two
  //      complex numbers
  //


  // === [ Equality Comparison Utility Methods ] ==========================

  /**
   * TBD: The return value is really a pair of long values and not
   *      a Complex number.  We need to rewrite the Complex number
   *      class to take a type arg and then the return time could
   *      be Complex<long>.  The nice thing about using Complex,
   *      or preferably Complex<long>, is that each axis is identified
   *      so there's no confusion about which ulps value is for the
   *      real number line and which is for the imaginary
   *
   */
  public static Complex ulps ( Complex z1, Complex z2 ) {
    //Complex<long> ulps = null;
    Complex ulps = null;

    ulps = new Complex( MathUtils.ulps(z1.real(),z2.real()),
                        MathUtils.ulps(z1.imag(),z2.imag())  );

    // log.debug( String.format( "Number of ulps (units in last place)"
    //          + " between z1 (%s) and z2 (%s) is %d on the real axis"
    //          + " and %d on the imaginary axis",
    //              z1, z2, (long)ulps.real(), (long)ulps.imag() ) );

    return ulps;
  }


  // TBD: Add float, Double, Float and Number versions of the 'ulps' method


  // === [ Comparison Operations ] ========================================

  //public static Complex max ( Complex a, Complex b ) {
  //  double distA = 
  //}

  // public static Complex min ( Complex a, Complex b );


  // === [ Rounding Methods ] =============================================

  // TBD: ???


  // === [ Random Number Generators ] =====================================

  // TBD: ???



  // === [ Conversion Utility Methods ] ===================================

  public static Complex toComplex ( org.apache.commons.math3.complex.Complex z ) {
    if ( z instanceof Complex ) {
      return (Complex)z;
    }
    return new Complex( z );
  }

  // TBD:
  //    * Add conversion methods for converting Complex number to/from
  //      polar coordinates
  //    * Add conversion methods for converting Complex number to/from
  //      a real number
  //

  // === [ Misc Utility Methods ] =========================================

  // TBD: ???


  // === [ Main & Testing Methods ] =======================================

  /**
   * TBD: Add doc
   *
   */
  public static void main ( String[] args ) {
    Complex x = Complex.ONE;  // new Complex( 1.0 );
    Complex y = Complex.i;    // new Complex( 0.0, 1.0 );
    double  d = 1.0;
    String  s = null;

    System.out.println( "x + y = " + add(x,y)      );
    System.out.println( "x - y = " + subtract(x,y) );
    System.out.println( "x * y = " + multiply(x,y) );
    System.out.println( "x / y = " + divide(x,y)   );
    System.out.println( "-------------------------" );

    System.out.println( "y + x = " + add(y,x)      );
    System.out.println( "y - x = " + subtract(y,x) );
    System.out.println( "y * x = " + multiply(y,x) );
    System.out.println( "y / x = " + divide(y,x)   );
    System.out.println( "-------------------------" );

    //d = 1.0;
    //System.out.println( "y + " + d + " = " + add(y,d)      );
    //System.out.println( "y - " + d + " = " + subtract(y,d) );
    //System.out.println( "y * " + d + " = " + multiply(y,d) );
    //System.out.println( "y / " + d + " = " + divide(y,d)   );
    //System.out.println( "-------------------------" );

  }

}  // end of ComplexMath class



// TO DO 6/17
// -----
//    * 
//    * 
//    * 
//    * 
// 



